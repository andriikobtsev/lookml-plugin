package com.yourcompany.lookml.completion

import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Milestone F: inside a `${...}` reference in a `sql:` block, completion suggests the fields available
 * to the enclosing view - its own plus everything inherited up the `extends` chain, mirroring Looker.
 */
class LookMLSqlFieldCompletionTest : BasePlatformTestCase() {

    private val base =
        """
        view: base {
          dimension: base_id { type: number }
          measure: base_count { type: count }
        }
        """.trimIndent()

    fun testSqlRefSuggestsOwnAndInheritedFields() {
        myFixture.addFileToProject("base.lkml", base)
        myFixture.configureByText(
            "child.lkml",
            """
            view: child {
              extends: [base]
              dimension: total {
                type: number
                sql: ${'$'}{<caret>} ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.completeBasic()
        val items = myFixture.lookupElementStrings ?: emptyList()
        assertTrue(
            "own + inherited fields offered, got $items",
            items.containsAll(listOf("base_id", "base_count", "total")),
        )
    }

    fun testSqlRefFiltersByTypedPrefix() {
        myFixture.addFileToProject("base.lkml", base)
        myFixture.configureByText(
            "child.lkml",
            """
            view: child {
              extends: [base]
              dimension: total {
                sql: ${'$'}{base_<caret>} ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.completeBasic()
        val items = myFixture.lookupElementStrings ?: emptyList()
        assertTrue("base_id/base_count match prefix", items.containsAll(listOf("base_id", "base_count")))
        assertFalse("total does not match prefix base_", items.contains("total"))
    }

    fun testDottedRefSuggestsNamedViewFields() {
        myFixture.addFileToProject("base.lkml", base)
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              dimension: id {
                sql: ${'$'}{base.<caret>} ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.completeBasic()
        val items = myFixture.lookupElementStrings ?: emptyList()
        assertTrue("cross-view fields of base offered, got $items", items.containsAll(listOf("base_id", "base_count")))
    }
}
