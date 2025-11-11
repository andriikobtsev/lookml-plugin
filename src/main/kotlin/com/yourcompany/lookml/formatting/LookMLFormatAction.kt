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
        var normalized = line

        // First, handle SQL blocks specially - preserve content between ;; markers
        if (line.contains("sql:", ignoreCase = true) && line.contains(";;")) {
            val parts = line.split(";;")
            if (parts.size >= 2) {
                val beforeSql = parts[0]
                val sqlContent = parts.dropLast(1).drop(1).joinToString(";;")
                val afterSql = if (parts.last().isNotBlank()) ";;" + parts.last() else ";;"

                // Normalize only the property name part
                val normalizedBefore = beforeSql.replace(Regex("\\s+"), " ")
                    .replace(" :", ":")
                    .replace(":  ", ": ")

                // Capitalize SQL keywords in SQL content
                val capitalizedSql = capitalizeSqlKeywords(sqlContent.trim())

                return normalizedBefore + " " + capitalizedSql + " " + afterSql
            }
        }

        // For non-SQL lines, do standard normalization
        normalized = normalized.replace(Regex("\\s+"), " ")
            .replace(" :", ":")
            .replace(":  ", ": ")
            .replace("{ ", "{")
            .replace(" }", "}")

        // Capitalize SQL keywords if this is an inline SQL property
        if (normalized.contains("sql:", ignoreCase = true) ||
            normalized.contains("sql_on:", ignoreCase = true) ||
            normalized.contains("sql_where:", ignoreCase = true) ||
            normalized.contains("sql_table_name:", ignoreCase = true) ||
            normalized.contains("sql_always_where:", ignoreCase = true) ||
            normalized.contains("sql_always_having:", ignoreCase = true) ||
            normalized.contains("sql_trigger:", ignoreCase = true)) {
            normalized = capitalizeSqlInProperty(normalized)
        }

        return normalized
    }

    private fun capitalizeSqlKeywords(sql: String): String {
        if (sql.isBlank()) return sql

        val sqlKeywords = setOf(
            "SELECT", "FROM", "WHERE", "JOIN", "LEFT", "RIGHT", "INNER", "OUTER", "FULL",
            "ON", "AND", "OR", "NOT", "IN", "EXISTS", "CASE", "WHEN", "THEN", "ELSE", "END",
            "GROUP", "BY", "HAVING", "ORDER", "ASC", "DESC", "LIMIT", "OFFSET",
            "UNION", "INTERSECT", "EXCEPT", "AS", "DISTINCT", "ALL", "COUNT", "SUM",
            "AVG", "MIN", "MAX", "CAST", "COALESCE", "NULLIF", "IS", "NULL",
            "BETWEEN", "LIKE", "ILIKE", "SIMILAR", "TO", "INSERT", "UPDATE", "DELETE",
            "CREATE", "DROP", "ALTER", "TABLE", "VIEW", "INDEX", "WITH"
        )

        // Split by spaces but preserve ${...} template expressions and strings
        val result = StringBuilder()
        var i = 0
        var currentWord = StringBuilder()
        var inTemplate = false
        var inString = false
        var stringChar = '\u0000'

        while (i < sql.length) {
            val char = sql[i]

            when {
                // Handle template expressions ${...}
                !inString && char == '$' && i + 1 < sql.length && sql[i + 1] == '{' -> {
                    if (currentWord.isNotEmpty()) {
                        result.append(capitalizeIfKeyword(currentWord.toString(), sqlKeywords))
                        currentWord.clear()
                    }
                    inTemplate = true
                    result.append(char)
                }
                inTemplate -> {
                    result.append(char)
                    if (char == '}') inTemplate = false
                }
                // Handle strings
                !inTemplate && (char == '"' || char == '\'') -> {
                    if (!inString) {
                        if (currentWord.isNotEmpty()) {
                            result.append(capitalizeIfKeyword(currentWord.toString(), sqlKeywords))
                            currentWord.clear()
                        }
                        inString = true
                        stringChar = char
                        result.append(char)
                    } else if (char == stringChar && (i == 0 || sql[i - 1] != '\\')) {
                        inString = false
                        result.append(char)
                    } else {
                        result.append(char)
                    }
                }
                inString -> {
                    result.append(char)
                }
                // Handle word boundaries
                !inTemplate && !inString && (char.isWhitespace() || char in "(),;") -> {
                    if (currentWord.isNotEmpty()) {
                        result.append(capitalizeIfKeyword(currentWord.toString(), sqlKeywords))
                        currentWord.clear()
                    }
                    result.append(char)
                }
                else -> {
                    currentWord.append(char)
                }
            }
            i++
        }

        // Don't forget the last word
        if (currentWord.isNotEmpty()) {
            result.append(capitalizeIfKeyword(currentWord.toString(), sqlKeywords))
        }

        return result.toString()
    }

    private fun capitalizeIfKeyword(word: String, keywords: Set<String>): String {
        val upper = word.uppercase()
        return if (upper in keywords) upper else word
    }

    private fun capitalizeSqlInProperty(line: String): String {
        // Find the SQL part after the colon
        val colonIndex = line.indexOf(':')
        if (colonIndex == -1) return line

        val propertyName = line.substring(0, colonIndex + 1)
        val value = line.substring(colonIndex + 1).trim()

        // Capitalize SQL keywords in the value
        val capitalizedValue = capitalizeSqlKeywords(value)

        return propertyName + " " + capitalizedValue
    }
}