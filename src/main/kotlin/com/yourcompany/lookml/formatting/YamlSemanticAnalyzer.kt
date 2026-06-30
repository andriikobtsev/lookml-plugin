package com.yourcompany.lookml.formatting

import com.intellij.lang.ASTNode
import com.yourcompany.lookml.psi.LookMLTypes

object YamlSemanticAnalyzer {

    fun analyzeNode(node: ASTNode): SemanticInfo {
        val text = node.psi?.containingFile?.text ?: ""
        return when (node.elementType) {
            LookMLTypes.YAML_LIST_ENTRY -> analyzeListEntry(node, text)
            LookMLTypes.YAML_PROPERTY -> analyzeProperty(node, text)
            else -> SemanticInfo(ObjectType.UNKNOWN, null, ValueType.UNKNOWN, 0)
        }
    }

    private fun analyzeListEntry(node: ASTNode, text: String): SemanticInfo {
        val key = getListEntryKey(node)
        return SemanticInfo(
            objectType = if (key == "dashboard") ObjectType.DASHBOARD else ObjectType.ELEMENT,
            propertyName = key,
            valueType = ValueType.UNKNOWN,
            level = calculateLevel(node)
        )
    }

    private fun analyzeProperty(node: ASTNode, text: String): SemanticInfo {
        val propertyName = getPropertyName(node)
        val objectType = classifyProperty(node, text)
        return SemanticInfo(
            objectType = objectType,
            propertyName = propertyName,
            valueType = YamlDashboardSchema.getValueType(objectType, propertyName ?: ""),
            level = calculateLevel(node)
        )
    }

    /** Column (0-based indent) of the node's first character. */
    private fun indentOf(node: ASTNode, text: String): Int {
        val off = node.startOffset
        if (off <= 0 || off > text.length) return 0
        var i = off
        while (i > 0 && text[i - 1] != '\n') i--
        return off - i
    }

    /**
     * Classify a property by indentation depth, not by section markers (the YAML PSI does not
     * reliably materialize `elements:` / `filters:` markers or nested map keys):
     * - parent is the `- dashboard:` entry -> DASHBOARD (dashboard-level property).
     * - parent is any other list entry     -> ELEMENT (item-level; validated against the
     *   element+filter union in [YamlDashboardSchema.isKnownProperty]).
     * - parent is another property          -> nested map entry (dynamic keys) -> UNKNOWN (skip).
     */
    private fun classifyProperty(node: ASTNode, text: String): ObjectType {
        val myIndent = indentOf(node, text)
        var sib = node.treePrev
        while (sib != null) {
            val t = sib.elementType
            if ((t == LookMLTypes.YAML_LIST_ENTRY || t == LookMLTypes.YAML_PROPERTY) &&
                indentOf(sib, text) < myIndent
            ) {
                return when {
                    t != LookMLTypes.YAML_LIST_ENTRY -> ObjectType.UNKNOWN
                    getListEntryKey(sib) == "dashboard" -> ObjectType.DASHBOARD
                    else -> ObjectType.ELEMENT
                }
            }
            sib = sib.treePrev
        }
        return ObjectType.UNKNOWN
    }

    private fun calculateLevel(node: ASTNode): Int {
        // Simple: count how deep we are by counting list entries
        var depth = 0
        var current = node.treePrev
        var foundAnyListEntry = false
        
        while (current != null) {
            if (current.elementType == LookMLTypes.YAML_LIST_ENTRY) {
                foundAnyListEntry = true
                depth++
                // Only count the first list entry we find
                break
            }
            current = current.treePrev
        }
        
        // If we're a list entry and found no previous list entry, we're at level 0
        if (node.elementType == LookMLTypes.YAML_LIST_ENTRY && !foundAnyListEntry) {
            return 0
        }
        
        // If we're a property and found a list entry, we're one level deeper
        if (node.elementType == LookMLTypes.YAML_PROPERTY && foundAnyListEntry) {
            return depth + 1
        }
        
        // If we're a property and found no list entry, we're at level 1 (after - dashboard)
        if (node.elementType == LookMLTypes.YAML_PROPERTY && !foundAnyListEntry) {
            return 1
        }
        
        return depth
    }

    private fun getPropertyName(node: ASTNode): String? {
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == LookMLTypes.YAML_PROPERTY_NAME) {
                return child.text.trim().removeSuffix(":")
            }
            child = child.treeNext
        }
        return null
    }

    private fun getListEntryKey(node: ASTNode): String? {
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == LookMLTypes.YAML_ITEM_CONTENT) {
                var prop = child.firstChildNode
                while (prop != null) {
                    if (prop.elementType == LookMLTypes.YAML_PROPERTY) {
                        return getPropertyName(prop)
                    }
                    prop = prop.treeNext
                }
            }
            child = child.treeNext
        }
        return null
    }
}
