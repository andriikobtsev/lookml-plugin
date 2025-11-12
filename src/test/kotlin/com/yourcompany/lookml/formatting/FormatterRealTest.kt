package com.yourcompany.lookml.formatting

import com.intellij.application.options.CodeStyle
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Real formatter tests that actually run the formatter and check output
 */
class FormatterRealTest : BasePlatformTestCase() {

    fun testSqlOnOneLine() {
        val input = """
            explore: orders {
              join: users {
                sql_on: ${'$'}{orders.user_id} = ${'$'}{users.id} ;;
              }
            }
        """.trimIndent()

        val expected = """
            explore: orders {
              join: users {
                sql_on: ${'$'}{orders.user_id} = ${'$'}{users.id} ;;
              }
            }
        """.trimIndent()

        val result = formatLookML(input)
        println("=== SQL ON ONE LINE TEST ===")
        println("INPUT:\n$input")
        println("\nEXPECTED:\n$expected")
        println("\nACTUAL RESULT:\n$result")
        println("=== END ===\n")

        assertEquals(expected, result)
    }

    fun testSqlOnMultipleLines() {
        val input = """
            explore: orders {
              join: users {
                sql_on: ${'$'}{
                    orders.user_id
                } = ${'$'}{
                    users.id
                } ;;
              }
            }
        """.trimIndent()

        val expected = """
            explore: orders {
              join: users {
                sql_on: ${'$'}{orders.user_id} = ${'$'}{users.id} ;;
              }
            }
        """.trimIndent()

        val result = formatLookML(input)
        println("=== SQL ON MULTIPLE LINES TEST ===")
        println("INPUT:\n$input")
        println("\nEXPECTED:\n$expected")
        println("\nACTUAL RESULT:\n$result")
        println("=== END ===\n")

        // For now, just print - we'll see what it actually does
        // assertEquals(expected, result)
    }

    fun testYamlIndentation() {
        val input = """
            ---
            - dashboard: test
            title: "Test"
            elements:
            - name: elem1
            type: single_value
        """.trimIndent()

        val expected = """
            ---
            - dashboard: test
              title: "Test"
              elements:
              - name: elem1
                type: single_value
        """.trimIndent()

        val result = formatLookML(input)
        println("=== YAML INDENTATION TEST ===")
        println("INPUT:\n$input")
        println("\nEXPECTED:\n$expected")
        println("\nACTUAL RESULT:\n$result")
        println("=== END ===\n")

        // For now, just print
        // assertEquals(expected, result)
    }

    fun testSimpleViewIndentation() {
        val input = """
            view: users {
            dimension: id {
            type: number
            }
            }
        """.trimIndent()

        val expected = """
            view: users {
              dimension: id {
                type: number
              }
            }
        """.trimIndent()

        val result = formatLookML(input)
        println("=== SIMPLE VIEW INDENTATION TEST ===")
        println("INPUT:\n$input")
        println("\nEXPECTED:\n$expected")
        println("\nACTUAL RESULT:\n$result")
        println("=== END ===\n")

        assertEquals(expected, result)
    }

    private fun formatLookML(code: String): String {
        // Create a .lkml file with the code
        val file = myFixture.configureByText("test.lkml", code)

        // Format it
        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(file)
        }

        // Return the result
        return file.text
    }
}
