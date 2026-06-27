package com.yourcompany.lookml.formatting

import com.intellij.lang.ASTNode
import com.yourcompany.lookml.psi.LookMLTypes

object YamlSemanticAnalyzer {

    fun analyzeNode(node: ASTNode): SemanticInfo {
        val nodeType = node.elementType

        when (nodeType) {
            LookMLTypes.YAML_LIST_ENTRY -> return analyzeListEntry(node)
            LookMLTypes.YAML_PROPERTY -> return analyzeProperty(node)
            else -> return SemanticInfo(
                objectType = ObjectType.UNKNOWN,
                propertyName = null,
                valueType = ValueType.UNKNOWN,
                level = 0
            )
        }
    }

    private fun analyzeListEntry(node: ASTNode): SemanticInfo {
        val key = getListEntryKey(node)
        val objectType = YamlDashboardSchema.getObjectTypeFromKey(key)
        val level = calculateLevel(node)

        return SemanticInfo(
            objectType = objectType,
            propertyName = key,
            valueType = ValueType.UNKNOWN,
            level = level
        )
    }

    private fun analyzeProperty(node: ASTNode): SemanticInfo {
        val propertyName = getPropertyName(node)
        val parentListEntry = findPreviousListEntry(node)
        val parentKey = parentListEntry?.let { getListEntryKey(it) }
        val objectType = YamlDashboardSchema.getObjectTypeFromKey(parentKey)
        val valueType = YamlDashboardSchema.getValueType(objectType, propertyName ?: "")
        val level = calculateLevel(node)

        return SemanticInfo(
            objectType = objectType,
            propertyName = propertyName,
            valueType = valueType,
            level = level
        )
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

    private fun findPreviousListEntry(node: ASTNode): ASTNode? {
        var sibling = node.treePrev
        while (sibling != null) {
            if (sibling.elementType == LookMLTypes.YAML_LIST_ENTRY) {
                return sibling
            }
            sibling = sibling.treePrev
        }
        return null
    }
}
