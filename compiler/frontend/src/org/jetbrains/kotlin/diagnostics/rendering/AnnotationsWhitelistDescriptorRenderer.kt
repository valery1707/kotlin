/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.diagnostics.rendering

import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.renderer.DescriptorRenderer

data class DeclarationWithAnnotationsWhitelist(
    val declaration: DeclarationDescriptor,
    val annotationsWhitelist: Set<FqName>
)

class AnnotationsWhitelistDescriptorRenderer(
    private val baseRenderer: DescriptorRenderer,
    private val toParameterRenderer: DescriptorRenderer.() -> DiagnosticParameterRenderer<DeclarationDescriptor>
) : DiagnosticParameterRenderer<DeclarationWithAnnotationsWhitelist> {
    override fun render(obj: DeclarationWithAnnotationsWhitelist, renderingContext: RenderingContext): String {
        val (descriptor, annotationsWhitelist) = obj
        return baseRenderer.withOptions {
            annotationFilter = { annotation ->
                annotation.fqName in annotationsWhitelist
            }
        }.toParameterRenderer().render(descriptor, renderingContext)
    }
}

fun DescriptorRenderer.withAnnotationsWhitelist(
    toParameterRenderer: DescriptorRenderer.() -> DiagnosticParameterRenderer<DeclarationDescriptor> = DescriptorRenderer::asRenderer
) = AnnotationsWhitelistDescriptorRenderer(this, toParameterRenderer)
