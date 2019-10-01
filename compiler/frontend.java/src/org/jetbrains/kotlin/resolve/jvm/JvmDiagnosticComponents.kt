/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.resolve.jvm

import org.jetbrains.kotlin.load.java.NULLABILITY_ANNOTATIONS
import org.jetbrains.kotlin.platform.DiagnosticComponents

object JvmDiagnosticComponents : DiagnosticComponents {
    override val nullabilityAnnotations
        get() = NULLABILITY_ANNOTATIONS
}