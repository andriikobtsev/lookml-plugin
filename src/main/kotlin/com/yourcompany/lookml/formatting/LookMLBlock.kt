package com.yourcompany.lookml.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.yourcompany.lookml.psi.LookMLTypes

/**
 * Proper PSI-based formatting block for LookML
 */
class LookMLBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val spacingBuilder: SpacingBuilder,
    private val indentLevel: Indent
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        // If this is a leaf, return empty list - don't descend
        if (isLeaf()) {
            return emptyList()
        }

        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode

        // DEBUG: YAML structure only
        if (myNode.elementType == LookMLTypes.YAML_CONTENT ||
            myNode.elementType == LookMLTypes.YAML_LIST_ENTRY) {
            println("Building ${myNode.elementType} children:")
            var c = myNode.firstChildNode
            var count = 0
            while (c != null && count < 8) {
                if (c.elementType != TokenType.WHITE_SPACE && c.textLength > 0) {
                    println("  Child: ${c.elementType}")
                }
                count++
                c = c.treeNext
            }
        }

        while (child != null) {
            // Skip whitespace - IntelliJ will add it based on our spacing rules
            if (child.elementType != TokenType.WHITE_SPACE &&
                child.textLength > 0) {

                val childIndent = calculateIndent(child)
                val childWrap = calculateWrap(child)


                // Use custom SqlPropertyBlock ONLY for SQL properties
                // All other children use regular LookMLBlock to preserve indentation
                val childBlock = if (isSqlProperty(child.elementType)) {
                    SqlPropertyBlock(child, spacingBuilder, childIndent)  // Pass correct indent!
                } else {
                    LookMLBlock(child, childWrap, null, spacingBuilder, childIndent)
                }
                blocks.add(childBlock)
            }
            child = child.treeNext
        }

        return blocks
    }

    private fun calculateWrap(child: ASTNode): Wrap? {
        // CRITICAL: Template expressions must NEVER wrap
        if (child.elementType == LookMLTypes.TEMPLATE_EXPRESSION ||
            isInsideTemplateExpression(child)) {
            return Wrap.createWrap(WrapType.NONE, false)
        }

        // Keep inline arrays on one line
        if (child.elementType == LookMLTypes.YAML_FLOW_ARRAY) {
            return Wrap.createWrap(WrapType.NONE, false)
        }

        return null
    }

    private fun isInsideTemplateExpression(node: ASTNode): Boolean {
        var parent = node.treeParent
        while (parent != null) {
            if (parent.elementType == LookMLTypes.TEMPLATE_EXPRESSION) {
                return true
            }
            parent = parent.treeParent
        }
        return false
    }

    override fun getIndent(): Indent? = indentLevel

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        val spacing = spacingBuilder.getSpacing(this, child1, child2)

        return spacing
    }

    override fun isLeaf(): Boolean {
        // CRITICAL: Don't make things leaves if their internal spacing needs fixing!
        // Leaves keep content EXACTLY as-is (including wrong spaces)

        // Only truly atomic YAML elements as leaves
        if (myNode.elementType in listOf(
                LookMLTypes.YAML_FLOW_ARRAY,
                LookMLTypes.YAML_FLOW_OBJECT)) {
            return true
        }

        return myNode.firstChildNode == null
    }

    private fun countChildren(): Int {
        var count = 0
        var child = myNode.firstChildNode
        while (child != null) {
            if (child.elementType != TokenType.WHITE_SPACE && child.textLength > 0) {
                count++
            }
            child = child.treeNext
        }
        return count
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        // When user presses Enter, what indent should next line have?
        val indent = when (myNode.elementType) {
            // Traditional LookML bodies
            LookMLTypes.VIEW_BODY,
            LookMLTypes.EXPLORE_BODY,
            LookMLTypes.DASHBOARD_BODY,
            LookMLTypes.JOIN_BODY,
            LookMLTypes.DERIVED_TABLE_BODY,
            LookMLTypes.FILTER_BODY -> Indent.getNormalIndent()

            // YAML contexts - match calculateYamlIndent
            LookMLTypes.YAML_CONTENT,
            LookMLTypes.YAML_LIST_ENTRY -> Indent.getNormalIndent()

            else -> Indent.getNoneIndent()
        }

        return ChildAttributes(indent, null)
    }

    private fun calculateIndent(child: ASTNode): Indent {
        val parentType = myNode.elementType
        val childType = child.elementType

        // CRITICAL: Don't manually count braces or calculate spaces!
        // Use PSI parent type to decide indent level.
        // IntelliJ will accumulate indents through the tree automatically.

        // For YAML, use different logic
        if (isYamlNode(childType) || isYamlNode(parentType)) {
            return calculateYamlIndent(child)
        }

        // Children of these parent types get normal indent
        // The indent accumulates as you go deeper in the tree
        val result = when (parentType) {
            // Traditional LookML bodies - children get normal indent
            LookMLTypes.VIEW_BODY,
            LookMLTypes.EXPLORE_BODY,
            LookMLTypes.DASHBOARD_BODY,
            LookMLTypes.JOIN_BODY,
            LookMLTypes.PROPERTY_LIST,
            LookMLTypes.ACCESS_GRANT_BODY,
            LookMLTypes.DATAGROUP_BODY,
            LookMLTypes.NAMED_VALUE_FORMAT_BODY -> Indent.getNormalIndent()

            // Derived table and nested bodies
            LookMLTypes.DERIVED_TABLE_BODY,
            LookMLTypes.EXPLORE_SOURCE_BODY,
            LookMLTypes.FILTER_BODY,
            LookMLTypes.SET_BODY -> Indent.getNormalIndent()

            // Top-level items and keywords/braces get no indent
            else -> Indent.getNoneIndent()
        }

        return result
    }

    private fun isYamlNode(type: com.intellij.psi.tree.IElementType): Boolean {
        return type.toString().startsWith("YAML_")
    }

    private fun isSqlProperty(type: com.intellij.psi.tree.IElementType): Boolean {
        return type in listOf(
            LookMLTypes.SQL_PROPERTY,
            LookMLTypes.SQL_ON_PROPERTY,
            LookMLTypes.SQL_ALWAYS_WHERE_PROPERTY,
            LookMLTypes.SQL_ALWAYS_FILTER_PROPERTY,
            LookMLTypes.SQL_ALWAYS_HAVING_PROPERTY,
            LookMLTypes.SQL_TRIGGER_PROPERTY,
            LookMLTypes.SQL_LATITUDE_PROPERTY,
            LookMLTypes.SQL_LONGITUDE_PROPERTY
        )
    }

    private fun calculateYamlIndent(child: ASTNode): Indent {
        val parentType = myNode.elementType
        val childType = child.elementType

        // YAML indentation: Children of content containers get indented
        // CRITICAL: Check childType FIRST before parentType!
        val result = when {
            // Don't indent the list marker itself or document start
            childType == LookMLTypes.YAML_LIST_ITEM -> Indent.getNoneIndent()
            childType == LookMLTypes.YAML_DOCUMENT_START -> Indent.getNoneIndent()

            // After list item marker (-), content is indented
            parentType == LookMLTypes.YAML_LIST_ENTRY -> Indent.getNormalIndent()

            // Content inside dashboard definition
            parentType == LookMLTypes.YAML_CONTENT -> Indent.getNormalIndent()

            else -> Indent.getNoneIndent()
        }

        // DEBUG
        if (parentType == LookMLTypes.YAML_CONTENT || parentType == LookMLTypes.YAML_LIST_ENTRY) {
            val matched = (childType == LookMLTypes.YAML_LIST_ITEM)
            println("  YAML indent: parent=$parentType, child=$childType → $result (is YAML_LIST_ITEM: $matched)")
        }

        return result
    }



}
