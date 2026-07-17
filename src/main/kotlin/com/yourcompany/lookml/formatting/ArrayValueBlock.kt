package com.yourcompany.lookml.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.yourcompany.lookml.psi.LookMLTypes

/**
 * Formatting block for a traditional LookML `array_value` (`[ ... ]`) used by `fields`,
 * `drill_fields`, `set`, `tags`, and similar list properties.
 *
 * When the array is short it stays on one line (existing behaviour). When it is long it is
 * broken one element per line, with the closing bracket back at the property's indent:
 *
 * ```
 * fields: [
 *   a,
 *   b,
 *   c
 * ]
 * ```
 */
class ArrayValueBlock(
    node: ASTNode,
    private val spacingBuilder: SpacingBuilder,
) : AbstractBlock(node, Wrap.createWrap(WrapType.NONE, false), null) {

    private val wrapElements: Boolean = node.textLength > MAX_INLINE_LENGTH

    override fun buildChildren(): List<Block> {
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode
        while (child != null) {
            if (child.elementType != TokenType.WHITE_SPACE && child.textLength > 0) {
                when (child.elementType) {
                    LookMLTypes.ARRAY_ELEMENTS -> addElementBlocks(child, blocks)
                    else -> blocks.add(leaf(child, Indent.getNoneIndent()))
                }
            }
            child = child.treeNext
        }
        return blocks
    }

    private fun addElementBlocks(elements: ASTNode, blocks: MutableList<Block>) {
        var child = elements.firstChildNode
        while (child != null) {
            if (child.elementType != TokenType.WHITE_SPACE && child.textLength > 0) {
                val indent =
                    if (child.elementType == LookMLTypes.ARRAY_ELEMENT && wrapElements) {
                        Indent.getNormalIndent()
                    } else {
                        Indent.getNoneIndent()
                    }
                blocks.add(leaf(child, indent))
            }
            child = child.treeNext
        }
    }

    private fun leaf(node: ASTNode, indent: Indent): Block =
        LookMLBlock(node, Wrap.createWrap(WrapType.NONE, false), null, spacingBuilder, indent)

    override fun getIndent(): Indent = Indent.getNoneIndent()

    override fun isLeaf(): Boolean = false

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        if (!wrapElements || child1 == null) {
            return spacingBuilder.getSpacing(this, child1, child2)
        }

        val type1 = (child1 as? ASTBlock)?.node?.elementType
        val type2 = (child2 as? ASTBlock)?.node?.elementType

        return when {
            // `[` then first element -> break to its own indented line.
            type1 == LookMLTypes.LBRACKET -> NEWLINE
            // element then `]` -> closing bracket on its own line.
            type2 == LookMLTypes.RBRACKET -> NEWLINE
            // element then `,` -> comma hugs the element.
            type2 == LookMLTypes.COMMA -> NO_SPACE
            // `,` then next element -> break to a new line.
            type1 == LookMLTypes.COMMA -> NEWLINE
            else -> spacingBuilder.getSpacing(this, child1, child2)
        }
    }

    private companion object {
        private const val MAX_INLINE_LENGTH = 80

        private val NEWLINE: Spacing = Spacing.createSpacing(0, 0, 1, false, 0)
        private val NO_SPACE: Spacing = Spacing.createSpacing(0, 0, 0, false, 0)
    }
}
