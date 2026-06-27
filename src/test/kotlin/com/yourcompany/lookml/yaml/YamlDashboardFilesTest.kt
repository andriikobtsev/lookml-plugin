package com.yourcompany.lookml.yaml

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class YamlDashboardFilesTest {

    @Test
    fun detectsListDashboardPrefix() {
        assertTrue(YamlDashboardFiles.isYamlDashboardContent("- dashboard: sales\n  title: x"))
    }

    @Test
    fun detectsDocumentStyle() {
        val y =
            """
            ---
            - dashboard: d
              elements: []
            """.trimIndent()
        assertTrue(YamlDashboardFiles.isYamlDashboardContent(y))
    }

    @Test
    fun rejectsPlainLookmlView() {
        assertFalse(YamlDashboardFiles.isYamlDashboardContent("view: orders {\n  dimension: id {}}\n"))
    }
}
