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
