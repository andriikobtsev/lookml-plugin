package com.yourcompany.lookml.formatting

import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * A YAML dashboard whose file starts with a #-comment before `---` must still be detected as YAML
 * (lexer routing) and parse without errors. A leading comment previously defeated detection, so the
 * whole file was parsed as traditional LookML and every line errored.
 */
class YamlLeadingCommentTest : BasePlatformTestCase() {

    private fun parseErrors(text: String): Int {
        val psi = myFixture.configureByText("d.lookml", text)
        return PsiTreeUtil.collectElementsOfType(psi, PsiErrorElement::class.java).size
    }

    fun testLeadingCommentBeforeDocumentStart() {
        val dashboard = """
            # Header comment that sits above the document start.
            ---
            - dashboard: d1
              title: "T"
              layout: newspaper
              elements:
              - name: e1
                type: looker_grid
                fields: [a.b, a.c]
                row: 0
        """.trimIndent()
        assertEquals("leading-comment dashboard should parse cleanly as YAML", 0, parseErrors(dashboard))
    }

    fun testNoLeadingCommentStillWorks() {
        val dashboard = """
            ---
            - dashboard: d1
              title: "T"
              elements:
              - name: e1
                type: looker_grid
        """.trimIndent()
        assertEquals(0, parseErrors(dashboard))
    }
}
