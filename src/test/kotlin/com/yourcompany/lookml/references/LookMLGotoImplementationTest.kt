package com.yourcompany.lookml.references

import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Milestone E: "Go to Implementation(s)" (Ctrl/Cmd+Alt+B) on a base view returns every view that
 * `extends` or refines it, via [LookMLImplementationsSearch] (definitionsSearch EP).
 *
 * The search yields the name leaves of the implementing views, so results are read via [text].
 */
class LookMLGotoImplementationTest : BasePlatformTestCase() {

    fun testImplementationsIncludeExtendersAndRefinements() {
        myFixture.addFileToProject("a.lkml", "view: child_a {\n  extends: [base]\n}")
        myFixture.addFileToProject("b.lkml", "view: child_b {\n  extends: [base]\n}")
        myFixture.addFileToProject("ref.lkml", "view: +base {\n  dimension: extra { type: number }\n}")
        myFixture.addFileToProject(
            "base.lkml",
            """
            view: base {
              dimension: id { type: number }
            }
            """.trimIndent(),
        )
        val base = LookMLResolve.findViews(project, "base").single()
        val impls = DefinitionsScopedSearch.search(base).findAll().map { it.text }.toSet()
        assertEquals(
            "Go to Implementation lists both extenders + the refinement",
            setOf("child_a", "child_b", "+base"),
            impls,
        )
    }

    fun testNoImplementationsForLeafView() {
        myFixture.addFileToProject(
            "solo.lkml",
            """
            view: solo {
              dimension: id { type: number }
            }
            """.trimIndent(),
        )
        val solo = LookMLResolve.findViews(project, "solo").single()
        assertTrue(
            "a view nobody extends has no implementations",
            DefinitionsScopedSearch.search(solo).findAll().isEmpty(),
        )
    }
}
