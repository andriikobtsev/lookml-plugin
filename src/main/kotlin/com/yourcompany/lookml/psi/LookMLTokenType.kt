package com.yourcompany.lookml.psi

import com.intellij.psi.tree.IElementType
import com.yourcompany.lookml.LookMLLanguage

class LookMLTokenType(debugName: String) : IElementType(debugName, LookMLLanguage) {
    override fun toString(): String = "LookMLTokenType." + super.toString()
}
