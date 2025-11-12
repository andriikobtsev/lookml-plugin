package com.yourcompany.lookml.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet
import com.yourcompany.lookml.LookMLLanguage
import com.yourcompany.lookml.psi.LookMLTypes

/**
 * Proper PSI-based formatter for LookML
 * Uses Block hierarchy and SpacingBuilder instead of string manipulation
 */
class LookMLFormattingModelBuilderV2 : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val codeStyleSettings = formattingContext.codeStyleSettings
        val element = formattingContext.psiElement


        // TODO: Re-enable syntax error check after fixing parser issues
        // Temporarily disabled to allow formatting to work
        // if (hasSyntaxErrors(element.containingFile)) {
        //     return FormattingModelProvider.createFormattingModelForPsiFile(
        //         element.containingFile,
        //         NoOpBlock(element.node),
        //         codeStyleSettings
        //     )
        // }

        val spacingBuilder = createSpacingBuilder(codeStyleSettings)

        val rootBlock = LookMLBlock(
            element.node,
            Wrap.createWrap(WrapType.NONE, false),
            null, // No alignment at root
            spacingBuilder,
            Indent.getNoneIndent()
        )

        return FormattingModelProvider.createFormattingModelForPsiFile(
            element.containingFile,
            rootBlock,
            codeStyleSettings
        )
    }

    override fun getRangeAffectingIndent(
        file: PsiFile,
        offset: Int,
        elementAtOffset: ASTNode
    ): TextRange? {
        return null
    }

    private fun hasSyntaxErrors(file: PsiFile): Boolean {
        // Check if file has any error elements (syntax errors)
        var hasErrors = false

        file.accept(object : com.intellij.psi.PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element is com.intellij.psi.PsiErrorElement) {
                    hasErrors = true
                    return
                }
                super.visitElement(element)
            }
        })

        return hasErrors
    }

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val lookmlSettings = settings.getCommonSettings(LookMLLanguage.getID())

        return SpacingBuilder(settings, LookMLLanguage)
            // Traditional LookML - Colons
            .after(LookMLTypes.COLON).spacing(1, 1, 0, false, 0)  // keepLineBreaks=false to normalize

            // Traditional LookML - Braces
            .before(LookMLTypes.LBRACE).spacing(1, 1, 0, true, 0)
            .after(LookMLTypes.LBRACE).spacing(0, 0, 1, true, 0) // Newline after {
            .before(LookMLTypes.RBRACE).spacing(0, 0, 1, true, 0) // Newline before }
            .after(LookMLTypes.RBRACE).spacing(0, 0, 1, true, 1) // Blank line after }

            // SQL terminators
            .before(LookMLTypes.SEMICOLON).spacing(1, 1, 0, true, 0) // Space before ;;
            .after(LookMLTypes.SEMICOLON).spacing(0, 0, 0, true, 0)

            // Arrays and lists
            .after(LookMLTypes.LBRACKET).spacing(0, 0, 0, false, 0) // No space after [
            .before(LookMLTypes.RBRACKET).spacing(0, 0, 0, false, 0) // No space before ]
            .after(LookMLTypes.COMMA).spacing(1, 1, 0, false, 0) // Space after comma

            // YAML - List items
            .after(LookMLTypes.YAML_DOCUMENT_START).spacing(0, 0, 1, true, 0) // Newline after ---
            .after(LookMLTypes.YAML_LIST_ITEM).spacing(1, 1, 0, true, 0) // Space after -

            // YAML - Keep inline arrays/objects on same line
            .withinPair(LookMLTypes.LBRACKET, LookMLTypes.RBRACKET).spacing(0, 1, 0, false, 0)

            // Template expressions ${...} - CRITICAL: No spaces, no line breaks!
            // ${ with NO space
            .between(LookMLTypes.DOLLAR, LookMLTypes.LBRACE).spacing(0, 0, 0, false, 0)
            // Inside ${}
            .afterInside(LookMLTypes.LBRACE, LookMLTypes.TEMPLATE_EXPRESSION).spacing(0, 0, 0, false, 0)
            .beforeInside(LookMLTypes.RBRACE, LookMLTypes.TEMPLATE_EXPRESSION).spacing(0, 0, 0, false, 0)
            .around(LookMLTypes.TEMPLATE_REFERENCE).spacing(0, 0, 0, false, 0)

            // CRITICAL: SQL blocks - force everything onto ONE line, remove existing line breaks
            // Parameters: (minSpaces, maxSpaces, minLineFeeds, keepLineBreaks, keepBlankLines)
            // keepLineBreaks=false is KEY - it removes existing newlines!
            .withinPair(LookMLTypes.SQL_BLOCK_START, LookMLTypes.SQL_BLOCK_END).spacing(1, 1, 0, false, 0)

            // Also force no line breaks between all elements inside SQL
            .afterInside(LookMLTypes.SQL_BLOCK_START, LookMLTypes.SQL_PROPERTY).spacing(1, 1, 0, false, 0)
            .afterInside(LookMLTypes.SQL_BLOCK_START, LookMLTypes.SQL_ON_PROPERTY).spacing(1, 1, 0, false, 0)
            .beforeInside(LookMLTypes.SQL_BLOCK_END, LookMLTypes.SQL_PROPERTY).spacing(1, 1, 0, false, 0)
            .beforeInside(LookMLTypes.SQL_BLOCK_END, LookMLTypes.SQL_ON_PROPERTY).spacing(1, 1, 0, false, 0)

            // Inside SQL expressions - no line breaks anywhere
            .aroundInside(LookMLTypes.TEMPLATE_EXPRESSION, LookMLTypes.SQL_EXPRESSION).spacing(0, 1, 0, false, 0)
            .aroundInside(LookMLTypes.SQL_FIELD_REFERENCE, LookMLTypes.SQL_EXPRESSION).spacing(0, 1, 0, false, 0)
            .aroundInside(LookMLTypes.SQL_EXPRESSION, LookMLTypes.SQL_BLOCK_CONTENT).spacing(0, 1, 0, false, 0)

            // Between template expression and other tokens (like . in ${TABLE}.user_id)
            .after(LookMLTypes.TEMPLATE_EXPRESSION).spacing(0, 0, 0, false, 0)
            .before(LookMLTypes.TEMPLATE_EXPRESSION).spacing(0, 0, 0, false, 0)

            // Between top-level definitions - blank line
            .between(LookMLTypes.VIEW_DEFINITION, LookMLTypes.VIEW_DEFINITION).blankLines(1)
            .between(LookMLTypes.VIEW_DEFINITION, LookMLTypes.EXPLORE_DEFINITION).blankLines(1)
            .between(LookMLTypes.EXPLORE_DEFINITION, LookMLTypes.EXPLORE_DEFINITION).blankLines(1)

            // Between dimensions/measures inside view - blank line
            .between(LookMLTypes.DIMENSION_DEFINITION, LookMLTypes.DIMENSION_DEFINITION).blankLines(1)
            .between(LookMLTypes.DIMENSION_DEFINITION, LookMLTypes.MEASURE_DEFINITION).blankLines(1)
            .between(LookMLTypes.MEASURE_DEFINITION, LookMLTypes.MEASURE_DEFINITION).blankLines(1)
            .between(LookMLTypes.MEASURE_DEFINITION, LookMLTypes.DIMENSION_DEFINITION).blankLines(1)

            // Between properties - single newline, no blank lines
            .aroundInside(TokenSet.create(LookMLTypes.TYPE_PROPERTY, LookMLTypes.SQL_PROPERTY),
                         LookMLTypes.PROPERTY_LIST).spacing(0, 0, 1, true, 0)

            // YAML - Between properties - single newline only (keep blank lines = 0)
            .between(LookMLTypes.YAML_PROPERTY, LookMLTypes.YAML_PROPERTY).spacing(0, 0, 1, false, 0)
            .between(LookMLTypes.YAML_LIST_ENTRY, LookMLTypes.YAML_LIST_ENTRY).spacing(0, 0, 1, false, 0)
    }

    /**
     * No-op block for files with syntax errors
     */
    private class NoOpBlock(private val node: ASTNode) : ASTBlock {
        override fun getNode(): ASTNode = node
        override fun getTextRange(): TextRange = node.textRange
        override fun getWrap(): Wrap? = null
        override fun getIndent(): Indent = Indent.getNoneIndent()
        override fun getAlignment(): Alignment? = null
        override fun getSpacing(child1: Block?, child2: Block): Spacing? = null
        override fun getChildAttributes(newChildIndex: Int): ChildAttributes =
            ChildAttributes(Indent.getNoneIndent(), null)
        override fun isIncomplete(): Boolean = false
        override fun isLeaf(): Boolean = true
        override fun getSubBlocks(): List<Block> = emptyList()
    }
}
