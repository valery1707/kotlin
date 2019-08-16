/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.core.script

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.EditorNotifications
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import org.jetbrains.kotlin.idea.core.script.ScriptDependenciesManager.Companion.toVfsRoots
import org.jetbrains.kotlin.idea.core.script.dependencies.FromRefinedConfigurationLoader
import org.jetbrains.kotlin.idea.core.script.dependencies.OutsiderFileDependenciesLoader
import org.jetbrains.kotlin.idea.core.script.dependencies.ScriptConfigurationFileAttributeCache
import org.jetbrains.kotlin.idea.core.script.settings.KotlinScriptingSettings
import org.jetbrains.kotlin.idea.core.util.EDT
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.scripting.definitions.findScriptDefinition
import org.jetbrains.kotlin.scripting.resolve.ScriptCompilationConfigurationResult
import org.jetbrains.kotlin.scripting.resolve.ScriptReportSink
import kotlin.script.experimental.api.valueOrNull

class ScriptConfigurationManagerImpl internal constructor(
    private val project: Project
) : ScriptConfigurationManager, ScriptDependenciesManager {
    private val rootsManager = ScriptClassRootsManager(project)
    private val memoryCache = ScriptConfigurationMemoryCache(project)
    private val fileAttributesCache = ScriptConfigurationFileAttributeCache(this)
    private val fromRefinedLoader = FromRefinedConfigurationLoader(project, this, rootsManager)
    private val loaders = arrayListOf(
        fileAttributesCache,
        OutsiderFileDependenciesLoader(this),
        fromRefinedLoader
    )
    private val listener = ScriptsListener(project, this)

    /**
     * Save configurations into cache.
     * Start indexing for new class/source roots.
     * Re-highlight opened scripts with changed configuration.
     */
    override fun saveCompilationConfigurationAfterImport(files: List<Pair<VirtualFile, ScriptCompilationConfigurationResult>>) {
        rootsManager.transaction {
            for ((file, result) in files) {
                saveConfiguration(file, result, skipNotification = true)
            }
        }
    }

    /**
     * Start configuration update for files if configuration isn't up to date.
     * Start indexing for new class/source roots.
     *
     * @return true if update was started for any file, false if all configurations are cached
     */
    override fun updateConfigurationsIfNotCached(files: List<KtFile>): Boolean {
        if (!ScriptDefinitionsManager.getInstance(project).isReady()) return false

        val notCached = files.filterNot { isConfigurationUpToDate(it.originalFile.virtualFile) }
        if (notCached.isNotEmpty()) {
            rootsManager.transaction {
                for (file in notCached) {
                    reloadConfiguration(file)
                }
            }
            return true
        }

        return false
    }

    /**
     * Check if configuration is already cached for [file] (in cache or FileAttributes).
     * Don't check if file was changed after the last update.
     * Supposed to be used to switch highlighting off for scripts without configuration.
     * to avoid all file being highlighted in red.
     */
    override fun isConfigurationCached(file: KtFile): Boolean {
        return isConfigurationCached(file.originalFile.virtualFile)
    }

    /**
     * Clear configuration caches
     * Start re-highlighting for opened scripts
     */
    override fun clearConfigurationCachesAndRehighlight() {
        clearAndRehighlight()
    }

    @Deprecated("Use getScriptClasspath(KtFile) instead")
    override fun getScriptClasspath(file: VirtualFile): List<VirtualFile> {
        val ktFile = PsiManager.getInstance(project).findFile(file) as? KtFile ?: return emptyList()
        return getScriptClasspath(ktFile)
    }

    override fun getScriptClasspath(file: KtFile): List<VirtualFile> =
        toVfsRoots(getConfiguration(file)?.valueOrNull()?.dependenciesClassPath.orEmpty())

    override fun getConfiguration(file: KtFile): ScriptCompilationConfigurationResult? {
        val virtualFile = file.originalFile.virtualFile

        val cached = getCachedConfiguration(virtualFile)
        if (cached != null) {
            return cached
        }

        if (!isConfigurationUpToDate(virtualFile)) {
            rootsManager.transaction {
                reloadConfiguration(file)
            }
        }

        return getCachedConfiguration(virtualFile)
    }

    override fun getScriptDependenciesClassFilesScope(file: VirtualFile) = scriptDependenciesClassFilesScope(file)
    override fun getScriptSdk(file: VirtualFile) = scriptSdk(file)

    override fun getFirstScriptsSdk() = memoryCache.firstScriptSdk

    override fun getAllScriptsDependenciesClassFilesScope() = memoryCache.allDependenciesClassFilesScope
    override fun getAllScriptDependenciesSourcesScope() = memoryCache.allDependenciesSourcesScope

    override fun getAllScriptsDependenciesClassFiles() = memoryCache.allDependenciesClassFiles
    override fun getAllScriptDependenciesSources() = memoryCache.allDependenciesSources

    @TestOnly
    fun updateScriptDependenciesSynchronously(file: PsiFile) {
        val scriptDefinition = file.findScriptDefinition() ?: return
        assert(file is KtFile) {
            "PsiFile should be a KtFile, otherwise script dependencies cannot be loaded"
        }

        rootsManager.transaction {
            fromRefinedLoader.runDependenciesUpdate(file as KtFile, scriptDefinition)
        }
    }

    private fun reloadConfiguration(file: KtFile) {
        if (!ScriptDefinitionsManager.getInstance(project).isReady()) return

        val scriptDefinition = file.findScriptDefinition() ?: return
        loaders.filter {
            it.isApplicable(file, scriptDefinition)
        }.forEach {
            it.loadDependencies(file, scriptDefinition)
        }
    }

    /**
     * Save [newConfiguration] for [file] into caches and update highlih.
     * Should be called inside `rootsManager.transaction { ... }`.
     *
     * @param skipNotification forces loading new configuration even if auto reload is disabled.
     * @param skipSaveToAttributes skips saving to FileAttributes (used in [ScriptConfigurationFileAttributeCache] only).
     *
     * @sample ScriptDependenciesManager.getConfiguration
     */
    internal fun saveConfiguration(
        file: VirtualFile,
        newConfiguration: ScriptCompilationConfigurationResult,
        skipNotification: Boolean = false,
        skipSaveToAttributes: Boolean = false
    ) {
        debug(file) { "configuration received = $newConfiguration" }

        val oldConfiguration = getCachedConfiguration(file)
        if (oldConfiguration == newConfiguration) {
            file.removeScriptDependenciesNotificationPanel(project)
        } else {
            val autoReload = skipNotification
                    || oldConfiguration == null
                    || oldConfiguration.valueOrNull() == newConfiguration.valueOrNull()
                    || KotlinScriptingSettings.getInstance(project).isAutoReloadEnabled
                    || ApplicationManager.getApplication().isUnitTestMode

            if (autoReload) {
                file.removeScriptDependenciesNotificationPanel(project)
                saveChangedConfiguration(file, oldConfiguration, newConfiguration, skipSaveToAttributes)
            } else {
                debug(file) {
                    "configuration changed, notification is shown: old = $oldConfiguration, new = $newConfiguration"
                }
                file.addScriptDependenciesNotificationPanel(
                    newConfiguration, project,
                    onClick = {
                        file.removeScriptDependenciesNotificationPanel(project)
                        rootsManager.transaction {
                            saveChangedConfiguration(file, getCachedConfiguration(file), it, skipSaveToAttributes)
                        }
                    }
                )
            }
        }
    }

    private fun saveChangedConfiguration(
        file: VirtualFile,
        oldConfiguration: ScriptCompilationConfigurationResult?,
        newConfiguration: ScriptCompilationConfigurationResult,
        skipSaveToAttributes: Boolean
    ) {
        val oldReports = IdeScriptReportSink.getReports(file)
        val newReports = newConfiguration.reports
        if (oldReports != newReports) {
            debug(file) { "new script reports = $newReports" }

            ServiceManager.getService(
                project,
                ScriptReportSink::class.java
            ).attachReports(file, newReports)
        }

        val oldValue = oldConfiguration?.valueOrNull()
        val newValue = newConfiguration.valueOrNull()
        if (oldValue != newValue) {
            debug(file) { "configuration changed = $newConfiguration" }

            if (newValue != null) {
                rootsManager.checkNonCachedRoots(memoryCache, file, newValue)

                if (!skipSaveToAttributes) {
                    debug(file) { "configuration saved to file attributes: $newConfiguration" }

                    fileAttributesCache.save(file, newValue)
                }

                memoryCache.replaceConfiguration(file, newConfiguration)
                memoryCache.clearClassRootsCaches()
            }
        }

        updateHighlighting(listOf(file))
    }

    private fun updateHighlighting(files: List<VirtualFile>) {
        if (files.isEmpty()) return

        GlobalScope.launch(EDT(project)) {
            if (project.isDisposed) return@launch

            val openFiles = FileEditorManager.getInstance(project).openFiles
            val openScripts = files.filter { it.isValid && openFiles.contains(it) }
            if (openScripts.isNotEmpty()) {
                EditorNotifications.getInstance(project).updateAllNotifications()
            }

            openScripts.forEach {
                PsiManager.getInstance(project).findFile(it)?.let { psiFile ->
                    DaemonCodeAnalyzer.getInstance(project).restart(psiFile)
                }
            }
        }
    }

    private fun getCachedConfiguration(file: VirtualFile): ScriptCompilationConfigurationResult? =
        memoryCache.getCachedConfiguration(file)

    private fun isConfigurationCached(file: VirtualFile): Boolean {
        return getCachedConfiguration(file) != null || file in fileAttributesCache
    }

    private fun isConfigurationUpToDate(file: VirtualFile): Boolean {
        return isConfigurationCached(file) && memoryCache.isConfigurationUpToDate(file)
    }

    private fun clearAndRehighlight() {
        updateHighlighting(memoryCache.clearConfigurationCaches())
    }

    private fun scriptDependenciesClassFilesScope(file: VirtualFile): GlobalSearchScope {
        return memoryCache.scriptsDependenciesClasspathScopeCache[file] ?: GlobalSearchScope.EMPTY_SCOPE
    }

    private fun scriptSdk(file: VirtualFile): Sdk? {
        return memoryCache.scriptsSdksCache[file]
    }

    companion object {
        internal val log =
            Logger.getInstance(ScriptDependenciesManager::class.java)
    }
}