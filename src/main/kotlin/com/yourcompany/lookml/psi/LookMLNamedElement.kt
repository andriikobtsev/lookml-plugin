package com.yourcompany.lookml.psi

import com.intellij.psi.PsiNameIdentifierOwner

/**
 * A LookML declaration that owns a name (view, explore, dimension, measure, set, constant, ...).
 *
 * This is the anchor of the Track A reference layer: [com.yourcompany.lookml.references.LookMLReference]s
 * resolve to a declaration's name leaf, and Find Usages / Rename operate on the owning declaration
 * (a [PsiNameIdentifierOwner]). Behaviour is provided uniformly by
 * [com.yourcompany.lookml.psi.impl.LookMLNamedElementMixin]; the grammar attaches it via `mixin=`.
 */
interface LookMLNamedElement : PsiNameIdentifierOwner
