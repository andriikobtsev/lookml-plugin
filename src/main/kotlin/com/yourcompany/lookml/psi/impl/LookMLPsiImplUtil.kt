package com.yourcompany.lookml.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.yourcompany.lookml.psi.*
import com.yourcompany.lookml.psi.LookMLTypes

object LookMLPsiImplUtil {

    // ─── Name getters for all Definition types ────────────────────────────────────

    @JvmStatic fun getName(element: LookMLViewDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getName(element: LookMLExploreDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getName(element: LookMLAccessGrantDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getName(element: LookMLAggregateTableDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getName(element: LookMLDashboardDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getName(element: LookMLDatagroupDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getName(element: LookMLNamedValueFormatDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getName(element: LookMLSetDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getName(element: LookMLTestDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getName(element: LookMLFilterDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getName(element: LookMLParameterDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getName(element: LookMLJoinDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    // ─── DimensionDefinition ─────────────────────────────────────────────────────

    @JvmStatic fun getName(element: LookMLDimensionDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getType(element: LookMLDimensionDefinition): String? =
        findPropertyValue(element, "type")

    @JvmStatic fun getSql(element: LookMLDimensionDefinition): String? =
        findPropertyValue(element, "sql")

    // ─── DimensionGroupDefinition ────────────────────────────────────────────────

    @JvmStatic fun getName(element: LookMLDimensionGroupDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getTimeframes(element: LookMLDimensionGroupDefinition): List<String> {
        // raw will look like `["day","week",...]`
        val raw = findPropertyValue(element, "timeframes") ?: return emptyList()
        return raw
            .removePrefix("[")
            .removeSuffix("]")
            .split(',')
            .map { it.trim().removeSurrounding("\"") }
    }

    // ─── MeasureDefinition ───────────────────────────────────────────────────────

    @JvmStatic fun getName(element: LookMLMeasureDefinition): String? =
        element.node.findChildByType(LookMLTypes.IDENTIFIER)?.text

    @JvmStatic fun getType(element: LookMLMeasureDefinition): String? =
        findPropertyValue(element, "type")

    @JvmStatic fun getSql(element: LookMLMeasureDefinition): String? =
        findPropertyValue(element, "sql")

    // ─── JoinDefinition ─────────────────────────────────────────────────────────

    @JvmStatic fun getType(element: LookMLJoinDefinition): String? =
        findPropertyValue(element, "type")

    @JvmStatic fun getSqlOn(element: LookMLJoinDefinition): String? =
        findPropertyValue(element, "sql_on")

    // ─── Helper ──────────────────────────────────────────────────────────────────

    /**
     * Finds the value of `propertyName` under any Definition element by
     * looking for a LookMLProperty whose name matches, then returning its value.text.
     */
    private fun findPropertyValue(element: PsiElement, propertyName: String): String? {
        val props = PsiTreeUtil.findChildrenOfType(element, LookMLProperty::class.java)
        val match = props.firstOrNull { it.propertyName.text == propertyName } ?: return null
        return match.propertyValue.text
    }
}