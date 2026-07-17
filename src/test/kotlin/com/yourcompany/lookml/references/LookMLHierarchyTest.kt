package com.yourcompany.lookml.references

import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Backs the extends/refinement gutter markers (up = base, down = extenders/refinements).
 *
 * Guards the target multiplicity the owner reported wrong: exactly one base going up (no duplicates),
 * and ALL extenders going down (not just one).
 */
class LookMLHierarchyTest : BasePlatformTestCase() {

    fun testSingleBaseFoundOnce() {
        myFixture.addFileToProject(
            "base.lkml",
            """
            view: base {
              dimension: id { type: number }
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject(
            "child.lkml",
            """
            view: child {
              extends: [base]
            }
            """.trimIndent(),
        )
        assertEquals("base must be found exactly once", 1, LookMLResolve.findViews(project, "base").size)
    }

    fun testAllExtendersFoundGoingDown() {
        myFixture.addFileToProject(
            "base.lkml",
            """
            view: base {
              dimension: id { type: number }
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject(
            "a.lkml",
            """
            view: child_a {
              extends: [base]
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject(
            "b.lkml",
            """
            view: child_b {
              extends: [base]
            }
            """.trimIndent(),
        )
        val extenders = LookMLResolve.findViewsExtending(project, "base").mapNotNull { LookMLResolve.declName(it) }
        assertEquals("both extenders must be found going down", setOf("child_a", "child_b"), extenders.toSet())
    }

    fun testRefinementFamilyAndBackReferences() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              dimension: id { type: number }
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject(
            "orders_ref.lkml",
            """
            view: +orders {
              dimension: extra { type: number }
            }
            """.trimIndent(),
        )
        assertEquals("one refinement", 1, LookMLResolve.findRefinements(project, "orders").size)
        assertEquals("family = base + refinement", 2, LookMLResolve.findViewFamily(project, "orders").size)
    }
}
