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
                val rawValue = trimmed.substring(colonIndex + 1).trim()
                // Join multi-line flow arrays, and capture block scalars / nested blocks verbatim.
                val (value, lastConsumed) = captureValue(lines, i, indentOf(line), key, rawValue)
                i = lastConsumed

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
                        properties = mutableMapOf(key to value),
                        commentsBefore = pendingComments.toMutableList()
                    )
                    pendingComments.clear()
                } else {
                    // Regular property. elements:/filters: is a section unless captureValue grabbed
                    // it as an element-level nested block (verbatim values start with "\n").
                    if ((key == "elements" || key == "filters") && !value.startsWith("\n")) {
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
                        currentItem!!.properties[key] = value
                    } else {
                        // Property not inside elements/filters
                        if (seenDashboard && currentSection.isEmpty()) {
                            // Dashboard-level property - context determines classification
                            val prop = PropertyWithComments(
                                key = key,
                                value = value,
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
            appendProperty(result, "  ", prop.key, prop.value)
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
                                result.appendLine("  - $firstKey: ${filter.properties[firstKey]}")
                                filter.properties.forEach { (k, v) ->
                                    if (k != firstKey) {
                                        appendProperty(result, "    ", k, v)
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
                                result.appendLine("  - $firstKey: ${element.properties[firstKey]}")
                                element.properties.forEach { (k, v) ->
                                    if (k != firstKey) {
                                        appendProperty(result, "    ", k, v)
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

    /** Leading-space count of a raw line. */
    private fun indentOf(line: String): Int = line.length - line.trimStart().length

    /** A block scalar starts with | or > optionally followed by chomping/indent indicators. */
    private fun isBlockScalarStart(value: String): Boolean {
        if (value.isEmpty() || (value[0] != '|' && value[0] != '>')) return false
        return value.drop(1).all { it == '-' || it == '+' || it.isDigit() }
    }

    /** True when [ and { are balanced by ] and }, ignoring brackets inside quotes. */
    private fun bracketsBalanced(s: String): Boolean {
        var depth = 0
        var quote: Char? = null
        for (c in s) {
            when {
                quote != null -> if (c == quote) quote = null
                c == '"' || c == '\'' -> quote = c
                c == '[' || c == '{' -> depth++
                c == ']' || c == '}' -> depth--
            }
        }
        return depth <= 0
    }

    /**
     * Capture a full logical value beginning at [startIndex]. Returns the captured value and the
     * index of the last consumed line.
     * - Block scalar (| or >): indicator + deeper-indented content lines verbatim (body_text tiles).
     * - Nested block (empty value, deeper-indented lines follow): the whole block is preserved
     *   verbatim (dynamic_fields, ui_config, listen, ...). Returned with a leading newline marker so
     *   the inner `-` items are never re-parsed as dashboard elements. The dashboard-level
     *   `elements:` / `filters:` sections are excluded - they must stay reformattable.
     * - Flow array/object ([ or {) spanning lines: scalar arrays (fields, pivots) join to one line;
     *   object arrays (contain {, e.g. y_axes, conditional_formatting) are preserved verbatim.
     * - Otherwise: the value unchanged.
     */
    private fun captureValue(lines: List<String>, startIndex: Int, keyIndent: Int, key: String, value: String): Pair<String, Int> {
        if (isBlockScalarStart(value)) {
            val sb = StringBuilder(value)
            var j = startIndex + 1
            while (j < lines.size) {
                val l = lines[j]
                if (l.isBlank()) { sb.append("\n").append(l); j++; continue }
                if (indentOf(l) > keyIndent) { sb.append("\n").append(l); j++ } else break
            }
            // Trailing blank lines belong to the following content, not the block.
            return sb.toString().trimEnd('\n', ' ', '\t') to (j - 1)
        }
        if (value.isEmpty()) {
            var peek = startIndex + 1
            while (peek < lines.size && lines[peek].isBlank()) peek++
            val hasNestedBlock = peek < lines.size && indentOf(lines[peek]) > keyIndent
            // The dashboard `elements:` / `filters:` SECTIONS are list sequences (- items) and must
            // stay reformattable. An element-level `filters:` is a MAP (field: value) and, like every
            // other nested block (dynamic_fields, ui_config, listen), is preserved verbatim.
            val isDashboardSection = (key == "elements" || key == "filters") &&
                peek < lines.size && lines[peek].trimStart().startsWith("-")
            if (hasNestedBlock && !isDashboardSection) {
                val block = mutableListOf<String>()
                var j = startIndex + 1
                while (j < lines.size) {
                    val l = lines[j]
                    if (l.isBlank()) { block.add(l); j++; continue }
                    if (indentOf(l) > keyIndent) { block.add(l); j++ } else break
                }
                while (block.isNotEmpty() && block.last().isBlank()) block.removeAt(block.size - 1)
                return ("\n" + block.joinToString("\n")) to (j - 1)
            }
        }
        if (value.startsWith("[") || value.startsWith("{")) {
            if (bracketsBalanced(value)) return value to startIndex
            val joined = StringBuilder(value)
            val verbatim = StringBuilder(value)
            var j = startIndex + 1
            while (j < lines.size && !bracketsBalanced(joined.toString())) {
                joined.append(" ").append(lines[j].trim())
                verbatim.append("\n").append(lines[j])
                j++
            }
            val out = if (joined.contains("{")) verbatim.toString() else joined.toString()
            return out to (j - 1)
        }
        return value to startIndex
    }

    /**
     * Emit a property verbatim.
     * - Leading-newline value: a nested block; emit `key:` then the block lines exactly as captured.
     * - Other multi-line value (block scalar / multi-line flow object): first segment on the key
     *   line, the rest verbatim.
     * - Empty value: `key:` with no trailing space.
     */
    private fun appendProperty(result: StringBuilder, indent: String, key: String, value: String) {
        when {
            value.startsWith("\n") -> {
                result.appendLine("$indent$key:")
                value.removePrefix("\n").split("\n").forEach { result.appendLine(it) }
            }
            value.contains("\n") -> {
                val parts = value.split("\n")
                result.appendLine("$indent$key: ${parts.first()}")
                parts.drop(1).forEach { result.appendLine(it) }
            }
            value.isEmpty() -> result.appendLine("$indent$key:")
            else -> result.appendLine("$indent$key: $value")
        }
    }
}
