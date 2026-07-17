package com.yourcompany.lookml.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.yourcompany.lookml.license.LicenseConditions
import com.yourcompany.lookml.references.LookMLReferences

/**
 * Base class for reference-bearing PSI elements (`${view.field}` template refs, field-list entries, ...).
 *
 * Overriding [getReferences] directly on the element is the reliable way to attach references for a custom
 * language (unlike `ContributedReferenceHost`, which the generated PSI does not implement). The actual
 * reference construction lives in [LookMLReferences]; the grammar attaches this base via `mixin=`.
 *
 * Pro-gated: navigation, Find Usages, and Rename all flow through these references, so they are withheld
 * without a license (silently, like completion - this runs on passive highlighting paths too).
 */
abstract class LookMLReferenceHostMixin(node: ASTNode) : ASTWrapperPsiElement(node) {

    override fun getReferences(): Array<PsiReference> =
        if (LicenseConditions.allowPaidPluginFeatures()) LookMLReferences.forHost(this) else PsiReference.EMPTY_ARRAY
}
