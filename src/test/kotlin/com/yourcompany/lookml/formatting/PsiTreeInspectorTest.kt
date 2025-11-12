package com.yourcompany.lookml.formatting

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.testFramework.ParsingTestCase
import com.yourcompany.lookml.LookMLParserDefinition

/**
 * Test to inspect the actual PSI tree structure
 * This helps us understand what we're formatting
 */
class PsiTreeInspectorTest : ParsingTestCase("", "lkml", LookMLParserDefinition()) {

    override fun getTestDataPath(): String {
        return "src/test/resources/formatter"
    }

    fun testInspectTraditionalSimple() {
        val file = parseFile("traditional_simple_input", loadFile("traditional_simple_input.lkml"))

        println("\n=== PSI TREE FOR TRADITIONAL SIMPLE ===")
        printPsiTree(file.node, 0)
        println("\n=== END PSI TREE ===")
    }

    fun testInspectYamlSimple() {
        val file = parseFile("yaml_simple_input", loadFile("yaml_simple_input.lkml"))

        println("\n=== PSI TREE FOR YAML SIMPLE ===")
        printPsiTree(file.node, 0)
        println("\n=== END PSI TREE ===")
    }

    private fun printPsiTree(node: ASTNode, depth: Int) {
        val indent = "  ".repeat(depth)
        val text = node.text.replace("\n", "\\n").take(50)
        println("$indent${node.elementType} [$text]")

        var child = node.firstChildNode
        while (child != null) {
            printPsiTree(child, depth + 1)
            child = child.treeNext
        }
    }
}
