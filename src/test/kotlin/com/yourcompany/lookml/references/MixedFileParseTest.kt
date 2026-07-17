package com.yourcompany.lookml.references

import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.ParsingTestCase
import com.yourcompany.lookml.LookMLParserDefinition
import com.yourcompany.lookml.psi.LookMLTemplateReference

/**
 * Real files interleave `@{constant}`, Liquid, and `${view.field}` in the same file. Before the lexer
 * fix, an earlier `@{}`/`{{}}` produced error elements whose recovery could desync parsing of a LATER
 * sql_on, so its `${...}` never became a template reference (-> "cannot find declaration"). This asserts
 * the whole file parses clean and every `${...}` is still a real reference.
 */
class MixedFileParseTest : ParsingTestCase("", "lkml", LookMLParserDefinition()) {

    override fun getTestDataPath(): String = "src/test/resources"

    fun testInterleavedConstantsLiquidAndTemplateRefs() {
        val file =
            parseFile(
                "s",
                """
                view: v {
                  sql_table_name: @{gcp_project}.@{ds}.orders ;;
                  dimension: id {
                    sql: ${'$'}{TABLE}.id ;;
                  }
                }

                explore: e {
                  join: bp {
                    sql_on: ${'$'}{v.id} = ${'$'}{bp.other_id}
                    AND DATE_TRUNC(${'$'}{bp.month}, MONTH) =
                    {% if x._is_filtered %}
                    DATE_TRUNC(DATE(TIMESTAMP({% date_start x.as_of %})), MONTH)
                    {% else %}
                    {{ some_output }}
                    {% endif %} ;;
                  }
                }
                """.trimIndent(),
            )
        val error = PsiTreeUtil.findChildOfType(file, PsiErrorElement::class.java)
        assertNull("mixed file should parse without errors: ${error?.errorDescription}", error)
        val refs = PsiTreeUtil.findChildrenOfType(file, LookMLTemplateReference::class.java).map { it.text }
        assertTrue("expected v.id template ref, got $refs", refs.contains("v.id"))
        assertTrue("expected bp.other_id template ref, got $refs", refs.contains("bp.other_id"))
        assertTrue("expected bp.month template ref, got $refs", refs.contains("bp.month"))
    }
}
