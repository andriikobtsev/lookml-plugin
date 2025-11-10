package com.yourcompany.lookml

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import com.intellij.psi.TokenType
import com.yourcompany.lookml.psi.LookMLTypes

open class SimpleLookMLLexer : LexerBase() {
    private var buffer: CharSequence? = null
    private var startOffset = 0
    private var endOffset = 0
    private var currentPosition = 0
    private var currentTokenType: IElementType? = null
    private var currentTokenStart = 0
    private var currentTokenEnd = 0
    private var inSqlBlock = false
    
    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.currentPosition = startOffset
        this.inSqlBlock = initialState == 1
        advance()
    }
    
    override fun getState(): Int = if (inSqlBlock) 1 else 0
    
    override fun getTokenType(): IElementType? = currentTokenType
    
    override fun getTokenStart(): Int = currentTokenStart
    
    override fun getTokenEnd(): Int = currentTokenEnd
    
    override fun advance() {
        currentTokenStart = currentPosition
        
        if (currentPosition >= endOffset) {
            currentTokenType = null
            return
        }
        
        // If we're in SQL block mode, handle SQL content
        if (inSqlBlock) {
            // Check for ;;
            if (currentPosition + 1 < endOffset && 
                buffer!![currentPosition] == ';' && 
                buffer!![currentPosition + 1] == ';') {
                currentPosition += 2
                currentTokenEnd = currentPosition
                currentTokenType = LookMLTypes.SQL_BLOCK_END
                inSqlBlock = false
                return
            }
            
            // Consume SQL content until we find ;;
            val start = currentPosition
            while (currentPosition < endOffset) {
                if (currentPosition + 1 < endOffset && 
                    buffer!![currentPosition] == ';' && 
                    buffer!![currentPosition + 1] == ';') {
                    break
                }
                currentPosition++
            }
            
            if (currentPosition > start) {
                currentTokenEnd = currentPosition
                currentTokenType = LookMLTypes.SQL_CONTENT_TOKEN
                return
            }
        }
        
        val ch = buffer!![currentPosition]
        
        // Handle whitespace as a token
        if (ch.isWhitespace()) {
            while (currentPosition < endOffset && buffer!![currentPosition].isWhitespace()) {
                currentPosition++
            }
            currentTokenEnd = currentPosition
            currentTokenType = TokenType.WHITE_SPACE
            return
        }
        
        when {
            // Comments
            ch == '#' -> {
                while (currentPosition < endOffset && buffer!![currentPosition] != '\n') {
                    currentPosition++
                }
                currentTokenEnd = currentPosition
                currentTokenType = LookMLTypes.COMMENT
            }
            
            // String literals
            ch == '"' -> {
                currentPosition++
                while (currentPosition < endOffset && buffer!![currentPosition] != '"') {
                    if (buffer!![currentPosition] == '\\' && currentPosition + 1 < endOffset) {
                        currentPosition += 2
                    } else {
                        currentPosition++
                    }
                }
                if (currentPosition < endOffset) currentPosition++
                currentTokenEnd = currentPosition
                currentTokenType = LookMLTypes.STRING
            }
            
            // Numbers
            ch.isDigit() -> {
                while (currentPosition < endOffset && (buffer!![currentPosition].isDigit() || buffer!![currentPosition] == '.')) {
                    currentPosition++
                }
                currentTokenEnd = currentPosition
                currentTokenType = LookMLTypes.NUMBER
            }
            
            // Identifiers and keywords
            ch.isLetter() || ch == '_' -> {
                val start = currentPosition
                while (currentPosition < endOffset && (buffer!![currentPosition].isLetterOrDigit() || buffer!![currentPosition] == '_')) {
                    currentPosition++
                }
                currentTokenEnd = currentPosition
                val text = buffer!!.subSequence(start, currentPosition).toString()
                
                // Check if this is a SQL block start (e.g., sql_trigger:)
                if (isSqlBlockKeyword(text) && checkForColonAfterWhitespace()) {
                    // Consume the colon and any whitespace
                    consumeColonAndWhitespace()
                    currentTokenEnd = currentPosition
                    
                    // Return the appropriate token type
                    currentTokenType = when (text) {
                        "sql_always_where" -> {
                            inSqlBlock = true
                            LookMLTypes.SQL_ALWAYS_WHERE_START
                        }
                        "sql_always_filter" -> {
                            inSqlBlock = true
                            LookMLTypes.SQL_ALWAYS_FILTER_START
                        }
                        "sql_always_having" -> {
                            inSqlBlock = true
                            LookMLTypes.SQL_ALWAYS_HAVING_START
                        }
                        "html" -> {
                            inSqlBlock = true
                            LookMLTypes.HTML_BLOCK_START
                        }
                        else -> {
                            inSqlBlock = true
                            LookMLTypes.SQL_BLOCK_START
                        }
                    }
                    return
                }
                
                currentTokenType = when (text) {
                    "view" -> LookMLTypes.VIEW
                    "explore" -> LookMLTypes.EXPLORE
                    "dashboard" -> LookMLTypes.DASHBOARD
                    "lookml_dashboard" -> LookMLTypes.LOOKML_DASHBOARD
                    "connection" -> LookMLTypes.CONNECTION
                    "include" -> LookMLTypes.INCLUDE
                    "persist_with" -> LookMLTypes.PERSIST_WITH
                    "week_start_day" -> LookMLTypes.WEEK_START_DAY
                    "case_sensitive" -> LookMLTypes.CASE_SENSITIVE
                    "datagroup" -> LookMLTypes.DATAGROUP
                    "access_grant" -> LookMLTypes.ACCESS_GRANT
                    "named_value_format" -> LookMLTypes.NAMED_VALUE_FORMAT
                    "dimension" -> LookMLTypes.DIMENSION
                    "dimension_group" -> LookMLTypes.DIMENSION_GROUP
                    "measure" -> LookMLTypes.MEASURE
                    "filter" -> LookMLTypes.FILTER
                    "parameter" -> LookMLTypes.PARAMETER
                    "join" -> LookMLTypes.JOIN
                    "from" -> LookMLTypes.FROM
                    "sql" -> LookMLTypes.SQL
                    "sql_table_name" -> LookMLTypes.SQL_TABLE_NAME
                    "sql_on" -> LookMLTypes.SQL_ON
                    "sql_where" -> LookMLTypes.SQL_WHERE
                    "sql_always_where" -> LookMLTypes.SQL_ALWAYS_WHERE
                    "sql_always_filter" -> LookMLTypes.SQL_ALWAYS_FILTER
                    "sql_always_having" -> LookMLTypes.SQL_ALWAYS_HAVING
                    "sql_case" -> LookMLTypes.SQL_CASE
                    "sql_trigger" -> LookMLTypes.SQL_TRIGGER
                    "type" -> LookMLTypes.TYPE
                    "label" -> LookMLTypes.LABEL
                    "view_label" -> LookMLTypes.VIEW_LABEL
                    "description" -> LookMLTypes.DESCRIPTION
                    "hidden" -> LookMLTypes.HIDDEN
                    "primary_key" -> LookMLTypes.PRIMARY_KEY
                    "alias" -> LookMLTypes.ALIAS
                    "required_access_grants" -> LookMLTypes.REQUIRED_ACCESS_GRANTS
                    "suggestions" -> LookMLTypes.SUGGESTIONS
                    "relationship" -> LookMLTypes.RELATIONSHIP
                    "max_cache_age" -> LookMLTypes.MAX_CACHE_AGE
                    "allowed_value" -> LookMLTypes.ALLOWED_VALUE
                    "allowed_values" -> LookMLTypes.ALLOWED_VALUES
                    "user_attribute" -> LookMLTypes.USER_ATTRIBUTE
                    "value_format" -> LookMLTypes.VALUE_FORMAT
                    "strict_value_format" -> LookMLTypes.STRICT_VALUE_FORMAT
                    "view_name" -> LookMLTypes.VIEW_NAME
                    "group_label" -> LookMLTypes.GROUP_LABEL
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
                    "datagroup_trigger" -> LookMLTypes.DATAGROUP_TRIGGER
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
                    "name" -> LookMLTypes.NAME
                    "value" -> LookMLTypes.VALUE
                    "default" -> LookMLTypes.DEFAULT
                    "required" -> LookMLTypes.REQUIRED
                    "html" -> LookMLTypes.HTML
                    "case" -> LookMLTypes.CASE
                    "when" -> LookMLTypes.WHEN
                    "else" -> LookMLTypes.ELSE
                    "monday" -> LookMLTypes.MONDAY
                    "tuesday" -> LookMLTypes.TUESDAY
                    "wednesday" -> LookMLTypes.WEDNESDAY
                    "thursday" -> LookMLTypes.THURSDAY
                    "friday" -> LookMLTypes.FRIDAY
                    "saturday" -> LookMLTypes.SATURDAY
                    "sunday" -> LookMLTypes.SUNDAY
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
                    "group_item_label" -> LookMLTypes.GROUP_ITEM_LABEL
                    "timeframes" -> LookMLTypes.TIMEFRAMES
                    "allow_fill" -> LookMLTypes.ALLOW_FILL
                    "list_field" -> LookMLTypes.LIST_FIELD
                    "suggest_dimension" -> LookMLTypes.SUGGEST_DIMENSION
                    "approximate" -> LookMLTypes.APPROXIMATE
                    "approximate_threshold" -> LookMLTypes.APPROXIMATE_THRESHOLD
                    "required_fields" -> LookMLTypes.REQUIRED_FIELDS
                    "yes" -> LookMLTypes.YES
                    "no" -> LookMLTypes.NO
                    "AND" -> LookMLTypes.AND
                    "OR" -> LookMLTypes.OR
                    else -> LookMLTypes.IDENTIFIER
                }
            }
            
            // Two-character operators need to be checked first
            ch == ';' && currentPosition + 1 < endOffset && buffer!![currentPosition + 1] == ';' -> {
                currentPosition += 2
                currentTokenEnd = currentPosition
                currentTokenType = LookMLTypes.SQL_BLOCK_END
            }
            
            ch == '!' && currentPosition + 1 < endOffset && buffer!![currentPosition + 1] == '=' -> {
                currentPosition += 2
                currentTokenEnd = currentPosition
                currentTokenType = LookMLTypes.NE
            }
            
            ch == '<' && currentPosition + 1 < endOffset && buffer!![currentPosition + 1] == '=' -> {
                currentPosition += 2
                currentTokenEnd = currentPosition
                currentTokenType = LookMLTypes.LE
            }
            
            ch == '>' && currentPosition + 1 < endOffset && buffer!![currentPosition + 1] == '=' -> {
                currentPosition += 2
                currentTokenEnd = currentPosition
                currentTokenType = LookMLTypes.GE
            }
            
            // Single character operators and delimiters
            else -> {
                currentPosition++
                currentTokenEnd = currentPosition
                currentTokenType = when (ch) {
                    '{' -> LookMLTypes.LBRACE
                    '}' -> LookMLTypes.RBRACE
                    '[' -> LookMLTypes.LBRACKET
                    ']' -> LookMLTypes.RBRACKET
                    ':' -> LookMLTypes.COLON
                    ';' -> LookMLTypes.SEMICOLON
                    ',' -> LookMLTypes.COMMA
                    '$' -> LookMLTypes.DOLLAR
                    '.' -> LookMLTypes.DOT
                    '=' -> LookMLTypes.EQ
                    '<' -> LookMLTypes.LT
                    '>' -> LookMLTypes.GT
                    '+' -> LookMLTypes.PLUS
                    '-' -> LookMLTypes.MINUS
                    '*' -> LookMLTypes.STAR
                    '/' -> LookMLTypes.SLASH
                    '%' -> LookMLTypes.PERCENT
                    '(' -> LookMLTypes.LPAREN
                    ')' -> LookMLTypes.RPAREN
                    else -> TokenType.BAD_CHARACTER
                }
            }
        }
    }
    
    private fun isSqlBlockKeyword(text: String): Boolean {
        return text in setOf("sql", "sql_on", "sql_where", "sql_table_name", "sql_trigger", "sql_latitude", "sql_longitude", "sql_always_where", "sql_always_filter", "sql_always_having", "html")
    }
    
    private fun checkForColonAfterWhitespace(): Boolean {
        var pos = currentPosition
        while (pos < endOffset && buffer!![pos].isWhitespace()) {
            pos++
        }
        return pos < endOffset && buffer!![pos] == ':'
    }
    
    private fun consumeColonAndWhitespace() {
        // Skip whitespace before colon
        while (currentPosition < endOffset && buffer!![currentPosition].isWhitespace()) {
            currentPosition++
        }
        // Skip the colon
        if (currentPosition < endOffset && buffer!![currentPosition] == ':') {
            currentPosition++
        }
        // Skip whitespace after colon
        while (currentPosition < endOffset && buffer!![currentPosition].isWhitespace()) {
            currentPosition++
        }
    }
    
    override fun getBufferSequence(): CharSequence = buffer!!
    
    override fun getBufferEnd(): Int = endOffset
}
