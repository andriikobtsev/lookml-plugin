package com.yourcompany.lookml.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet
import com.yourcompany.lookml.LookMLLanguage
import com.yourcompany.lookml.license.LicenseConditions
import com.yourcompany.lookml.psi.LookMLTypes

/**
 * Proper PSI-based formatter for LookML
 * Uses Block hierarchy and SpacingBuilder instead of string manipulation
 */
class LookMLFormattingModelBuilderV2 : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val codeStyleSettings = formattingContext.codeStyleSettings
        val element = formattingContext.psiElement
        val file = element.containingFile

        if (!LicenseConditions.allowPaidPluginFeatures()) {
            return FormattingModelProvider.createFormattingModelForPsiFile(
                file,
                SimpleBlock(element.node),
                codeStyleSettings
            )
        }

        val isYaml = hasYamlContent(file)

        if (isYaml) {
            val document = PsiDocumentManager.getInstance(file.project).getDocument(file)
            if (document != null) {
                WriteCommandAction.runWriteCommandAction(file.project) {
                    val reformatted = YamlDashboardRewriter.rewriteDashboard(file)
                    document.setText(reformatted)
                    PsiDocumentManager.getInstance(file.project).commitDocument(document)
                }
            }

            return FormattingModelProvider.createFormattingModelForPsiFile(
                file,
                SimpleBlock(element.node),
                codeStyleSettings
            )
        }

        val spacingBuilder = createSpacingBuilder(codeStyleSettings)

        val rootBlock = LookMLBlock(
            element.node,
            Wrap.createWrap(WrapType.NONE, false),
            null,
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

    /**
     * Check if file is a YAML dashboard based on content
     */
    private fun hasYamlContent(file: PsiFile): Boolean {
        val text = file.text.trim()
        
        // Check for YAML dashboard indicators in the raw text
        return text.contains("- dashboard:") ||
            text.contains("- dashboard :") ||
            (text.contains("dashboard") && text.contains("elements:")) ||
            (text.contains("dashboard") && text.contains("filters:")) ||
            text.startsWith("---") ||
            text.startsWith("# ") && text.contains("dashboard")
    }

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val lookmlSettings = settings.getCommonSettings(LookMLLanguage.getID())

        // Set indent size to 2 spaces
        lookmlSettings.indentOptions?.apply {
            INDENT_SIZE = 2
            CONTINUATION_INDENT_SIZE = 2
            TAB_SIZE = 2
            USE_TAB_CHARACTER = false
        }

        return SpacingBuilder(settings, LookMLLanguage)
            // Traditional LookML - Colons
            .before(LookMLTypes.COLON).spacing(0, 0, 0, false, 0)  // NO space before colon
            .after(LookMLTypes.COLON).spacing(1, 1, 0, false, 0)   // ONE space after colon

            // Traditional LookML - Braces
            .before(LookMLTypes.LBRACE).spacing(1, 1, 0, true, 0)
            .after(LookMLTypes.LBRACE).spacing(0, 0, 1, true, 0)
            .before(LookMLTypes.RBRACE).spacing(0, 0, 1, true, 0)
            .after(LookMLTypes.RBRACE).spacing(0, 0, 1, true, 1)

            // SQL terminators
            .before(LookMLTypes.SEMICOLON).spacing(1, 1, 0, true, 0)
            .after(LookMLTypes.SEMICOLON).spacing(0, 0, 0, true, 0)

            // Arrays and lists
            .after(LookMLTypes.LBRACKET).spacing(0, 0, 0, false, 0)
            .before(LookMLTypes.RBRACKET).spacing(0, 0, 0, false, 0)
            .after(LookMLTypes.COMMA).spacing(1, 1, 0, false, 0)

            // YAML - List items
            .after(LookMLTypes.YAML_DOCUMENT_START).spacing(0, 0, 1, false, 0)
            .after(LookMLTypes.YAML_LIST_ITEM).spacing(1, 1, 0, false, 0)

            // YAML - Keep inline arrays/objects on same line
            .withinPair(LookMLTypes.LBRACKET, LookMLTypes.RBRACKET).spacing(0, 1, 0, false, 0)

            // Template expressions
            .between(LookMLTypes.DOLLAR, LookMLTypes.LBRACE).spacing(0, 0, 0, false, 0)
            .afterInside(LookMLTypes.LBRACE, LookMLTypes.TEMPLATE_EXPRESSION).spacing(0, 0, 0, false, 0)
            .beforeInside(LookMLTypes.RBRACE, LookMLTypes.TEMPLATE_EXPRESSION).spacing(0, 0, 0, false, 0)
            .around(LookMLTypes.TEMPLATE_REFERENCE).spacing(0, 0, 0, false, 0)

            // SQL blocks
            .withinPair(LookMLTypes.SQL_BLOCK_START, LookMLTypes.SQL_BLOCK_END).spacing(1, 1, 0, false, 0)
            .afterInside(LookMLTypes.SQL_BLOCK_START, LookMLTypes.SQL_PROPERTY).spacing(1, 1, 0, false, 0)
            .afterInside(LookMLTypes.SQL_BLOCK_START, LookMLTypes.SQL_ON_PROPERTY).spacing(1, 1, 0, false, 0)
            .beforeInside(LookMLTypes.SQL_BLOCK_END, LookMLTypes.SQL_PROPERTY).spacing(1, 1, 0, false, 0)
            .beforeInside(LookMLTypes.SQL_BLOCK_END, LookMLTypes.SQL_ON_PROPERTY).spacing(1, 1, 0, false, 0)

            // Inside SQL expressions
            .aroundInside(LookMLTypes.TEMPLATE_EXPRESSION, LookMLTypes.SQL_EXPRESSION).spacing(0, 1, 0, false, 0)
            .aroundInside(LookMLTypes.SQL_FIELD_REFERENCE, LookMLTypes.SQL_EXPRESSION).spacing(0, 1, 0, false, 0)
            .aroundInside(LookMLTypes.SQL_EXPRESSION, LookMLTypes.SQL_BLOCK_CONTENT).spacing(0, 1, 0, false, 0)

            // Between template expression and other tokens
            .after(LookMLTypes.TEMPLATE_EXPRESSION).spacing(0, 0, 0, false, 0)
            .before(LookMLTypes.TEMPLATE_EXPRESSION).spacing(0, 0, 0, false, 0)

            // Between top-level definitions
            .between(LookMLTypes.VIEW_DEFINITION, LookMLTypes.VIEW_DEFINITION).blankLines(1)
            .between(LookMLTypes.VIEW_DEFINITION, LookMLTypes.EXPLORE_DEFINITION).blankLines(1)
            .between(LookMLTypes.EXPLORE_DEFINITION, LookMLTypes.EXPLORE_DEFINITION).blankLines(1)

            // Between dimensions/measures
            .between(LookMLTypes.DIMENSION_DEFINITION, LookMLTypes.DIMENSION_DEFINITION).blankLines(1)
            .between(LookMLTypes.DIMENSION_DEFINITION, LookMLTypes.MEASURE_DEFINITION).blankLines(1)
            .between(LookMLTypes.MEASURE_DEFINITION, LookMLTypes.MEASURE_DEFINITION).blankLines(1)
            .between(LookMLTypes.MEASURE_DEFINITION, LookMLTypes.DIMENSION_DEFINITION).blankLines(1)

            // Between properties
            .aroundInside(TokenSet.create(LookMLTypes.TYPE_PROPERTY, LookMLTypes.SQL_PROPERTY),
                         LookMLTypes.PROPERTY_LIST).spacing(0, 0, 1, true, 0)

            // YAML - Between properties
            .between(LookMLTypes.YAML_PROPERTY, LookMLTypes.YAML_PROPERTY).spacing(0, 0, 1, false, 0)
            .between(LookMLTypes.YAML_LIST_ENTRY, LookMLTypes.YAML_LIST_ENTRY).spacing(0, 0, 1, false, 0)
    }
}
