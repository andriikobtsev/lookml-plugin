package com.yourcompany.lookml.formatting

import com.intellij.psi.PsiFile

/**
 * Complete rewriter for YAML dashboards
 * Parses the text directly and rebuilds with correct formatting
 */
object YamlDashboardRewriter {
    
    fun rewriteDashboard(file: PsiFile): String {
        val text = file.text
        val lines = text.lines()
        
        println("🔍 Parsing ${lines.size} lines")
        
        val result = StringBuilder()
        var dashboardName = ""
        val dashboardProps = mutableListOf<Pair<String, String>>()
        val elements = mutableListOf<MutableMap<String, String>>()
        val filters = mutableListOf<MutableMap<String, String>>()
        
        var currentSection = ""  // "", "elements", "filters"
        var currentItem: MutableMap<String, String>? = null
        
        var i = 0
        while (i < lines.size) {
            val line = lines[i]
            val trimmed = line.trim()
            
            // Skip comments, empty, ---
            if (trimmed.isEmpty() || trimmed.startsWith("#") || trimmed == "---" || trimmed == "- - -") {
                if (trimmed.startsWith("#")) {
                    result.appendLine(trimmed)
                }
                i++
                continue
            }
            
            // Dashboard entry
            if (trimmed.startsWith("- dashboard:") || trimmed.startsWith("-dashboard:")) {
                val parts = trimmed.split(":", limit = 2)
                if (parts.size == 2) {
                    dashboardName = parts[1].trim().removeSurrounding("\"").removeSurrounding("'")
                }
                i++
                continue
            }
            
            // Check if it's a property line
            if (trimmed.contains(":")) {
                val colonIndex = trimmed.indexOf(':')
                val key = trimmed.substring(0, colonIndex).trim().removePrefix("-").trim()
                val value = trimmed.substring(colonIndex + 1).trim()
                
                // List entry (starts with -)
                if (trimmed.startsWith("-")) {
                    // Save previous item
                    if (currentItem != null) {
                        when (currentSection) {
                            "elements" -> elements.add(currentItem)
                            "filters" -> filters.add(currentItem)
                        }
                    }
                    
                    // Start new item
                    currentItem = mutableMapOf(key to value.removeSurrounding("\"").removeSurrounding("'"))
                } else {
                    // Regular property
                    if (key == "elements" || key == "filters") {
                        // Save previous item before switching sections
                        if (currentItem != null) {
                            when (currentSection) {
                                "elements" -> elements.add(currentItem)
                                "filters" -> filters.add(currentItem)
                            }
                            currentItem = null
                        }
                        
                        currentSection = key
                        
                        // Check if list entry is on same line
                        if (value.isNotEmpty() && !value.startsWith("[")) {
                            // Parse inline: elements: - name:value
                            val afterColon = value.removePrefix("-").trim()
                            if (afterColon.contains(":")) {
                                val entryParts = afterColon.split(":", limit = 2)
                                currentItem = mutableMapOf(
                                    entryParts[0].trim() to entryParts[1].trim().removeSurrounding("\"").removeSurrounding("'")
                                )
                            }
                        }
                    } else if (currentItem != null) {
                        // Add to current item
                        currentItem!![key] = value.removeSurrounding("\"").removeSurrounding("'")
                    } else {
                        // Dashboard-level property
                        dashboardProps.add(key to value.removeSurrounding("\"").removeSurrounding("'"))
                    }
                }
            }
            
            i++
        }
        
        // Save last item
        if (currentItem != null) {
            when (currentSection) {
                "elements" -> elements.add(currentItem)
                "filters" -> filters.add(currentItem)
            }
        }
        
        println("📦 Parsed: dashboard=$dashboardName, props=${dashboardProps.size}, elements=${elements.size}, filters=${filters.size}")
        
        // Build output
        result.appendLine("- dashboard: $dashboardName")
        
        // Dashboard properties
        dashboardProps.forEach { (key, value) ->
            result.appendLine("  $key: \"$value\"")
        }
        
        // Elements
        if (elements.isNotEmpty()) {
            result.appendLine("  elements:")
            elements.forEach { element ->
                val firstKey = element.keys.first()
                result.appendLine("  - $firstKey: \"${element[firstKey]}\"")
                element.forEach { (k, v) ->
                    if (k != firstKey) {
                        result.appendLine("    $k: \"$v\"")
                    }
                }
            }
        }
        
        // Filters
        if (filters.isNotEmpty()) {
            result.appendLine("  filters:")
            filters.forEach { filter ->
                val firstKey = filter.keys.first()
                result.appendLine("  - $firstKey: \"${filter[firstKey]}\"")
                filter.forEach { (k, v) ->
                    if (k != firstKey) {
                        result.appendLine("    $k: \"$v\"")
                    }
                }
            }
        }
        
        return result.toString()
    }
}
