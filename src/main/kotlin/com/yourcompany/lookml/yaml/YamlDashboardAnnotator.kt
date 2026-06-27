package com.yourcompany.lookml.yaml

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.yourcompany.lookml.formatting.ObjectType
import com.yourcompany.lookml.formatting.ValueType
import com.yourcompany.lookml.formatting.YamlDashboardSchema
import com.yourcompany.lookml.formatting.YamlSemanticAnalyzer
import com.yourcompany.lookml.psi.LookMLFile
import com.yourcompany.lookml.psi.LookMLTypes
import com.yourcompany.lookml.psi.LookMLYamlProperty

/**
 * Offline validation for YAML dashboard block properties against [YamlDashboardSchema].
 * Skips [listen] subtrees (dynamic keys) to limit false positives.
 */
class YamlDashboardAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is LookMLYamlProperty) return
        val file = element.containingFile as? LookMLFile ?: return
        if (!YamlDashboardFiles.isYamlDashboardContent(file.text)) return
        if (element.isUnderPropertyNamed("listen")) return

        val node = element.node
        if (node.elementType != LookMLTypes.YAML_PROPERTY) return

        val info = YamlSemanticAnalyzer.analyzeNode(node)
        if (info.objectType == ObjectType.UNKNOWN) return

        val propertyName = info.propertyName ?: return
        if (propertyName.isEmpty()) return

        if (YamlDashboardSchema.getValueType(info.objectType, propertyName) != ValueType.UNKNOWN) {
            return
        }

        val label = objectLabel(info.objectType)
        holder.newAnnotation(
            HighlightSeverity.WEAK_WARNING,
            "Unknown $label property '$propertyName' (schema-backed local check; not Looker server validation)",
        )
            .range(element.yamlPropertyName.textRange)
            .create()
    }

    private fun objectLabel(type: ObjectType): String =
        when (type) {
            ObjectType.DASHBOARD -> "dashboard"
            ObjectType.ELEMENT -> "element"
            ObjectType.DASHBOARD_FILTER -> "dashboard filter"
            ObjectType.ELEMENT_FILTER -> "element filter"
            ObjectType.UNKNOWN -> "YAML"
        }

    private fun LookMLYamlProperty.isUnderPropertyNamed(name: String): Boolean {
        var p: PsiElement? = parent
        while (p != null) {
            if (p is LookMLYamlProperty) {
                val key = p.yamlPropertyName.text.trim().removeSuffix(":").substringBefore(' ').trim()
                if (key == name) return true
            }
            p = p.parent
        }
        return false
    }
}
