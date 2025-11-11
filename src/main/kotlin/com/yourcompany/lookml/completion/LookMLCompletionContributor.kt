package com.yourcompany.lookml.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import com.yourcompany.lookml.LookMLLanguage

/**
 * Context-aware completion contributor for LookML with fixed context detection
 */
class LookMLCompletionContributor : CompletionContributor() {
    
    companion object {
        private val TOP_LEVEL_KEYWORDS = listOf(
            "view", "explore", "dashboard", "lookml_dashboard", "datagroup", "access_grant", 
            "named_value_format", "test", "connection", "include", "persist_with", 
            "week_start_day", "case_sensitive", "label"
        )
        
        private val VIEW_BLOCK_KEYWORDS = listOf(
            "dimension", "dimension_group", "measure", "filter", "parameter", "set",
            "derived_table", "sql_table_name", "fields_hidden_by_default",
            "suggestions", "drill_fields", "extend", "test"
        )
        
        private val EXPLORE_BLOCK_KEYWORDS = listOf(
            "join", "from", "view_name", "always_filter", "conditionally_filter",
            "access_filter", "fields", "required_access_grants", "persist_with",
            "sql_always_where", "sql_always_having", "view_label", "extend",
            "explore_source", "assert", "label"
        )
        
        private val FIELD_PROPERTIES = listOf(
            "sql", "type", "label", "description", "hidden", "primary_key", "group_label",
            "value_format", "value_format_name", "drill_fields", "link", "html",
            "case", "when", "else", "tags", "can_filter", "alpha_sort", "tiers",
            "style", "convert_tz", "datatype", "precision", "group_item_label",
            "timeframes", "allow_fill", "list_field", "suggest_dimension",
            "listens_to_filters", "approximate", "approximate_threshold",
            "required_fields", "suggestions"
        )
        
        private val PARAMETER_PROPERTIES = listOf(
            "type", "label", "description", "hidden", "default_value", "allowed_value",
            "suggestions", "view_label", "group_label", "group_item_label", "can_filter"
        )
        
        private val TEST_PROPERTIES = listOf(
            "explore_source", "assert", "expression"
        )
        
        private val JOIN_PROPERTIES = listOf(
            "sql_on", "sql_where", "relationship", "from", "view_label", "fields",
            "required_access_grants", "type", "foreign_key", "outer_only"
        )
        
        private val BOOLEAN_VALUES = listOf("yes", "no", "true", "false")
        
        private val DIMENSION_TYPES = listOf(
            // Basic types
            "string", "number", "date", "time", "date_time", "yesno", "tier", "bin",
            "location", "duration", "distance", "zipcode", "unquoted",
            // Individual time/date types
            "date_raw", "date_time_of_day", "date_hour", "date_hour_of_day", 
            "date_minute", "date_second", "date_millisecond", "date_microsecond",
            "date_week", "date_day_of_week", "date_day_of_week_index", 
            "date_month", "date_month_num", "date_month_name", "date_day_of_month",
            "date_quarter", "date_year",
            // Individual duration types
            "duration_day", "duration_hour", "duration_minute", "duration_month",
            "duration_quarter", "duration_second", "duration_week", "duration_year"
        )
        
        private val MEASURE_TYPES = listOf(
            "average", "average_distinct", "count", "count_distinct", "date", "list",
            "max", "median", "median_distinct", "min", "number", "percent_of_previous",
            "percent_of_total", "percentile", "percentile_distinct", "period_over_period",
            "running_total", "string", "sum", "sum_distinct", "yesno"
        )
        
        private val RELATIONSHIP_TYPES = listOf(
            "one_to_one", "one_to_many", "many_to_one", "many_to_many"
        )
        
        private val PARAMETER_TYPES = listOf(
            "string", "number", "date", "yesno", "unquoted"
        )
        
        private val DATATYPE_VALUES = listOf(
            "string", "number", "int", "real", "datetime", "date", "time", "timestamp"
        )
        
        private val STYLE_VALUES = listOf(
            "integer", "decimal", "percent", "currency", "link", "email"
        )
        
        private val CONVERT_TZ_VALUES = listOf(
            "yes", "no"
        )
    }
    
    init {
        extend(CompletionType.BASIC, 
               PlatformPatterns.psiElement().withLanguage(LookMLLanguage),
               ContextAwareCompletionProvider())
    }
    
    private class ContextAwareCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val position = parameters.position
            val completionContext = detectContext(position)
            val caseInsensitiveResult = result.caseInsensitive()
            
            when (completionContext) {
                CompletionContext.TOP_LEVEL -> {
                    addTopLevelCompletions(caseInsensitiveResult)
                }
                CompletionContext.VIEW_BODY -> {
                    addViewBodyCompletions(caseInsensitiveResult)
                }
                CompletionContext.EXPLORE_BODY -> {
                    addExploreBodyCompletions(caseInsensitiveResult)
                }
                CompletionContext.DIMENSION_FIELD, CompletionContext.MEASURE_FIELD -> {
                    addFieldCompletions(caseInsensitiveResult)
                }
                CompletionContext.PARAMETER_FIELD -> {
                    addParameterCompletions(caseInsensitiveResult)
                }
                CompletionContext.JOIN_BODY -> {
                    addJoinCompletions(caseInsensitiveResult)
                }
                CompletionContext.TEST_BODY -> {
                    addTestCompletions(caseInsensitiveResult)
                }
                CompletionContext.PROPERTY_VALUE -> {
                    addPropertyValueCompletions(position, caseInsensitiveResult)
                }
                CompletionContext.YAML_DASHBOARD -> {
                    // Use YAML-specific completion provider
                    YamlDashboardCompletionProvider().addCompletions(parameters, ProcessingContext(), caseInsensitiveResult)
                }
                CompletionContext.UNKNOWN -> {
                    // No completions
                }
            }
        }
        
        private fun detectContext(position: PsiElement): CompletionContext {
            val text = position.containingFile?.text ?: ""
            val caretOffset = position.textOffset

            // Check for YAML dashboard files - use special YAML completion
            if (text.trimStart().startsWith("---") || text.contains("- dashboard:")) {
                return CompletionContext.YAML_DASHBOARD
            }
            
            // Check if we're in a property value position (after a colon)
            val textBefore = text.substring(0, caretOffset)
            val currentLineStart = textBefore.lastIndexOf('\n') + 1
            val currentLine = textBefore.substring(currentLineStart)
            
            // Check if current line has a colon and we're after it
            if (currentLine.contains(':') && currentLine.indexOf(':') < (caretOffset - currentLineStart)) {
                val afterColon = currentLine.substring(currentLine.indexOf(':') + 1)
                if (afterColon.trim().isEmpty() || afterColon.trim().length < 10) { // Arbitrary threshold for partial completion
                    return CompletionContext.PROPERTY_VALUE
                }
            }
            
            // Track nested contexts with proper depth tracking
            var braceDepth = 0
            var currentContext = CompletionContext.TOP_LEVEL
            var viewBodyContext = false
            var exploreBodyContext = false
            
            val lines = textBefore.split('\n')
            for (line in lines) {
                val trimmed = line.trim()
                
                // Count braces first
                braceDepth += line.count { it == '{' }
                braceDepth -= line.count { it == '}' }
                
                // Update context based on block starts and current depth
                when {
                    trimmed.startsWith("view:") -> {
                        if (braceDepth >= 1) {
                            currentContext = CompletionContext.VIEW_BODY
                            viewBodyContext = true
                        }
                    }
                    trimmed.startsWith("explore:") -> {
                        if (braceDepth >= 1) {
                            currentContext = CompletionContext.EXPLORE_BODY
                            exploreBodyContext = true
                        }
                    }
                    trimmed.startsWith("test:") -> {
                        if (braceDepth >= 2) currentContext = CompletionContext.TEST_BODY
                    }
                    trimmed.startsWith("measure:") -> {
                        if (braceDepth >= 2) currentContext = CompletionContext.MEASURE_FIELD
                    }
                    trimmed.startsWith("dimension:") -> {
                        if (braceDepth >= 2) currentContext = CompletionContext.DIMENSION_FIELD
                    }
                    trimmed.startsWith("parameter:") -> {
                        if (braceDepth >= 2) currentContext = CompletionContext.PARAMETER_FIELD
                    }
                    trimmed.startsWith("join:") -> {
                        if (braceDepth >= 2) currentContext = CompletionContext.JOIN_BODY
                    }
                }
                
                // Reset to parent context when we close nested blocks
                if (braceDepth == 1 && viewBodyContext) {
                    currentContext = CompletionContext.VIEW_BODY
                } else if (braceDepth == 1 && exploreBodyContext) {
                    currentContext = CompletionContext.EXPLORE_BODY
                } else if (braceDepth == 0) {
                    currentContext = CompletionContext.TOP_LEVEL
                    viewBodyContext = false
                    exploreBodyContext = false
                }
            }
            
            return currentContext
        }
        
        private fun addTopLevelCompletions(result: CompletionResultSet) {
            TOP_LEVEL_KEYWORDS.forEach { keyword ->
                val builder = when (keyword) {
                    "view", "explore", "dashboard", "datagroup", "access_grant", "named_value_format", "test" -> {
                        LookupElementBuilder.create(keyword)
                            .withIcon(com.intellij.icons.AllIcons.Nodes.Class)
                            .withTypeText("LookML keyword")
                            .withInsertHandler { context, item ->
                                val editor = context.editor
                                val caretOffset = editor.caretModel.offset
                                editor.document.insertString(caretOffset, ": name {\n  \n}")
                                editor.caretModel.moveToOffset(caretOffset + 2)
                            }
                    }
                    else -> {
                        LookupElementBuilder.create(keyword)
                            .withIcon(com.intellij.icons.AllIcons.Nodes.Class)
                            .withTypeText("LookML keyword")
                            .withInsertHandler { context, item ->
                                val editor = context.editor
                                val caretOffset = editor.caretModel.offset
                                editor.document.insertString(caretOffset, ": ")
                                editor.caretModel.moveToOffset(caretOffset + 2)
                            }
                    }
                }
                
                result.addElement(builder)
            }
        }
        
        private fun addViewBodyCompletions(result: CompletionResultSet) {
            VIEW_BLOCK_KEYWORDS.forEach { keyword ->
                val builder = when (keyword) {
                    "dimension", "dimension_group", "measure", "filter", "parameter", "set", "test" -> {
                        LookupElementBuilder.create(keyword)
                            .withIcon(com.intellij.icons.AllIcons.Nodes.Method)
                            .withTypeText("View element")
                            .withInsertHandler { context, item ->
                                val editor = context.editor
                                val document = editor.document
                                val caretOffset = editor.caretModel.offset
                                
                                // Check if next non-whitespace character is already a closing brace
                                val textAfterCaret = document.text.substring(caretOffset).trim()
                                val needsClosingBrace = !textAfterCaret.startsWith("}")
                                
                                val insertText = if (needsClosingBrace) {
                                    ": name {\n    \n  }"
                                } else {
                                    ": name {\n    \n  "
                                }
                                
                                document.insertString(caretOffset, insertText)
                                editor.caretModel.moveToOffset(caretOffset + 2)
                            }
                    }
                    "derived_table" -> {
                        LookupElementBuilder.create(keyword)
                            .withIcon(com.intellij.icons.AllIcons.Nodes.Method)
                            .withTypeText("View element")
                            .withInsertHandler { context, item ->
                                val editor = context.editor
                                val caretOffset = editor.caretModel.offset
                                editor.document.insertString(caretOffset, ": {\n    \n  }")
                                editor.caretModel.moveToOffset(caretOffset + 4)
                            }
                    }
                    else -> {
                        LookupElementBuilder.create(keyword)
                            .withIcon(com.intellij.icons.AllIcons.Nodes.Method)
                            .withTypeText("View element")
                            .withInsertHandler { context, item ->
                                val editor = context.editor
                                val caretOffset = editor.caretModel.offset
                                editor.document.insertString(caretOffset, ": ")
                                editor.caretModel.moveToOffset(caretOffset + 2)
                            }
                    }
                }
                
                result.addElement(builder)
            }
        }
        
        private fun addExploreBodyCompletions(result: CompletionResultSet) {
            EXPLORE_BLOCK_KEYWORDS.forEach { keyword ->
                val builder = LookupElementBuilder.create(keyword)
                    .withIcon(com.intellij.icons.AllIcons.Nodes.Method)
                    .withTypeText("Explore element")
                
                when (keyword) {
                    "join" -> {
                        builder.withInsertHandler { context, item ->
                            val editor = context.editor
                            val caretOffset = editor.caretModel.offset
                            editor.document.insertString(caretOffset, ": view_name {\\n    \\n  }")
                            editor.caretModel.moveToOffset(caretOffset + 2)
                        }
                    }
                    "always_filter", "conditionally_filter", "access_filter" -> {
                        builder.withInsertHandler { context, item ->
                            val editor = context.editor
                            val caretOffset = editor.caretModel.offset
                            editor.document.insertString(caretOffset, ": {\\n    \\n  }")
                            editor.caretModel.moveToOffset(caretOffset + 4)
                        }
                    }
                    else -> {
                        builder.withInsertHandler { context, item ->
                            val editor = context.editor
                            val caretOffset = editor.caretModel.offset
                            editor.document.insertString(caretOffset, ": ")
                            editor.caretModel.moveToOffset(caretOffset + 2)
                        }
                    }
                }
                
                result.addElement(builder)
            }
        }
        
        private fun addFieldCompletions(result: CompletionResultSet) {
            FIELD_PROPERTIES.forEach { property ->
                val builder = when (property) {
                    "sql" -> {
                        LookupElementBuilder.create(property)
                            .withIcon(com.intellij.icons.AllIcons.Nodes.Property)
                            .withTypeText("Field property")
                            .withInsertHandler { context, item ->
                                val editor = context.editor
                                val caretOffset = editor.caretModel.offset
                                editor.document.insertString(caretOffset, ": ;;\n")
                                editor.caretModel.moveToOffset(caretOffset + 2)
                            }
                    }
                    "html" -> {
                        LookupElementBuilder.create(property)
                            .withIcon(com.intellij.icons.AllIcons.Nodes.Property)
                            .withTypeText("Field property")
                            .withInsertHandler { context, item ->
                                val editor = context.editor
                                val caretOffset = editor.caretModel.offset
                                editor.document.insertString(caretOffset, ": ;;\n")
                                editor.caretModel.moveToOffset(caretOffset + 2)
                            }
                    }
                    "case", "when", "else", "link" -> {
                        LookupElementBuilder.create(property)
                            .withIcon(com.intellij.icons.AllIcons.Nodes.Property)
                            .withTypeText("Field property")
                            .withInsertHandler { context, item ->
                                val editor = context.editor
                                val caretOffset = editor.caretModel.offset
                                editor.document.insertString(caretOffset, ": {\n    \n  }")
                                editor.caretModel.moveToOffset(caretOffset + 4)
                            }
                    }
                    else -> {
                        LookupElementBuilder.create(property)
                            .withIcon(com.intellij.icons.AllIcons.Nodes.Property)
                            .withTypeText("Field property")
                            .withInsertHandler { context, item ->
                                val editor = context.editor
                                val caretOffset = editor.caretModel.offset
                                editor.document.insertString(caretOffset, ": ")
                                editor.caretModel.moveToOffset(caretOffset + 2)
                            }
                    }
                }
                
                result.addElement(builder)
            }
        }
        
        private fun addParameterCompletions(result: CompletionResultSet) {
            PARAMETER_PROPERTIES.forEach { property ->
                val builder = when (property) {
                    "allowed_value" -> {
                        LookupElementBuilder.create(property)
                            .withIcon(com.intellij.icons.AllIcons.Nodes.Property)
                            .withTypeText("Parameter property")
                            .withInsertHandler { context, item ->
                                val editor = context.editor
                                val caretOffset = editor.caretModel.offset
                                editor.document.insertString(caretOffset, ": {\n    \n  }")
                                editor.caretModel.moveToOffset(caretOffset + 4)
                            }
                    }
                    else -> {
                        LookupElementBuilder.create(property)
                            .withIcon(com.intellij.icons.AllIcons.Nodes.Property)
                            .withTypeText("Parameter property")
                            .withInsertHandler { context, item ->
                                val editor = context.editor
                                val caretOffset = editor.caretModel.offset
                                editor.document.insertString(caretOffset, ": ")
                                editor.caretModel.moveToOffset(caretOffset + 2)
                            }
                    }
                }
                
                result.addElement(builder)
            }
        }
        
        private fun addJoinCompletions(result: CompletionResultSet) {
            JOIN_PROPERTIES.forEach { property ->
                result.addElement(
                    LookupElementBuilder.create(property)
                        .withIcon(com.intellij.icons.AllIcons.Nodes.Property)
                        .withTypeText("Join property")
                        .withInsertHandler { context, item ->
                            val editor = context.editor
                            val caretOffset = editor.caretModel.offset
                            editor.document.insertString(caretOffset, ": ")
                            editor.caretModel.moveToOffset(caretOffset + 2)
                        }
                )
            }
        }
        
        private fun addTestCompletions(result: CompletionResultSet) {
            TEST_PROPERTIES.forEach { property ->
                val builder = when (property) {
                    "assert" -> {
                        LookupElementBuilder.create(property)
                            .withIcon(com.intellij.icons.AllIcons.Nodes.Property)
                            .withTypeText("Test property")
                            .withInsertHandler { context, item ->
                                val editor = context.editor
                                val caretOffset = editor.caretModel.offset
                                editor.document.insertString(caretOffset, ": {\n    \n  }")
                                editor.caretModel.moveToOffset(caretOffset + 4)
                            }
                    }
                    else -> {
                        LookupElementBuilder.create(property)
                            .withIcon(com.intellij.icons.AllIcons.Nodes.Property)
                            .withTypeText("Test property")
                            .withInsertHandler { context, item ->
                                val editor = context.editor
                                val caretOffset = editor.caretModel.offset
                                editor.document.insertString(caretOffset, ": ")
                                editor.caretModel.moveToOffset(caretOffset + 2)
                            }
                    }
                }
                
                result.addElement(builder)
            }
        }
        
        private fun addPropertyValueCompletions(position: PsiElement, result: CompletionResultSet) {
            val propertyName = findPropertyName(position)
            val completionContext = detectContext(position)
            
            val values = when (propertyName) {
                "type" -> when (completionContext) {
                    CompletionContext.DIMENSION_FIELD -> DIMENSION_TYPES
                    CompletionContext.MEASURE_FIELD -> MEASURE_TYPES
                    CompletionContext.PARAMETER_FIELD -> PARAMETER_TYPES
                    else -> DIMENSION_TYPES + MEASURE_TYPES + PARAMETER_TYPES
                }
                "relationship" -> RELATIONSHIP_TYPES
                "datatype" -> DATATYPE_VALUES
                "style" -> STYLE_VALUES
                "convert_tz" -> CONVERT_TZ_VALUES
                "hidden", "primary_key", "case_sensitive", "required", "alpha_sort", "can_filter", 
                "approximate", "allow_fill" -> BOOLEAN_VALUES
                else -> emptyList()
            }
            
            values.forEach { value ->
                result.addElement(
                    LookupElementBuilder.create(value)
                        .withIcon(com.intellij.icons.AllIcons.Nodes.Variable)
                        .withTypeText("Value")
                )
            }
        }
        
        private fun findPropertyName(position: PsiElement): String? {
            val text = position.containingFile?.text ?: return null
            val caretOffset = position.textOffset
            
            // Look backwards for property name pattern
            val textBeforeCaret = text.substring(0, caretOffset)
            val lines = textBeforeCaret.split('\n')
            
            for (line in lines.reversed()) {
                val trimmed = line.trim()
                if (trimmed.contains(':') && !trimmed.startsWith("//")) {
                    val parts = trimmed.split(':')
                    if (parts.size >= 2) {
                        return parts[0].trim()
                    }
                }
            }
            
            return null
        }
    }
    
    enum class CompletionContext {
        TOP_LEVEL,
        VIEW_BODY,
        EXPLORE_BODY,
        DIMENSION_FIELD,
        MEASURE_FIELD,
        PARAMETER_FIELD,
        JOIN_BODY,
        TEST_BODY,
        PROPERTY_VALUE,
        YAML_DASHBOARD,
        UNKNOWN
    }
}