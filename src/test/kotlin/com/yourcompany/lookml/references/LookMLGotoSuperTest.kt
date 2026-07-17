package com.yourcompany.lookml.references

import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Milestone E: "Go to Super" (Ctrl/Cmd+U) on a view resolves to the base view(s) it extends and,
 * for a refinement, the base view it refines. Exercises [LookMLGotoSuperHandler.superTargets].
 */
class LookMLGotoSuperTest : BasePlatformTestCase() {

    fun testExtendingViewGoesToBase() {
        myFixture.addFileToProject(
            "base.lkml",
            """
            view: base {
              dimension: id { type: number }
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject("child.lkml", "view: child {\n  extends: [base]\n}")

        val child = LookMLResolve.findViews(project, "child").single()
        val targets = LookMLGotoSuperHandler.superTargets(project, child).map { it.text }.toSet()
        assertEquals("extends -> base view", setOf("base"), targets)
    }

    fun testRefinementGoesToBaseOnce() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              dimension: id { type: number }
            }
            """.trimIndent(),
        )
        // A refinement that also lists the base in extends: must still be offered exactly once.
        myFixture.addFileToProject("orders_ref.lkml", "view: +orders {\n  extends: [orders]\n}")

        val refinement = LookMLResolve.findViews(project, "+orders").single()
        val targets = LookMLGotoSuperHandler.superTargets(project, refinement).map { it.text }
        assertEquals("refinement -> base, deduped to one", listOf("orders"), targets)
    }

    fun testSingleDeclarationYieldsExactlyOneSuper() {
        // Regression guard for the "up arrow shows the same base twice" report: with a single base
        // declaration and a single extender, Go to Super (and the up-arrow gutter, same logic) must
        // return exactly one target - the code path does not duplicate.
        myFixture.addFileToProject("base.lkml", "view: base {\n  dimension: id { type: number }\n}")
        myFixture.addFileToProject("child.lkml", "view: child {\n  extends: [base]\n}")

        val child = LookMLResolve.findViews(project, "child").single()
        assertEquals(1, LookMLGotoSuperHandler.superTargets(project, child).size)
    }

    fun testDuplicateBaseDeclarationsYieldTwoSupers() {
        // Documents the actual cause of the perceived "doubling": the base view name is declared in
        // two files, so both real declarations are offered. Different physical locations => dedup
        // (by file path + offset) correctly keeps both; it is not a spurious duplicate.
        myFixture.addFileToProject("base_a.lkml", "view: base {\n  dimension: id { type: number }\n}")
        myFixture.addFileToProject("base_b.lkml", "view: base {\n  dimension: id { type: number }\n}")
        myFixture.addFileToProject("child.lkml", "view: child {\n  extends: [base]\n}")

        val child = LookMLResolve.findViews(project, "child").single()
        val targets = LookMLGotoSuperHandler.superTargets(project, child)
        assertEquals("two real declarations of the same name -> two distinct targets", 2, targets.size)
        assertEquals(
            "the two targets live in different files (not one file counted twice)",
            2,
            targets.mapNotNull { it.containingFile?.viewProvider?.virtualFile?.path }.toSet().size,
        )
    }

    fun testBaseViewHasNoSuper() {
        myFixture.addFileToProject(
            "solo.lkml",
            """
            view: solo {
              dimension: id { type: number }
            }
            """.trimIndent(),
        )
        val solo = LookMLResolve.findViews(project, "solo").single()
        assertTrue("a base view has no super", LookMLGotoSuperHandler.superTargets(project, solo).isEmpty())
    }
}
