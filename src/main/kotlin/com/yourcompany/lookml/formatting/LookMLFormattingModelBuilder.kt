package com.yourcompany.lookml.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiDocumentManager
import com.yourcompany.lookml.LookMLLanguage

class LookMLFormattingModelBuilder : FormattingModelBuilder {
    
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val element = formattingContext.psiElement
        val settings = formattingContext.codeStyleSettings
        
        // Only apply formatting when explicitly requested (not on every keystroke)
        // Remove auto-formatting to prevent it from running on every keystroke
        // applySimpleFormatting(element.containingFile)
        
        return FormattingModelProvider.createFormattingModelForPsiFile(
            element.containingFile,
            SimpleBlock(element.node),
            settings
        )
    }
    
    override fun getRangeAffectingIndent(file: PsiFile, offset: Int, elementAtOffset: ASTNode): TextRange? {
        return null
    }
    
    private fun applySimpleFormatting(file: PsiFile) {
        val document = PsiDocumentManager.getInstance(file.project).getDocument(file) ?: return
        
        WriteCommandAction.runWriteCommandAction(file.project) {
            val text = document.text
            
            // Check if this is a YAML dashboard file
            if (isYamlDashboard(text)) {
                applyYamlFormatting(text, document)
            } else {
                applyClassicFormatting(text, document)
            }
        }
    }
    
    private fun isYamlDashboard(text: String): Boolean {
        val trimmed = text.trimStart()
        return trimmed.startsWith("---") || trimmed.startsWith("- dashboard:")
    }
    
    private fun applyYamlFormatting(text: String, document: Document) {
        val lines = text.split('\n')
        val formattedLines = mutableListOf<String>()
        
        var currentContext = mutableListOf<String>()
        var lastLineWasEmpty = false
        
        for (i in lines.indices) {
            val line = lines[i]
            val trimmed = line.trim()
            
            // Skip multiple empty lines
            if (trimmed.isEmpty()) {
                if (!lastLineWasEmpty) {
                    formattedLines.add("")
                    lastLineWasEmpty = true
                }
                continue
            }
            
            // Calculate YAML indentation level based on context
            val yamlIndent = calculateYamlIndentWithContext(trimmed, currentContext)
            
            // Add proper indentation
            val indent = "  ".repeat(yamlIndent)
            formattedLines.add(indent + trimmed)
            
            // Update context for next line
            updateYamlContext(trimmed, currentContext)
            
            lastLineWasEmpty = false
        }
        
        // Join lines and update document
        val formattedText = formattedLines.joinToString("\n")
        document.replaceString(0, document.textLength, formattedText)
    }
    
    private fun calculateYamlIndentWithContext(line: String, context: List<String>): Int {
        return when {
            line.startsWith("---") -> 0
            line.startsWith("- dashboard:") -> 0
            line.startsWith("- title:") || line.startsWith("- name:") -> {
                // Element in elements array
                if (context.contains("elements")) 1 else 0
            }
            line.startsWith("-") -> 1
            line.contains(":") -> {
                when {
                    // Top-level dashboard properties
                    line.startsWith("title:") || line.startsWith("layout:") || 
                    line.startsWith("description:") || line.startsWith("elements:") -> 1
                    
                    // Element properties
                    line.startsWith("model:") || line.startsWith("explore:") || 
                    line.startsWith("type:") || line.startsWith("fields:") -> 2
                    
                    // Nested objects (filters, listen)
                    line.startsWith("filters:") || line.startsWith("listen:") -> 2
                    
                    // Properties inside filters/listen
                    context.contains("filters") || context.contains("listen") -> 3
                    
                    // Row/col properties
                    line.startsWith("row:") || line.startsWith("col:") || 
                    line.startsWith("width:") || line.startsWith("height:") -> 2
                    
                    else -> 2
                }
            }
            else -> 2
        }
    }
    
    private fun updateYamlContext(line: String, context: MutableList<String>) {
        when {
            line.startsWith("elements:") -> {
                context.clear()
                context.add("elements")
            }
            line.startsWith("filters:") -> {
                context.add("filters")
            }
            line.startsWith("listen:") -> {
                context.add("listen")
            }
            line.startsWith("- title:") || line.startsWith("- name:") -> {
                // Starting new element, clear nested contexts
                context.removeAll { it in listOf("filters", "listen") }
            }
        }
    }
    
    private fun calculateYamlIndent(line: String): Int {
        return when {
            line.startsWith("---") -> 0
            line.startsWith("- dashboard:") -> 0
            line.startsWith("- title:") || line.startsWith("- name:") -> 1
            line.startsWith("-") -> 1
            line.contains(":") -> {
                // Count nesting level based on context
                when {
                    line.startsWith("title:") || line.startsWith("layout:") || 
                    line.startsWith("description:") || line.startsWith("elements:") -> 1
                    line.startsWith("model:") || line.startsWith("explore:") || 
                    line.startsWith("type:") || line.startsWith("fields:") ||
                    line.startsWith("filters:") || line.startsWith("listen:") -> 2
                    else -> 2
                }
            }
            else -> 2
        }
    }
    
    private fun applyClassicFormatting(text: String, document: Document) {
        val lines = text.split('\n')
        val formattedLines = mutableListOf<String>()
        
        var indentLevel = 0
        var lastLineWasEmpty = false
        var lastLineWasOpenBrace = false
        
        for (i in lines.indices) {
            val line = lines[i]
            val trimmed = line.trim()
            
            // Skip empty lines
            if (trimmed.isEmpty()) {
                // Don't add empty lines after opening braces or before closing braces
                val nextLineIsClosingBrace = (i + 1 < lines.size && lines[i + 1].trim() == "}")
                if (!lastLineWasOpenBrace && !nextLineIsClosingBrace) {
                    if (!lastLineWasEmpty) {
                        formattedLines.add("")
                        lastLineWasEmpty = true
                    }
                }
                continue
            }
            
            // Decrease indent for closing braces
            if (trimmed == "}") {
                indentLevel = maxOf(0, indentLevel - 1)
            }
            
            // Normalize spaces in the line (remove extra spaces)
            val normalizedLine = normalizeSpaces(trimmed)
            
            // Add proper indentation
            val indent = "  ".repeat(indentLevel)
            formattedLines.add(indent + normalizedLine)
            
            // Increase indent for opening braces
            if (normalizedLine.endsWith("{")) {
                indentLevel++
                lastLineWasOpenBrace = true
            } else {
                lastLineWasOpenBrace = false
            }
            
            lastLineWasEmpty = false
        }
        
        // Join lines and update document
        val formattedText = formattedLines.joinToString("\n")
        document.replaceString(0, document.textLength, formattedText)
    }
    
    private fun normalizeSpaces(line: String): String {
        // Replace multiple spaces with single space, but preserve spaces in strings
        return line.replace(Regex("\\s+"), " ")
            .replace(" :", ":")  // Remove space before colon
            .replace(":  ", ": ") // Single space after colon
            .replace("{ ", "{")   // Remove space after opening brace
            .replace(" }", "}")   // Remove space before closing brace
    }
    
    private fun isClosingBrace(line: String): Boolean {
        return line.trim() == "}"
    }
    
    private fun isBlockLine(line: String): Boolean {
        return line.contains(":") && line.endsWith("{") && 
               (line.startsWith("view:") || line.startsWith("explore:") || 
                line.startsWith("dimension:") || line.startsWith("measure:") ||
                line.startsWith("parameter:") || line.startsWith("test:") ||
                line.startsWith("join:"))
    }
}

class SimpleBlock(private val node: ASTNode) : ASTBlock {
    override fun getNode(): ASTNode = node
    override fun getTextRange(): TextRange = node.textRange
    override fun getWrap(): Wrap? = null
    override fun getIndent(): Indent = Indent.getNoneIndent()
    override fun getAlignment(): Alignment? = null
    override fun getSpacing(child1: Block?, child2: Block): Spacing? = null
    override fun getChildAttributes(newChildIndex: Int): ChildAttributes = ChildAttributes(Indent.getNoneIndent(), null)
    override fun isIncomplete(): Boolean = false
    override fun isLeaf(): Boolean = node.firstChildNode == null
    override fun getSubBlocks(): List<Block> = emptyList()
}