package com.yourcompany.lookml.references

import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Milestone A (reference layer): Find Usages for LookML declarations.
 *
 * Exercises the real Find Usages pipeline (words scanner + [LookMLReference.isReferenceTo]) end to end via
 * the fixture, so it also proves the PsiNamedElement wiring and reference contribution work together.
 */
class LookMLFindUsagesTest : BasePlatformTestCase() {

    private fun usageCountAtCaret(): Int = myFixture.findUsages(myFixture.elementAtCaret).size

    fun testFieldUsagesAcrossFiles() {
        myFixture.addFileToProject(
            "explore.lkml",
            """
            explore: e {
              sql_always_where: ${'$'}{orders.id} = 1 ;;
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              dimension: i<caret>d {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
              measure: total {
                type: count
                sql: ${'$'}{id} ;;
              }
            }
            """.trimIndent(),
        )
        // `${id}` in the measure (same file) + `${orders.id}` field segment (other file).
        assertEquals("field should have 2 usages", 2, usageCountAtCaret())
    }

    fun testViewUsages() {
        myFixture.addFileToProject(
            "explore.lkml",
            """
            explore: e {
              sql_always_where: ${'$'}{orders.id} = 1 ;;
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders.lkml",
            """
            view: ord<caret>ers {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        // `${orders.id}` view segment.
        assertEquals("view should have 1 usage", 1, usageCountAtCaret())
    }

    fun testFieldListUsage() {
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              dimension: i<caret>d {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
              measure: total {
                type: count
                drill_fields: [id]
              }
            }
            """.trimIndent(),
        )
        assertEquals("field-list entry counts as a usage", 1, usageCountAtCaret())
    }

    fun testSetUsage() {
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              set: detail {
                fields: [id]
              }
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
              measure: total {
                type: count
                drill_fields: [detail*]
              }
            }
            """.trimIndent(),
        )
        myFixture.editor.caretModel.moveToOffset(myFixture.file.text.indexOf("detail {") + 1)
        assertEquals("set wildcard counts as a usage", 1, usageCountAtCaret())
    }
}
