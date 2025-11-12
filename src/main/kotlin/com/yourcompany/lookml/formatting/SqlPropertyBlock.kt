package com.yourcompany.lookml.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.yourcompany.lookml.psi.LookMLTypes

/**
 * Special block for SQL properties that forces all content onto one line
 * regardless of what child elements exist
 */
class SqlPropertyBlock(
    node: ASTNode,
    private val spacingBuilder: SpacingBuilder,
    indent: Indent
) : AbstractBlock(node, null, null) {

    private val myIndent = indent

    override fun buildChildren(): List<Block> {
        // If this is a leaf (like NUMBER, STRING), don't build children
        if (isLeaf()) {
            return emptyList()
        }

        // Build children as SqlPropertyBlocks
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode

        while (child != null) {
            if (child.elementType != TokenType.WHITE_SPACE && child.textLength > 0) {
                // SQL property children don't get additional indent - they're inline content
                blocks.add(SqlPropertyBlock(child, spacingBuilder, Indent.getNoneIndent()))
            }
            child = child.treeNext
        }

        return blocks
    }

    override fun getIndent(): Indent? {
        return myIndent
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        if (child1 == null) return null

        val child1Node = (child1 as? SqlPropertyBlock)?.myNode
        val child2Node = (child2 as? SqlPropertyBlock)?.myNode

        // TEMPLATE_EXPRESSION: ${TABLE} should have NO spaces anywhere
        if (myNode.elementType == LookMLTypes.TEMPLATE_EXPRESSION) {
            return Spacing.createSpacing(0, 0, 0, false, 0)  // NO spaces at all
        }

        // SQL_FIELD_REFERENCE: ${TABLE}.user_id - no spaces around dot or anything
        if (myNode.elementType == LookMLTypes.SQL_FIELD_REFERENCE) {
            return Spacing.createSpacing(0, 0, 0, false, 0)  // NO spaces
        }

        // After SQL_BLOCK_START tokens - exactly 1 space
        if (child1Node?.elementType.toString().contains("SQL_BLOCK_START") ||
            child1Node?.elementType.toString().contains("SQL_ALWAYS_WHERE_START") ||
            child1Node?.elementType.toString().contains("SQL_ALWAYS_HAVING_START") ||
            child1Node?.elementType.toString().contains("SQL_ALWAYS_FILTER_START")) {
            return Spacing.createSpacing(1, 1, 0, false, 0)
        }

        // At SQL_BLOCK_CONTENT level: add spaces between expressions
        if (myNode.elementType == LookMLTypes.SQL_BLOCK_CONTENT) {
            val c1Type = child1Node?.elementType
            val c2Type = child2Node?.elementType

            // Between SQL_EXPRESSIONs - add 1 space (handles operators)
            if (c1Type == LookMLTypes.SQL_EXPRESSION && c2Type == LookMLTypes.SQL_EXPRESSION) {
                return Spacing.createSpacing(1, 1, 0, false, 0)
            }
        }

        // Between other SQL elements - allow 0-1 spaces
        return Spacing.createSpacing(
            0,      // min spaces
            1,      // max spaces
            0,      // min line feeds - NO newlines
            false,  // keep line breaks = FALSE
            0       // keep blank lines
        )
    }

    override fun isLeaf(): Boolean {
        // CRITICAL: Literal values (numbers, strings, tokens) must NEVER be reformatted
        // SQL_CONTENT_TOKEN can contain numbers, identifiers, etc. in SQL blocks
        if (myNode.elementType.toString().contains("NUMBER") ||
            myNode.elementType.toString().contains("STRING") ||
            myNode.elementType.toString().contains("IDENTIFIER") ||
            myNode.elementType.toString().contains("SQL_CONTENT_TOKEN")) {
            return true  // Keep literals exactly as-is - prevents "100" becoming "1 0 0"
        }

        return myNode.firstChildNode == null
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        return ChildAttributes(Indent.getNoneIndent(), null)
    }

    override fun isIncomplete(): Boolean = false
}
