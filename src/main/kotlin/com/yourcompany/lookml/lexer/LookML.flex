package com.yourcompany.lookml.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.yourcompany.lookml.psi.LookMLTypes;
import com.intellij.psi.TokenType;

%%

%{
  public _LookMLLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _LookMLLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

%char
%line
%column

%state SQL_BLOCK

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]

Comment = "#" {InputCharacter}*
BlockCommentStart = "/*"
BlockCommentEnd = "*/"

Identifier = [a-zA-Z_][a-zA-Z0-9_]*
Number = [0-9]+(\.[0-9]+)?
StringLiteral = \"([^\\\"]|\\.)*\"

%%

// Keywords
<YYINITIAL> {
  "view"              { return LookMLTypes.VIEW; }
  "explore"           { return LookMLTypes.EXPLORE; }
  "dashboard"         { return LookMLTypes.DASHBOARD; }
  "lookml_dashboard"  { return LookMLTypes.LOOKML_DASHBOARD; }
  "connection"        { return LookMLTypes.CONNECTION; }
  "include"           { return LookMLTypes.INCLUDE; }
  "from"              { return LookMLTypes.FROM; }
  "persist_with"      { return LookMLTypes.PERSIST_WITH; }
  "week_start_day"    { return LookMLTypes.WEEK_START_DAY; }
  "case_sensitive"    { return LookMLTypes.CASE_SENSITIVE; }
  "datagroup"         { return LookMLTypes.DATAGROUP; }
  "access_grant"      { return LookMLTypes.ACCESS_GRANT; }
  "named_value_format" { return LookMLTypes.NAMED_VALUE_FORMAT; }
  "test"              { return LookMLTypes.TEST; }
  
  "dimension"         { return LookMLTypes.DIMENSION; }
  "dimension_group"   { return LookMLTypes.DIMENSION_GROUP; }
  "measure"           { return LookMLTypes.MEASURE; }
  "filter"            { return LookMLTypes.FILTER; }
  "parameter"         { return LookMLTypes.PARAMETER; }
  
  "join"              { return LookMLTypes.JOIN; }
  "sql_table_name"    { return LookMLTypes.SQL_TABLE_NAME; }
  "sql_on"            { return LookMLTypes.SQL_ON; }
  "sql_where"         { return LookMLTypes.SQL_WHERE; }
  "sql_always_where"  { return LookMLTypes.SQL_ALWAYS_WHERE; }
  "sql_always_filter" { return LookMLTypes.SQL_ALWAYS_FILTER; }
  "sql_always_having" { return LookMLTypes.SQL_ALWAYS_HAVING; }
  "sql_case"          { return LookMLTypes.SQL_CASE; }
  "sql_trigger"       { return LookMLTypes.SQL_TRIGGER; }
  "sql_latitude"      { return LookMLTypes.SQL_LATITUDE; }
  "sql_longitude"     { return LookMLTypes.SQL_LONGITUDE; }
  "sql"               { return LookMLTypes.SQL; }
  
  "name"              { return LookMLTypes.NAME; }
  "type"              { return LookMLTypes.TYPE; }
  "label"             { return LookMLTypes.LABEL; }
  "view_label"        { return LookMLTypes.VIEW_LABEL; }
  "group_label"       { return LookMLTypes.GROUP_LABEL; }
  "description"       { return LookMLTypes.DESCRIPTION; }
  "hidden"            { return LookMLTypes.HIDDEN; }
  "primary_key"       { return LookMLTypes.PRIMARY_KEY; }
  "alias"             { return LookMLTypes.ALIAS; }
  "required_access_grants" { return LookMLTypes.REQUIRED_ACCESS_GRANTS; }
  "suggestions"       { return LookMLTypes.SUGGESTIONS; }
  "relationship"      { return LookMLTypes.RELATIONSHIP; }
  "max_cache_age"     { return LookMLTypes.MAX_CACHE_AGE; }
  "allowed_value"     { return LookMLTypes.ALLOWED_VALUE; }
  "allowed_values"    { return LookMLTypes.ALLOWED_VALUES; }
  "user_attribute"    { return LookMLTypes.USER_ATTRIBUTE; }
  "value_format"      { return LookMLTypes.VALUE_FORMAT; }
  "strict_value_format" { return LookMLTypes.STRICT_VALUE_FORMAT; }
  "view_name"         { return LookMLTypes.VIEW_NAME; }
  "always_filter"     { return LookMLTypes.ALWAYS_FILTER; }
  "conditionally_filter" { return LookMLTypes.CONDITIONALLY_FILTER; }
  "filters"           { return LookMLTypes.FILTERS; }
  "unless"            { return LookMLTypes.UNLESS; }
  "access_filter"     { return LookMLTypes.ACCESS_FILTER; }
  "field"             { return LookMLTypes.FIELD; }
  "fields"            { return LookMLTypes.FIELDS; }
  "assert"            { return LookMLTypes.ASSERT; }
  "expression"        { return LookMLTypes.EXPRESSION; }
  "required_joins"    { return LookMLTypes.REQUIRED_JOINS; }
  "aggregate_table"   { return LookMLTypes.AGGREGATE_TABLE; }
  "query"             { return LookMLTypes.QUERY; }
  "dimensions"        { return LookMLTypes.DIMENSIONS; }
  "measures"          { return LookMLTypes.MEASURES; }
  "timezone"          { return LookMLTypes.TIMEZONE; }
  "materialization"   { return LookMLTypes.MATERIALIZATION; }
  "datagroup_trigger" { return LookMLTypes.DATAGROUP_TRIGGER; }
  "partition_keys"    { return LookMLTypes.PARTITION_KEYS; }
  "cluster_keys"      { return LookMLTypes.CLUSTER_KEYS; }
  "increment_key"     { return LookMLTypes.INCREMENT_KEY; }
  "increment_offset"  { return LookMLTypes.INCREMENT_OFFSET; }
  "link"              { return LookMLTypes.LINK; }
  "action"            { return LookMLTypes.ACTION; }
  "url"               { return LookMLTypes.URL; }
  "icon_url"          { return LookMLTypes.ICON_URL; }
  "form_url"          { return LookMLTypes.FORM_URL; }
  "param"             { return LookMLTypes.PARAM; }
  "form_param"        { return LookMLTypes.FORM_PARAM; }
  "user_attribute_param" { return LookMLTypes.USER_ATTRIBUTE_PARAM; }
  "value"             { return LookMLTypes.VALUE; }
  "default"           { return LookMLTypes.DEFAULT; }
  "required"          { return LookMLTypes.REQUIRED; }
  "html"              { return LookMLTypes.HTML; }
  "case"              { return LookMLTypes.CASE; }
  "when"              { return LookMLTypes.WHEN; }
  "else"              { return LookMLTypes.ELSE; }
  "set"               { return LookMLTypes.SET; }
  "drill_fields"      { return LookMLTypes.DRILL_FIELDS; }
  "tags"              { return LookMLTypes.TAGS; }
  "can_filter"        { return LookMLTypes.CAN_FILTER; }
  "alpha_sort"        { return LookMLTypes.ALPHA_SORT; }
  "tiers"             { return LookMLTypes.TIERS; }
  "style"             { return LookMLTypes.STYLE; }
  "convert_tz"        { return LookMLTypes.CONVERT_TZ; }
  "datatype"          { return LookMLTypes.DATATYPE; }
  "value_format_name" { return LookMLTypes.VALUE_FORMAT_NAME; }
  "precision"         { return LookMLTypes.PRECISION; }
  "group_item_label"  { return LookMLTypes.GROUP_ITEM_LABEL; }
  "timeframes"        { return LookMLTypes.TIMEFRAMES; }
  "allow_fill"        { return LookMLTypes.ALLOW_FILL; }
  "list_field"        { return LookMLTypes.LIST_FIELD; }
  "suggest_dimension" { return LookMLTypes.SUGGEST_DIMENSION; }
  "approximate"       { return LookMLTypes.APPROXIMATE; }
  "approximate_threshold" { return LookMLTypes.APPROXIMATE_THRESHOLD; }
  "required_fields"   { return LookMLTypes.REQUIRED_FIELDS; }
  "elements"          { return LookMLTypes.ELEMENTS; }
  "element"           { return LookMLTypes.ELEMENT; }
  "title"             { return LookMLTypes.TITLE; }
  "model"             { return LookMLTypes.MODEL; }
  "sorts"             { return LookMLTypes.SORTS; }
  "limit"             { return LookMLTypes.LIMIT; }
  "show_single_value_title" { return LookMLTypes.SHOW_SINGLE_VALUE_TITLE; }
  "single_value_title" { return LookMLTypes.SINGLE_VALUE_TITLE; }
  "conditional_formatting" { return LookMLTypes.CONDITIONAL_FORMATTING; }
  "background_color"  { return LookMLTypes.BACKGROUND_COLOR; }
  "font_color"        { return LookMLTypes.FONT_COLOR; }
  "bold"              { return LookMLTypes.BOLD; }
  "derived_table"     { return LookMLTypes.DERIVED_TABLE; }
  "explore_source"    { return LookMLTypes.EXPLORE_SOURCE; }
  "column"            { return LookMLTypes.COLUMN; }
  "persist_for"       { return LookMLTypes.PERSIST_FOR; }
  "indexes"           { return LookMLTypes.INDEXES; }
  "fields_hidden_by_default" { return LookMLTypes.FIELDS_HIDDEN_BY_DEFAULT; }
  "layout"            { return LookMLTypes.LAYOUT; }
  "preferred_viewer"  { return LookMLTypes.PREFERRED_VIEWER; }
  "refresh"           { return LookMLTypes.REFRESH; }
  "ui_config"         { return LookMLTypes.UI_CONFIG; }
  "display"           { return LookMLTypes.DISPLAY; }
  "options"           { return LookMLTypes.OPTIONS; }
  "allow_multiple_values" { return LookMLTypes.ALLOW_MULTIPLE_VALUES; }
  "default_value"     { return LookMLTypes.DEFAULT_VALUE; }
  "greater_than"      { return LookMLTypes.GREATER_THAN; }
  "less_than"         { return LookMLTypes.LESS_THAN; }
  "between"           { return LookMLTypes.BETWEEN; }
  "equal_to"          { return LookMLTypes.EQUAL_TO; }
  "white"             { return LookMLTypes.WHITE; }
  
  // Boolean values
  "yes"               { return LookMLTypes.YES; }
  "no"                { return LookMLTypes.NO; }
  
  // Days of week
  "monday"            { return LookMLTypes.MONDAY; }
  "tuesday"           { return LookMLTypes.TUESDAY; }
  "wednesday"         { return LookMLTypes.WEDNESDAY; }
  "thursday"          { return LookMLTypes.THURSDAY; }
  "friday"            { return LookMLTypes.FRIDAY; }
  "saturday"          { return LookMLTypes.SATURDAY; }
  "sunday"            { return LookMLTypes.SUNDAY; }
  
  // Operators and delimiters
  "{"                 { return LookMLTypes.LBRACE; }
  "}"                 { return LookMLTypes.RBRACE; }
  "["                 { return LookMLTypes.LBRACKET; }
  "]"                 { return LookMLTypes.RBRACKET; }
  ":"                 { return LookMLTypes.COLON; }
  ";"                 { return LookMLTypes.SEMICOLON; }
  ","                 { return LookMLTypes.COMMA; }
  "$"                 { return LookMLTypes.DOLLAR; }
  "."                 { return LookMLTypes.DOT; }
  "="                 { return LookMLTypes.EQ; }
  "!="                { return LookMLTypes.NE; }
  "<"                 { return LookMLTypes.LT; }
  ">"                 { return LookMLTypes.GT; }
  "<="                { return LookMLTypes.LE; }
  ">="                { return LookMLTypes.GE; }
  "+"                 { return LookMLTypes.PLUS; }
  "-"                 { return LookMLTypes.MINUS; }
  "*"                 { return LookMLTypes.STAR; }
  "/"                 { return LookMLTypes.SLASH; }
  "%"                 { return LookMLTypes.PERCENT; }
  "("                 { return LookMLTypes.LPAREN; }
  ")"                 { return LookMLTypes.RPAREN; }
  "AND"               { return LookMLTypes.AND; }
  "OR"                { return LookMLTypes.OR; }
  
  // Literals
  {Number}            { return LookMLTypes.NUMBER; }
  {StringLiteral}     { return LookMLTypes.STRING; }
  {Identifier}        { return LookMLTypes.IDENTIFIER; }
  
  // Comments
  {Comment}           { return LookMLTypes.COMMENT; }
  
  // SQL block handling with newline - for multi-line SQL blocks
  "sql:" {WhiteSpace}* {LineTerminator}  { yybegin(SQL_BLOCK); return LookMLTypes.SQL_BLOCK_START; }
  "sql_on:" {WhiteSpace}* {LineTerminator}  { yybegin(SQL_BLOCK); return LookMLTypes.SQL_BLOCK_START; }
  "sql_where:" {WhiteSpace}* {LineTerminator}  { yybegin(SQL_BLOCK); return LookMLTypes.SQL_BLOCK_START; }
  "sql_trigger:" {WhiteSpace}* {LineTerminator}  { yybegin(SQL_BLOCK); return LookMLTypes.SQL_BLOCK_START; }
  "sql_always_where:" {WhiteSpace}* {LineTerminator}  { yybegin(SQL_BLOCK); return LookMLTypes.SQL_ALWAYS_WHERE_START; }
  "sql_always_filter:" {WhiteSpace}* {LineTerminator}  { yybegin(SQL_BLOCK); return LookMLTypes.SQL_ALWAYS_FILTER_START; }
  "sql_always_having:" {WhiteSpace}* {LineTerminator}  { yybegin(SQL_BLOCK); return LookMLTypes.SQL_ALWAYS_HAVING_START; }
  "html:" {WhiteSpace}* {LineTerminator}  { yybegin(SQL_BLOCK); return LookMLTypes.HTML_BLOCK_START; }
  
  // Whitespace
  {WhiteSpace}+       { return TokenType.WHITE_SPACE; }
}



<SQL_BLOCK> {
  ";;"                    { yybegin(YYINITIAL); return LookMLTypes.SQL_BLOCK_END; }
  [^;]+                   { return LookMLTypes.SQL_CONTENT_TOKEN; }
  ";"                     { return LookMLTypes.SQL_CONTENT_TOKEN; }
}

// Block comments - treat as regular comments
{BlockCommentStart} ~{BlockCommentEnd} { return LookMLTypes.COMMENT; }

// Catch all
[^] { return TokenType.BAD_CHARACTER; }
