package com.yourcompany.lookml.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import com.intellij.psi.TokenType
import com.yourcompany.lookml.psi.LookMLTypes

class LookMLLexerAdapter : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset = 0
    private var endOffset = 0
    private var currentPosition = 0
    private var tokenStart = 0
    private var tokenEnd = 0
    private var currentToken: IElementType? = null
    private var state = 0
    private var isYamlDashboard = false
    private var yamlLexer: YamlDashboardLexer? = null
    
    companion object {
        private const val YYINITIAL = 0
        private const val SQL_BLOCK = 1
    }
    
    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.currentPosition = startOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        this.currentToken = null
        this.state = initialState
        
        // Check if this is a YAML dashboard (starts with --- or - dashboard: or YAML list items)
        isYamlDashboard = (startOffset + 3 <= endOffset && 
            buffer[startOffset] == '-' && 
            buffer[startOffset + 1] == '-' && 
            buffer[startOffset + 2] == '-') ||
            (startOffset + 12 <= endOffset &&
            buffer.substring(startOffset, startOffset + 12) == "- dashboard:") ||
            // Also detect YAML dashboard elements starting with common properties
            (startOffset + 8 <= endOffset &&
            buffer.substring(startOffset, startOffset + 8) == "- title:") ||
            (startOffset + 7 <= endOffset &&
            buffer.substring(startOffset, startOffset + 7) == "- name:") ||
            (startOffset + 7 <= endOffset &&
            buffer.substring(startOffset, startOffset + 7) == "- type:") ||
            (startOffset + 8 <= endOffset &&
            buffer.substring(startOffset, startOffset + 8) == "- model:")
        
        if (isYamlDashboard) {
            yamlLexer = YamlDashboardLexer()
            yamlLexer!!.start(buffer, startOffset, endOffset, initialState)
        } else {
            advance()
        }
    }
    
    override fun getState(): Int = if (isYamlDashboard && yamlLexer != null) yamlLexer!!.state else state
    
    override fun getTokenType(): IElementType? = if (isYamlDashboard && yamlLexer != null) yamlLexer!!.tokenType else currentToken
    
    override fun getTokenStart(): Int = if (isYamlDashboard && yamlLexer != null) yamlLexer!!.tokenStart else tokenStart
    
    override fun getTokenEnd(): Int = if (isYamlDashboard && yamlLexer != null) yamlLexer!!.tokenEnd else tokenEnd
    
    override fun advance() {
        if (isYamlDashboard && yamlLexer != null) {
            yamlLexer!!.advance()
            return
        }
        
        tokenStart = tokenEnd
        
        if (tokenEnd >= endOffset) {
            currentToken = null
            return
        }
        
        currentPosition = tokenEnd
        
        // Handle SQL block state
        if (state == SQL_BLOCK) {
            // Check for end of SQL block
            if (currentPosition + 2 <= endOffset && 
                buffer[currentPosition] == ';' && 
                buffer[currentPosition + 1] == ';') {
                tokenEnd = currentPosition + 2
                currentToken = LookMLTypes.SQL_BLOCK_END
                state = YYINITIAL
                return
            }
            
            // Skip whitespace in SQL blocks too
            while (currentPosition < endOffset && buffer[currentPosition].isWhitespace()) {
                currentPosition++
            }
            
            if (currentPosition > tokenStart) {
                tokenEnd = currentPosition
                currentToken = TokenType.WHITE_SPACE
                return
            }
            
            if (currentPosition >= endOffset) {
                currentToken = null
                return
            }
            
            val ch = buffer[currentPosition]
            
            // Handle SQL comments
            if (ch == '/' && currentPosition + 1 < endOffset && buffer[currentPosition + 1] == '*') {
                currentPosition += 2
                while (currentPosition + 1 < endOffset) {
                    if (buffer[currentPosition] == '*' && buffer[currentPosition + 1] == '/') {
                        currentPosition += 2
                        break
                    }
                    currentPosition++
                }
                tokenEnd = currentPosition
                currentToken = LookMLTypes.COMMENT
                return
            }
            
            if (ch == '-' && currentPosition + 1 < endOffset && buffer[currentPosition + 1] == '-') {
                currentPosition += 2
                while (currentPosition < endOffset && buffer[currentPosition] != '\n') {
                    currentPosition++
                }
                tokenEnd = currentPosition
                currentToken = LookMLTypes.COMMENT
                return
            }
            
            // Handle strings in SQL blocks
            if (ch == '"' || ch == '`' || ch == '\'') {
                val quoteChar = ch
                currentPosition++
                while (currentPosition < endOffset) {
                    if (buffer[currentPosition] == quoteChar) {
                        currentPosition++
                        break
                    }
                    if ((ch == '"' || ch == '\'') && buffer[currentPosition] == '\\' && currentPosition + 1 < endOffset) {
                        currentPosition++
                    }
                    currentPosition++
                }
                tokenEnd = currentPosition
                currentToken = LookMLTypes.STRING
                return
            }
            
            // Handle special characters in SQL blocks
            when (ch) {
                '$' -> {
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.DOLLAR
                    return
                }
                '{' -> {
                    if (currentPosition + 1 < endOffset && buffer[currentPosition + 1] == '%') {
                        currentPosition += 2
                        while (currentPosition + 1 < endOffset) {
                            if (buffer[currentPosition] == '%' && buffer[currentPosition + 1] == '}') {
                                currentPosition += 2
                                break
                            }
                            currentPosition++
                        }
                        tokenEnd = currentPosition
                        currentToken = LookMLTypes.SQL_CONTENT_TOKEN
                        return
                    }
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.LBRACE
                    return
                }
                '}' -> {
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.RBRACE
                    return
                }
                '.' -> {
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.DOT
                    return
                }
                '=' -> {
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.EQ
                    return
                }
                '!' -> {
                    if (currentPosition + 1 < endOffset && buffer[currentPosition + 1] == '=') {
                        tokenEnd = currentPosition + 2
                        currentToken = LookMLTypes.NE
                        return
                    }
                }
                '<' -> {
                    if (currentPosition + 1 < endOffset && buffer[currentPosition + 1] == '=') {
                        tokenEnd = currentPosition + 2
                        currentToken = LookMLTypes.LE
                        return
                    }
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.LT
                    return
                }
                '>' -> {
                    if (currentPosition + 1 < endOffset && buffer[currentPosition + 1] == '=') {
                        tokenEnd = currentPosition + 2
                        currentToken = LookMLTypes.GE
                        return
                    }
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.GT
                    return
                }
                '(' -> {
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.LPAREN
                    return
                }
                ')' -> {
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.RPAREN
                    return
                }
                '%' -> {
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.PERCENT
                    return
                }
                '/' -> {
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.SLASH
                    return
                }
                '-' -> {
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.MINUS
                    return
                }
                '*' -> {
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.STAR
                    return
                }
                ',' -> {
                    tokenEnd = currentPosition + 1
                    currentToken = LookMLTypes.COMMA
                    return
                }
            }
            
            // Handle identifiers
            if (ch.isLetter() || ch == '_') {
                val start = currentPosition
                while (currentPosition < endOffset && 
                       (buffer[currentPosition].isLetterOrDigit() || buffer[currentPosition] == '_')) {
                    currentPosition++
                }
                val word = buffer.substring(start, currentPosition).toString()
                tokenEnd = currentPosition
                
                currentToken = when (word.uppercase()) {
                    "TABLE" -> LookMLTypes.IDENTIFIER
                    "AND" -> LookMLTypes.AND
                    "OR" -> LookMLTypes.OR
                    else -> LookMLTypes.IDENTIFIER
                }
                return
            }
            
            // Fallback: consume one character as SQL content
            tokenEnd = currentPosition + 1
            currentToken = LookMLTypes.SQL_CONTENT_TOKEN
            return
        }
        
        // Normal mode (not in SQL block)
        
        // Skip whitespace
        while (currentPosition < endOffset && buffer[currentPosition].isWhitespace()) {
            currentPosition++
        }
        
        if (currentPosition > tokenStart) {
            tokenEnd = currentPosition
            currentToken = TokenType.WHITE_SPACE
            return
        }
        
        if (currentPosition >= endOffset) {
            currentToken = null
            return
        }
        
        val ch = buffer[currentPosition]
        
        // Comments
        if (ch == '#') {
            currentPosition++
            while (currentPosition < endOffset && buffer[currentPosition] != '\n') {
                currentPosition++
            }
            tokenEnd = currentPosition
            currentToken = LookMLTypes.COMMENT
            return
        }
        
        // String literals
        if (ch == '"' || ch == '`' || ch == '\'') {
            val quoteChar = ch
            currentPosition++
            while (currentPosition < endOffset) {
                if (buffer[currentPosition] == quoteChar) {
                    currentPosition++
                    break
                }
                if ((ch == '"' || ch == '\'') && buffer[currentPosition] == '\\' && currentPosition + 1 < endOffset) {
                    currentPosition++
                }
                currentPosition++
            }
            tokenEnd = currentPosition
            currentToken = LookMLTypes.STRING
            return
        }
        
        // Numbers
        if (ch.isDigit()) {
            while (currentPosition < endOffset && 
                   (buffer[currentPosition].isDigit() || buffer[currentPosition] == '.')) {
                currentPosition++
            }
            tokenEnd = currentPosition
            currentToken = LookMLTypes.NUMBER
            return
        }
        
        // Single character tokens
        when (ch) {
            '{' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.LBRACE
                return
            }
            '}' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.RBRACE
                return
            }
            '[' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.LBRACKET
                return
            }
            ']' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.RBRACKET
                return
            }
            ':' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.COLON
                return
            }
            ';' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.SEMICOLON
                return
            }
            ',' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.COMMA
                return
            }
            '$' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.DOLLAR
                return
            }
            '.' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.DOT
                return
            }
            '=' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.EQ
                return
            }
            '<' -> {
                if (currentPosition + 1 < endOffset && buffer[currentPosition + 1] == '=') {
                    tokenEnd = currentPosition + 2
                    currentToken = LookMLTypes.LE
                    return
                }
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.LT
                return
            }
            '>' -> {
                if (currentPosition + 1 < endOffset && buffer[currentPosition + 1] == '=') {
                    tokenEnd = currentPosition + 2
                    currentToken = LookMLTypes.GE
                    return
                }
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.GT
                return
            }
            '!' -> {
                if (currentPosition + 1 < endOffset && buffer[currentPosition + 1] == '=') {
                    tokenEnd = currentPosition + 2
                    currentToken = LookMLTypes.NE
                    return
                }
                tokenEnd = currentPosition + 1
                currentToken = TokenType.BAD_CHARACTER
                return
            }
            '+' -> {
                if (currentPosition + 1 < endOffset && 
                    (buffer[currentPosition + 1].isLetter() || buffer[currentPosition + 1] == '_')) {
                    currentPosition++
                    val start = currentPosition
                    while (currentPosition < endOffset && 
                           (buffer[currentPosition].isLetterOrDigit() || buffer[currentPosition] == '_')) {
                        currentPosition++
                    }
                    tokenEnd = currentPosition
                    currentToken = LookMLTypes.IDENTIFIER
                    return
                }
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.PLUS
                return
            }
            '-' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.MINUS
                return
            }
            '*' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.STAR
                return
            }
            '/' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.SLASH
                return
            }
            '%' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.PERCENT
                return
            }
            '(' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.LPAREN
                return
            }
            ')' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.RPAREN
                return
            }
            '|' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.PIPE
                return
            }
        }
        
        // Keywords and identifiers
        if (ch.isLetter() || ch == '_') {
            val start = currentPosition
            while (currentPosition < endOffset && 
                   (buffer[currentPosition].isLetterOrDigit() || buffer[currentPosition] == '_')) {
                currentPosition++
            }
            
            // Check if this identifier continues with hyphens
            while (currentPosition < endOffset && buffer[currentPosition] == '-' && 
                   currentPosition + 1 < endOffset && 
                   (buffer[currentPosition + 1].isLetter() || buffer[currentPosition + 1] == '_')) {
                currentPosition++
                while (currentPosition < endOffset && 
                       (buffer[currentPosition].isLetterOrDigit() || buffer[currentPosition] == '_')) {
                    currentPosition++
                }
            }
            
            // Check for compound identifiers with dots (e.g., file.dashboard.ext)
            // If there's a dot after our identifier, consume the whole compound identifier
            var isCompoundIdentifier = false
            if (currentPosition < endOffset && buffer[currentPosition] == '.') {
                val checkPosition = currentPosition + 1
                if (checkPosition < endOffset && 
                    (buffer[checkPosition].isLetter() || buffer[checkPosition] == '_')) {
                    // This looks like a compound identifier, consume the whole thing
                    isCompoundIdentifier = true
                    while (currentPosition < endOffset) {
                        if (buffer[currentPosition] == '.') {
                            currentPosition++
                            // Consume the next identifier part
                            while (currentPosition < endOffset &&
                                   (buffer[currentPosition].isLetterOrDigit() ||
                                    buffer[currentPosition] == '_' ||
                                    buffer[currentPosition] == '-')) {
                                currentPosition++
                            }
                        } else {
                            break
                        }
                    }
                }
            }
            
            val word = buffer.substring(start, currentPosition).toString()
            tokenEnd = currentPosition
            
            // If it's a compound identifier with dots, always treat as IDENTIFIER
            if (isCompoundIdentifier) {
                currentToken = LookMLTypes.IDENTIFIER
                return
            }
            
            // Check for sql: pattern
            if (word == "sql" && currentPosition < endOffset && buffer[currentPosition] == ':') {
                var pos = currentPosition + 1
                while (pos < endOffset && buffer[pos].isWhitespace()) {
                    pos++
                }
                currentPosition = pos
                tokenEnd = currentPosition
                currentToken = LookMLTypes.SQL_BLOCK_START
                state = SQL_BLOCK
                return
            }
            
            // Check for sql_on: pattern
            if (word == "sql_on" && currentPosition < endOffset && buffer[currentPosition] == ':') {
                var pos = currentPosition + 1
                while (pos < endOffset && buffer[pos].isWhitespace()) {
                    pos++
                }
                currentPosition = pos
                tokenEnd = currentPosition
                currentToken = LookMLTypes.SQL_BLOCK_START
                state = SQL_BLOCK
                return
            }
            
            // Check for sql_where: pattern
            if (word == "sql_where" && currentPosition < endOffset && buffer[currentPosition] == ':') {
                var pos = currentPosition + 1
                while (pos < endOffset && buffer[pos].isWhitespace()) {
                    pos++
                }
                currentPosition = pos
                tokenEnd = currentPosition
                currentToken = LookMLTypes.SQL_BLOCK_START
                state = SQL_BLOCK
                return
            }
            
            // Check for sql_table_name: pattern  
            if (word == "sql_table_name" && currentPosition < endOffset && buffer[currentPosition] == ':') {
                var pos = currentPosition + 1
                while (pos < endOffset && buffer[pos].isWhitespace()) {
                    pos++
                }
                currentPosition = pos
                tokenEnd = currentPosition
                currentToken = LookMLTypes.SQL_BLOCK_START
                state = SQL_BLOCK
                return
            }
            
            // Check for sql_always_where: pattern
            if (word == "sql_always_where" && currentPosition < endOffset && buffer[currentPosition] == ':') {
                var pos = currentPosition + 1
                while (pos < endOffset && buffer[pos].isWhitespace()) {
                    pos++
                }
                currentPosition = pos
                tokenEnd = currentPosition
                currentToken = LookMLTypes.SQL_ALWAYS_WHERE_START
                state = SQL_BLOCK
                return
            }
            
            // Check for sql_always_filter: pattern
            if (word == "sql_always_filter" && currentPosition < endOffset && buffer[currentPosition] == ':') {
                var pos = currentPosition + 1
                while (pos < endOffset && buffer[pos].isWhitespace()) {
                    pos++
                }
                currentPosition = pos
                tokenEnd = currentPosition
                currentToken = LookMLTypes.SQL_ALWAYS_FILTER_START
                state = SQL_BLOCK
                return
            }
            
            // Check for sql_trigger: pattern
            if (word == "sql_trigger" && currentPosition < endOffset && buffer[currentPosition] == ':') {
                var pos = currentPosition + 1
                while (pos < endOffset && buffer[pos].isWhitespace()) {
                    pos++
                }
                currentPosition = pos
                tokenEnd = currentPosition
                currentToken = LookMLTypes.SQL_BLOCK_START
                state = SQL_BLOCK
                return
            }
            
            // Check for sql_always_having: pattern
            if (word == "sql_always_having" && currentPosition < endOffset && buffer[currentPosition] == ':') {
                var pos = currentPosition + 1
                while (pos < endOffset && buffer[pos].isWhitespace()) {
                    pos++
                }
                currentPosition = pos
                tokenEnd = currentPosition
                currentToken = LookMLTypes.SQL_ALWAYS_HAVING_START
                state = SQL_BLOCK
                return
            }
            
            // Check for sql_latitude: pattern
            if (word == "sql_latitude" && currentPosition < endOffset && buffer[currentPosition] == ':') {
                var pos = currentPosition + 1
                while (pos < endOffset && buffer[pos].isWhitespace()) {
                    pos++
                }
                currentPosition = pos
                tokenEnd = currentPosition
                currentToken = LookMLTypes.SQL_BLOCK_START
                state = SQL_BLOCK
                return
            }
            
            // Check for sql_longitude: pattern
            if (word == "sql_longitude" && currentPosition < endOffset && buffer[currentPosition] == ':') {
                var pos = currentPosition + 1
                while (pos < endOffset && buffer[pos].isWhitespace()) {
                    pos++
                }
                currentPosition = pos
                tokenEnd = currentPosition
                currentToken = LookMLTypes.SQL_BLOCK_START
                state = SQL_BLOCK
                return
            }
            
            // Check for html: pattern
            if (word == "html" && currentPosition < endOffset && buffer[currentPosition] == ':') {
                var pos = currentPosition + 1
                while (pos < endOffset && buffer[pos].isWhitespace()) {
                    pos++
                }
                currentPosition = pos
                tokenEnd = currentPosition
                currentToken = LookMLTypes.HTML_BLOCK_START
                state = SQL_BLOCK
                return
            }
            
            currentToken = when (word) {
                "view" -> LookMLTypes.VIEW
                "explore" -> LookMLTypes.EXPLORE
                "dashboard" -> LookMLTypes.DASHBOARD
                "lookml_dashboard" -> LookMLTypes.LOOKML_DASHBOARD
                "dimension" -> LookMLTypes.DIMENSION
                "dimension_group" -> LookMLTypes.DIMENSION_GROUP
                "measure" -> LookMLTypes.MEASURE
                "filter" -> LookMLTypes.FILTER
                "parameter" -> LookMLTypes.PARAMETER
                "join" -> LookMLTypes.JOIN
                "sql" -> LookMLTypes.SQL
                "sql_table_name" -> LookMLTypes.SQL_TABLE_NAME
                "sql_on" -> LookMLTypes.SQL_ON
                "sql_where" -> LookMLTypes.SQL_WHERE
                "sql_always_where" -> LookMLTypes.SQL_ALWAYS_WHERE
                "sql_always_filter" -> LookMLTypes.SQL_ALWAYS_FILTER
                "sql_latitude" -> LookMLTypes.SQL_LATITUDE
                "sql_longitude" -> LookMLTypes.SQL_LONGITUDE
                "from" -> LookMLTypes.FROM
                "view_label" -> LookMLTypes.VIEW_LABEL
                "alias" -> LookMLTypes.ALIAS
                "required_access_grants" -> LookMLTypes.REQUIRED_ACCESS_GRANTS
                "suggestions" -> LookMLTypes.SUGGESTIONS
                "relationship" -> LookMLTypes.RELATIONSHIP
                "name" -> LookMLTypes.NAME
                "type" -> LookMLTypes.TYPE
                "label" -> LookMLTypes.LABEL
                "group_label" -> LookMLTypes.GROUP_LABEL
                "description" -> LookMLTypes.DESCRIPTION
                "hidden" -> LookMLTypes.HIDDEN
                "primary_key" -> LookMLTypes.PRIMARY_KEY
                "persist_with" -> LookMLTypes.PERSIST_WITH
                "week_start_day" -> LookMLTypes.WEEK_START_DAY
                "case_sensitive" -> LookMLTypes.CASE_SENSITIVE
                "connection" -> LookMLTypes.CONNECTION
                "include" -> LookMLTypes.INCLUDE
                "datagroup" -> LookMLTypes.DATAGROUP
                "access_grant" -> LookMLTypes.ACCESS_GRANT
                "named_value_format" -> LookMLTypes.NAMED_VALUE_FORMAT
                "test" -> LookMLTypes.TEST
                "assert" -> LookMLTypes.ASSERT
                "expression" -> LookMLTypes.EXPRESSION
                "sql_trigger" -> LookMLTypes.SQL_TRIGGER
                "max_cache_age" -> LookMLTypes.MAX_CACHE_AGE
                "allowed_values" -> LookMLTypes.ALLOWED_VALUES
                "user_attribute" -> LookMLTypes.USER_ATTRIBUTE
                "value_format" -> LookMLTypes.VALUE_FORMAT
                "strict_value_format" -> LookMLTypes.STRICT_VALUE_FORMAT
                "always_filter" -> LookMLTypes.ALWAYS_FILTER
                "conditionally_filter" -> LookMLTypes.CONDITIONALLY_FILTER
                "filters" -> LookMLTypes.FILTERS
                "unless" -> LookMLTypes.UNLESS
                "access_filter" -> LookMLTypes.ACCESS_FILTER
                "field" -> LookMLTypes.FIELD
                "fields" -> LookMLTypes.FIELDS
                "required_joins" -> LookMLTypes.REQUIRED_JOINS
                "aggregate_table" -> LookMLTypes.AGGREGATE_TABLE
                "query" -> LookMLTypes.QUERY
                "dimensions" -> LookMLTypes.DIMENSIONS
                "measures" -> LookMLTypes.MEASURES
                "timezone" -> LookMLTypes.TIMEZONE
                "materialization" -> LookMLTypes.MATERIALIZATION
                "partition_keys" -> LookMLTypes.PARTITION_KEYS
                "cluster_keys" -> LookMLTypes.CLUSTER_KEYS
                "increment_key" -> LookMLTypes.INCREMENT_KEY
                "increment_offset" -> LookMLTypes.INCREMENT_OFFSET
                "link" -> LookMLTypes.LINK
                "action" -> LookMLTypes.ACTION
                "url" -> LookMLTypes.URL
                "icon_url" -> LookMLTypes.ICON_URL
                "form_url" -> LookMLTypes.FORM_URL
                "param" -> LookMLTypes.PARAM
                "form_param" -> LookMLTypes.FORM_PARAM
                "user_attribute_param" -> LookMLTypes.USER_ATTRIBUTE_PARAM
                "value" -> LookMLTypes.VALUE
                "default" -> LookMLTypes.DEFAULT
                "required" -> LookMLTypes.REQUIRED
                "html" -> LookMLTypes.HTML
                "case" -> LookMLTypes.CASE
                "when" -> LookMLTypes.WHEN
                "else" -> LookMLTypes.ELSE
                "set" -> LookMLTypes.SET
                "tags" -> LookMLTypes.TAGS
                "can_filter" -> LookMLTypes.CAN_FILTER
                "alpha_sort" -> LookMLTypes.ALPHA_SORT
                "tiers" -> LookMLTypes.TIERS
                "style" -> LookMLTypes.STYLE
                "convert_tz" -> LookMLTypes.CONVERT_TZ
                "datatype" -> LookMLTypes.DATATYPE
                "value_format_name" -> LookMLTypes.VALUE_FORMAT_NAME
                "precision" -> LookMLTypes.PRECISION
                "group_item_label" -> LookMLTypes.GROUP_ITEM_LABEL
                "timeframes" -> LookMLTypes.TIMEFRAMES
                "allow_fill" -> LookMLTypes.ALLOW_FILL
                "list_field" -> LookMLTypes.LIST_FIELD
                "suggest_dimension" -> LookMLTypes.SUGGEST_DIMENSION
                "listens_to_filters" -> LookMLTypes.LISTENS_TO_FILTERS
                "approximate" -> LookMLTypes.APPROXIMATE
                "approximate_threshold" -> LookMLTypes.APPROXIMATE_THRESHOLD
                "required_fields" -> LookMLTypes.REQUIRED_FIELDS
                "drill_fields" -> LookMLTypes.DRILL_FIELDS
                "elements" -> LookMLTypes.ELEMENTS
                "element" -> LookMLTypes.ELEMENT
                "title" -> LookMLTypes.TITLE
                "model" -> LookMLTypes.MODEL
                "sorts" -> LookMLTypes.SORTS
                "limit" -> LookMLTypes.LIMIT
                "show_single_value_title" -> LookMLTypes.SHOW_SINGLE_VALUE_TITLE
                "single_value_title" -> LookMLTypes.SINGLE_VALUE_TITLE
                "conditional_formatting" -> LookMLTypes.CONDITIONAL_FORMATTING
                "background_color" -> LookMLTypes.BACKGROUND_COLOR
                "font_color" -> LookMLTypes.FONT_COLOR
                "bold" -> LookMLTypes.BOLD
                "derived_table" -> LookMLTypes.DERIVED_TABLE
                "explore_source" -> LookMLTypes.EXPLORE_SOURCE
                "column" -> LookMLTypes.COLUMN
                "persist_for" -> LookMLTypes.PERSIST_FOR
                "indexes" -> LookMLTypes.INDEXES
                "fields_hidden_by_default" -> LookMLTypes.FIELDS_HIDDEN_BY_DEFAULT
                "datagroup_trigger" -> LookMLTypes.DATAGROUP_TRIGGER
                "layout" -> LookMLTypes.LAYOUT
                "preferred_viewer" -> LookMLTypes.PREFERRED_VIEWER
                "refresh" -> LookMLTypes.REFRESH
                "ui_config" -> LookMLTypes.UI_CONFIG
                "display" -> LookMLTypes.DISPLAY
                "options" -> LookMLTypes.OPTIONS
                "allow_multiple_values" -> LookMLTypes.ALLOW_MULTIPLE_VALUES
                "default_value" -> LookMLTypes.DEFAULT_VALUE
                "greater_than" -> LookMLTypes.GREATER_THAN
                "less_than" -> LookMLTypes.LESS_THAN
                "between" -> LookMLTypes.BETWEEN
                "equal_to" -> LookMLTypes.EQUAL_TO
                "white" -> LookMLTypes.WHITE
                "yes" -> LookMLTypes.YES
                "no" -> LookMLTypes.NO
                // Dashboard visualization properties
                "preferred_slug" -> LookMLTypes.PREFERRED_SLUG
                "column_limit" -> LookMLTypes.COLUMN_LIMIT
                "stacking" -> LookMLTypes.STACKING
                "legend_position" -> LookMLTypes.LEGEND_POSITION
                "x_axis_gridlines" -> LookMLTypes.X_AXIS_GRIDLINES
                "y_axis_gridlines" -> LookMLTypes.Y_AXIS_GRIDLINES
                "interpolation" -> LookMLTypes.INTERPOLATION
                "series_types" -> LookMLTypes.SERIES_TYPES
                "series_colors" -> LookMLTypes.SERIES_COLORS
                "color_application" -> LookMLTypes.COLOR_APPLICATION
                "inner_radius" -> LookMLTypes.INNER_RADIUS
                "trellis" -> LookMLTypes.TRELLIS
                "ordering" -> LookMLTypes.ORDERING
                "show_dropoff" -> LookMLTypes.SHOW_DROPOFF
                "table_theme" -> LookMLTypes.TABLE_THEME
                "header_text_alignment" -> LookMLTypes.HEADER_TEXT_ALIGNMENT
                "comparison" -> LookMLTypes.COMPARISON
                "comparison_type" -> LookMLTypes.COMPARISON_TYPE
                "comparison_reverse_colors" -> LookMLTypes.COMPARISON_REVERSE_COLORS
                "show_comparison" -> LookMLTypes.SHOW_COMPARISON
                "show_comparison_label" -> LookMLTypes.SHOW_COMPARISON_LABEL
                "comparison_label" -> LookMLTypes.COMPARISON_LABEL
                "enable_conditional_formatting" -> LookMLTypes.ENABLE_CONDITIONAL_FORMATTING
                "conditional_formatting_include_totals" -> LookMLTypes.CONDITIONAL_FORMATTING_INCLUDE_TOTALS
                "conditional_formatting_include_nulls" -> LookMLTypes.CONDITIONAL_FORMATTING_INCLUDE_NULLS
                "vis_config" -> LookMLTypes.VIS_CONFIG
                "hidden_fields" -> LookMLTypes.HIDDEN_FIELDS
                "series_labels" -> LookMLTypes.SERIES_LABELS
                "pivots" -> LookMLTypes.PIVOTS
                "dynamic_fields" -> LookMLTypes.DYNAMIC_FIELDS
                "query_timezone" -> LookMLTypes.QUERY_TIMEZONE
                "listen" -> LookMLTypes.LISTEN
                "show_value_labels" -> LookMLTypes.SHOW_VALUE_LABELS
                "label_density" -> LookMLTypes.LABEL_DENSITY
                "show_view_names" -> LookMLTypes.SHOW_VIEW_NAMES
                "y_axis_combined" -> LookMLTypes.Y_AXIS_COMBINED
                "show_y_axis_labels" -> LookMLTypes.SHOW_Y_AXIS_LABELS
                "show_y_axis_ticks" -> LookMLTypes.SHOW_Y_AXIS_TICKS
                "y_axis_tick_density" -> LookMLTypes.Y_AXIS_TICK_DENSITY
                "y_axis_tick_density_custom" -> LookMLTypes.Y_AXIS_TICK_DENSITY_CUSTOM
                "show_x_axis_label" -> LookMLTypes.SHOW_X_AXIS_LABEL
                "show_x_axis_ticks" -> LookMLTypes.SHOW_X_AXIS_TICKS
                "x_axis_scale" -> LookMLTypes.X_AXIS_SCALE
                "y_axis_scale_mode" -> LookMLTypes.Y_AXIS_SCALE_MODE
                "x_axis_reversed" -> LookMLTypes.X_AXIS_REVERSED
                "y_axis_reversed" -> LookMLTypes.Y_AXIS_REVERSED
                "plot_size_by_field" -> LookMLTypes.PLOT_SIZE_BY_FIELD
                "show_null_points" -> LookMLTypes.SHOW_NULL_POINTS
                "value_labels" -> LookMLTypes.VALUE_LABELS
                "label_type" -> LookMLTypes.LABEL_TYPE
                "start_angle" -> LookMLTypes.START_ANGLE
                "end_angle" -> LookMLTypes.END_ANGLE
                "collection_id" -> LookMLTypes.COLLECTION_ID
                "palette_id" -> LookMLTypes.PALETTE_ID
                "steps" -> LookMLTypes.STEPS
                "limit_displayed_rows" -> LookMLTypes.LIMIT_DISPLAYED_ROWS
                "limit_displayed_rows_values" -> LookMLTypes.LIMIT_DISPLAYED_ROWS_VALUES
                "point_style" -> LookMLTypes.POINT_STYLE
                "show_null_labels" -> LookMLTypes.SHOW_NULL_LABELS
                "show_totals_labels" -> LookMLTypes.SHOW_TOTALS_LABELS
                "show_silhouette" -> LookMLTypes.SHOW_SILHOUETTE
                "totals_color" -> LookMLTypes.TOTALS_COLOR
                "show_row_numbers" -> LookMLTypes.SHOW_ROW_NUMBERS
                "transpose" -> LookMLTypes.TRANSPOSE
                "truncate_text" -> LookMLTypes.TRUNCATE_TEXT
                "hide_totals" -> LookMLTypes.HIDE_TOTALS
                "hide_row_totals" -> LookMLTypes.HIDE_ROW_TOTALS
                "size_to_fit" -> LookMLTypes.SIZE_TO_FIT
                "header_font_size" -> LookMLTypes.HEADER_FONT_SIZE
                "rows_font_size" -> LookMLTypes.ROWS_FONT_SIZE
                "map_plot_mode" -> LookMLTypes.MAP_PLOT_MODE
                "heatmap_gridlines" -> LookMLTypes.HEATMAP_GRIDLINES
                "heatmap_gridlines_empty" -> LookMLTypes.HEATMAP_GRIDLINES_EMPTY
                "heatmap_opacity" -> LookMLTypes.HEATMAP_OPACITY
                "show_region_field" -> LookMLTypes.SHOW_REGION_FIELD
                "draw_map_labels_above_data" -> LookMLTypes.DRAW_MAP_LABELS_ABOVE_DATA
                "map_tile_provider" -> LookMLTypes.MAP_TILE_PROVIDER
                "map_position" -> LookMLTypes.MAP_POSITION
                "map_scale_indicator" -> LookMLTypes.MAP_SCALE_INDICATOR
                "map_pannable" -> LookMLTypes.MAP_PANNABLE
                "map_zoomable" -> LookMLTypes.MAP_ZOOMABLE
                "map_marker_type" -> LookMLTypes.MAP_MARKER_TYPE
                "map_marker_icon_name" -> LookMLTypes.MAP_MARKER_ICON_NAME
                "map_marker_radius_mode" -> LookMLTypes.MAP_MARKER_RADIUS_MODE
                "map_marker_units" -> LookMLTypes.MAP_MARKER_UNITS
                "map_marker_proportional_scale_type" -> LookMLTypes.MAP_MARKER_PROPORTIONAL_SCALE_TYPE
                "map_marker_color_mode" -> LookMLTypes.MAP_MARKER_COLOR_MODE
                "show_legend" -> LookMLTypes.SHOW_LEGEND
                "quantize_map_value_colors" -> LookMLTypes.QUANTIZE_MAP_VALUE_COLORS
                "reverse_map_value_colors" -> LookMLTypes.REVERSE_MAP_VALUE_COLORS
                "title_text" -> LookMLTypes.TITLE_TEXT
                "subtitle_text" -> LookMLTypes.SUBTITLE_TEXT
                "body_text" -> LookMLTypes.BODY_TEXT
                "custom_color_enabled" -> LookMLTypes.CUSTOM_COLOR_ENABLED
                "custom_color" -> LookMLTypes.CUSTOM_COLOR
                "font_size" -> LookMLTypes.FONT_SIZE
                "text_color" -> LookMLTypes.TEXT_COLOR
                "hidden_points_if_no" -> LookMLTypes.HIDDEN_POINTS_IF_NO
                "truncate_column_names" -> LookMLTypes.TRUNCATE_COLUMN_NAMES
                "row_total" -> LookMLTypes.ROW_TOTAL
                "category" -> LookMLTypes.CATEGORY
                "show_hide" -> LookMLTypes.SHOW_HIDE
                "first_last" -> LookMLTypes.FIRST_LAST
                "num_rows" -> LookMLTypes.NUM_ROWS
                "js_library_url" -> LookMLTypes.JS_LIBRARY_URL
                "dependencies" -> LookMLTypes.DEPENDENCIES
                "showLegend" -> LookMLTypes.SHOW_LEGEND_PROP
                "legendPosition" -> LookMLTypes.LEGEND_POSITION_PROP
                "colorScheme" -> LookMLTypes.COLOR_SCHEME
                "animationDuration" -> LookMLTypes.ANIMATION_DURATION
                "strikethrough" -> LookMLTypes.STRIKETHROUGH
                "italic" -> LookMLTypes.ITALIC
                "constraints" -> LookMLTypes.CONSTRAINTS
                "min" -> LookMLTypes.MIN
                "mid" -> LookMLTypes.MID
                "max" -> LookMLTypes.MAX
                "filter_expression" -> LookMLTypes.FILTER_EXPRESSION
                "monday" -> LookMLTypes.MONDAY
                "tuesday" -> LookMLTypes.TUESDAY
                "wednesday" -> LookMLTypes.WEDNESDAY
                "thursday" -> LookMLTypes.THURSDAY
                "friday" -> LookMLTypes.FRIDAY
                "saturday" -> LookMLTypes.SATURDAY
                "sunday" -> LookMLTypes.SUNDAY
                "desc" -> LookMLTypes.DESC
                "asc" -> LookMLTypes.ASC
                // Additional dashboard properties
                "row" -> LookMLTypes.ROW_PROPERTY
                "col" -> LookMLTypes.COL_PROPERTY
                "width" -> LookMLTypes.WIDTH_PROPERTY
                "height" -> LookMLTypes.HEIGHT_PROPERTY
                "fill_fields" -> LookMLTypes.FILL_FIELDS
                "true" -> LookMLTypes.TRUE_VALUE
                "false" -> LookMLTypes.FALSE_VALUE
                "x_axis_label" -> LookMLTypes.X_AXIS_LABEL
                "x_axis_zoom" -> LookMLTypes.X_AXIS_ZOOM
                "y_axis_zoom" -> LookMLTypes.Y_AXIS_ZOOM
                "defaults_version" -> LookMLTypes.DEFAULTS_VERSION
                "show_totals" -> LookMLTypes.SHOW_TOTALS
                "show_row_totals" -> LookMLTypes.SHOW_ROW_TOTALS
                "series_cell_visualizations" -> LookMLTypes.SERIES_CELL_VISUALIZATIONS
                "hide_legend" -> LookMLTypes.HIDE_LEGEND
                "swap_axes" -> LookMLTypes.SWAP_AXES
                "show_sql_query_menu_options" -> LookMLTypes.SHOW_SQL_QUERY_MENU_OPTIONS
                "field_filter" -> LookMLTypes.FIELD_FILTER
                "label_value_format" -> LookMLTypes.LABEL_VALUE_FORMAT
                "is_active" -> LookMLTypes.IS_ACTIVE
                else -> LookMLTypes.IDENTIFIER
            }
            return
        }
        
        // Unknown character
        tokenEnd = currentPosition + 1
        currentToken = TokenType.BAD_CHARACTER
    }
    
    override fun getBufferSequence(): CharSequence = buffer
    
    override fun getBufferEnd(): Int = endOffset
}
