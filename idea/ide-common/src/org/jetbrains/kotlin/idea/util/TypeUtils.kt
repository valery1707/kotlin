/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("TypeUtils")

package org.jetbrains.kotlin.idea.util

import org.jetbrains.kotlin.builtins.getReturnTypeFromFunctionType
import org.jetbrains.kotlin.builtins.isBuiltinFunctionalType
import org.jetbrains.kotlin.builtins.jvm.JavaToKotlinClassMap
import org.jetbrains.kotlin.builtins.replaceReturnType
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.TypeParameterDescriptor
import org.jetbrains.kotlin.idea.imports.canBeReferencedViaImport
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.resolve.DescriptorUtils
import org.jetbrains.kotlin.resolve.descriptorUtil.builtIns
import org.jetbrains.kotlin.resolve.scopes.LexicalScope
import org.jetbrains.kotlin.resolve.scopes.utils.findClassifier
import org.jetbrains.kotlin.types.*
import org.jetbrains.kotlin.types.typeUtil.*
import org.jetbrains.kotlin.utils.SmartSet

fun KotlinType.approximateFlexibleTypes(
    preferNotNull: Boolean = false,
    preferStarForRaw: Boolean = false
): KotlinType {
    if (isDynamic()) return this
    return unwrapEnhancement().approximateNonDynamicFlexibleTypes(preferNotNull, preferStarForRaw)
}

private fun KotlinType.approximateNonDynamicFlexibleTypes(
    preferNotNull: Boolean = false,
    preferStarForRaw: Boolean = false
): SimpleType {
    if (this is ErrorType) return this

    if (isFlexible()) {
        val flexible = asFlexibleType()
        val lowerClass = flexible.lowerBound.constructor.declarationDescriptor as? ClassDescriptor?
        val isCollection = lowerClass != null && JavaToKotlinClassMap.isMutable(lowerClass)
        // (Mutable)Collection<T>! -> MutableCollection<T>?
        // Foo<(Mutable)Collection<T>!>! -> Foo<Collection<T>>?
        // Foo! -> Foo?
        // Foo<Bar!>! -> Foo<Bar>?
        var approximation =
            if (isCollection)
                flexible.lowerBound.makeNullableAsSpecified(!preferNotNull)
            else
                if (this is RawType && preferStarForRaw) flexible.upperBound.makeNullableAsSpecified(!preferNotNull)
                else
                    if (preferNotNull) flexible.lowerBound else flexible.upperBound

        approximation = approximation.approximateNonDynamicFlexibleTypes()

        approximation = if (nullability() == TypeNullability.NOT_NULL) approximation.makeNullableAsSpecified(false) else approximation

        if (approximation.isMarkedNullable && !flexible.lowerBound.isMarkedNullable && TypeUtils.isTypeParameter(approximation) && TypeUtils.hasNullableSuperType(
                approximation
            )
        ) {
            approximation = approximation.makeNullableAsSpecified(false)
        }

        return approximation
    }

    (unwrap() as? AbbreviatedType)?.let {
        return AbbreviatedType(it.expandedType, it.abbreviation.approximateNonDynamicFlexibleTypes(preferNotNull))
    }
    return KotlinTypeFactory.simpleTypeWithNonTrivialMemberScope(
        annotations,
        constructor,
        arguments.map { it.substitute { type -> type.approximateFlexibleTypes(preferNotNull = true) } },
        isMarkedNullable,
        ErrorUtils.createErrorScope("This type is not supposed to be used in member resolution", true)
    )
}

fun KotlinType.isResolvableInScope(scope: LexicalScope?, checkTypeParameters: Boolean, allowIntersections: Boolean = false): Boolean {
    if (constructor is IntersectionTypeConstructor) {
        if (!allowIntersections) return false
        return constructor.supertypes.all { it.isResolvableInScope(scope, checkTypeParameters, allowIntersections) }
    }

    if (canBeReferencedViaImport()) return true

    val descriptor = constructor.declarationDescriptor
    if (descriptor == null || descriptor.name.isSpecial) return false
    if (!checkTypeParameters && descriptor is TypeParameterDescriptor) return true

    return scope != null && scope.findClassifier(descriptor.name, NoLookupLocation.FROM_IDE) == descriptor
}

fun KotlinType.approximateWithResolvableType(scope: LexicalScope?, checkTypeParameters: Boolean): KotlinType {
    if (isError || isResolvableInScope(scope, checkTypeParameters)) return this
    return supertypes().firstOrNull { it.isResolvableInScope(scope, checkTypeParameters) }
        ?: builtIns.anyType
}

fun KotlinType.anonymousObjectSuperTypeOrNull(): KotlinType? {
    val classDescriptor = constructor.declarationDescriptor
    if (classDescriptor != null && DescriptorUtils.isAnonymousObject(classDescriptor)) {
        return immediateSupertypes().firstOrNull() ?: classDescriptor.builtIns.anyType
    }
    return null
}

fun KotlinType.getResolvableApproximations(
    scope: LexicalScope?,
    checkTypeParameters: Boolean,
    allowIntersections: Boolean = false
): Sequence<KotlinType> {
    return (listOf(this) + TypeUtils.getAllSupertypes(this))
        .asSequence()
        .mapNotNull {
            it.asTypeProjection()
                .fixTypeProjection(scope, checkTypeParameters, allowIntersections, isOutVariance = true)
                ?.type
        }
}

private fun TypeProjection.fixTypeProjection(
    scope: LexicalScope?,
    checkTypeParameters: Boolean,
    allowIntersections: Boolean,
    isOutVariance: Boolean
): TypeProjection? {
    if (!type.isResolvableInScope(scope, checkTypeParameters, allowIntersections)) return null
    if (type.arguments.isEmpty()) return this

    val resolvableArgs = type.arguments.filterTo(SmartSet.create()) { typeProjection ->
        typeProjection.type.isResolvableInScope(scope, checkTypeParameters, allowIntersections)
    }

    if (resolvableArgs.containsAll(type.arguments)) {
        fun fixArguments(type: KotlinType): KotlinType? = type.replace(
            (type.arguments zip type.constructor.parameters).map { (arg, param) ->
                if (arg.isStarProjection) arg
                else arg.fixTypeProjection(
                    scope,
                    checkTypeParameters,
                    allowIntersections,
                    isOutVariance = isOutVariance && param.variance == Variance.OUT_VARIANCE
                ) ?: when {
                    !isOutVariance -> return null
                    param.variance == Variance.OUT_VARIANCE -> arg.type.approximateWithResolvableType(
                        scope,
                        checkTypeParameters
                    ).asTypeProjection()
                    else -> type.replaceArgumentsWithStarProjections().arguments.first()
                }
            })

        return if (type.isBuiltinFunctionalType) {
            val returnType = type.getReturnTypeFromFunctionType()
            type.replaceReturnType(fixArguments(returnType) ?: return null).asTypeProjection()
        } else fixArguments(type)?.asTypeProjection()
    }

    if (!isOutVariance) return null

    val newArguments = (type.arguments zip type.constructor.parameters).map { (arg, param) ->
        when {
            arg in resolvableArgs -> arg

            arg.projectionKind == Variance.OUT_VARIANCE ||
                    param.variance == Variance.OUT_VARIANCE -> TypeProjectionImpl(
                arg.projectionKind,
                arg.type.approximateWithResolvableType(scope, checkTypeParameters)
            )

            else -> return if (isOutVariance) type.replaceArgumentsWithStarProjections().asTypeProjection() else null
        }
    }

    return type.replace(newArguments).asTypeProjection()
}

fun KotlinType.isAbstract(): Boolean {
    val modality = (constructor.declarationDescriptor as? ClassDescriptor)?.modality
    return modality == Modality.ABSTRACT || modality == Modality.SEALED
}
