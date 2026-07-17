package com.yourcompany.lookml.references

import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.ParsingTestCase
import com.yourcompany.lookml.LookMLParserDefinition
import com.yourcompany.lookml.psi.LookMLTemplateReference

/**
 * Locks in the SQL-block lexer contract around brace syntax:
 * - `@{constant}` and Liquid `{{ ... }}` / `{% ... %}` parse cleanly (no error elements),
 * - `${view.field}` still parses to a real template reference (unchanged behaviour).
 *
 * These guard against regressions from the `@{}` / `{{}}` lexer additions.
 */
class LookMLSqlBlockParseTest : ParsingTestCase("", "lkml", LookMLParserDefinition()) {

    override fun getTestDataPath(): String = "src/test/resources"

    private fun assertNoErrors(src: String) {
        val file = parseFile("s", src)
        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)
        assertNull("unexpected parse error: ${error?.errorDescription} in:\n$src", error)
    }

    fun testConstantInSqlTableNameParsesClean() =
        assertNoErrors(
            """
            view: v {
              sql_table_name: @{gcp_project}.@{mfh_dataset_name}.tbl ;;
            }
            """.trimIndent(),
        )

    fun testSqlDistinctKeyParsesClean() =
        assertNoErrors(
            """
            view: v {
              measure: count_buildings {
                type: sum_distinct
                sql: ${'$'}{num} ;;
                sql_distinct_key: ${'$'}{id} ;;
                value_format_name: decimal_0
                drill_fields: [detail*]
              }
            }
            """.trimIndent(),
        )

    fun testArbitrarySqlBlockPropertyParsesClean() =
        assertNoErrors(
            """
            view: v {
              dimension_group: created {
                type: duration
                sql_start: ${'$'}{TABLE}.start ;;
                sql_end: ${'$'}{TABLE}.finish ;;
              }
            }
            """.trimIndent(),
        )

    fun testLiquidOutputParsesClean() =
        assertNoErrors(
            """
            view: v {
              dimension: d {
                sql: {{ some_liquid_output }} ;;
              }
            }
            """.trimIndent(),
        )

    fun testLiquidTagStillParsesClean() =
        assertNoErrors(
            """
            view: v {
              dimension: d {
                sql: {% if x %} a {% else %} b {% endif %} ;;
              }
            }
            """.trimIndent(),
        )

    fun testTemplateRefStillParsesToReference() {
        val file =
            parseFile(
                "s",
                """
                view: v {
                  dimension: d {
                    sql: ${'$'}{orders.id} ;;
                  }
                }
                """.trimIndent(),
            )
        val ref = PsiTreeUtil.findChildOfType(file, LookMLTemplateReference::class.java)
        assertNotNull("\${'$'}{orders.id} must still produce a template reference", ref)
        assertEquals("orders.id", ref!!.text)
        assertNull(
            "template ref sample must have no errors",
            PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java),
        )
    }
}
