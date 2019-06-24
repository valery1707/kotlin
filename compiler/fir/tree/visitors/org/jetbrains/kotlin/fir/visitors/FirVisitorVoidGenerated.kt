/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */
package org.jetbrains.kotlin.fir.visitors

import org.jetbrains.kotlin.fir.*
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.impl.*
import org.jetbrains.kotlin.fir.expressions.*
import org.jetbrains.kotlin.fir.expressions.impl.*
import org.jetbrains.kotlin.fir.types.*


/** This file generated by :compiler:fir:tree:generateVisitors. DO NOT MODIFY MANUALLY! */
abstract class FirVisitorVoid : FirVisitor<Unit, Nothing?>() {
    abstract fun visitElement(element: FirElement)

    open fun visitCatch(catch: FirCatch) {
        visitElement(catch, null)
    }

    open fun visitDeclaration(declaration: FirDeclaration) {
        visitElement(declaration, null)
    }

    open fun <F : FirCallableDeclaration<F>> visitCallableDeclaration(callableDeclaration: FirCallableDeclaration<F>) {
        visitDeclaration(callableDeclaration, null)
    }

    open fun <F : FirCallableMemberDeclaration<F>> visitCallableMemberDeclaration(callableMemberDeclaration: FirCallableMemberDeclaration<F>) {
        visitDeclaration(callableMemberDeclaration, null)
    }

    open fun visitDeclarationWithBody(declarationWithBody: FirDeclarationWithBody) {
        visitDeclaration(declarationWithBody, null)
    }

    open fun visitAnonymousInitializer(anonymousInitializer: FirAnonymousInitializer) {
        visitDeclarationWithBody(anonymousInitializer, null)
    }

    open fun visitFunction(function: FirFunction) {
        visitDeclarationWithBody(function, null)
    }

    open fun visitAnonymousFunction(anonymousFunction: FirAnonymousFunction) {
        visitFunction(anonymousFunction, null)
    }

    open fun <F : FirMemberFunction<F>> visitMemberFunction(memberFunction: FirMemberFunction<F>) {
        visitFunction(memberFunction, null)
    }

    open fun visitConstructor(constructor: FirConstructor) {
        visitMemberFunction(constructor, null)
    }

    open fun visitNamedFunction(namedFunction: FirNamedFunction) {
        visitMemberFunction(namedFunction, null)
    }

    open fun visitModifiableFunction(modifiableFunction: FirModifiableFunction) {
        visitFunction(modifiableFunction, null)
    }

    open fun visitPropertyAccessor(propertyAccessor: FirPropertyAccessor) {
        visitFunction(propertyAccessor, null)
    }

    open fun visitDefaultPropertyAccessor(defaultPropertyAccessor: FirDefaultPropertyAccessor) {
        visitPropertyAccessor(defaultPropertyAccessor, null)
    }

    open fun visitErrorDeclaration(errorDeclaration: FirErrorDeclaration) {
        visitDeclaration(errorDeclaration, null)
    }

    open fun visitField(field: FirField) {
        visitDeclaration(field, null)
    }

    open fun visitNamedDeclaration(namedDeclaration: FirNamedDeclaration) {
        visitDeclaration(namedDeclaration, null)
    }

    open fun visitMemberDeclaration(memberDeclaration: FirMemberDeclaration) {
        visitNamedDeclaration(memberDeclaration, null)
    }

    open fun <F : FirClassLikeDeclaration<F>> visitClassLikeDeclaration(classLikeDeclaration: FirClassLikeDeclaration<F>) {
        visitMemberDeclaration(classLikeDeclaration, null)
    }

    open fun visitRegularClass(regularClass: FirRegularClass) {
        visitClassLikeDeclaration(regularClass, null)
    }

    open fun visitEnumEntry(enumEntry: FirEnumEntry) {
        visitRegularClass(enumEntry, null)
    }

    open fun visitTypeAlias(typeAlias: FirTypeAlias) {
        visitClassLikeDeclaration(typeAlias, null)
    }

    open fun visitTypeParameter(typeParameter: FirTypeParameter) {
        visitNamedDeclaration(typeParameter, null)
    }

    open fun visitProperty(property: FirProperty) {
        visitDeclaration(property, null)
    }

    open fun visitTypedDeclaration(typedDeclaration: FirTypedDeclaration) {
        visitDeclaration(typedDeclaration, null)
    }

    open fun visitValueParameter(valueParameter: FirValueParameter) {
        visitDeclaration(valueParameter, null)
    }

    open fun <F : FirVariable<F>> visitVariable(variable: FirVariable<F>) {
        visitDeclaration(variable, null)
    }

    open fun visitDeclarationStatus(declarationStatus: FirDeclarationStatus) {
        visitElement(declarationStatus, null)
    }

    open fun visitResolvedDeclarationStatus(resolvedDeclarationStatus: FirResolvedDeclarationStatus) {
        visitDeclarationStatus(resolvedDeclarationStatus, null)
    }

    open fun visitImport(import: FirImport) {
        visitElement(import, null)
    }

    open fun visitResolvedImport(resolvedImport: FirResolvedImport) {
        visitImport(resolvedImport, null)
    }

    open fun visitLabel(label: FirLabel) {
        visitElement(label, null)
    }

    open fun visitPackageFragment(packageFragment: FirPackageFragment) {
        visitElement(packageFragment, null)
    }

    open fun visitFile(file: FirFile) {
        visitPackageFragment(file, null)
    }

    open fun visitReference(reference: FirReference) {
        visitElement(reference, null)
    }

    open fun visitNamedReference(namedReference: FirNamedReference) {
        visitReference(namedReference, null)
    }

    open fun visitResolvedCallableReference(resolvedCallableReference: FirResolvedCallableReference) {
        visitNamedReference(resolvedCallableReference, null)
    }

    open fun visitBackingFieldReference(backingFieldReference: FirBackingFieldReference) {
        visitResolvedCallableReference(backingFieldReference, null)
    }

    open fun visitSuperReference(superReference: FirSuperReference) {
        visitReference(superReference, null)
    }

    open fun visitThisReference(thisReference: FirThisReference) {
        visitReference(thisReference, null)
    }

    open fun visitStatement(statement: FirStatement) {
        visitElement(statement, null)
    }

    open fun visitAnnotatedStatement(annotatedStatement: FirAnnotatedStatement) {
        visitStatement(annotatedStatement, null)
    }

    open fun visitExpression(expression: FirExpression) {
        visitAnnotatedStatement(expression, null)
    }

    open fun visitCall(call: FirCall) {
        visitExpression(call, null)
    }

    open fun visitArraySetCall(arraySetCall: FirArraySetCall) {
        visitCall(arraySetCall, null)
    }

    open fun visitCallWithArgumentList(callWithArgumentList: FirCallWithArgumentList) {
        visitCall(callWithArgumentList, null)
    }

    open fun visitAnnotationCall(annotationCall: FirAnnotationCall) {
        visitCallWithArgumentList(annotationCall, null)
    }

    open fun visitDelegatedConstructorCall(delegatedConstructorCall: FirDelegatedConstructorCall) {
        visitCallWithArgumentList(delegatedConstructorCall, null)
    }

    open fun visitOperatorCall(operatorCall: FirOperatorCall) {
        visitCallWithArgumentList(operatorCall, null)
    }

    open fun visitOperationBasedCall(operationBasedCall: FirOperationBasedCall) {
        visitOperatorCall(operationBasedCall, null)
    }

    open fun visitTypeOperatorCall(typeOperatorCall: FirTypeOperatorCall) {
        visitOperationBasedCall(typeOperatorCall, null)
    }

    open fun visitStringConcatenationCall(stringConcatenationCall: FirStringConcatenationCall) {
        visitCallWithArgumentList(stringConcatenationCall, null)
    }

    open fun visitUnknownTypeCallWithArgumentList(unknownTypeCallWithArgumentList: FirUnknownTypeCallWithArgumentList) {
        visitCallWithArgumentList(unknownTypeCallWithArgumentList, null)
    }

    open fun visitArrayOfCall(arrayOfCall: FirArrayOfCall) {
        visitUnknownTypeCallWithArgumentList(arrayOfCall, null)
    }

    open fun visitFunctionCall(functionCall: FirFunctionCall) {
        visitUnknownTypeCallWithArgumentList(functionCall, null)
    }

    open fun visitComponentCall(componentCall: FirComponentCall) {
        visitFunctionCall(componentCall, null)
    }

    open fun visitGetClassCall(getClassCall: FirGetClassCall) {
        visitUnknownTypeCallWithArgumentList(getClassCall, null)
    }

    open fun visitUncheckedNotNullCast(uncheckedNotNullCast: FirUncheckedNotNullCast) {
        visitUnknownTypeCallWithArgumentList(uncheckedNotNullCast, null)
    }

    open fun <E : FirTargetElement> visitJump(jump: FirJump<E>) {
        visitExpression(jump, null)
    }

    open fun visitLoopJump(loopJump: FirLoopJump) {
        visitJump(loopJump, null)
    }

    open fun visitBreakExpression(breakExpression: FirBreakExpression) {
        visitLoopJump(breakExpression, null)
    }

    open fun visitContinueExpression(continueExpression: FirContinueExpression) {
        visitLoopJump(continueExpression, null)
    }

    open fun visitReturnExpression(returnExpression: FirReturnExpression) {
        visitJump(returnExpression, null)
    }

    open fun visitThrowExpression(throwExpression: FirThrowExpression) {
        visitExpression(throwExpression, null)
    }

    open fun visitUnknownTypeExpression(unknownTypeExpression: FirUnknownTypeExpression) {
        visitExpression(unknownTypeExpression, null)
    }

    open fun visitBlock(block: FirBlock) {
        visitUnknownTypeExpression(block, null)
    }

    open fun visitClassReferenceExpression(classReferenceExpression: FirClassReferenceExpression) {
        visitUnknownTypeExpression(classReferenceExpression, null)
    }

    open fun <T> visitConstExpression(constExpression: FirConstExpression<T>) {
        visitUnknownTypeExpression(constExpression, null)
    }

    open fun visitErrorExpression(errorExpression: FirErrorExpression) {
        visitUnknownTypeExpression(errorExpression, null)
    }

    open fun visitQualifiedAccessExpression(qualifiedAccessExpression: FirQualifiedAccessExpression) {
        visitUnknownTypeExpression(qualifiedAccessExpression, null)
    }

    open fun visitCallableReferenceAccess(callableReferenceAccess: FirCallableReferenceAccess) {
        visitQualifiedAccessExpression(callableReferenceAccess, null)
    }

    open fun visitResolvedQualifier(resolvedQualifier: FirResolvedQualifier) {
        visitUnknownTypeExpression(resolvedQualifier, null)
    }

    open fun visitTryExpression(tryExpression: FirTryExpression) {
        visitUnknownTypeExpression(tryExpression, null)
    }

    open fun visitWhenExpression(whenExpression: FirWhenExpression) {
        visitUnknownTypeExpression(whenExpression, null)
    }

    open fun visitWhenSubjectExpression(whenSubjectExpression: FirWhenSubjectExpression) {
        visitUnknownTypeExpression(whenSubjectExpression, null)
    }

    open fun visitWrappedArgumentExpression(wrappedArgumentExpression: FirWrappedArgumentExpression) {
        visitExpression(wrappedArgumentExpression, null)
    }

    open fun visitLambdaArgumentExpression(lambdaArgumentExpression: FirLambdaArgumentExpression) {
        visitWrappedArgumentExpression(lambdaArgumentExpression, null)
    }

    open fun visitNamedArgumentExpression(namedArgumentExpression: FirNamedArgumentExpression) {
        visitWrappedArgumentExpression(namedArgumentExpression, null)
    }

    open fun visitSpreadArgumentExpression(spreadArgumentExpression: FirSpreadArgumentExpression) {
        visitWrappedArgumentExpression(spreadArgumentExpression, null)
    }

    open fun visitClass(klass: FirClass) {
        visitStatement(klass, null)
    }

    open fun visitAnonymousObject(anonymousObject: FirAnonymousObject) {
        visitClass(anonymousObject, null)
    }

    open fun visitModifiableClass(modifiableClass: FirModifiableClass) {
        visitClass(modifiableClass, null)
    }

    open fun visitErrorStatement(errorStatement: FirErrorStatement) {
        visitStatement(errorStatement, null)
    }

    open fun visitLoop(loop: FirLoop) {
        visitStatement(loop, null)
    }

    open fun visitDoWhileLoop(doWhileLoop: FirDoWhileLoop) {
        visitLoop(doWhileLoop, null)
    }

    open fun visitWhileLoop(whileLoop: FirWhileLoop) {
        visitLoop(whileLoop, null)
    }

    open fun visitQualifiedAccess(qualifiedAccess: FirQualifiedAccess) {
        visitStatement(qualifiedAccess, null)
    }

    open fun visitAssignment(assignment: FirAssignment) {
        visitQualifiedAccess(assignment, null)
    }

    open fun visitVariableAssignment(variableAssignment: FirVariableAssignment) {
        visitAssignment(variableAssignment, null)
    }

    open fun <C : FirReference> visitModifiableQualifiedAccess(modifiableQualifiedAccess: FirModifiableQualifiedAccess<C>) {
        visitQualifiedAccess(modifiableQualifiedAccess, null)
    }

    open fun visitTargetElement(targetElement: FirTargetElement) {
        visitElement(targetElement, null)
    }

    open fun visitLabeledElement(labeledElement: FirLabeledElement) {
        visitTargetElement(labeledElement, null)
    }

    open fun visitTypeProjection(typeProjection: FirTypeProjection) {
        visitElement(typeProjection, null)
    }

    open fun visitStarProjection(starProjection: FirStarProjection) {
        visitTypeProjection(starProjection, null)
    }

    open fun visitTypeProjectionWithVariance(typeProjectionWithVariance: FirTypeProjectionWithVariance) {
        visitTypeProjection(typeProjectionWithVariance, null)
    }

    open fun visitTypeRef(typeRef: FirTypeRef) {
        visitElement(typeRef, null)
    }

    open fun visitDelegatedTypeRef(delegatedTypeRef: FirDelegatedTypeRef) {
        visitTypeRef(delegatedTypeRef, null)
    }

    open fun visitImplicitTypeRef(implicitTypeRef: FirImplicitTypeRef) {
        visitTypeRef(implicitTypeRef, null)
    }

    open fun visitResolvedTypeRef(resolvedTypeRef: FirResolvedTypeRef) {
        visitTypeRef(resolvedTypeRef, null)
    }

    open fun visitErrorTypeRef(errorTypeRef: FirErrorTypeRef) {
        visitResolvedTypeRef(errorTypeRef, null)
    }

    open fun visitResolvedFunctionTypeRef(resolvedFunctionTypeRef: FirResolvedFunctionTypeRef) {
        visitResolvedTypeRef(resolvedFunctionTypeRef, null)
    }

    open fun visitTypeRefWithNullability(typeRefWithNullability: FirTypeRefWithNullability) {
        visitTypeRef(typeRefWithNullability, null)
    }

    open fun visitDynamicTypeRef(dynamicTypeRef: FirDynamicTypeRef) {
        visitTypeRefWithNullability(dynamicTypeRef, null)
    }

    open fun visitFunctionTypeRef(functionTypeRef: FirFunctionTypeRef) {
        visitTypeRefWithNullability(functionTypeRef, null)
    }

    open fun visitUserTypeRef(userTypeRef: FirUserTypeRef) {
        visitTypeRefWithNullability(userTypeRef, null)
    }

    open fun visitWhenBranch(whenBranch: FirWhenBranch) {
        visitElement(whenBranch, null)
    }

    final override fun visitAnnotatedStatement(annotatedStatement: FirAnnotatedStatement, data: Nothing?) {
        visitAnnotatedStatement(annotatedStatement)
    }

    final override fun visitAnnotationCall(annotationCall: FirAnnotationCall, data: Nothing?) {
        visitAnnotationCall(annotationCall)
    }

    final override fun visitAnonymousFunction(anonymousFunction: FirAnonymousFunction, data: Nothing?) {
        visitAnonymousFunction(anonymousFunction)
    }

    final override fun visitAnonymousInitializer(anonymousInitializer: FirAnonymousInitializer, data: Nothing?) {
        visitAnonymousInitializer(anonymousInitializer)
    }

    final override fun visitAnonymousObject(anonymousObject: FirAnonymousObject, data: Nothing?) {
        visitAnonymousObject(anonymousObject)
    }

    final override fun visitArrayOfCall(arrayOfCall: FirArrayOfCall, data: Nothing?) {
        visitArrayOfCall(arrayOfCall)
    }

    final override fun visitArraySetCall(arraySetCall: FirArraySetCall, data: Nothing?) {
        visitArraySetCall(arraySetCall)
    }

    final override fun visitAssignment(assignment: FirAssignment, data: Nothing?) {
        visitAssignment(assignment)
    }

    final override fun visitBackingFieldReference(backingFieldReference: FirBackingFieldReference, data: Nothing?) {
        visitBackingFieldReference(backingFieldReference)
    }

    final override fun visitBlock(block: FirBlock, data: Nothing?) {
        visitBlock(block)
    }

    final override fun visitBreakExpression(breakExpression: FirBreakExpression, data: Nothing?) {
        visitBreakExpression(breakExpression)
    }

    final override fun visitCall(call: FirCall, data: Nothing?) {
        visitCall(call)
    }

    final override fun visitCallWithArgumentList(callWithArgumentList: FirCallWithArgumentList, data: Nothing?) {
        visitCallWithArgumentList(callWithArgumentList)
    }

    final override fun <F : FirCallableDeclaration<F>> visitCallableDeclaration(callableDeclaration: FirCallableDeclaration<F>, data: Nothing?) {
        visitCallableDeclaration(callableDeclaration)
    }

    final override fun <F : FirCallableMemberDeclaration<F>> visitCallableMemberDeclaration(callableMemberDeclaration: FirCallableMemberDeclaration<F>, data: Nothing?) {
        visitCallableMemberDeclaration(callableMemberDeclaration)
    }

    final override fun visitCallableReferenceAccess(callableReferenceAccess: FirCallableReferenceAccess, data: Nothing?) {
        visitCallableReferenceAccess(callableReferenceAccess)
    }

    final override fun visitCatch(catch: FirCatch, data: Nothing?) {
        visitCatch(catch)
    }

    final override fun visitClass(klass: FirClass, data: Nothing?) {
        visitClass(klass)
    }

    final override fun <F : FirClassLikeDeclaration<F>> visitClassLikeDeclaration(classLikeDeclaration: FirClassLikeDeclaration<F>, data: Nothing?) {
        visitClassLikeDeclaration(classLikeDeclaration)
    }

    final override fun visitClassReferenceExpression(classReferenceExpression: FirClassReferenceExpression, data: Nothing?) {
        visitClassReferenceExpression(classReferenceExpression)
    }

    final override fun visitComponentCall(componentCall: FirComponentCall, data: Nothing?) {
        visitComponentCall(componentCall)
    }

    final override fun <T> visitConstExpression(constExpression: FirConstExpression<T>, data: Nothing?) {
        visitConstExpression(constExpression)
    }

    final override fun visitConstructor(constructor: FirConstructor, data: Nothing?) {
        visitConstructor(constructor)
    }

    final override fun visitContinueExpression(continueExpression: FirContinueExpression, data: Nothing?) {
        visitContinueExpression(continueExpression)
    }

    final override fun visitDeclaration(declaration: FirDeclaration, data: Nothing?) {
        visitDeclaration(declaration)
    }

    final override fun visitDeclarationStatus(declarationStatus: FirDeclarationStatus, data: Nothing?) {
        visitDeclarationStatus(declarationStatus)
    }

    final override fun visitDeclarationWithBody(declarationWithBody: FirDeclarationWithBody, data: Nothing?) {
        visitDeclarationWithBody(declarationWithBody)
    }

    final override fun visitDefaultPropertyAccessor(defaultPropertyAccessor: FirDefaultPropertyAccessor, data: Nothing?) {
        visitDefaultPropertyAccessor(defaultPropertyAccessor)
    }

    final override fun visitDelegatedConstructorCall(delegatedConstructorCall: FirDelegatedConstructorCall, data: Nothing?) {
        visitDelegatedConstructorCall(delegatedConstructorCall)
    }

    final override fun visitDelegatedTypeRef(delegatedTypeRef: FirDelegatedTypeRef, data: Nothing?) {
        visitDelegatedTypeRef(delegatedTypeRef)
    }

    final override fun visitDoWhileLoop(doWhileLoop: FirDoWhileLoop, data: Nothing?) {
        visitDoWhileLoop(doWhileLoop)
    }

    final override fun visitDynamicTypeRef(dynamicTypeRef: FirDynamicTypeRef, data: Nothing?) {
        visitDynamicTypeRef(dynamicTypeRef)
    }

    final override fun visitEnumEntry(enumEntry: FirEnumEntry, data: Nothing?) {
        visitEnumEntry(enumEntry)
    }

    final override fun visitErrorDeclaration(errorDeclaration: FirErrorDeclaration, data: Nothing?) {
        visitErrorDeclaration(errorDeclaration)
    }

    final override fun visitErrorExpression(errorExpression: FirErrorExpression, data: Nothing?) {
        visitErrorExpression(errorExpression)
    }

    final override fun visitErrorStatement(errorStatement: FirErrorStatement, data: Nothing?) {
        visitErrorStatement(errorStatement)
    }

    final override fun visitErrorTypeRef(errorTypeRef: FirErrorTypeRef, data: Nothing?) {
        visitErrorTypeRef(errorTypeRef)
    }

    final override fun visitExpression(expression: FirExpression, data: Nothing?) {
        visitExpression(expression)
    }

    final override fun visitField(field: FirField, data: Nothing?) {
        visitField(field)
    }

    final override fun visitFile(file: FirFile, data: Nothing?) {
        visitFile(file)
    }

    final override fun visitFunction(function: FirFunction, data: Nothing?) {
        visitFunction(function)
    }

    final override fun visitFunctionCall(functionCall: FirFunctionCall, data: Nothing?) {
        visitFunctionCall(functionCall)
    }

    final override fun visitFunctionTypeRef(functionTypeRef: FirFunctionTypeRef, data: Nothing?) {
        visitFunctionTypeRef(functionTypeRef)
    }

    final override fun visitGetClassCall(getClassCall: FirGetClassCall, data: Nothing?) {
        visitGetClassCall(getClassCall)
    }

    final override fun visitImplicitTypeRef(implicitTypeRef: FirImplicitTypeRef, data: Nothing?) {
        visitImplicitTypeRef(implicitTypeRef)
    }

    final override fun visitImport(import: FirImport, data: Nothing?) {
        visitImport(import)
    }

    final override fun <E : FirTargetElement> visitJump(jump: FirJump<E>, data: Nothing?) {
        visitJump(jump)
    }

    final override fun visitLabel(label: FirLabel, data: Nothing?) {
        visitLabel(label)
    }

    final override fun visitLabeledElement(labeledElement: FirLabeledElement, data: Nothing?) {
        visitLabeledElement(labeledElement)
    }

    final override fun visitLambdaArgumentExpression(lambdaArgumentExpression: FirLambdaArgumentExpression, data: Nothing?) {
        visitLambdaArgumentExpression(lambdaArgumentExpression)
    }

    final override fun visitLoop(loop: FirLoop, data: Nothing?) {
        visitLoop(loop)
    }

    final override fun visitLoopJump(loopJump: FirLoopJump, data: Nothing?) {
        visitLoopJump(loopJump)
    }

    final override fun visitMemberDeclaration(memberDeclaration: FirMemberDeclaration, data: Nothing?) {
        visitMemberDeclaration(memberDeclaration)
    }

    final override fun <F : FirMemberFunction<F>> visitMemberFunction(memberFunction: FirMemberFunction<F>, data: Nothing?) {
        visitMemberFunction(memberFunction)
    }

    final override fun visitModifiableClass(modifiableClass: FirModifiableClass, data: Nothing?) {
        visitModifiableClass(modifiableClass)
    }

    final override fun visitModifiableFunction(modifiableFunction: FirModifiableFunction, data: Nothing?) {
        visitModifiableFunction(modifiableFunction)
    }

    final override fun <C : FirReference> visitModifiableQualifiedAccess(modifiableQualifiedAccess: FirModifiableQualifiedAccess<C>, data: Nothing?) {
        visitModifiableQualifiedAccess(modifiableQualifiedAccess)
    }

    final override fun visitNamedArgumentExpression(namedArgumentExpression: FirNamedArgumentExpression, data: Nothing?) {
        visitNamedArgumentExpression(namedArgumentExpression)
    }

    final override fun visitNamedDeclaration(namedDeclaration: FirNamedDeclaration, data: Nothing?) {
        visitNamedDeclaration(namedDeclaration)
    }

    final override fun visitNamedFunction(namedFunction: FirNamedFunction, data: Nothing?) {
        visitNamedFunction(namedFunction)
    }

    final override fun visitNamedReference(namedReference: FirNamedReference, data: Nothing?) {
        visitNamedReference(namedReference)
    }

    final override fun visitOperationBasedCall(operationBasedCall: FirOperationBasedCall, data: Nothing?) {
        visitOperationBasedCall(operationBasedCall)
    }

    final override fun visitOperatorCall(operatorCall: FirOperatorCall, data: Nothing?) {
        visitOperatorCall(operatorCall)
    }

    final override fun visitPackageFragment(packageFragment: FirPackageFragment, data: Nothing?) {
        visitPackageFragment(packageFragment)
    }

    final override fun visitProperty(property: FirProperty, data: Nothing?) {
        visitProperty(property)
    }

    final override fun visitPropertyAccessor(propertyAccessor: FirPropertyAccessor, data: Nothing?) {
        visitPropertyAccessor(propertyAccessor)
    }

    final override fun visitQualifiedAccess(qualifiedAccess: FirQualifiedAccess, data: Nothing?) {
        visitQualifiedAccess(qualifiedAccess)
    }

    final override fun visitQualifiedAccessExpression(qualifiedAccessExpression: FirQualifiedAccessExpression, data: Nothing?) {
        visitQualifiedAccessExpression(qualifiedAccessExpression)
    }

    final override fun visitReference(reference: FirReference, data: Nothing?) {
        visitReference(reference)
    }

    final override fun visitRegularClass(regularClass: FirRegularClass, data: Nothing?) {
        visitRegularClass(regularClass)
    }

    final override fun visitResolvedCallableReference(resolvedCallableReference: FirResolvedCallableReference, data: Nothing?) {
        visitResolvedCallableReference(resolvedCallableReference)
    }

    final override fun visitResolvedDeclarationStatus(resolvedDeclarationStatus: FirResolvedDeclarationStatus, data: Nothing?) {
        visitResolvedDeclarationStatus(resolvedDeclarationStatus)
    }

    final override fun visitResolvedFunctionTypeRef(resolvedFunctionTypeRef: FirResolvedFunctionTypeRef, data: Nothing?) {
        visitResolvedFunctionTypeRef(resolvedFunctionTypeRef)
    }

    final override fun visitResolvedImport(resolvedImport: FirResolvedImport, data: Nothing?) {
        visitResolvedImport(resolvedImport)
    }

    final override fun visitResolvedQualifier(resolvedQualifier: FirResolvedQualifier, data: Nothing?) {
        visitResolvedQualifier(resolvedQualifier)
    }

    final override fun visitResolvedTypeRef(resolvedTypeRef: FirResolvedTypeRef, data: Nothing?) {
        visitResolvedTypeRef(resolvedTypeRef)
    }

    final override fun visitReturnExpression(returnExpression: FirReturnExpression, data: Nothing?) {
        visitReturnExpression(returnExpression)
    }

    final override fun visitSpreadArgumentExpression(spreadArgumentExpression: FirSpreadArgumentExpression, data: Nothing?) {
        visitSpreadArgumentExpression(spreadArgumentExpression)
    }

    final override fun visitStarProjection(starProjection: FirStarProjection, data: Nothing?) {
        visitStarProjection(starProjection)
    }

    final override fun visitStatement(statement: FirStatement, data: Nothing?) {
        visitStatement(statement)
    }

    final override fun visitStringConcatenationCall(stringConcatenationCall: FirStringConcatenationCall, data: Nothing?) {
        visitStringConcatenationCall(stringConcatenationCall)
    }

    final override fun visitSuperReference(superReference: FirSuperReference, data: Nothing?) {
        visitSuperReference(superReference)
    }

    final override fun visitTargetElement(targetElement: FirTargetElement, data: Nothing?) {
        visitTargetElement(targetElement)
    }

    final override fun visitThisReference(thisReference: FirThisReference, data: Nothing?) {
        visitThisReference(thisReference)
    }

    final override fun visitThrowExpression(throwExpression: FirThrowExpression, data: Nothing?) {
        visitThrowExpression(throwExpression)
    }

    final override fun visitTryExpression(tryExpression: FirTryExpression, data: Nothing?) {
        visitTryExpression(tryExpression)
    }

    final override fun visitTypeAlias(typeAlias: FirTypeAlias, data: Nothing?) {
        visitTypeAlias(typeAlias)
    }

    final override fun visitTypeOperatorCall(typeOperatorCall: FirTypeOperatorCall, data: Nothing?) {
        visitTypeOperatorCall(typeOperatorCall)
    }

    final override fun visitTypeParameter(typeParameter: FirTypeParameter, data: Nothing?) {
        visitTypeParameter(typeParameter)
    }

    final override fun visitTypeProjection(typeProjection: FirTypeProjection, data: Nothing?) {
        visitTypeProjection(typeProjection)
    }

    final override fun visitTypeProjectionWithVariance(typeProjectionWithVariance: FirTypeProjectionWithVariance, data: Nothing?) {
        visitTypeProjectionWithVariance(typeProjectionWithVariance)
    }

    final override fun visitTypeRef(typeRef: FirTypeRef, data: Nothing?) {
        visitTypeRef(typeRef)
    }

    final override fun visitTypeRefWithNullability(typeRefWithNullability: FirTypeRefWithNullability, data: Nothing?) {
        visitTypeRefWithNullability(typeRefWithNullability)
    }

    final override fun visitTypedDeclaration(typedDeclaration: FirTypedDeclaration, data: Nothing?) {
        visitTypedDeclaration(typedDeclaration)
    }

    final override fun visitUncheckedNotNullCast(uncheckedNotNullCast: FirUncheckedNotNullCast, data: Nothing?) {
        visitUncheckedNotNullCast(uncheckedNotNullCast)
    }

    final override fun visitUnknownTypeCallWithArgumentList(unknownTypeCallWithArgumentList: FirUnknownTypeCallWithArgumentList, data: Nothing?) {
        visitUnknownTypeCallWithArgumentList(unknownTypeCallWithArgumentList)
    }

    final override fun visitUnknownTypeExpression(unknownTypeExpression: FirUnknownTypeExpression, data: Nothing?) {
        visitUnknownTypeExpression(unknownTypeExpression)
    }

    final override fun visitUserTypeRef(userTypeRef: FirUserTypeRef, data: Nothing?) {
        visitUserTypeRef(userTypeRef)
    }

    final override fun visitValueParameter(valueParameter: FirValueParameter, data: Nothing?) {
        visitValueParameter(valueParameter)
    }

    final override fun <F : FirVariable<F>> visitVariable(variable: FirVariable<F>, data: Nothing?) {
        visitVariable(variable)
    }

    final override fun visitVariableAssignment(variableAssignment: FirVariableAssignment, data: Nothing?) {
        visitVariableAssignment(variableAssignment)
    }

    final override fun visitWhenBranch(whenBranch: FirWhenBranch, data: Nothing?) {
        visitWhenBranch(whenBranch)
    }

    final override fun visitWhenExpression(whenExpression: FirWhenExpression, data: Nothing?) {
        visitWhenExpression(whenExpression)
    }

    final override fun visitWhenSubjectExpression(whenSubjectExpression: FirWhenSubjectExpression, data: Nothing?) {
        visitWhenSubjectExpression(whenSubjectExpression)
    }

    final override fun visitWhileLoop(whileLoop: FirWhileLoop, data: Nothing?) {
        visitWhileLoop(whileLoop)
    }

    final override fun visitWrappedArgumentExpression(wrappedArgumentExpression: FirWrappedArgumentExpression, data: Nothing?) {
        visitWrappedArgumentExpression(wrappedArgumentExpression)
    }

    final override fun visitElement(element: FirElement, data: Nothing?) {
        visitElement(element)
    }

}
