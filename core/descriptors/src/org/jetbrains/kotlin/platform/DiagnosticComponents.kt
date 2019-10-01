/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.platform

import org.jetbrains.kotlin.container.DefaultImplementation
import org.jetbrains.kotlin.container.PlatformSpecificExtension
import org.jetbrains.kotlin.name.FqName

@DefaultImplementation(impl = DiagnosticComponents.Default::class)
interface DiagnosticComponents : PlatformSpecificExtension<DiagnosticComponents> {
    val nullabilityAnnotations: Set<FqName>

    object Default : DiagnosticComponents {
        override val nullabilityAnnotations: Set<FqName> = emptySet()
    }
}