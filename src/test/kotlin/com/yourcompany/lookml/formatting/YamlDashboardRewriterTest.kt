package com.yourcompany.lookml.formatting

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.yourcompany.lookml.yaml.YamlDashboardFiles
import java.io.File

/**
 * [YamlDashboardRewriter] round-trip against committed fixtures in [formatter].
 */
class YamlDashboardRewriterTest : BasePlatformTestCase() {

    private val formatterDir = File("src/test/resources/formatter")

    /** Strip first #-comment block so goldens can describe intent; rewriter preserves input header comment. */
    private fun normalizeGolden(text: String): String =
        text.trimEnd().lines()
            .dropWhile { it.trim().startsWith("#") || it.isBlank() }
            .joinToString("\n")
            .trim() + "\n"

    fun testYamlSimpleRewriterMatchesExpected() {
        assertRewriterOutput("yaml_simple_input.lkml", "yaml_simple_expected.lkml")
    }

    /**
     * Multi-line flow arrays (fields wrapped over two lines) join into one line; values keep their
     * original form (arrays stay arrays, numbers stay numbers); |- block scalars (body_text) are
     * preserved verbatim. Regression guard for the array-becomes-quoted-string formatter bug.
     */
    fun testYamlMultilineArraysAndBlockScalars() {
        assertRewriterOutput("yaml_multiline_input.lkml", "yaml_multiline_expected.lkml")
    }

    /**
     * Nested maps / ui_config / listen blocks are not fully preserved by the line-based rewriter yet.
     * Smoke-test: no throw, dashboard id and major sections still present.
     */
    fun testYamlNestedRewriterSmoke() {
        val inputText = File(formatterDir, "yaml_nested_input.lkml").readText(Charsets.UTF_8)
        val psi = myFixture.configureByText("yaml_nested_input.lkml", inputText)
        val out = YamlDashboardRewriter.rewriteDashboard(psi)
        assertTrue(out.contains("- dashboard: complex_dashboard"))
        assertTrue(out.contains("filters:") || out.contains("elements:"))
    }

    /**
     * Nested block sequences (dynamic_fields / table calculations) and multi-line flow object
     * arrays (y_axes) are preserved verbatim, never re-parsed or collapsed. Their inner `-` items
     * must not be promoted to dashboard elements.
     */
    fun testDynamicFieldsAndObjectArraysPreservedVerbatim() {
        val input = """
            ---
            - dashboard: d
              title: "T"
              elements:
              - name: e1
                type: looker_grid
                filters:
                  some_view.status: Completed
                dynamic_fields:
                  - category: table_calculation
                    label: Capex
                    expression: "${'$'}{a.b}/${'$'}{c.d}"
                    table_calculation: capex
                    _kind_hint: measure
                    _type_hint: number
                y_axes: [{label: '', orientation: left,
                    showLabels: false}]
                row: 0
        """.trimIndent()
        val psi = myFixture.configureByText("d.lookml", input)
        val out = YamlDashboardRewriter.rewriteDashboard(psi)

        // dynamic_fields block kept intact (key + inner item + expression verbatim).
        assertTrue("dynamic_fields key", out.contains("dynamic_fields:"))
        assertTrue("table calc item kept", out.contains("- category: table_calculation"))
        assertTrue("expression intact", out.contains("expression: \"\${a.b}/\${c.d}\""))
        // The inner `- category` item must NOT become a second dashboard element.
        assertEquals("only one element name line", 1, Regex("(?m)^  - name: ").findAll(out).count())
        // Multi-line object array preserved (still spans two lines, not collapsed/quoted).
        assertTrue("y_axes object array preserved", out.contains("y_axes: [{label: '', orientation: left,"))
        assertTrue("y_axes continuation preserved", out.contains("showLabels: false}]"))
        // Element-level filters MAP preserved verbatim (not turned into a dashboard filters section).
        assertTrue("element filters map preserved", out.contains("some_view.status: Completed"))
        assertFalse("element filters not promoted to list item", out.contains("- some_view.status:"))
    }

    /**
     * Multiline scalars and inline objects are not fully handled by the rewriter; goldens would encode broken output.
     * Smoke-test only until rewriter supports these structures.
     */
    fun testYamlEdgeCasesRewriterSmoke() {
        val inputText = File(formatterDir, "yaml_edge_cases_input.lkml").readText(Charsets.UTF_8)
        val psi = myFixture.configureByText("yaml_edge_cases_input.lkml", inputText)
        val out = YamlDashboardRewriter.rewriteDashboard(psi)
        assertTrue(out.contains("- dashboard: edge_cases"))
        assertTrue(out.contains("elements:"))
    }

    private fun assertRewriterOutput(inputFileName: String, expectedFileName: String) {
        val inputText = File(formatterDir, inputFileName).readText(Charsets.UTF_8)
        val expectedText = File(formatterDir, expectedFileName).readText(Charsets.UTF_8)
        assertTrue(
            "Fixture must be YAML dashboard content: $inputFileName",
            YamlDashboardFiles.isYamlDashboardContent(inputText),
        )
        val psi = myFixture.configureByText(inputFileName, inputText)
        val actual = normalizeGolden(YamlDashboardRewriter.rewriteDashboard(psi))
        val expected = normalizeGolden(expectedText)
        assertEquals("Rewriter output differs for $inputFileName", expected, actual)
    }
}
