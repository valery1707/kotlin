/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.caches.trackers

import com.intellij.lang.ASTNode
import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.ModificationTracker
import com.intellij.openapi.util.SimpleModificationTracker
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.pom.PomManager
import com.intellij.pom.PomModelAspect
import com.intellij.pom.event.PomModelEvent
import com.intellij.pom.event.PomModelListener
import com.intellij.pom.tree.TreeAspect
import com.intellij.pom.tree.events.TreeChangeEvent
import com.intellij.psi.*
import com.intellij.psi.impl.PsiManagerImpl
import com.intellij.psi.impl.PsiModificationTrackerImpl
import com.intellij.psi.impl.PsiTreeChangeEventImpl
import com.intellij.psi.impl.PsiTreeChangeEventImpl.PsiEventType.CHILD_MOVED
import com.intellij.psi.impl.PsiTreeChangeEventImpl.PsiEventType.PROPERTY_CHANGED
import com.intellij.psi.impl.PsiTreeChangePreprocessor
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.parents
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getTopmostParentOfType
import org.jetbrains.kotlin.psi.psiUtil.isAncestor

val KOTLIN_CONSOLE_KEY = Key.create<Boolean>("kotlin.console")

/**
 * Tested in OutOfBlockModificationTestGenerated
 */
class KotlinCodeBlockModificationListener(
    modificationTracker: PsiModificationTracker,
    project: Project,
    private val treeAspect: TreeAspect
) : PsiTreeChangePreprocessor {
    private val modificationTrackerImpl = modificationTracker as PsiModificationTrackerImpl

    @Suppress("UnstableApiUsage")
    private val isLanguageTrackerEnabled = modificationTrackerImpl.isEnableLanguageTrackerCompat

    // BUNCH: 183
    // When there're we no per-language trackers we had to increment global tracker first and process result afterward
    private val customIncrement = if (isLanguageTrackerEnabled) 0 else 1

    @Volatile
    private var kotlinModificationTracker: Long = 0

    private val kotlinOutOfCodeBlockTrackerImpl: SimpleModificationTracker = if (isLanguageTrackerEnabled) {
        SimpleModificationTracker()
    } else {
        object : SimpleModificationTracker() {
            override fun getModificationCount(): Long {
                @Suppress("DEPRECATION")
                return modificationTracker.outOfCodeBlockModificationCount
            }
        }
    }

    val kotlinOutOfCodeBlockTracker: ModificationTracker = kotlinOutOfCodeBlockTrackerImpl

    internal val perModuleOutOfCodeBlockTrackerUpdater = KotlinModuleOutOfCodeBlockModificationTracker.Updater(project)

    init {
        val model = PomManager.getModel(project)
        val messageBusConnection = project.messageBus.connect()

        if (isLanguageTrackerEnabled) {
            (PsiManager.getInstance(project) as PsiManagerImpl).addTreeChangePreprocessor(this)
        }

        model.addModelListener(object : PomModelListener {
            override fun isAspectChangeInteresting(aspect: PomModelAspect): Boolean {
                return aspect == treeAspect
            }

            override fun modelChanged(event: PomModelEvent) {
                val changeSet = event.getChangeSet(treeAspect) as TreeChangeEvent? ?: return
                val ktFile = changeSet.rootElement.psi.containingFile as? KtFile ?: return

                val changedElements = changeSet.changedElements

                // skip change if it contains only virtual/fake change
                if (changedElements.isNotEmpty() && changedElements.all { !it.psi.isPhysical }) return

                incFileModificationCount(ktFile)

                // When a code fragment is reparsed, Intellij doesn't do an AST diff and considers the entire
                // contents to be replaced, which is represented in a POM event as an empty list of changed elements
                val outOfBlockChange =
                    changedElements.filter { it.psi.isPhysical }.any { getInsideCodeBlockModificationScope(it.psi) == null } || changedElements.isEmpty()

                val inBlockChange = if (!outOfBlockChange) {
                    // ignore formatting (whitespaces etc)
                    if (!isFormattingChange(changeSet))
                        incInBlockModificationCount(changedElements)
                    else true
                } else false

                if (outOfBlockChange || !inBlockChange) {
                    messageBusConnection.deliverImmediately()

                    if (ktFile.isPhysical && !isReplLine(ktFile.virtualFile)) {
                        if (isLanguageTrackerEnabled) {
                            kotlinOutOfCodeBlockTrackerImpl.incModificationCount()
                            perModuleOutOfCodeBlockTrackerUpdater.onKotlinPhysicalFileOutOfBlockChange(ktFile, true)
                        } else {
                            perModuleOutOfCodeBlockTrackerUpdater.onKotlinPhysicalFileOutOfBlockChange(ktFile, false)
                            // Increment counter and process changes in PsiModificationTracker.Listener
                            modificationTrackerImpl.incCounter()
                        }
                    }

                    incOutOfBlockModificationCount(ktFile)
                }
            }
        })

        @Suppress("UnstableApiUsage")
        messageBusConnection.subscribe(PsiModificationTracker.TOPIC, PsiModificationTracker.Listener {
            if (isLanguageTrackerEnabled) {
                val kotlinTrackerInternalIDECount =
                    modificationTrackerImpl.forLanguage(KotlinLanguage.INSTANCE).modificationCount
                if (kotlinModificationTracker == kotlinTrackerInternalIDECount) {
                    // Some update that we are not sure is from Kotlin language, as Kotlin language tracker wasn't changed
                    kotlinOutOfCodeBlockTrackerImpl.incModificationCount()
                } else {
                    kotlinModificationTracker = kotlinTrackerInternalIDECount
                }
            }

            perModuleOutOfCodeBlockTrackerUpdater.onPsiModificationTrackerUpdate(customIncrement)
        })
    }

    override fun treeChanged(event: PsiTreeChangeEventImpl) {
        assert(isLanguageTrackerEnabled)

        if (!PsiModificationTrackerImpl.canAffectPsi(event)) {
            return
        }

        // Copy logic from PsiModificationTrackerImpl.treeChanged(). Some out-of-code-block events are written to language modification
        // tracker in PsiModificationTrackerImpl but don't have correspondent PomModelEvent. Increase kotlinOutOfCodeBlockTracker
        // manually if needed.
        val outOfCodeBlock = when (event.code) {
            PROPERTY_CHANGED ->
                event.propertyName === PsiTreeChangeEvent.PROP_UNLOADED_PSI || event.propertyName === PsiTreeChangeEvent.PROP_ROOTS
            CHILD_MOVED -> event.oldParent is PsiDirectory || event.newParent is PsiDirectory
            else -> event.parent is PsiDirectory
        }

        if (outOfCodeBlock) {
            kotlinOutOfCodeBlockTrackerImpl.incModificationCount()
        }
    }

    companion object {
        private fun isReplLine(file: VirtualFile): Boolean {
            return file.getUserData(KOTLIN_CONSOLE_KEY) == true
        }

        private fun incOutOfBlockModificationCount(file: KtFile) {
            file.cleanInBlockModifications()

            val count = file.getUserData(FILE_OUT_OF_BLOCK_MODIFICATION_COUNT) ?: 0
            file.putUserData(FILE_OUT_OF_BLOCK_MODIFICATION_COUNT, count + 1)
        }

        private fun incFileModificationCount(file: KtFile) {
            val tracker = file.getUserData(PER_FILE_MODIFICATION_TRACKER)
                ?: file.putUserDataIfAbsent(PER_FILE_MODIFICATION_TRACKER, SimpleModificationTracker())
            tracker.incModificationCount()
        }

        private fun incInBlockModificationCount(elements: Array<ASTNode>): Boolean {
            val inBlockElements = mutableSetOf<KtElement>()
            for (element in elements) {
                // skip fake PSI elements like `IntellijIdeaRulezzz$`
                if (!element.psi.isPhysical) continue

                for (parent in element.psi.parents()) {
                    ProgressIndicatorProvider.checkCanceled()

                    if (parent is KtFile) break
                    // TODO: generalization item: seems private items (methods/properties) could limited to the outer class

                    // TODO: could be generalized as well for other cases those could provide incremental analysis

                    val isInBlockElement = when (parent) {
                        // WARNING: top level function or regular class function only
                        // as it is not possible to perform incremental analysis for local declarations
                        is KtNamedFunction -> (parent.parent is KtFile || parent.parent?.parent?.parent is KtFile)
                        // top level class declarations only
                        is KtClass -> (parent.parent is KtFile)
                        // property of top level class declarations only
                        is KtProperty -> (parent.parent?.parent is KtClass && parent.parent?.parent?.parent is KtFile)
                        // top level script initializers only
                        is KtScriptInitializer -> (parent.parent?.parent?.parent is KtFile)
                        else -> false
                    }

                    if (isInBlockElement) {
                        inBlockElements.add(parent as KtElement)
                        break
                    }
                }
            }
            if (inBlockElements.isNotEmpty()) {
                inBlockElements.forEach { incInBlockModificationCount(it) }
            }
            return inBlockElements.isNotEmpty()
        }

        private fun incInBlockModificationCount(item: KtElement) {
            val inBlockModifications = item.containingKtFile.inBlockModifications
            inBlockModifications.add(item)
        }

        fun isFormattingChange(changeSet: TreeChangeEvent): Boolean =
            changeSet.changedElements.all {
                changeSet.getChangesByElement(it).affectedChildren.all { c -> (c is PsiWhiteSpace || c is PsiComment) }
            }

        fun getInsideCodeBlockModificationScope(element: PsiElement): KtElement? {
            ProgressIndicatorProvider.checkCanceled()

            val lambda = element.getTopmostParentOfType<KtLambdaExpression>()
            if (lambda is KtLambdaExpression) {
                lambda.getTopmostParentOfType<KtSuperTypeCallEntry>()?.let {
                    return it
                }
            }

            val blockDeclaration =
                KtPsiUtil.getTopmostParentOfTypes(element, *BLOCK_DECLARATION_TYPES) as? KtDeclaration ?:
                return null
            if (KtPsiUtil.isLocal(blockDeclaration)) return null // should not be local declaration

            when (blockDeclaration) {
                is KtNamedFunction -> {
                    if (blockDeclaration.hasBlockBody()) {
                        // case like `fun foo(): String = {...<caret>...}`
                        return blockDeclaration.bodyExpression?.takeIf { it.isAncestor(element) }
                    } else if (blockDeclaration.hasDeclaredReturnType()) {
                        // case like `fun foo(): String = b<caret>labla`
                        return blockDeclaration.initializer?.takeIf { it.isAncestor(element) }
                    }
                }

                is KtProperty -> {
                    if (blockDeclaration.typeReference != null) {
                        for (accessor in blockDeclaration.accessors) {
                            (accessor.initializer ?: accessor.bodyExpression)
                                ?.takeIf { it.isAncestor(element) || (element is KtPropertyAccessor && element.isAncestor(it)) }
                                ?.let { return it }
                        }
                        blockDeclaration.initializer
                            ?.takeIf { it.isAncestor(element) }
                            ?.let { return it }
                    }
                }

                is KtScriptInitializer -> {
                    return (blockDeclaration.body as? KtCallExpression)
                        ?.lambdaArguments
                        ?.lastOrNull()
                        ?.getLambdaExpression()
                        ?.takeIf { it.isAncestor(element) }
                }

                is KtClassInitializer -> {
                    blockDeclaration
                        .takeIf { it.isAncestor(element) }
                        ?.let { return it }
                }

                // TODO: still under consideration - is it worth to track changes of private properties / methods
                // problem could be in diagnostics - it is worth to manage it with modTracker
//                is KtClass -> {
//                    return when (element) {
//                        is KtProperty -> if (element.visibilityModifierType()?.toVisibility() == Visibilities.PRIVATE) blockDeclaration else null
//                        is KtNamedFunction -> if (element.visibilityModifierType()?.toVisibility() == Visibilities.PRIVATE) blockDeclaration else null
//                        else -> null
//                    }
//                }

                else -> throw IllegalStateException()
            }

            return null
        }

        fun isBlockDeclaration(declaration: KtDeclaration): Boolean {
            return BLOCK_DECLARATION_TYPES.any { it.isInstance(declaration) }
        }

        private val BLOCK_DECLARATION_TYPES = arrayOf<Class<out KtDeclaration>>(
            KtProperty::class.java,
            KtNamedFunction::class.java,
            KtClassInitializer::class.java,
            KtScriptInitializer::class.java
        )

        fun getInstance(project: Project): KotlinCodeBlockModificationListener =
            project.getComponent(KotlinCodeBlockModificationListener::class.java)
    }
}

private val PER_FILE_MODIFICATION_TRACKER = Key<SimpleModificationTracker>("FILE_OUT_OF_BLOCK_MODIFICATION_COUNT")

val KtFile.perFileModificationTracker: ModificationTracker
    get() = putUserDataIfAbsent(PER_FILE_MODIFICATION_TRACKER, SimpleModificationTracker())

private val FILE_OUT_OF_BLOCK_MODIFICATION_COUNT = Key<Long>("FILE_OUT_OF_BLOCK_MODIFICATION_COUNT")

private val IN_BLOCK_MODIFICATIONS = Key<MutableCollection<KtElement>>("IN_BLOCK_MODIFICATIONS")

val KtFile.outOfBlockModificationCount: Long by NotNullableUserDataProperty(FILE_OUT_OF_BLOCK_MODIFICATION_COUNT, 0)

/**
 * inBlockModificationCount means how many changes have been made since last outOfBlockModificationCount for this item
 * it is reset to 0 on any outOfBlockModificationCount
 */

val KtFile.inBlockModifications: MutableCollection<KtElement> by NotNullableUserDataProperty(IN_BLOCK_MODIFICATIONS, mutableSetOf())

fun KtFile.cleanInBlockModifications() {
    getUserData(IN_BLOCK_MODIFICATIONS)?.clear()
}