package com.yourcompany.lookml.references

import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Milestone B: Rename a LookML declaration and update its references (via the reference layer).
 *
 * Also guards precision: a coincidental same-name string that is NOT a reference (e.g. the literal
 * `.id` after `${TABLE}`) must stay untouched, and a same-named field in another view is not renamed.
 */
class LookMLRenameTest : BasePlatformTestCase() {

    fun testRenameFieldUpdatesTemplateAndFieldListUsages() {
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
                drill_fields: [id]
              }
            }
            """.trimIndent(),
        )
        myFixture.renameElementAtCaret("identifier")
        myFixture.checkResult(
            """
            view: orders {
              dimension: identifier {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
              measure: total {
                type: count
                sql: ${'$'}{identifier} ;;
                drill_fields: [identifier]
              }
            }
            """.trimIndent(),
        )
    }

    fun testRenameDoesNotTouchSameNameInOtherView() {
        myFixture.addFileToProject(
            "other.lkml",
            """
            view: other {
              dimension: id {
                type: number
                sql: ${'$'}{id} ;;
              }
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
        myFixture.renameElementAtCaret("identifier")
        // orders.id and its ${id} usage renamed; other.id untouched.
        val other = myFixture.findFileInTempDir("other.lkml")
        val otherText = String(other.contentsToByteArray())
        assertTrue("other view's id must be untouched", otherText.contains("dimension: id"))
        assertTrue("other view's \${id} must be untouched", otherText.contains("\${id}"))
    }
}
