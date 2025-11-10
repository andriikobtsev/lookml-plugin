package com.yourcompany.lookml.formatting

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiDocumentManager
import com.yourcompany.lookml.LookMLLanguage

class LookMLFormatAction : AnAction("Format LookML Code") {
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val document = editor.document
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document) ?: return
        
        // Only format LookML files
        if (psiFile.language != LookMLLanguage) return
        
        WriteCommandAction.runWriteCommandAction(project, "Format LookML", null, {
            val text = document.text
            
            // Check if this is a YAML dashboard file
            if (isYamlDashboard(text)) {
                applyYamlFormatting(text, document)
            } else {
                applyClassicFormatting(text, document)
            }
        })
    }
    
    override fun update(e: AnActionEvent) {
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)
        val psiFile = editor?.let { PsiDocumentManager.getInstance(project!!).getPsiFile(it.document) }
        
        e.presentation.isEnabled = psiFile?.language == LookMLLanguage
    }
    
    private fun isYamlDashboard(text: String): Boolean {
        val trimmed = text.trimStart()
        return trimmed.startsWith("---") || trimmed.startsWith("- dashboard:")
    }
    
    private fun applyYamlFormatting(text: String, document: com.intellij.openapi.editor.Document) {
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
        
        // Only update if content actually changed to avoid saying "formatted" when nothing changed
        if (formattedText != text) {
            document.replaceString(0, document.textLength, formattedText)
        }
    }
    
    private fun applyClassicFormatting(text: String, document: com.intellij.openapi.editor.Document) {
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
        
        // Only update if content actually changed to avoid saying "formatted" when nothing changed
        if (formattedText != text) {
            document.replaceString(0, document.textLength, formattedText)
        }
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
                    // Top-level dashboard properties (right after - dashboard:)
                    line.startsWith("title:") || line.startsWith("layout:") || 
                    line.startsWith("description:") || line.startsWith("elements:") || 
                    line.startsWith("preferred_slug:") -> 1
                    
                    // Nested objects (filters, listen) - these are at element level
                    line.startsWith("filters:") || line.startsWith("listen:") -> 2
                    
                    // Properties inside filters/listen sections - need deeper indent
                    context.contains("filters") || context.contains("listen") -> 3
                    
                    // Element properties (inside elements array but not in nested objects)
                    context.contains("elements") && !context.contains("filters") && !context.contains("listen") -> 2
                    
                    // Default element level properties
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
                // Remove previous nested context and add filters
                context.removeAll { it in listOf("filters", "listen") }
                context.add("filters")
            }
            line.startsWith("listen:") -> {
                // Remove previous nested context and add listen
                context.removeAll { it in listOf("filters", "listen") }
                context.add("listen")
            }
            line.startsWith("- title:") || line.startsWith("- name:") -> {
                // Starting new element, clear nested contexts but keep elements
                context.removeAll { it in listOf("filters", "listen") }
            }
            // Clear nested contexts when we encounter other element-level properties
            line.startsWith("model:") || line.startsWith("explore:") || line.startsWith("type:") ||
            line.startsWith("fields:") || line.startsWith("limit:") || line.startsWith("custom_color_enabled:") ||
            line.startsWith("show_single_value_title:") || line.startsWith("show_comparison:") ||
            line.startsWith("comparison_type:") || line.startsWith("defaults_version:") ||
            line.startsWith("row:") || line.startsWith("col:") || line.startsWith("width:") || line.startsWith("height:") -> {
                context.removeAll { it in listOf("filters", "listen") }
            }
        }
    }
    
    private fun normalizeSpaces(line: String): String {
        return line.replace(Regex("\\s+"), " ")
            .replace(" :", ":")
            .replace(":  ", ": ")
            .replace("{ ", "{")
            .replace(" }", "}")
    }
}