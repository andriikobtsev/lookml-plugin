package com.yourcompany.lookml.formatting

import com.intellij.psi.formatter.FormatterTestCase
import com.yourcompany.lookml.LookMLFileType

/**
 * Test the V2 formatter implementation
 */
class LookMLFormatterTestV2 : FormatterTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources/formatter"
    }

    override fun getBasePath(): String {
        return "formatter"
    }

    override fun getFileExtension(): String {
        return "lkml"
    }

    fun testTraditionalSimple() {
        doTestFromFiles("traditional_simple_input.lkml", "traditional_simple_expected.lkml")
    }

    fun testYamlSimple() {
        doTestFromFiles("yaml_simple_input.lkml", "yaml_simple_expected.lkml")
    }

    private fun doTestFromFiles(inputFile: String, expectedFile: String) {
        val input = loadFile(inputFile)
        val expected = loadFile(expectedFile)
        doTextTest(input, expected)
    }

    private fun loadFile(fileName: String): String {
        return java.io.File("$testDataPath/$fileName").readText()
    }
}
