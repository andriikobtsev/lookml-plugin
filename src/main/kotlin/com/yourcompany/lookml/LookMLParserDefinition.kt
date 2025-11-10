package com.yourcompany.lookml

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.yourcompany.lookml.lexer.LookMLLexerAdapter
import com.yourcompany.lookml.parser.LookMLParser
import com.yourcompany.lookml.psi.LookMLFile
import com.yourcompany.lookml.psi.LookMLTypes

class LookMLParserDefinition : ParserDefinition {
    companion object {
        val FILE = IFileElementType(LookMLLanguage)
        val COMMENTS = TokenSet.create(LookMLTypes.COMMENT)
        val STRINGS = TokenSet.create(LookMLTypes.STRING)
    }

    override fun createLexer(project: Project?): Lexer = LookMLLexerAdapter()

    override fun createParser(project: Project?): PsiParser = LookMLParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRINGS

    override fun createElement(node: ASTNode): PsiElement = LookMLTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = LookMLFile(viewProvider)
}
