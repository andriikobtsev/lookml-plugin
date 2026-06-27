package com.yourcompany.lookml.formatting

import com.intellij.psi.PsiFile

/**
 * Complete rewriter for YAML dashboards
 * Parses the text directly and rebuilds with correct formatting
 * Uses schema to determine property placement
 * Preserves comment blocks in their original context
 * Preserves original section order (filters/elements can appear in any order)
 */
object YamlDashboardRewriter {

    enum class SectionType { FILTERS, ELEMENTS }

    data class ElementWithComments(
        val properties: MutableMap<String, String>,
        val commentsBefore: MutableList<String> = mutableListOf(),
        val commentsAfter: MutableList<String> = mutableListOf()
    )

    data class PropertyWithComments(
        val key: String,
        val value: String,
        val commentsBefore: MutableList<String> = mutableListOf()
    )

    fun rewriteDashboard(file: PsiFile): String {
        val text = file.text
        val lines = text.lines()

        var dashboardName = ""
        val headerComments = mutableListOf<String>()
        val dashboardProps = mutableListOf<PropertyWithComments>()
        val orphanedProps = mutableListOf<PropertyWithComments>()
        val elements = mutableListOf<ElementWithComments>()
        val filters = mutableListOf<ElementWithComments>()
        val footerComments = mutableListOf<String>()
        val sectionOrder = mutableListOf<SectionType>()  // Track order of filters/elements sections

        var currentSection = ""  // "", "elements", "filters"
        var currentItem: ElementWithComments? = null
        var seenDashboard = false
        val pendingComments = mutableListOf<String>()

        var i = 0
        while (i < lines.size) {
            val line = lines[i]
            val trimmed = line.trim()

            // Skip empty lines
            if (trimmed.isEmpty()) {
                i++
                continue
            }

            // Comments
            if (trimmed.startsWith("#")) {
                pendingComments.add(trimmed)
                i++
                continue
            }

            // YAML document start
            if (trimmed == "---" || trimmed == "- - -") {
                i++
                continue
            }

            // Dashboard entry
            if (trimmed.startsWith("- dashboard:") || trimmed.startsWith("-dashboard:")) {
                headerComments.addAll(pendingComments)
                pendingComments.clear()

                val parts = trimmed.split(":", limit = 2)
                if (parts.size == 2) {
                    dashboardName = parts[1].trim().removeSurrounding("\"").removeSurrounding("'")
                    seenDashboard = true
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
                    // This is a list item
                    if (currentItem != null) {
                        // Save previous item with any trailing comments
                        currentItem.commentsAfter.addAll(pendingComments)
                        pendingComments.clear()

                        when (currentSection) {
                            "elements" -> {
                                elements.add(currentItem)
                            }
                            "filters" -> {
                                filters.add(currentItem)
                            }
                        }
                    }

                    // Start new item
                    currentItem = ElementWithComments(
                        properties = mutableMapOf(key to value.removeSurrounding("\"").removeSurrounding("'")),
                        commentsBefore = pendingComments.toMutableList()
                    )
                    pendingComments.clear()
                } else {
                    // Regular property
                    if (key == "elements" || key == "filters") {
                        // Section marker
                        if (currentItem != null) {
                            currentItem.commentsAfter.addAll(pendingComments)
                            pendingComments.clear()

                            when (currentSection) {
                                "elements" -> {
                                    elements.add(currentItem)
                                }
                                "filters" -> {
                                    filters.add(currentItem)
                                }
                            }
                            currentItem = null
                        }

                        currentSection = key

                        // Track section order (only add each section once)
                        when (key) {
                            "elements" -> {
                                if (!sectionOrder.contains(SectionType.ELEMENTS)) {
                                    sectionOrder.add(SectionType.ELEMENTS)
                                }
                            }
                            "filters" -> {
                                if (!sectionOrder.contains(SectionType.FILTERS)) {
                                    sectionOrder.add(SectionType.FILTERS)
                                }
                            }
                        }

                        // Check if list entry is on same line
                        if (value.isNotEmpty() && !value.startsWith("[")) {
                            val afterColon = value.removePrefix("-").trim()
                            if (afterColon.contains(":")) {
                                val entryParts = afterColon.split(":", limit = 2)
                                currentItem = ElementWithComments(
                                    properties = mutableMapOf(
                                        entryParts[0].trim() to entryParts[1].trim().removeSurrounding("\"").removeSurrounding("'")
                                    ),
                                    commentsBefore = pendingComments.toMutableList()
                                )
                                pendingComments.clear()
                            }
                        }
                    } else if (currentItem != null) {
                        // Add to current item
                        currentItem!!.properties[key] = value.removeSurrounding("\"").removeSurrounding("'")
                    } else {
                        // Property not inside elements/filters
                        if (seenDashboard && currentSection.isEmpty()) {
                            // Dashboard-level property - context determines classification
                            val prop = PropertyWithComments(
                                key = key,
                                value = value.removeSurrounding("\"").removeSurrounding("'"),
                                commentsBefore = pendingComments.toMutableList()
                            )
                            pendingComments.clear()

                            // At dashboard level, properties belong to dashboard
                            // (even if they could also be element properties)
                            dashboardProps.add(prop)
                        }
                    }
                }
            }

            i++
        }

        // Save last item
        if (currentItem != null) {
            currentItem.commentsAfter.addAll(pendingComments)
            pendingComments.clear()

            when (currentSection) {
                "elements" -> {
                    elements.add(currentItem)
                }
                "filters" -> {
                    filters.add(currentItem)
                }
            }
        } else if (pendingComments.isNotEmpty()) {
            // Trailing comments
            footerComments.addAll(pendingComments)
            pendingComments.clear()
        }

        // Build output
        val result = StringBuilder()

        // Header comments
        headerComments.forEach { result.appendLine(it) }

        // YAML document marker
        result.appendLine("---")

        result.appendLine("- dashboard: $dashboardName")

        // Dashboard properties with their comments
        dashboardProps.forEach { prop ->
            prop.commentsBefore.forEach { result.appendLine("  $it") }
            result.appendLine("  ${prop.key}: \"${prop.value}\"")
        }

        // Output sections in their original order (preserves filters/elements order from input)
        // If no sections were found, default to filters-then-elements for backward compatibility
        val orderToUse = if (sectionOrder.isEmpty()) {
            // Default order when parsing didn't find explicit sections
            if (filters.isNotEmpty() && elements.isNotEmpty()) {
                listOf(SectionType.FILTERS, SectionType.ELEMENTS)
            } else if (filters.isNotEmpty()) {
                listOf(SectionType.FILTERS)
            } else if (elements.isNotEmpty()) {
                listOf(SectionType.ELEMENTS)
            } else {
                emptyList()
            }
        } else {
            sectionOrder
        }

        orderToUse.forEach { sectionType ->
            when (sectionType) {
                SectionType.FILTERS -> {
                    if (filters.isNotEmpty()) {
                        result.appendLine("  filters:")
                        filters.forEach { filter ->
                            filter.commentsBefore.forEach { result.appendLine("  $it") }

                            val firstKey = filter.properties.keys.firstOrNull()
                            if (firstKey != null) {
                                result.appendLine("  - $firstKey: \"${filter.properties[firstKey]}\"")
                                filter.properties.forEach { (k, v) ->
                                    if (k != firstKey) {
                                        result.appendLine("    $k: \"$v\"")
                                    }
                                }
                            }

                            filter.commentsAfter.forEach { result.appendLine("  $it") }
                        }
                    }
                }
                SectionType.ELEMENTS -> {
                    if (elements.isNotEmpty()) {
                        result.appendLine("  elements:")
                        elements.forEach { element ->
                            element.commentsBefore.forEach { result.appendLine("  $it") }

                            val firstKey = element.properties.keys.find { it in listOf("name", "title", "type") }
                                ?: element.properties.keys.firstOrNull()

                            if (firstKey != null) {
                                result.appendLine("  - $firstKey: \"${element.properties[firstKey]}\"")
                                element.properties.forEach { (k, v) ->
                                    if (k != firstKey) {
                                        result.appendLine("    $k: \"$v\"")
                                    }
                                }
                            }

                            element.commentsAfter.forEach { result.appendLine("  $it") }
                        }
                    }
                }
            }
        }

        // Footer comments
        if (footerComments.isNotEmpty()) {
            result.appendLine()
            footerComments.forEach { result.appendLine(it) }
        }

        return result.toString()
    }
}
