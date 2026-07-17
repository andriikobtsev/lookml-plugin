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

        // Never a space *before* a comma in SQL - `SELECT a, b`, not `SELECT a , b`. Applies at every
        // level (top-level list and inside `(...)`); the space *after* the comma is left to the rules
        // below, which already yield exactly one.
        if (isComma(child2Node)) return Spacing.createSpacing(0, 0, 0, false, 0)

        // TEMPLATE_EXPRESSION: ${TABLE} should have NO spaces anywhere
        if (myNode.elementType == LookMLTypes.TEMPLATE_EXPRESSION) {
            return Spacing.createSpacing(0, 0, 0, false, 0)  // NO spaces at all
        }

        // SQL_FIELD_REFERENCE: ${TABLE}.user_id - no spaces around dot or anything
        if (myNode.elementType == LookMLTypes.SQL_FIELD_REFERENCE) {
            return Spacing.createSpacing(0, 0, 0, false, 0)  // NO spaces
        }

        // After SQL_BLOCK_START tokens - 1 space when on the same line, but keep an author line break
        // (`sql:` on its own line, SQL starting on the next) if they wrote one.
        if (child1Node?.elementType.toString().contains("SQL_BLOCK_START") ||
            child1Node?.elementType.toString().contains("SQL_ALWAYS_WHERE_START") ||
            child1Node?.elementType.toString().contains("SQL_ALWAYS_HAVING_START") ||
            child1Node?.elementType.toString().contains("SQL_ALWAYS_FILTER_START")) {
            return Spacing.createSpacing(1, 1, 0, KEEP_LINE_BREAKS, 0)
        }

        // At SQL_BLOCK_CONTENT level: add spaces between expressions
        if (myNode.elementType == LookMLTypes.SQL_BLOCK_CONTENT) {
            val c1Type = child1Node?.elementType
            val c2Type = child2Node?.elementType

            if (c1Type == LookMLTypes.SQL_EXPRESSION && c2Type == LookMLTypes.SQL_EXPRESSION) {
                // Function call: a bare function name immediately followed by a parenthesised group
                // must stay tight - `DATE_TRUNC(...)`, not `DATE_TRUNC (...)` (a space before `(`
                // after a function name is unsafe/ugly SQL). SQL keywords (IN, NOT, WHEN, ...) keep
                // their space before `(`. Never break before the `(`.
                if (isFunctionName(child1Node) && startsWithLParen(child2Node)) {
                    return Spacing.createSpacing(0, 0, 0, false, 0)
                }
                // Between SQL_EXPRESSIONs - 1 space on the same line, but preserve author line breaks
                // (Phase 1: multi-line SQL - e.g. a `derived_table` query - stays multi-line instead
                // of collapsing onto one line).
                return Spacing.createSpacing(1, 1, 0, KEEP_LINE_BREAKS, 0)
            }
        }

        // Between other SQL elements - allow 0-1 spaces, and keep any author line break.
        return Spacing.createSpacing(
            0,               // min spaces
            1,               // max spaces
            0,               // min line feeds - never force a newline
            KEEP_LINE_BREAKS, // preserve line breaks the author wrote
            0,               // keep blank lines
        )
    }

    /** A parenthesised SQL group `( ... )` - its first token is `(`. */
    private fun startsWithLParen(node: ASTNode?): Boolean =
        node?.firstChildNode?.elementType == LookMLTypes.LPAREN

    /** A bare comma in SQL - either the `,` leaf itself or the `sql_expression` wrapping just a `,`. */
    private fun isComma(node: ASTNode?): Boolean {
        node ?: return false
        val only = node.firstChildNode ?: return node.elementType == LookMLTypes.COMMA
        return only === node.lastChildNode && only.elementType == LookMLTypes.COMMA
    }

    /**
     * True when [node] is a bare identifier expression that reads as a function name (so `name(` is
     * kept tight). SQL keywords that legitimately precede `(` (`IN (...)`, `WHEN (...)`, ...) are
     * excluded so they keep their space.
     */
    private fun isFunctionName(node: ASTNode?): Boolean {
        val only = node?.firstChildNode ?: return false
        if (only !== node.lastChildNode || only.elementType != LookMLTypes.IDENTIFIER) return false
        return only.text.uppercase() !in SQL_KEYWORDS_BEFORE_PAREN
    }

    override fun isLeaf(): Boolean {
        // Phase 1 - multi-line SQL is kept verbatim: if the author wrote a `sql:` block across several
        // lines (e.g. a `derived_table` query), preserve their exact line breaks and indentation instead
        // of collapsing it onto one line or flattening the indentation. Only the property's own first
        // line is re-indented by the parent. Single-line SQL still flows through the token rules below
        // (so `DATE_TRUNC(`, commas, etc. are normalized).
        if (myNode.elementType in SQL_PROPERTY_TYPES && myNode.textContains('\n')) {
            return true
        }

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

    private companion object {
        /** Preserve line breaks the author wrote inside a `sql:` block (do not collapse to one line). */
        private const val KEEP_LINE_BREAKS = true

        /** Top-level `sql*:` property wrappers (a multi-line one is kept verbatim; see [isLeaf]). */
        private val SQL_PROPERTY_TYPES =
            setOf(
                LookMLTypes.SQL_PROPERTY,
                LookMLTypes.SQL_ON_PROPERTY,
                LookMLTypes.SQL_ALWAYS_WHERE_PROPERTY,
                LookMLTypes.SQL_ALWAYS_FILTER_PROPERTY,
                LookMLTypes.SQL_ALWAYS_HAVING_PROPERTY,
                LookMLTypes.SQL_TRIGGER_PROPERTY,
                LookMLTypes.SQL_LATITUDE_PROPERTY,
                LookMLTypes.SQL_LONGITUDE_PROPERTY,
            )

        /** SQL keywords that keep a space before a following `(` (they are not function calls). */
        private val SQL_KEYWORDS_BEFORE_PAREN =
            setOf(
                "IN", "NOT", "AND", "OR", "IS", "LIKE", "ILIKE", "RLIKE", "BETWEEN",
                "CASE", "WHEN", "THEN", "ELSE", "END", "WHERE", "HAVING", "GROUP",
                "ORDER", "BY", "SELECT", "FROM", "AS", "ON", "USING", "DISTINCT",
                "EXISTS", "ALL", "ANY", "SOME", "UNION", "INTERSECT", "EXCEPT",
                "OVER", "PARTITION", "INTERVAL", "SIMILAR", "VALUES", "RETURNING",
                "LIMIT", "OFFSET",
            )
    }
}
