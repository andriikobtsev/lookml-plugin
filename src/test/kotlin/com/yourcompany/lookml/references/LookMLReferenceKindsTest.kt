package com.yourcompany.lookml.references

import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Milestone C (reference layer): the remaining reference kinds now carry real [LookMLReference]s, so they
 * power Find Usages / Rename (and Go to Definition dedupes against the handler):
 * - `filters: [ field: "v", view.field: "v" ]` keys -> field (fixes B8: bare vs qualified inconsistency),
 * - `from:` / `view_name:` -> view,
 * - `value_format_name: fmt` -> `named_value_format`,
 * - `@{constant}` inside SQL blocks -> `constant`.
 */
class LookMLReferenceKindsTest : BasePlatformTestCase() {

    private fun usageCountAtCaret(): Int = myFixture.findUsages(myFixture.elementAtCaret).size

    fun testFilterKeyUsagesBareAndQualified() {
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              dimension: st<caret>atus {
                type: string
                sql: ${'$'}{TABLE}.status ;;
              }
              measure: a {
                type: count
                filters: [status: "x"]
              }
              measure: b {
                type: count
                filters: [orders.status: "y"]
              }
            }
            """.trimIndent(),
        )
        // Bare `status:` + qualified `orders.status:` filter keys.
        assertEquals("both filter keys count as usages", 2, usageCountAtCaret())
    }

    fun testBareFilterKeyResolvesForGoTo() {
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              dimension: status {
                type: string
                sql: ${'$'}{TABLE}.status ;;
              }
              measure: a {
                type: count
                filters: [sta<caret>tus: "x"]
              }
            }
            """.trimIndent(),
        )
        val target = myFixture.getReferenceAtCaretPosition()?.resolve()
        assertNotNull("bare filter key resolves (B8)", target)
        assertEquals("status", target!!.text)
    }

    fun testFromPropertyUsages() {
        myFixture.configureByText(
            "orders.lkml",
            """
            view: or<caret>ders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            explore: e {
              join: j {
                from: orders
              }
            }
            """.trimIndent(),
        )
        assertEquals("from: value counts as a view usage", 1, usageCountAtCaret())
    }

    fun testValueFormatNameUsages() {
        myFixture.configureByText(
            "model.lkml",
            """
            named_value_format: my_f<caret>mt {}
            view: orders {
              measure: total {
                type: sum
                sql: ${'$'}{TABLE}.x ;;
                value_format_name: my_fmt
              }
            }
            """.trimIndent(),
        )
        assertEquals("value_format_name references the format", 1, usageCountAtCaret())
    }

    fun testConstantUsagesInSqlBlock() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              sql_table_name: @{table_prefix}.orders ;;
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "manifest.lkml",
            """
            constant: table_pre<caret>fix {
              value: "analytics"
            }
            """.trimIndent(),
        )
        assertEquals("@{constant} in a SQL block counts as a usage", 1, usageCountAtCaret())
    }
}
