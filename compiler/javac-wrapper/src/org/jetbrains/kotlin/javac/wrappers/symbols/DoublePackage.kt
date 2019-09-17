/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.javac.wrappers.symbols

import org.jetbrains.kotlin.javac.wrappers.trees.TreeBasedPackage
import org.jetbrains.kotlin.load.java.structure.JavaAnnotation
import org.jetbrains.kotlin.load.java.structure.JavaClass
import org.jetbrains.kotlin.load.java.structure.JavaPackage
import org.jetbrains.kotlin.load.java.structure.MapBasedJavaAnnotationOwner
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class DoublePackage(
    private val treeBased: TreeBasedPackage, private val symbolBased: SymbolBasedPackage
) : JavaPackage, MapBasedJavaAnnotationOwner {
    override val annotationsByFqName: Map<FqName?, JavaAnnotation>
        get() = treeBased.annotationsByFqName + symbolBased.annotationsByFqName

    override val fqName: FqName
        get() = treeBased.fqName

    override val subPackages: Collection<JavaPackage>
        get() = treeBased.subPackages + symbolBased.subPackages

    override fun getClasses(nameFilter: (Name) -> Boolean): Collection<JavaClass> =
        treeBased.getClasses(nameFilter) + symbolBased.getClasses(nameFilter)

    override val annotations: Collection<JavaAnnotation>
        get() = treeBased.annotations + symbolBased.annotations
}