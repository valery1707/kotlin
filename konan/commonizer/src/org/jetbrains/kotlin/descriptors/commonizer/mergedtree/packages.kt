/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.descriptors.commonizer.mergedtree

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor
import org.jetbrains.kotlin.descriptors.TypeAliasDescriptor
import org.jetbrains.kotlin.descriptors.commonizer.CommonizedGroupMap
import org.jetbrains.kotlin.descriptors.commonizer.mergedtree.ir.PackageNode
import org.jetbrains.kotlin.descriptors.commonizer.mergedtree.ir.RootNode
import org.jetbrains.kotlin.descriptors.commonizer.mergedtree.ir.buildPackageNode
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.scopes.MemberScope
import org.jetbrains.kotlin.storage.StorageManager

internal fun mergePackages(
    storageManager: StorageManager,
    cacheRW: RootNode.ClassifiersCacheImpl,
    packageFqName: FqName,
    packageMemberScopes: List<MemberScope?>
): PackageNode {
    val node = buildPackageNode(packageFqName, packageMemberScopes)

    val propertiesMap = CommonizedGroupMap<PropertyApproximationKey, PropertyDescriptor>(packageMemberScopes.size)
    val functionsMap = CommonizedGroupMap<FunctionApproximationKey, SimpleFunctionDescriptor>(packageMemberScopes.size)
    val classesMap = CommonizedGroupMap<Name, ClassDescriptor>(packageMemberScopes.size)
    val typeAliasesMap = CommonizedGroupMap<Name, TypeAliasDescriptor>(packageMemberScopes.size)

    packageMemberScopes.forEachIndexed { index, memberScope ->
        memberScope?.collectMembers(
            CallableMemberCollector<PropertyDescriptor> { propertiesMap[PropertyApproximationKey(it)][index] = it },
            CallableMemberCollector<SimpleFunctionDescriptor> { functionsMap[FunctionApproximationKey(it)][index] = it },
            Collector<ClassDescriptor> { classesMap[it.name][index] = it },
            Collector<TypeAliasDescriptor> { typeAliasesMap[it.name][index] = it }
        )
    }

    for ((_, propertiesGroup) in propertiesMap) {
        node.properties += mergeProperties(storageManager, cacheRW, null, propertiesGroup.toList())
    }

    for ((_, functionsGroup) in functionsMap) {
        node.functions += mergeFunctions(storageManager, cacheRW, null, functionsGroup.toList())
    }

    for ((_, classesGroup) in classesMap) {
        node.classes += mergeClasses(storageManager, cacheRW, null, classesGroup.toList())
    }

    for ((_, typeAliasesGroup) in typeAliasesMap) {
        node.typeAliases += mergeTypeAliases(storageManager, cacheRW, typeAliasesGroup.toList())
    }

    return node
}
