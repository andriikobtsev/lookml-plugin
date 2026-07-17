package com.yourcompany.lookml.references

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.tree.LeafElement

/**
 * A single LookML reference over [range] of its host element, resolving via [resolver] (which reuses the
 * [LookMLResolve] core) to a declaration's name leaf.
 *
 * [isReferenceTo] also matches the leaf's owning declaration, so Find Usages / Rename - which target the
 * [com.yourcompany.lookml.psi.LookMLNamedElement] declaration - see this reference even though [resolve]
 * returns the name leaf (kept identical to the Go-to-Definition handler so navigation dedupes cleanly).
 */
class LookMLReference(
    element: PsiElement,
    range: TextRange,
    private val resolver: () -> PsiElement?,
) : PsiReferenceBase<PsiElement>(element, range) {

    override fun resolve(): PsiElement? = resolver()

    override fun isReferenceTo(element: PsiElement): Boolean {
        val resolved = resolve() ?: return false
        val manager = element.manager
        if (manager.areElementsEquivalent(resolved, element)) return true
        val declaration = resolved.parent
        return declaration != null && manager.areElementsEquivalent(declaration, element)
    }

    /**
     * Rename this usage by replacing only [rangeInElement] inside the host. We edit the underlying leaf
     * directly (no `ElementManipulator` registration needed): the range always sits inside a single
     * IDENTIFIER token, whether the host lexed `view.field` as one token or as separate `.`-joined tokens.
     */
    override fun handleElementRename(newElementName: String): PsiElement {
        val host = element
        val range = rangeInElement
        val leaf = host.findElementAt(range.startOffset) ?: return host
        val leafStartInHost = leaf.textRange.startOffset - host.textRange.startOffset
        val startInLeaf = (range.startOffset - leafStartInHost).coerceIn(0, leaf.textLength)
        val endInLeaf = (range.endOffset - leafStartInHost).coerceIn(startInLeaf, leaf.textLength)
        val leafText = leaf.text
        val newLeafText = leafText.substring(0, startInLeaf) + newElementName + leafText.substring(endInLeaf)
        (leaf.node as? LeafElement)?.replaceWithText(newLeafText)
        return host
    }
}
