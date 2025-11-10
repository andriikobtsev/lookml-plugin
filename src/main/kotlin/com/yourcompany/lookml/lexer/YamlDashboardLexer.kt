package com.yourcompany.lookml.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import com.intellij.psi.TokenType
import com.yourcompany.lookml.psi.LookMLTypes

/**
 * Specialized lexer for YAML-style LookML dashboards.
 * Handles YAML syntax with LookML-specific extensions like property names with pipes.
 * Now includes full Unicode support for property values.
 */
class YamlDashboardLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset = 0
    private var endOffset = 0
    private var currentPosition = 0
    private var tokenStart = 0
    private var tokenEnd = 0
    private var currentToken: IElementType? = null
    private var indentStack = mutableListOf<Int>()
    private var isAtLineStart = true
    private var lastNonWhitespaceToken: IElementType? = null
    
    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.currentPosition = startOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        this.currentToken = null
        this.indentStack.clear()
        this.indentStack.add(0) // Base indent level
        this.isAtLineStart = true
        advance()
    }
    
    override fun getState(): Int = 0 // YAML lexer is stateless for now
    
    override fun getTokenType(): IElementType? = currentToken
    
    override fun getTokenStart(): Int = tokenStart
    
    override fun getTokenEnd(): Int = tokenEnd
    
    override fun advance() {
        // Store the previous token type before advancing
        if (currentToken != TokenType.WHITE_SPACE && currentToken != null) {
            lastNonWhitespaceToken = currentToken
        }
        
        tokenStart = tokenEnd
        
        if (tokenEnd >= endOffset) {
            currentToken = null
            return
        }
        
        currentPosition = tokenEnd
        
        // Handle document start marker
        if (currentPosition == startOffset && currentPosition + 3 <= endOffset &&
            buffer[currentPosition] == '-' && 
            buffer[currentPosition + 1] == '-' && 
            buffer[currentPosition + 2] == '-') {
            tokenEnd = currentPosition + 3
            currentToken = LookMLTypes.YAML_DOCUMENT_START
            return
        }
        
        // Skip whitespace but track line starts
        if (buffer[currentPosition].isWhitespace()) {
            val start = currentPosition
            while (currentPosition < endOffset && buffer[currentPosition].isWhitespace()) {
                if (buffer[currentPosition] == '\n') {
                    isAtLineStart = true
                }
                currentPosition++
            }
            tokenEnd = currentPosition
            currentToken = TokenType.WHITE_SPACE
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
        
        // List item marker at line start
        if (isAtLineStart && ch == '-' && currentPosition + 1 < endOffset && 
            buffer[currentPosition + 1].isWhitespace()) {
            tokenEnd = currentPosition + 1
            currentToken = LookMLTypes.YAML_LIST_ITEM
            isAtLineStart = false
            return
        }
        
        // String literals
        if (ch == '"' || ch == '\'') {
            val quoteChar = ch
            currentPosition++
            while (currentPosition < endOffset) {
                if (buffer[currentPosition] == quoteChar) {
                    // Check for escaped quote
                    if (currentPosition > tokenStart + 1 && buffer[currentPosition - 1] == '\\') {
                        currentPosition++
                        continue
                    }
                    currentPosition++
                    break
                }
                currentPosition++
            }
            tokenEnd = currentPosition
            currentToken = LookMLTypes.STRING
            return
        }
        
        // Multi-line string indicators (block scalars)
        if ((ch == '|' || ch == '>') && isAfterColon()) {
            currentPosition++
            // Check for chomping indicators (-, +)
            if (currentPosition < endOffset && 
                (buffer[currentPosition] == '-' || buffer[currentPosition] == '+')) {
                currentPosition++
            }
            // Check for explicit indentation indicator (1-9)
            if (currentPosition < endOffset && buffer[currentPosition].isDigit()) {
                currentPosition++
            }
            tokenEnd = currentPosition
            currentToken = LookMLTypes.YAML_BLOCK_SCALAR_INDICATOR
            return
        }
        
        // Numbers
        if (ch.isDigit() || (ch == '-' && currentPosition + 1 < endOffset && 
            buffer[currentPosition + 1].isDigit())) {
            if (ch == '-') currentPosition++
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
                isAtLineStart = false
                return
            }
            '}' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.RBRACE
                isAtLineStart = false
                return
            }
            '[' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.LBRACKET
                isAtLineStart = false
                return
            }
            ']' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.RBRACKET
                isAtLineStart = false
                return
            }
            ':' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.COLON
                isAtLineStart = false
                return
            }
            ',' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.COMMA
                isAtLineStart = false
                return
            }
            '=' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.EQ
                isAtLineStart = false
                return
            }
            '!' -> {
                if (currentPosition + 1 < endOffset && buffer[currentPosition + 1] == '=') {
                    tokenEnd = currentPosition + 2
                    currentToken = LookMLTypes.NE
                    isAtLineStart = false
                    return
                }
                // YAML tag indicator
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.YAML_TAG_INDICATOR
                isAtLineStart = false
                return
            }
            '<' -> {
                if (currentPosition + 1 < endOffset && buffer[currentPosition + 1] == '=') {
                    tokenEnd = currentPosition + 2
                    currentToken = LookMLTypes.LE
                    isAtLineStart = false
                    return
                }
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.LT
                isAtLineStart = false
                return
            }
            '>' -> {
                if (currentPosition + 1 < endOffset && buffer[currentPosition + 1] == '=') {
                    tokenEnd = currentPosition + 2
                    currentToken = LookMLTypes.GE
                    isAtLineStart = false
                    return
                }
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.GT
                isAtLineStart = false
                return
            }
            '+' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.PLUS
                isAtLineStart = false
                return
            }
            '-' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.MINUS
                isAtLineStart = false
                return
            }
            '*' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.STAR
                isAtLineStart = false
                return
            }
            '/' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.SLASH
                isAtLineStart = false
                return
            }
            '%' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.PERCENT
                isAtLineStart = false
                return
            }
            '(' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.LPAREN
                isAtLineStart = false
                return
            }
            ')' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.RPAREN
                isAtLineStart = false
                return
            }
            '|' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.PIPE
                isAtLineStart = false
                return
            }
            '$' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.DOLLAR
                isAtLineStart = false
                return
            }
            '.' -> {
                tokenEnd = currentPosition + 1
                currentToken = LookMLTypes.DOT
                isAtLineStart = false
                return
            }
        }
        
        // Keywords and identifiers (including those with pipes for LookML extensions)
        // ENHANCED: Now handles Unicode characters properly
        if (isIdentifierStart(ch)) {
            val start = currentPosition
            
            // First, consume the regular identifier part (now with Unicode support)
            while (currentPosition < endOffset && isIdentifierPart(buffer[currentPosition])) {
                currentPosition++
            }
            
            // Check for pipe extension (e.g., label|field_name)
            if (currentPosition < endOffset && buffer[currentPosition] == '|') {
                currentPosition++ // consume pipe
                // Continue with the second part (also with Unicode support)
                while (currentPosition < endOffset && isIdentifierPart(buffer[currentPosition])) {
                    currentPosition++
                }
            }
            
            // Check for compound identifiers with dots (e.g., file.dashboard.ext)
            var isCompoundIdentifier = false
            if (currentPosition < endOffset && buffer[currentPosition] == '.') {
                val checkPosition = currentPosition + 1
                if (checkPosition < endOffset && isIdentifierStart(buffer[checkPosition])) {
                    // This looks like a compound identifier, consume the whole thing
                    isCompoundIdentifier = true
                    while (currentPosition < endOffset) {
                        if (buffer[currentPosition] == '.') {
                            currentPosition++
                            // Consume the next identifier part
                            while (currentPosition < endOffset && isIdentifierPart(buffer[currentPosition])) {
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
            isAtLineStart = false
            
            // If it's a compound identifier with dots, always treat as IDENTIFIER
            if (isCompoundIdentifier) {
                currentToken = LookMLTypes.IDENTIFIER
                return
            }
            
            // Check for keywords (without the pipe extension)
            val baseWord = word.substringBefore('|')
            currentToken = getKeywordToken(baseWord) ?: LookMLTypes.IDENTIFIER
            return
        }
        
        // ENHANCED: Handle unquoted YAML text values with Unicode support
        // This is the key fix for the Unicode issue and complex expressions
        if (isAfterColon() && !ch.isWhitespace() && 
            ch != '{' && ch != '[' && ch != ',' && ch != '}' && ch != ']' && ch != '#') {
            
            // FIRST: Check if this could be a simple keyword value (yes, no, true, false)
            if (isIdentifierStart(ch)) {
                val start = currentPosition
                while (currentPosition < endOffset && isIdentifierPart(buffer[currentPosition])) {
                    currentPosition++
                }
                
                val word = buffer.substring(start, currentPosition).toString()
                
                // DEBUG: Always try to get keyword token for debugging
                val keywordToken = getKeywordToken(word)
                if (keywordToken != null) {
                    tokenEnd = currentPosition
                    currentToken = keywordToken
                    isAtLineStart = false
                    return
                }
                
                // Reset position if not a keyword
                currentPosition = start
            }
            
            val start = currentPosition
            
            // For complex expressions (starting with ${ or containing operators),
            // consume the entire value as a single token
            val isComplexExpression = (ch == '$' && currentPosition + 1 < endOffset && 
                                     buffer[currentPosition + 1] == '{') ||
                                     containsOperatorsAhead()
            
            if (isComplexExpression) {
                // Consume everything until line end or comment
                while (currentPosition < endOffset) {
                    val currentChar = buffer[currentPosition]
                    
                    // Stop at line endings
                    if (currentChar == '\n' || currentChar == '\r') {
                        break
                    }
                    
                    // Stop at comments (but only if preceded by whitespace)
                    if (currentChar == '#' && currentPosition > start && 
                        buffer[currentPosition - 1].isWhitespace()) {
                        break
                    }
                    
                    currentPosition++
                }
            } else {
                // Simple value - stop at structural YAML characters
                while (currentPosition < endOffset) {
                    val currentChar = buffer[currentPosition]
                    
                    // Stop at line endings
                    if (currentChar == '\n' || currentChar == '\r') {
                        break
                    }
                    
                    // Stop at comments
                    if (currentChar == '#') {
                        break
                    }
                    
                    // Stop at YAML structural characters
                    if (currentChar == '{' || currentChar == '}' || 
                        currentChar == '[' || currentChar == ']' ||
                        currentChar == ',' || currentChar == ':') {
                        break
                    }
                    
                    currentPosition++
                }
            }
            
            // Trim trailing whitespace
            while (currentPosition > start && buffer[currentPosition - 1].isWhitespace()) {
                currentPosition--
            }
            
            if (currentPosition > start) {
                tokenEnd = currentPosition
                currentToken = LookMLTypes.INLINE_CHAR
                isAtLineStart = false
                return
            }
        }
        
        // ENHANCED: Handle individual Unicode characters that don't fit other patterns
        // This ensures we don't generate BAD_CHARACTER tokens for valid Unicode
        if (isUnicodeCharacter(ch)) {
            currentPosition++
            tokenEnd = currentPosition
            currentToken = LookMLTypes.INLINE_CHAR
            isAtLineStart = false
            return
        }
        
        // Unknown character - only for truly invalid characters
        tokenEnd = currentPosition + 1
        currentToken = TokenType.BAD_CHARACTER
        isAtLineStart = false
    }
    
    override fun getBufferSequence(): CharSequence = buffer
    
    override fun getBufferEnd(): Int = endOffset
    
    /**
     * Check if character can start an identifier.
     * ENHANCED: Now supports Unicode letters and categories.
     */
    private fun isIdentifierStart(ch: Char): Boolean {
        return ch.isLetter() || ch == '_' || Character.isUnicodeIdentifierStart(ch)
    }
    
    /**
     * Check if character can be part of an identifier.
     * ENHANCED: Now supports Unicode letters, digits, and appropriate symbols.
     */
    private fun isIdentifierPart(ch: Char): Boolean {
        return ch.isLetterOrDigit() || ch == '_' || ch == '-' || 
               Character.isUnicodeIdentifierPart(ch) ||
               // Allow common mathematical symbols in identifiers
               isAllowedUnicodeSymbol(ch)
    }
    
    /**
     * Check if the text ahead contains operators that indicate a complex expression.
     * This helps identify LookML expressions that should be consumed as single tokens.
     */
    private fun containsOperatorsAhead(): Boolean {
        var pos = currentPosition
        val maxLookAhead = minOf(currentPosition + 50, endOffset) // Look ahead up to 50 chars
        
        while (pos < maxLookAhead) {
            val ch = buffer[pos]
            
            // Stop at line endings
            if (ch == '\n' || ch == '\r') {
                break
            }
            
            // Look for operators that indicate complex expressions
            when (ch) {
                '!' -> {
                    if (pos + 1 < endOffset && buffer[pos + 1] == '=') {
                        return true // Found !=
                    }
                }
                '=' -> {
                    if (pos + 1 < endOffset && buffer[pos + 1] == '=') {
                        return true // Found ==
                    }
                }
                '<', '>' -> {
                    return true // Found < or >
                }
                '&' -> {
                    if (pos + 1 < endOffset && buffer[pos + 1] == '&') {
                        return true // Found &&
                    }
                }
                '|' -> {
                    if (pos + 1 < endOffset && buffer[pos + 1] == '|') {
                        return true // Found ||
                    }
                }
            }
            pos++
        }
        
        return false
    }
    
    /**
     * Check if character is an allowed Unicode symbol in YAML text values.
     * This includes mathematical symbols like superscripts, subscripts, etc.
     */
    private fun isAllowedUnicodeSymbol(ch: Char): Boolean {
        val codePoint = ch.code
        
        // Superscripts and subscripts (U+2070-U+209F)
        if (codePoint in 0x2070..0x209F) return true
        
        // Mathematical symbols (U+2200-U+22FF)
        if (codePoint in 0x2200..0x22FF) return true
        
        // Miscellaneous mathematical symbols (U+2900-U+297F)
        if (codePoint in 0x2900..0x297F) return true
        
        // Currency symbols (U+20A0-U+20CF)
        if (codePoint in 0x20A0..0x20CF) return true
        
        // Additional common symbols
        when (codePoint) {
            0x00B2, 0x00B3, 0x00B9 -> return true  // ², ³, ¹
            0x00BC, 0x00BD, 0x00BE -> return true  // ¼, ½, ¾
            0x00B0 -> return true                   // °
            in 0x03B1..0x03C9 -> return true       // Greek letters α-ω
            in 0x0391..0x03A9 -> return true       // Greek letters Α-Ω
        }
        
        return false
    }
    
    /**
     * Check if character is a valid Unicode character that should be tokenized
     * as INLINE_CHAR rather than BAD_CHARACTER.
     */
    private fun isUnicodeCharacter(ch: Char): Boolean {
        val codePoint = ch.code
        
        // Exclude control characters and surrogates
        if (Character.isISOControl(ch) || Character.isSurrogate(ch)) {
            return false
        }
        
        // Include all printable Unicode characters
        return Character.isDefined(ch) && !Character.isSpaceChar(ch)
    }
    
    private fun isAfterColon(): Boolean {
        // Check if the last non-whitespace token was a colon
        return lastNonWhitespaceToken == LookMLTypes.COLON
    }
    
    private fun getKeywordToken(word: String): IElementType? {
        return when (word) {
            // Dashboard structure keywords
            "dashboard" -> LookMLTypes.DASHBOARD
            "elements" -> LookMLTypes.ELEMENTS
            "filters" -> LookMLTypes.FILTERS
            "title" -> LookMLTypes.TITLE
            "name" -> LookMLTypes.NAME
            "type" -> LookMLTypes.TYPE
            "model" -> LookMLTypes.MODEL
            "explore" -> LookMLTypes.EXPLORE
            "fields" -> LookMLTypes.FIELDS
            "sorts" -> LookMLTypes.SORTS
            "limit" -> LookMLTypes.LIMIT
            "listen" -> LookMLTypes.LISTEN
            "row" -> LookMLTypes.ROW_PROPERTY
            "col" -> LookMLTypes.COL_PROPERTY
            "width" -> LookMLTypes.WIDTH_PROPERTY
            "height" -> LookMLTypes.HEIGHT_PROPERTY
            
            // Value keywords
            "yes" -> LookMLTypes.YES
            "no" -> LookMLTypes.NO
            "true" -> LookMLTypes.TRUE_VALUE
            "false" -> LookMLTypes.FALSE_VALUE
            "desc" -> LookMLTypes.DESC
            "asc" -> LookMLTypes.ASC
            
            // All other dashboard properties from the lexer
            "layout" -> LookMLTypes.LAYOUT
            "preferred_viewer" -> LookMLTypes.PREFERRED_VIEWER
            "preferred_slug" -> LookMLTypes.PREFERRED_SLUG
            "description" -> LookMLTypes.DESCRIPTION
            "refresh" -> LookMLTypes.REFRESH
            "default_value" -> LookMLTypes.DEFAULT_VALUE
            "allow_multiple_values" -> LookMLTypes.ALLOW_MULTIPLE_VALUES
            "required" -> LookMLTypes.REQUIRED
            "ui_config" -> LookMLTypes.UI_CONFIG
            "display" -> LookMLTypes.DISPLAY
            "field" -> LookMLTypes.FIELD
            "pivots" -> LookMLTypes.PIVOTS
            "fill_fields" -> LookMLTypes.FILL_FIELDS
            "column_limit" -> LookMLTypes.COLUMN_LIMIT
            
            // Visualization properties
            "x_axis_gridlines" -> LookMLTypes.X_AXIS_GRIDLINES
            "y_axis_gridlines" -> LookMLTypes.Y_AXIS_GRIDLINES
            "show_view_names" -> LookMLTypes.SHOW_VIEW_NAMES
            "show_y_axis_labels" -> LookMLTypes.SHOW_Y_AXIS_LABELS
            "show_y_axis_ticks" -> LookMLTypes.SHOW_Y_AXIS_TICKS
            "y_axis_tick_density" -> LookMLTypes.Y_AXIS_TICK_DENSITY
            "y_axis_tick_density_custom" -> LookMLTypes.Y_AXIS_TICK_DENSITY_CUSTOM
            "show_x_axis_label" -> LookMLTypes.SHOW_X_AXIS_LABEL
            "show_x_axis_ticks" -> LookMLTypes.SHOW_X_AXIS_TICKS
            "y_axis_scale_mode" -> LookMLTypes.Y_AXIS_SCALE_MODE
            "x_axis_reversed" -> LookMLTypes.X_AXIS_REVERSED
            "y_axis_reversed" -> LookMLTypes.Y_AXIS_REVERSED
            "plot_size_by_field" -> LookMLTypes.PLOT_SIZE_BY_FIELD
            "trellis" -> LookMLTypes.TRELLIS
            "stacking" -> LookMLTypes.STACKING
            "limit_displayed_rows" -> LookMLTypes.LIMIT_DISPLAYED_ROWS
            "legend_position" -> LookMLTypes.LEGEND_POSITION
            "point_style" -> LookMLTypes.POINT_STYLE
            "show_value_labels" -> LookMLTypes.SHOW_VALUE_LABELS
            "label_density" -> LookMLTypes.LABEL_DENSITY
            "x_axis_scale" -> LookMLTypes.X_AXIS_SCALE
            "y_axis_combined" -> LookMLTypes.Y_AXIS_COMBINED
            "show_null_points" -> LookMLTypes.SHOW_NULL_POINTS
            "interpolation" -> LookMLTypes.INTERPOLATION
            "color_application" -> LookMLTypes.COLOR_APPLICATION
            "x_axis_label" -> LookMLTypes.X_AXIS_LABEL
            "x_axis_zoom" -> LookMLTypes.X_AXIS_ZOOM
            "y_axis_zoom" -> LookMLTypes.Y_AXIS_ZOOM
            "series_types" -> LookMLTypes.SERIES_TYPES
            "series_colors" -> LookMLTypes.SERIES_COLORS
            "series_labels" -> LookMLTypes.SERIES_LABELS
            "defaults_version" -> LookMLTypes.DEFAULTS_VERSION
            "hidden_points_if_no" -> LookMLTypes.HIDDEN_POINTS_IF_NO
            "enable_conditional_formatting" -> LookMLTypes.ENABLE_CONDITIONAL_FORMATTING
            "conditional_formatting_include_totals" -> LookMLTypes.CONDITIONAL_FORMATTING_INCLUDE_TOTALS
            "conditional_formatting_include_nulls" -> LookMLTypes.CONDITIONAL_FORMATTING_INCLUDE_NULLS
            "header_text_alignment" -> LookMLTypes.HEADER_TEXT_ALIGNMENT
            "header_font_size" -> LookMLTypes.HEADER_FONT_SIZE
            "rows_font_size" -> LookMLTypes.ROWS_FONT_SIZE
            "table_theme" -> LookMLTypes.TABLE_THEME
            "show_totals" -> LookMLTypes.SHOW_TOTALS
            "show_row_totals" -> LookMLTypes.SHOW_ROW_TOTALS
            "show_row_numbers" -> LookMLTypes.SHOW_ROW_NUMBERS
            "transpose" -> LookMLTypes.TRANSPOSE
            "truncate_text" -> LookMLTypes.TRUNCATE_TEXT
            "size_to_fit" -> LookMLTypes.SIZE_TO_FIT
            "series_cell_visualizations" -> LookMLTypes.SERIES_CELL_VISUALIZATIONS
            "hide_legend" -> LookMLTypes.HIDE_LEGEND
            "swap_axes" -> LookMLTypes.SWAP_AXES
            "show_sql_query_menu_options" -> LookMLTypes.SHOW_SQL_QUERY_MENU_OPTIONS
            "custom_color_enabled" -> LookMLTypes.CUSTOM_COLOR_ENABLED
            "show_single_value_title" -> LookMLTypes.SHOW_SINGLE_VALUE_TITLE
            "show_comparison" -> LookMLTypes.SHOW_COMPARISON
            "comparison_type" -> LookMLTypes.COMPARISON_TYPE
            "comparison_reverse_colors" -> LookMLTypes.COMPARISON_REVERSE_COLORS
            "show_comparison_label" -> LookMLTypes.SHOW_COMPARISON_LABEL
            "hidden_fields" -> LookMLTypes.HIDDEN_FIELDS
            "listens_to_filters" -> LookMLTypes.LISTENS_TO_FILTERS
            "field_filter" -> LookMLTypes.FIELD_FILTER
            "options" -> LookMLTypes.OPTIONS
            "collection_id" -> LookMLTypes.COLLECTION_ID
            "palette_id" -> LookMLTypes.PALETTE_ID
            "value_format" -> LookMLTypes.VALUE_FORMAT
            "label_value_format" -> LookMLTypes.LABEL_VALUE_FORMAT
            "is_active" -> LookMLTypes.IS_ACTIVE
            "first_last" -> LookMLTypes.FIRST_LAST
            "num_rows" -> LookMLTypes.NUM_ROWS
            "show_hide" -> LookMLTypes.SHOW_HIDE
            
            else -> null
        }
    }
}
