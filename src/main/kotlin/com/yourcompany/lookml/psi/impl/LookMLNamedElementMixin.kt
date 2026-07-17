package com.yourcompany.lookml.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafElement
import com.yourcompany.lookml.psi.LookMLNamedElement
import com.yourcompany.lookml.references.LookMLResolve

/**
 * Shared [LookMLNamedElement] behaviour for every LookML declaration. The name is the first meaningful
 * leaf after the declaration's `:` (see [LookMLResolve.nameLeaf]); `getTextOffset` points at that leaf so
 * navigation and Find Usages land on the name rather than the `view`/`dimension` keyword.
 *
 * The grammar wires this in via `mixin=` on each declaration rule, so it applies uniformly without any
 * generated-method dependency (Grammar-Kit 2022.3 does not reliably generate nullable Kotlin `getName`).
 */
abstract class LookMLNamedElementMixin(node: ASTNode) :
    ASTWrapperPsiElement(node), LookMLNamedElement {

    override fun getNameIdentifier(): PsiElement? = LookMLResolve.nameLeaf(this)

    override fun getName(): String? = nameIdentifier?.text

    override fun setName(name: String): PsiElement {
        (nameIdentifier?.node as? LeafElement)?.replaceWithText(name)
        return this
    }

    override fun getTextOffset(): Int = nameIdentifier?.textOffset ?: super.getTextOffset()
}
