package com.yourcompany.lookml.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.yourcompany.lookml.lexer.LookMLLexerAdapter
import com.yourcompany.lookml.psi.LookMLTypes

class LookMLSyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        // Define text attribute keys
        private val KEYWORD = TextAttributesKey.createTextAttributesKey(
            "LOOKML_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD
        )
        private val STRING = TextAttributesKey.createTextAttributesKey(
            "LOOKML_STRING", DefaultLanguageHighlighterColors.STRING
        )
        private val NUMBER = TextAttributesKey.createTextAttributesKey(
            "LOOKML_NUMBER", DefaultLanguageHighlighterColors.NUMBER
        )
        private val COMMENT = TextAttributesKey.createTextAttributesKey(
            "LOOKML_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT
        )
        private val PROPERTY = TextAttributesKey.createTextAttributesKey(
            "LOOKML_PROPERTY", DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )
        private val SQL = TextAttributesKey.createTextAttributesKey(
            "LOOKML_SQL", DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR
        )
        private val BRACES = TextAttributesKey.createTextAttributesKey(
            "LOOKML_BRACES", DefaultLanguageHighlighterColors.BRACES
        )
        private val BRACKETS = TextAttributesKey.createTextAttributesKey(
            "LOOKML_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS
        )
        private val OPERATOR = TextAttributesKey.createTextAttributesKey(
            "LOOKML_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        private val BAD_CHARACTER = TextAttributesKey.createTextAttributesKey(
            "LOOKML_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER
        )
        
        // Token sets for easier matching
        private val KEYWORDS = TokenSet.create(
            LookMLTypes.VIEW, LookMLTypes.EXPLORE, LookMLTypes.DASHBOARD,
            LookMLTypes.LOOKML_DASHBOARD, LookMLTypes.DIMENSION, LookMLTypes.DIMENSION_GROUP,
            LookMLTypes.MEASURE, LookMLTypes.FILTER, LookMLTypes.PARAMETER, LookMLTypes.JOIN,
            LookMLTypes.TEST
        )
        
        private val PROPERTY_KEYWORDS = TokenSet.create(
            LookMLTypes.TYPE, LookMLTypes.LABEL, LookMLTypes.VIEW_LABEL, LookMLTypes.DESCRIPTION,
            LookMLTypes.HIDDEN, LookMLTypes.PRIMARY_KEY, LookMLTypes.SQL,
            LookMLTypes.SQL_TABLE_NAME, LookMLTypes.SQL_ON, LookMLTypes.SQL_WHERE,
            LookMLTypes.SQL_ALWAYS_WHERE, LookMLTypes.SQL_ALWAYS_FILTER,
            LookMLTypes.FROM, LookMLTypes.ALIAS, LookMLTypes.REQUIRED_ACCESS_GRANTS,
            LookMLTypes.SUGGESTIONS, LookMLTypes.RELATIONSHIP,
            LookMLTypes.ASSERT, LookMLTypes.EXPRESSION, LookMLTypes.EXPLORE_SOURCE,
            // Dashboard visualization properties
            LookMLTypes.PREFERRED_SLUG, LookMLTypes.COLUMN_LIMIT, LookMLTypes.STACKING,
            LookMLTypes.LEGEND_POSITION, LookMLTypes.X_AXIS_GRIDLINES, LookMLTypes.Y_AXIS_GRIDLINES,
            LookMLTypes.INTERPOLATION, LookMLTypes.SERIES_TYPES, LookMLTypes.SERIES_COLORS,
            LookMLTypes.COLOR_APPLICATION, LookMLTypes.INNER_RADIUS, LookMLTypes.TRELLIS,
            LookMLTypes.ORDERING, LookMLTypes.SHOW_DROPOFF, LookMLTypes.TABLE_THEME,
            LookMLTypes.HEADER_TEXT_ALIGNMENT, LookMLTypes.COMPARISON, LookMLTypes.COMPARISON_TYPE,
            LookMLTypes.COMPARISON_REVERSE_COLORS, LookMLTypes.SHOW_COMPARISON,
            LookMLTypes.SHOW_COMPARISON_LABEL, LookMLTypes.COMPARISON_LABEL,
            LookMLTypes.ENABLE_CONDITIONAL_FORMATTING, LookMLTypes.CONDITIONAL_FORMATTING_INCLUDE_TOTALS,
            LookMLTypes.CONDITIONAL_FORMATTING_INCLUDE_NULLS, LookMLTypes.VIS_CONFIG,
            LookMLTypes.HIDDEN_FIELDS, LookMLTypes.SERIES_LABELS, LookMLTypes.PIVOTS,
            LookMLTypes.DYNAMIC_FIELDS, LookMLTypes.QUERY_TIMEZONE, LookMLTypes.LISTEN,
            LookMLTypes.SHOW_VALUE_LABELS, LookMLTypes.LABEL_DENSITY, LookMLTypes.SHOW_VIEW_NAMES,
            LookMLTypes.Y_AXIS_COMBINED, LookMLTypes.SHOW_Y_AXIS_LABELS, LookMLTypes.SHOW_Y_AXIS_TICKS,
            LookMLTypes.Y_AXIS_TICK_DENSITY, LookMLTypes.Y_AXIS_TICK_DENSITY_CUSTOM,
            LookMLTypes.SHOW_X_AXIS_LABEL, LookMLTypes.SHOW_X_AXIS_TICKS, LookMLTypes.X_AXIS_SCALE,
            LookMLTypes.Y_AXIS_SCALE_MODE, LookMLTypes.X_AXIS_REVERSED, LookMLTypes.Y_AXIS_REVERSED,
            LookMLTypes.PLOT_SIZE_BY_FIELD, LookMLTypes.SHOW_NULL_POINTS, LookMLTypes.VALUE_LABELS,
            LookMLTypes.LABEL_TYPE, LookMLTypes.START_ANGLE, LookMLTypes.END_ANGLE,
            LookMLTypes.COLLECTION_ID, LookMLTypes.PALETTE_ID, LookMLTypes.STEPS,
            LookMLTypes.LIMIT_DISPLAYED_ROWS, LookMLTypes.LIMIT_DISPLAYED_ROWS_VALUES,
            LookMLTypes.POINT_STYLE, LookMLTypes.SHOW_NULL_LABELS, LookMLTypes.SHOW_TOTALS_LABELS,
            LookMLTypes.SHOW_SILHOUETTE, LookMLTypes.TOTALS_COLOR, LookMLTypes.SHOW_ROW_NUMBERS,
            LookMLTypes.TRANSPOSE, LookMLTypes.TRUNCATE_TEXT, LookMLTypes.HIDE_TOTALS,
            LookMLTypes.HIDE_ROW_TOTALS, LookMLTypes.SIZE_TO_FIT, LookMLTypes.HEADER_FONT_SIZE,
            LookMLTypes.ROWS_FONT_SIZE, LookMLTypes.MAP_PLOT_MODE, LookMLTypes.HEATMAP_GRIDLINES,
            LookMLTypes.HEATMAP_GRIDLINES_EMPTY, LookMLTypes.HEATMAP_OPACITY,
            LookMLTypes.SHOW_REGION_FIELD, LookMLTypes.DRAW_MAP_LABELS_ABOVE_DATA,
            LookMLTypes.MAP_TILE_PROVIDER, LookMLTypes.MAP_POSITION, LookMLTypes.MAP_SCALE_INDICATOR,
            LookMLTypes.MAP_PANNABLE, LookMLTypes.MAP_ZOOMABLE, LookMLTypes.MAP_MARKER_TYPE,
            LookMLTypes.MAP_MARKER_ICON_NAME, LookMLTypes.MAP_MARKER_RADIUS_MODE,
            LookMLTypes.MAP_MARKER_UNITS, LookMLTypes.MAP_MARKER_PROPORTIONAL_SCALE_TYPE,
            LookMLTypes.MAP_MARKER_COLOR_MODE, LookMLTypes.SHOW_LEGEND,
            LookMLTypes.QUANTIZE_MAP_VALUE_COLORS, LookMLTypes.REVERSE_MAP_VALUE_COLORS,
            LookMLTypes.TITLE_TEXT, LookMLTypes.SUBTITLE_TEXT, LookMLTypes.BODY_TEXT,
            LookMLTypes.CUSTOM_COLOR_ENABLED, LookMLTypes.CUSTOM_COLOR, LookMLTypes.FONT_SIZE,
            LookMLTypes.TEXT_COLOR, LookMLTypes.HIDDEN_POINTS_IF_NO, LookMLTypes.TRUNCATE_COLUMN_NAMES,
            LookMLTypes.ROW_TOTAL, LookMLTypes.CATEGORY, LookMLTypes.SHOW_HIDE, LookMLTypes.FIRST_LAST,
            LookMLTypes.NUM_ROWS, LookMLTypes.JS_LIBRARY_URL, LookMLTypes.DEPENDENCIES,
            LookMLTypes.SHOW_LEGEND_PROP, LookMLTypes.LEGEND_POSITION_PROP, LookMLTypes.COLOR_SCHEME,
            LookMLTypes.ANIMATION_DURATION, LookMLTypes.STRIKETHROUGH, LookMLTypes.ITALIC,
            LookMLTypes.CONSTRAINTS, LookMLTypes.MIN, LookMLTypes.MID, LookMLTypes.MAX,
            LookMLTypes.FILTER_EXPRESSION
        )
        
        private val BOOLEAN_VALUES = TokenSet.create(LookMLTypes.YES, LookMLTypes.NO)
        
        private val LOGICAL_OPERATORS = TokenSet.create(LookMLTypes.AND, LookMLTypes.OR)
    }

    override fun getHighlightingLexer(): Lexer = LookMLLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when {
            tokenType in KEYWORDS -> arrayOf(KEYWORD)
            tokenType in PROPERTY_KEYWORDS -> arrayOf(PROPERTY)
            tokenType in BOOLEAN_VALUES -> arrayOf(KEYWORD)
            tokenType in LOGICAL_OPERATORS -> arrayOf(KEYWORD)
            tokenType == LookMLTypes.STRING -> arrayOf(STRING)
            tokenType == LookMLTypes.NUMBER -> arrayOf(NUMBER)
            tokenType == LookMLTypes.COMMENT -> arrayOf(COMMENT)
            tokenType == LookMLTypes.LBRACE || tokenType == LookMLTypes.RBRACE -> arrayOf(BRACES)
            tokenType == LookMLTypes.LBRACKET || tokenType == LookMLTypes.RBRACKET -> arrayOf(BRACKETS)
            tokenType == LookMLTypes.COLON || tokenType == LookMLTypes.SEMICOLON || tokenType == LookMLTypes.COMMA ||
            tokenType == LookMLTypes.EQ || tokenType == LookMLTypes.NE || tokenType == LookMLTypes.LT ||
            tokenType == LookMLTypes.GT || tokenType == LookMLTypes.LE || tokenType == LookMLTypes.GE ||
            tokenType == LookMLTypes.PLUS || tokenType == LookMLTypes.MINUS || tokenType == LookMLTypes.STAR ||
            tokenType == LookMLTypes.SLASH || tokenType == LookMLTypes.PERCENT ||
            tokenType == LookMLTypes.LPAREN || tokenType == LookMLTypes.RPAREN -> arrayOf(OPERATOR)
            tokenType == LookMLTypes.SQL_CONTENT_TOKEN -> arrayOf(SQL)
            tokenType == LookMLTypes.SQL_BLOCK_START || 
            tokenType == LookMLTypes.SQL_ALWAYS_WHERE_START ||
            tokenType == LookMLTypes.SQL_ALWAYS_FILTER_START -> arrayOf(PROPERTY)
            tokenType == TokenType.BAD_CHARACTER -> arrayOf(BAD_CHARACTER)
            else -> arrayOf()
        }
    }
}
