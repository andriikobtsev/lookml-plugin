package com.yourcompany.lookml.formatting

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.formatter.FormatterTestCase
import com.intellij.util.IncorrectOperationException

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

    fun testLongArrayWrapsOnePerLine() {
        doTestFromFiles("traditional_array_wrap_input.lkml", "traditional_array_wrap_expected.lkml")
    }

    fun testSqlFunctionCallKeepsParenTight() {
        doTestFromFiles("sql_function_paren_input.lkml", "sql_function_paren_expected.lkml")
    }

    fun testDerivedTableWraps() {
        doTestFromFiles("derived_table_input.lkml", "derived_table_expected.lkml")
    }

    fun testSqlCommaHasNoSpaceBefore() {
        doTestFromFiles("sql_comma_input.lkml", "sql_comma_expected.lkml")
    }

    fun testMultilineSqlIsPreserved() {
        doTestFromFiles("sql_multiline_input.lkml", "sql_multiline_expected.lkml")
    }

    private fun doTestFromFiles(inputFile: String, expectedFile: String) {
        val input = loadFile(inputFile)
        val expected = loadFile(expectedFile)
        doTextTest(input, expected)
    }

    private fun loadFile(fileName: String): String {
        return java.io.File("$testDataPath/$fileName").readText()
    }

    /**
     * Strip leading #-comment block so goldens can use descriptive headers while the formatter
     * preserves the input file's header comment (same approach as [YamlDashboardRewriterTest]).
     */
    private fun normalizeGolden(text: String): String =
        text.trimEnd().lines()
            .dropWhile { it.trimStart().startsWith("#") || it.isBlank() }
            .joinToString("\n")
            .trim() + "\n"

    override fun checkDocument(file: PsiFile, text: String, textAfter: String) {
        val document = PsiDocumentManager.getInstance(project).getDocument(file)!!

        if (doCheckDocumentUpdate()) {
            val editor = FileEditorManager.getInstance(project).openTextEditor(
                OpenFileDescriptor(project, file.virtualFile, 0),
                false,
            ) as EditorImpl
            assertNotNull(editor)
            if (myFile != null) {
                FileEditorManager.getInstance(project).closeFile(myFile.virtualFile)
            }
            myEditor = editor
            myFile = file
        }

        WriteCommandAction.runWriteCommandAction(project) {
            document.replaceString(0, document.textLength, text)
            PsiDocumentManager.getInstance(project).commitDocument(document)
            assertEquals(file.text, document.text)
            try {
                when {
                    doReformatRangeTest ->
                        CodeStyleManager.getInstance(project).reformatRange(
                            file,
                            file.textRange.startOffset,
                            file.textRange.endOffset,
                        )
                    myTextRange != null ->
                        CodeStyleManager.getInstance(project).reformatText(
                            file,
                            myTextRange.startOffset,
                            myTextRange.endOffset,
                        )
                    else ->
                        CodeStyleManager.getInstance(project).reformatText(
                            file,
                            file.textRange.startOffset,
                            file.textRange.endOffset,
                        )
                }
            } catch (_: IncorrectOperationException) {
                fail()
            }
        }

        assertEquals(normalizeGolden(textAfter), normalizeGolden(document.text))
        PsiDocumentManager.getInstance(project).commitDocument(document)
        assertEquals(normalizeGolden(textAfter), normalizeGolden(file.text))
    }
}
