package com.yourcompany.lookml.formatting

import com.intellij.lang.ASTNode
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.yourcompany.lookml.psi.LookMLTypes

/**
 * Classification regression tests for [YamlSemanticAnalyzer] + [YamlDashboardSchema].
 * Asserts the actual validator outcome (would this property be flagged?), guarding the
 * element-vs-filter ambiguity that caused repeated false positives.
 */
class YamlSemanticAnalyzerTest : BasePlatformTestCase() {

    private val dashboard = """
        ---
        - dashboard: d1
          title: "T"
          layout: newspaper
          not_a_real_prop_zzz: 1
          elements:
          - name: e1
            type: looker_column
            fields: [a.b, a.c]
          filters:
          - name: f1
            type: field_filter
            default_value: x
            allow_multiple_values: true
            required: false
    """.trimIndent()

    /** True when the validator would NOT flag this property (i.e. recognized as known). */
    private fun isAccepted(propertyName: String): Boolean {
        val node = myFixture.configureByText("d.lookml", dashboard).node
        val props = mutableListOf<ASTNode>()
        collectProperties(node, props)
        val target = props.firstOrNull { name(it) == propertyName }
            ?: error("'$propertyName' not parsed. Available: " + props.map { name(it) })
        val info = YamlSemanticAnalyzer.analyzeNode(target)
        return YamlDashboardSchema.isKnownProperty(info.objectType, propertyName)
    }

    private fun collectProperties(node: ASTNode, out: MutableList<ASTNode>) {
        if (node.elementType == LookMLTypes.YAML_PROPERTY) out.add(node)
        var c = node.firstChildNode
        while (c != null) { collectProperties(c, out); c = c.treeNext }
    }

    private fun name(node: ASTNode): String? {
        var c = node.firstChildNode
        while (c != null) {
            if (c.elementType == LookMLTypes.YAML_PROPERTY_NAME) return c.text.trim().removeSuffix(":")
            c = c.treeNext
        }
        return null
    }

    fun testDashboardLevelPropertyAccepted() = assertTrue(isAccepted("layout"))

    fun testElementPropertyAccepted() = assertTrue(isAccepted("fields"))

    fun testUnknownDashboardPropertyFlagged() = assertFalse(isAccepted("not_a_real_prop_zzz"))

    // Filter-only properties must be accepted on item entries (element/filter are indistinguishable
    // in the PSI, so both validate against the union). These were the original false positives.
    fun testFilterDefaultValueAccepted() = assertTrue(isAccepted("default_value"))

    fun testFilterAllowMultipleValuesAccepted() = assertTrue(isAccepted("allow_multiple_values"))

    fun testFilterRequiredAccepted() = assertTrue(isAccepted("required"))

    // `filters` is valid both as the dashboard-level section and as an element/tile property.
    // The PSI does not reliably surface the `filters:` node, so assert acceptance at the schema
    // level for both object types (the union also covers it on items).
    fun testFiltersAcceptedAsDashboardSection() =
        assertTrue(YamlDashboardSchema.isKnownProperty(ObjectType.DASHBOARD, "filters"))

    fun testFiltersAcceptedAsElementProperty() =
        assertTrue(YamlDashboardSchema.isKnownProperty(ObjectType.ELEMENT, "filters"))

    // dynamic_fields (table calculation) entries are item-level; their properties must be accepted
    // via the union, including the internal _kind_hint / _type_hint Looker generates.
    fun testTableCalculationPropertiesAccepted() {
        listOf("expression", "_kind_hint", "_type_hint", "value_format_name", "based_on", "label")
            .forEach { assertTrue("table-calc prop '$it'", YamlDashboardSchema.isKnownProperty(ObjectType.ELEMENT, it)) }
    }
}
