package com.yourcompany.lookml.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

/**
 * Completion provider specifically for YAML dashboard files
 */
class YamlDashboardCompletionProvider : CompletionProvider<CompletionParameters>() {

    public override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val position = parameters.position
        val yamlContext = detectYamlContext(position)

        // ALWAYS provide all completions for now (simplified approach)
        // This ensures something shows up regardless of context detection
        addDashboardRootCompletions(result)
        addElementPropertyCompletions(result)
        addFilterPropertyCompletions(result)
        addVisualizationTypeCompletions(result)
        addLayoutTypeCompletions(result)
        addFilterTypeCompletions(result)
    }

    private fun detectYamlContext(position: PsiElement): YamlContext {
        val file = position.containingFile ?: return YamlContext.UNKNOWN
        val text = file.text
        val offset = position.textOffset

        // Get current line and indentation
        val textBefore = text.substring(0, offset)
        val lines = textBefore.split('\n')
        val currentLine = lines.lastOrNull() ?: return YamlContext.UNKNOWN
        val indentLevel = currentLine.takeWhile { it == ' ' }.length / 2

        // Check if we're after a colon (property value position)
        if (currentLine.contains(':')) {
            val beforeCaret = currentLine.substring(0, currentLine.length - currentLine.substringAfter(textBefore.last()).length)
            if (beforeCaret.contains(':')) {
                val propertyName = beforeCaret.substringBefore(':').trim().removePrefix("-").trim()

                return when (propertyName) {
                    "type" -> {
                        // Check if we're in a filter or element
                        if (isInFilterContext(lines)) YamlContext.FILTER_TYPE_VALUE
                        else YamlContext.TYPE_VALUE
                    }
                    "layout" -> YamlContext.LAYOUT_VALUE
                    else -> YamlContext.UNKNOWN
                }
            }
        }

        // Determine context by indentation and nearby keywords
        return when {
            // Root dashboard properties (indentation 1, after "- dashboard:")
            indentLevel == 1 && hasDashboardDeclaration(lines) -> YamlContext.DASHBOARD_ROOT

            // Inside elements array
            indentLevel >= 1 && isInElementsContext(lines) -> {
                if (indentLevel == 1) YamlContext.ELEMENT_START
                else YamlContext.ELEMENT_PROPERTIES
            }

            // Inside filters array
            indentLevel >= 1 && isInFiltersContext(lines) -> YamlContext.FILTER_PROPERTIES

            else -> YamlContext.DASHBOARD_ROOT
        }
    }

    private fun hasDashboardDeclaration(lines: List<String>): Boolean {
        return lines.any { it.trim().startsWith("- dashboard:") }
    }

    private fun isInElementsContext(lines: List<String>): Boolean {
        // Check if we're after an "elements:" declaration
        for (i in lines.indices.reversed()) {
            val trimmed = lines[i].trim()
            if (trimmed.startsWith("elements:")) return true
            if (trimmed.startsWith("- dashboard:") || trimmed.startsWith("filters:")) return false
        }
        return false
    }

    private fun isInFiltersContext(lines: List<String>): Boolean {
        // Check if we're after a "filters:" declaration
        for (i in lines.indices.reversed()) {
            val trimmed = lines[i].trim()
            if (trimmed.startsWith("filters:")) return true
            if (trimmed.startsWith("- dashboard:") || trimmed.startsWith("elements:")) return false
        }
        return false
    }

    private fun isInFilterContext(lines: List<String>): Boolean {
        // Check recent lines for filter context
        return lines.takeLast(10).any { it.trim().startsWith("filters:") || it.contains("field_filter") }
    }

    private fun addDashboardRootCompletions(result: CompletionResultSet) {
        YamlDashboardProperties.DASHBOARD_PROPERTIES.forEach { (name, description) ->
            result.addElement(
                LookupElementBuilder.create(name)
                    .withIcon(com.intellij.icons.AllIcons.Nodes.Property)
                    .withTypeText("Dashboard property")
                    .withTailText(" - $description", true)
                    .withInsertHandler { context, _ ->
                        val editor = context.editor
                        val caretOffset = editor.caretModel.offset
                        editor.document.insertString(caretOffset, ": ")
                        editor.caretModel.moveToOffset(caretOffset + 2)
                    }
            )
        }
    }

    private fun addElementPropertyCompletions(result: CompletionResultSet) {
        YamlDashboardProperties.ELEMENT_PROPERTIES.forEach { (name, description) ->
            result.addElement(
                LookupElementBuilder.create(name)
                    .withIcon(com.intellij.icons.AllIcons.Nodes.Method)
                    .withTypeText("Element property")
                    .withTailText(" - $description", true)
                    .withInsertHandler { context, _ ->
                        val editor = context.editor
                        val caretOffset = editor.caretModel.offset
                        editor.document.insertString(caretOffset, ": ")
                        editor.caretModel.moveToOffset(caretOffset + 2)
                    }
            )
        }
    }

    private fun addFilterPropertyCompletions(result: CompletionResultSet) {
        YamlDashboardProperties.FILTER_PROPERTIES.forEach { (name, description) ->
            result.addElement(
                LookupElementBuilder.create(name)
                    .withIcon(com.intellij.icons.AllIcons.General.Filter)
                    .withTypeText("Filter property")
                    .withTailText(" - $description", true)
                    .withInsertHandler { context, _ ->
                        val editor = context.editor
                        val caretOffset = editor.caretModel.offset
                        editor.document.insertString(caretOffset, ": ")
                        editor.caretModel.moveToOffset(caretOffset + 2)
                    }
            )
        }
    }

    private fun addVisualizationTypeCompletions(result: CompletionResultSet) {
        YamlDashboardProperties.VISUALIZATION_TYPES.forEach { type ->
            result.addElement(
                LookupElementBuilder.create(type)
                    .withIcon(com.intellij.icons.AllIcons.Actions.GroupByPrefix)
                    .withTypeText("Visualization type")
            )
        }
    }

    private fun addLayoutTypeCompletions(result: CompletionResultSet) {
        YamlDashboardProperties.LAYOUT_TYPES.forEach { layout ->
            result.addElement(
                LookupElementBuilder.create(layout)
                    .withIcon(com.intellij.icons.AllIcons.Actions.GroupByPrefix)
                    .withTypeText("Layout type")
            )
        }
    }

    private fun addFilterTypeCompletions(result: CompletionResultSet) {
        YamlDashboardProperties.FILTER_TYPES.forEach { filterType ->
            result.addElement(
                LookupElementBuilder.create(filterType)
                    .withIcon(com.intellij.icons.AllIcons.General.Filter)
                    .withTypeText("Filter type")
            )
        }
    }

    enum class YamlContext {
        DASHBOARD_ROOT,
        ELEMENT_START,
        ELEMENT_PROPERTIES,
        FILTER_PROPERTIES,
        TYPE_VALUE,
        LAYOUT_VALUE,
        FILTER_TYPE_VALUE,
        UNKNOWN
    }
}
