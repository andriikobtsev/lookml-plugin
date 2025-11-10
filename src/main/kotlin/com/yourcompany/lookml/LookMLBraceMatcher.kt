package com.yourcompany.lookml

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.yourcompany.lookml.psi.LookMLTypes

class LookMLBraceMatcher : PairedBraceMatcher {
    override fun getPairs(): Array<BracePair> = PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset

    companion object {
        private val PAIRS = arrayOf(
            BracePair(LookMLTypes.LBRACE, LookMLTypes.RBRACE, true),
            BracePair(LookMLTypes.LBRACKET, LookMLTypes.RBRACKET, false),
            BracePair(LookMLTypes.SQL_BLOCK_START, LookMLTypes.SQL_BLOCK_END, true)
        )
    }
}
