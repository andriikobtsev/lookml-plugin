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
    
    companion object {
        private val rewritingFiles = mutableSetOf<String>()
    }

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val codeStyleSettings = formattingContext.codeStyleSettings
        val element = formattingContext.psiElement
        val file = element.containingFile

        val fileName = file.name
        
        // Detect YAML dashboard files - check if parser created YAML nodes
        val isYaml = hasYamlContent(file)
        
        println("=".repeat(60))
        println("FORMATTER CALLED: $fileName")
        println("IS YAML: $isYaml")
        
        // For YAML dashboards, skip formatting - use the manual action instead
        if (isYaml) {
            println("⏭️ YAML dashboard detected - skipping formatter (use 'Reformat YAML Dashboard' action instead)")
        }
        
        println("=".repeat(60))

        val spacingBuilder = createSpacingBuilder(codeStyleSettings)

        val rootBlock = LookMLBlock(
            element.node,
            Wrap.createWrap(WrapType.NONE, false),
            null,
            spacingBuilder,
            Indent.getNoneIndent()
        )

        if (isYaml) {
            println("ROOT BLOCK created: ${rootBlock.node.elementType}")
            println("Root has ${rootBlock.subBlocks.size} children")
        }

        val model = FormattingModelProvider.createFormattingModelForPsiFile(
            element.containingFile,
            rootBlock,
            codeStyleSettings
        )

        if (isYaml) {
            println("FORMATTING MODEL created: ${model.javaClass.simpleName}")
            println("=".repeat(60))
        }

        return model
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
        val hasYamlIndicators = text.contains("- dashboard:") ||
                                text.contains("- dashboard :") ||
                                (text.contains("dashboard") && text.contains("elements:")) ||
                                (text.contains("dashboard") && text.contains("filters:")) ||
                                text.startsWith("---") ||
                                text.startsWith("# ") && text.contains("dashboard")
        
        if (hasYamlIndicators) {
            println("✅ Detected YAML dashboard from content patterns")
        } else {
            println("❌ No YAML dashboard patterns found in content")
        }
        
        return hasYamlIndicators
    }

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val lookmlSettings = settings.getCommonSettings(LookMLLanguage.getID())

        // Set indent size to 2 spaces
        lookmlSettings.indentOptions?.INDENT_SIZE = 2
        lookmlSettings.indentOptions?.CONTINUATION_INDENT_SIZE = 2

        return SpacingBuilder(settings, LookMLLanguage)
            // Traditional LookML - Colons
            .after(LookMLTypes.COLON).spacing(1, 1, 0, false, 0)

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
