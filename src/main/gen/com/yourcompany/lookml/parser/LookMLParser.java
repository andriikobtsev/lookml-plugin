// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.yourcompany.lookml.psi.LookMLTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class LookMLParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, null);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return lookmlFile(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // access_filter_item*
  public static boolean access_filter_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "access_filter_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ACCESS_FILTER_BODY, "<access filter body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!access_filter_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "access_filter_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // field_property | user_attribute_property | COMMENT
  static boolean access_filter_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "access_filter_item")) return false;
    boolean result_;
    result_ = field_property(builder_, level_ + 1);
    if (!result_) result_ = user_attribute_property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // ACCESS_FILTER COLON LBRACE access_filter_body RBRACE
  public static boolean access_filter_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "access_filter_property")) return false;
    if (!nextTokenIs(builder_, ACCESS_FILTER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, ACCESS_FILTER, COLON, LBRACE);
    result_ = result_ && access_filter_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, ACCESS_FILTER_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // access_grant_item*
  public static boolean access_grant_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "access_grant_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ACCESS_GRANT_BODY, "<access grant body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!access_grant_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "access_grant_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // ACCESS_GRANT COLON IDENTIFIER LBRACE access_grant_body RBRACE
  public static boolean access_grant_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "access_grant_definition")) return false;
    if (!nextTokenIs(builder_, ACCESS_GRANT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, ACCESS_GRANT, COLON, IDENTIFIER, LBRACE);
    result_ = result_ && access_grant_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, ACCESS_GRANT_DEFINITION, result_);
    return result_;
  }

  /* ********************************************************** */
  // property | COMMENT
  static boolean access_grant_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "access_grant_item")) return false;
    boolean result_;
    result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // action_item*
  public static boolean action_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "action_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ACTION_BODY, "<action body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!action_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "action_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // url_property
  //   | icon_url_property
  //   | form_url_property
  //   | param_property
  //   | form_param_property
  //   | user_attribute_param_property
  //   | property
  //   | COMMENT
  static boolean action_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "action_item")) return false;
    boolean result_;
    result_ = url_property(builder_, level_ + 1);
    if (!result_) result_ = icon_url_property(builder_, level_ + 1);
    if (!result_) result_ = form_url_property(builder_, level_ + 1);
    if (!result_) result_ = param_property(builder_, level_ + 1);
    if (!result_) result_ = form_param_property(builder_, level_ + 1);
    if (!result_) result_ = user_attribute_param_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // ACTION COLON LBRACE action_body RBRACE
  public static boolean action_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "action_property")) return false;
    if (!nextTokenIs(builder_, ACTION)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, ACTION, COLON, LBRACE);
    result_ = result_ && action_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, ACTION_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // aggregate_table_item*
  public static boolean aggregate_table_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "aggregate_table_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, AGGREGATE_TABLE_BODY, "<aggregate table body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!aggregate_table_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "aggregate_table_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // AGGREGATE_TABLE COLON IDENTIFIER LBRACE aggregate_table_body RBRACE
  public static boolean aggregate_table_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "aggregate_table_definition")) return false;
    if (!nextTokenIs(builder_, AGGREGATE_TABLE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, AGGREGATE_TABLE, COLON, IDENTIFIER, LBRACE);
    result_ = result_ && aggregate_table_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, AGGREGATE_TABLE_DEFINITION, result_);
    return result_;
  }

  /* ********************************************************** */
  // query_property | materialization_property | property | COMMENT
  static boolean aggregate_table_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "aggregate_table_item")) return false;
    boolean result_;
    result_ = query_property(builder_, level_ + 1);
    if (!result_) result_ = materialization_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // ALLOW_MULTIPLE_VALUES COLON boolean_value SEMICOLON?
  public static boolean allow_multiple_values_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "allow_multiple_values_property")) return false;
    if (!nextTokenIs(builder_, ALLOW_MULTIPLE_VALUES)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, ALLOW_MULTIPLE_VALUES, COLON);
    result_ = result_ && boolean_value(builder_, level_ + 1);
    result_ = result_ && allow_multiple_values_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, ALLOW_MULTIPLE_VALUES_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean allow_multiple_values_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "allow_multiple_values_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // allowed_value_item*
  public static boolean allowed_value_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "allowed_value_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ALLOWED_VALUE_BODY, "<allowed value body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!allowed_value_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "allowed_value_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // label_property | value_property | property | COMMENT
  static boolean allowed_value_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "allowed_value_item")) return false;
    boolean result_;
    result_ = label_property(builder_, level_ + 1);
    if (!result_) result_ = value_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // ALLOWED_VALUE COLON LBRACE allowed_value_body RBRACE
  public static boolean allowed_value_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "allowed_value_property")) return false;
    if (!nextTokenIs(builder_, ALLOWED_VALUE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, ALLOWED_VALUE, COLON, LBRACE);
    result_ = result_ && allowed_value_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, ALLOWED_VALUE_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // ALWAYS_FILTER COLON LBRACE filter_body RBRACE
  public static boolean always_filter_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "always_filter_property")) return false;
    if (!nextTokenIs(builder_, ALWAYS_FILTER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, ALWAYS_FILTER, COLON, LBRACE);
    result_ = result_ && filter_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, ALWAYS_FILTER_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // field_pattern | wildcard_identifier | sort_specification | template_expression | qualified_identifier | STRING | NUMBER | IDENTIFIER
  public static boolean array_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_element")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY_ELEMENT, "<array element>");
    result_ = field_pattern(builder_, level_ + 1);
    if (!result_) result_ = wildcard_identifier(builder_, level_ + 1);
    if (!result_) result_ = sort_specification(builder_, level_ + 1);
    if (!result_) result_ = template_expression(builder_, level_ + 1);
    if (!result_) result_ = qualified_identifier(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // array_element (COMMA array_element)*
  public static boolean array_elements(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_elements")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY_ELEMENTS, "<array elements>");
    result_ = array_element(builder_, level_ + 1);
    result_ = result_ && array_elements_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (COMMA array_element)*
  private static boolean array_elements_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_elements_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!array_elements_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "array_elements_1", pos_)) break;
    }
    return true;
  }

  // COMMA array_element
  private static boolean array_elements_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_elements_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && array_element(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // LBRACKET array_elements? RBRACKET
  public static boolean array_value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_value")) return false;
    if (!nextTokenIs(builder_, LBRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && array_value_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, ARRAY_VALUE, result_);
    return result_;
  }

  // array_elements?
  private static boolean array_value_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_value_1")) return false;
    array_elements(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // assert_item*
  public static boolean assert_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assert_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ASSERT_BODY, "<assert body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!assert_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "assert_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // expression_property | property | COMMENT
  static boolean assert_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assert_item")) return false;
    boolean result_;
    result_ = expression_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // ASSERT COLON IDENTIFIER LBRACE assert_body RBRACE
  public static boolean assert_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assert_property")) return false;
    if (!nextTokenIs(builder_, ASSERT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, ASSERT, COLON, IDENTIFIER, LBRACE);
    result_ = result_ && assert_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, ASSERT_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // BACKGROUND_COLOR COLON STRING SEMICOLON?
  public static boolean background_color_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "background_color_property")) return false;
    if (!nextTokenIs(builder_, BACKGROUND_COLOR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, BACKGROUND_COLOR, COLON, STRING);
    result_ = result_ && background_color_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, BACKGROUND_COLOR_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean background_color_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "background_color_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // BOLD COLON boolean_value SEMICOLON?
  public static boolean bold_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bold_property")) return false;
    if (!nextTokenIs(builder_, BOLD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, BOLD, COLON);
    result_ = result_ && boolean_value(builder_, level_ + 1);
    result_ = result_ && bold_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, BOLD_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean bold_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bold_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // YES | NO | TRUE_VALUE | FALSE_VALUE
  public static boolean boolean_value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "boolean_value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BOOLEAN_VALUE, "<boolean value>");
    result_ = consumeToken(builder_, YES);
    if (!result_) result_ = consumeToken(builder_, NO);
    if (!result_) result_ = consumeToken(builder_, TRUE_VALUE);
    if (!result_) result_ = consumeToken(builder_, FALSE_VALUE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // case_item*
  public static boolean case_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CASE_BODY, "<case body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!case_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "case_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // when_clause | else_clause | COMMENT
  static boolean case_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_item")) return false;
    boolean result_;
    result_ = when_clause(builder_, level_ + 1);
    if (!result_) result_ = else_clause(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // CASE COLON LBRACE case_body RBRACE
  public static boolean case_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_property")) return false;
    if (!nextTokenIs(builder_, CASE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, CASE, COLON, LBRACE);
    result_ = result_ && case_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, CASE_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // CASE_SENSITIVE COLON boolean_value SEMICOLON?
  public static boolean case_sensitive_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_sensitive_statement")) return false;
    if (!nextTokenIs(builder_, CASE_SENSITIVE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, CASE_SENSITIVE, COLON);
    result_ = result_ && boolean_value(builder_, level_ + 1);
    result_ = result_ && case_sensitive_statement_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, CASE_SENSITIVE_STATEMENT, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean case_sensitive_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_sensitive_statement_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // CLUSTER_KEYS COLON array_value SEMICOLON?
  public static boolean cluster_keys_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "cluster_keys_property")) return false;
    if (!nextTokenIs(builder_, CLUSTER_KEYS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, CLUSTER_KEYS, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && cluster_keys_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, CLUSTER_KEYS_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean cluster_keys_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "cluster_keys_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // column_item*
  public static boolean column_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "column_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, COLUMN_BODY, "<column body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!column_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "column_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // field_property | property | COMMENT
  static boolean column_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "column_item")) return false;
    boolean result_;
    result_ = field_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // COLUMN COLON IDENTIFIER LBRACE column_body RBRACE
  public static boolean column_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "column_property")) return false;
    if (!nextTokenIs(builder_, COLUMN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, COLUMN, COLON, IDENTIFIER, LBRACE);
    result_ = result_ && column_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, COLUMN_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // conditional_format_item*
  public static boolean conditional_format_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conditional_format_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONDITIONAL_FORMAT_BODY, "<conditional format body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!conditional_format_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "conditional_format_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // type_property | value_property | background_color_property | font_color_property | bold_property | property | COMMENT
  static boolean conditional_format_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conditional_format_item")) return false;
    boolean result_;
    result_ = type_property(builder_, level_ + 1);
    if (!result_) result_ = value_property(builder_, level_ + 1);
    if (!result_) result_ = background_color_property(builder_, level_ + 1);
    if (!result_) result_ = font_color_property(builder_, level_ + 1);
    if (!result_) result_ = bold_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // conditional_format_object (COMMA conditional_format_object)*
  public static boolean conditional_format_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conditional_format_list")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = conditional_format_object(builder_, level_ + 1);
    result_ = result_ && conditional_format_list_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, CONDITIONAL_FORMAT_LIST, result_);
    return result_;
  }

  // (COMMA conditional_format_object)*
  private static boolean conditional_format_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conditional_format_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!conditional_format_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "conditional_format_list_1", pos_)) break;
    }
    return true;
  }

  // COMMA conditional_format_object
  private static boolean conditional_format_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conditional_format_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && conditional_format_object(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // LBRACE conditional_format_body RBRACE
  public static boolean conditional_format_object(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conditional_format_object")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACE);
    result_ = result_ && conditional_format_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, CONDITIONAL_FORMAT_OBJECT, result_);
    return result_;
  }

  /* ********************************************************** */
  // CONDITIONAL_FORMATTING COLON LBRACKET conditional_format_list? RBRACKET
  public static boolean conditional_formatting_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conditional_formatting_property")) return false;
    if (!nextTokenIs(builder_, CONDITIONAL_FORMATTING)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, CONDITIONAL_FORMATTING, COLON, LBRACKET);
    result_ = result_ && conditional_formatting_property_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, CONDITIONAL_FORMATTING_PROPERTY, result_);
    return result_;
  }

  // conditional_format_list?
  private static boolean conditional_formatting_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conditional_formatting_property_3")) return false;
    conditional_format_list(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // conditionally_filter_item*
  public static boolean conditionally_filter_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conditionally_filter_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONDITIONALLY_FILTER_BODY, "<conditionally filter body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!conditionally_filter_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "conditionally_filter_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // filters_property | unless_property | COMMENT
  static boolean conditionally_filter_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conditionally_filter_item")) return false;
    boolean result_;
    result_ = filters_property(builder_, level_ + 1);
    if (!result_) result_ = unless_property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // CONDITIONALLY_FILTER COLON LBRACE conditionally_filter_body RBRACE
  public static boolean conditionally_filter_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conditionally_filter_property")) return false;
    if (!nextTokenIs(builder_, CONDITIONALLY_FILTER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, CONDITIONALLY_FILTER, COLON, LBRACE);
    result_ = result_ && conditionally_filter_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, CONDITIONALLY_FILTER_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // CONNECTION COLON STRING SEMICOLON?
  public static boolean connection_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "connection_statement")) return false;
    if (!nextTokenIs(builder_, CONNECTION)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, CONNECTION, COLON, STRING);
    result_ = result_ && connection_statement_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, CONNECTION_STATEMENT, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean connection_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "connection_statement_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // dashboard_item*
  public static boolean dashboard_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dashboard_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DASHBOARD_BODY, "<dashboard body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!dashboard_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "dashboard_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // (DASHBOARD | LOOKML_DASHBOARD) COLON IDENTIFIER LBRACE dashboard_body RBRACE
  public static boolean dashboard_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dashboard_definition")) return false;
    if (!nextTokenIs(builder_, "<dashboard definition>", DASHBOARD, LOOKML_DASHBOARD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DASHBOARD_DEFINITION, "<dashboard definition>");
    result_ = dashboard_definition_0(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, COLON, IDENTIFIER, LBRACE);
    result_ = result_ && dashboard_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // DASHBOARD | LOOKML_DASHBOARD
  private static boolean dashboard_definition_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dashboard_definition_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, DASHBOARD);
    if (!result_) result_ = consumeToken(builder_, LOOKML_DASHBOARD);
    return result_;
  }

  /* ********************************************************** */
  // dashboard_filter_item*
  public static boolean dashboard_filter_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dashboard_filter_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DASHBOARD_FILTER_BODY, "<dashboard filter body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!dashboard_filter_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "dashboard_filter_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // name_property | title_property | type_property | default_value_property | allow_multiple_values_property | required_property | ui_config_property | property | COMMENT
  static boolean dashboard_filter_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dashboard_filter_item")) return false;
    boolean result_;
    result_ = name_property(builder_, level_ + 1);
    if (!result_) result_ = title_property(builder_, level_ + 1);
    if (!result_) result_ = type_property(builder_, level_ + 1);
    if (!result_) result_ = default_value_property(builder_, level_ + 1);
    if (!result_) result_ = allow_multiple_values_property(builder_, level_ + 1);
    if (!result_) result_ = required_property(builder_, level_ + 1);
    if (!result_) result_ = ui_config_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // dashboard_filter_object (COMMA dashboard_filter_object)*
  public static boolean dashboard_filter_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dashboard_filter_list")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = dashboard_filter_object(builder_, level_ + 1);
    result_ = result_ && dashboard_filter_list_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, DASHBOARD_FILTER_LIST, result_);
    return result_;
  }

  // (COMMA dashboard_filter_object)*
  private static boolean dashboard_filter_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dashboard_filter_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!dashboard_filter_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "dashboard_filter_list_1", pos_)) break;
    }
    return true;
  }

  // COMMA dashboard_filter_object
  private static boolean dashboard_filter_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dashboard_filter_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && dashboard_filter_object(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // LBRACE dashboard_filter_body RBRACE
  public static boolean dashboard_filter_object(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dashboard_filter_object")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACE);
    result_ = result_ && dashboard_filter_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, DASHBOARD_FILTER_OBJECT, result_);
    return result_;
  }

  /* ********************************************************** */
  // FILTERS COLON LBRACKET dashboard_filter_list? RBRACKET
  public static boolean dashboard_filters_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dashboard_filters_property")) return false;
    if (!nextTokenIs(builder_, FILTERS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, FILTERS, COLON, LBRACKET);
    result_ = result_ && dashboard_filters_property_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, DASHBOARD_FILTERS_PROPERTY, result_);
    return result_;
  }

  // dashboard_filter_list?
  private static boolean dashboard_filters_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dashboard_filters_property_3")) return false;
    dashboard_filter_list(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // elements_property | dashboard_filters_property | property | COMMENT
  static boolean dashboard_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dashboard_item")) return false;
    boolean result_;
    result_ = elements_property(builder_, level_ + 1);
    if (!result_) result_ = dashboard_filters_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // datagroup_item*
  public static boolean datagroup_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "datagroup_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DATAGROUP_BODY, "<datagroup body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!datagroup_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "datagroup_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // DATAGROUP COLON IDENTIFIER LBRACE datagroup_body RBRACE
  public static boolean datagroup_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "datagroup_definition")) return false;
    if (!nextTokenIs(builder_, DATAGROUP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DATAGROUP, COLON, IDENTIFIER, LBRACE);
    result_ = result_ && datagroup_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, DATAGROUP_DEFINITION, result_);
    return result_;
  }

  /* ********************************************************** */
  // property | sql_trigger_property | COMMENT
  static boolean datagroup_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "datagroup_item")) return false;
    boolean result_;
    result_ = property(builder_, level_ + 1);
    if (!result_) result_ = sql_trigger_property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // DATAGROUP_TRIGGER COLON IDENTIFIER SEMICOLON?
  public static boolean datagroup_trigger_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "datagroup_trigger_property")) return false;
    if (!nextTokenIs(builder_, DATAGROUP_TRIGGER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DATAGROUP_TRIGGER, COLON, IDENTIFIER);
    result_ = result_ && datagroup_trigger_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, DATAGROUP_TRIGGER_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean datagroup_trigger_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "datagroup_trigger_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // MONDAY | TUESDAY | WEDNESDAY | THURSDAY | FRIDAY | SATURDAY | SUNDAY
  public static boolean day_of_week(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "day_of_week")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DAY_OF_WEEK, "<day of week>");
    result_ = consumeToken(builder_, MONDAY);
    if (!result_) result_ = consumeToken(builder_, TUESDAY);
    if (!result_) result_ = consumeToken(builder_, WEDNESDAY);
    if (!result_) result_ = consumeToken(builder_, THURSDAY);
    if (!result_) result_ = consumeToken(builder_, FRIDAY);
    if (!result_) result_ = consumeToken(builder_, SATURDAY);
    if (!result_) result_ = consumeToken(builder_, SUNDAY);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // DEFAULT COLON STRING SEMICOLON?
  public static boolean default_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_property")) return false;
    if (!nextTokenIs(builder_, DEFAULT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DEFAULT, COLON, STRING);
    result_ = result_ && default_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, DEFAULT_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean default_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // DEFAULT_VALUE COLON (STRING | IDENTIFIER) SEMICOLON?
  public static boolean default_value_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_value_property")) return false;
    if (!nextTokenIs(builder_, DEFAULT_VALUE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DEFAULT_VALUE, COLON);
    result_ = result_ && default_value_property_2(builder_, level_ + 1);
    result_ = result_ && default_value_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, DEFAULT_VALUE_PROPERTY, result_);
    return result_;
  }

  // STRING | IDENTIFIER
  private static boolean default_value_property_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_value_property_2")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    return result_;
  }

  // SEMICOLON?
  private static boolean default_value_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_value_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // derived_table_item*
  public static boolean derived_table_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "derived_table_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DERIVED_TABLE_BODY, "<derived table body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!derived_table_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "derived_table_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // explore_source_property | sql_property | persist_for_property | indexes_property | property | COMMENT
  static boolean derived_table_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "derived_table_item")) return false;
    boolean result_;
    result_ = explore_source_property(builder_, level_ + 1);
    if (!result_) result_ = sql_property(builder_, level_ + 1);
    if (!result_) result_ = persist_for_property(builder_, level_ + 1);
    if (!result_) result_ = indexes_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // DERIVED_TABLE COLON LBRACE derived_table_body RBRACE
  public static boolean derived_table_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "derived_table_property")) return false;
    if (!nextTokenIs(builder_, DERIVED_TABLE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DERIVED_TABLE, COLON, LBRACE);
    result_ = result_ && derived_table_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, DERIVED_TABLE_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // DIMENSION COLON (IDENTIFIER | keyword) LBRACE property_list RBRACE
  public static boolean dimension_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dimension_definition")) return false;
    if (!nextTokenIs(builder_, DIMENSION)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DIMENSION, COLON);
    result_ = result_ && dimension_definition_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LBRACE);
    result_ = result_ && property_list(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, DIMENSION_DEFINITION, result_);
    return result_;
  }

  // IDENTIFIER | keyword
  private static boolean dimension_definition_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dimension_definition_2")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // DIMENSION_GROUP COLON (IDENTIFIER | keyword) LBRACE property_list RBRACE
  public static boolean dimension_group_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dimension_group_definition")) return false;
    if (!nextTokenIs(builder_, DIMENSION_GROUP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DIMENSION_GROUP, COLON);
    result_ = result_ && dimension_group_definition_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LBRACE);
    result_ = result_ && property_list(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, DIMENSION_GROUP_DEFINITION, result_);
    return result_;
  }

  // IDENTIFIER | keyword
  private static boolean dimension_group_definition_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dimension_group_definition_2")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // DIMENSIONS COLON array_value SEMICOLON?
  public static boolean dimensions_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dimensions_property")) return false;
    if (!nextTokenIs(builder_, DIMENSIONS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DIMENSIONS, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && dimensions_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, DIMENSIONS_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean dimensions_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dimensions_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // DISPLAY COLON IDENTIFIER SEMICOLON?
  public static boolean display_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "display_property")) return false;
    if (!nextTokenIs(builder_, DISPLAY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DISPLAY, COLON, IDENTIFIER);
    result_ = result_ && display_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, DISPLAY_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean display_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "display_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // DRILL_FIELDS COLON array_value SEMICOLON?
  public static boolean drill_fields_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "drill_fields_property")) return false;
    if (!nextTokenIs(builder_, DRILL_FIELDS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DRILL_FIELDS, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && drill_fields_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, DRILL_FIELDS_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean drill_fields_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "drill_fields_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // element_item*
  public static boolean element_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "element_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ELEMENT_BODY, "<element body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!element_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "element_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // conditional_formatting_property | property | COMMENT
  static boolean element_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "element_item")) return false;
    boolean result_;
    result_ = conditional_formatting_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // element_object (COMMA element_object)*
  public static boolean element_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "element_list")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = element_object(builder_, level_ + 1);
    result_ = result_ && element_list_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, ELEMENT_LIST, result_);
    return result_;
  }

  // (COMMA element_object)*
  private static boolean element_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "element_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!element_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "element_list_1", pos_)) break;
    }
    return true;
  }

  // COMMA element_object
  private static boolean element_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "element_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && element_object(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // LBRACE element_body RBRACE
  public static boolean element_object(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "element_object")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACE);
    result_ = result_ && element_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, ELEMENT_OBJECT, result_);
    return result_;
  }

  /* ********************************************************** */
  // ELEMENTS COLON LBRACKET element_list? RBRACKET
  public static boolean elements_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "elements_property")) return false;
    if (!nextTokenIs(builder_, ELEMENTS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, ELEMENTS, COLON, LBRACKET);
    result_ = result_ && elements_property_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, ELEMENTS_PROPERTY, result_);
    return result_;
  }

  // element_list?
  private static boolean elements_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "elements_property_3")) return false;
    element_list(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // ELSE COLON STRING SEMICOLON?
  public static boolean else_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "else_clause")) return false;
    if (!nextTokenIs(builder_, ELSE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, ELSE, COLON, STRING);
    result_ = result_ && else_clause_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, ELSE_CLAUSE, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean else_clause_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "else_clause_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // explore_item*
  public static boolean explore_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "explore_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXPLORE_BODY, "<explore body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!explore_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "explore_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // EXPLORE COLON IDENTIFIER LBRACE explore_body RBRACE
  public static boolean explore_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "explore_definition")) return false;
    if (!nextTokenIs(builder_, EXPLORE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, EXPLORE, COLON, IDENTIFIER, LBRACE);
    result_ = result_ && explore_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, EXPLORE_DEFINITION, result_);
    return result_;
  }

  /* ********************************************************** */
  // from_property
  //   | view_name_property
  //   | join_definition
  //   | sql_table_name_property
  //   | sql_always_where_property
  //   | sql_always_filter_property
  //   | sql_always_having_property
  //   | always_filter_property
  //   | conditionally_filter_property
  //   | access_filter_property
  //   | required_access_grants_property
  //   | aggregate_table_definition
  //   | property
  //   | COMMENT
  static boolean explore_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "explore_item")) return false;
    boolean result_;
    result_ = from_property(builder_, level_ + 1);
    if (!result_) result_ = view_name_property(builder_, level_ + 1);
    if (!result_) result_ = join_definition(builder_, level_ + 1);
    if (!result_) result_ = sql_table_name_property(builder_, level_ + 1);
    if (!result_) result_ = sql_always_where_property(builder_, level_ + 1);
    if (!result_) result_ = sql_always_filter_property(builder_, level_ + 1);
    if (!result_) result_ = sql_always_having_property(builder_, level_ + 1);
    if (!result_) result_ = always_filter_property(builder_, level_ + 1);
    if (!result_) result_ = conditionally_filter_property(builder_, level_ + 1);
    if (!result_) result_ = access_filter_property(builder_, level_ + 1);
    if (!result_) result_ = required_access_grants_property(builder_, level_ + 1);
    if (!result_) result_ = aggregate_table_definition(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // explore_source_item*
  public static boolean explore_source_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "explore_source_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXPLORE_SOURCE_BODY, "<explore source body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!explore_source_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "explore_source_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // column_property | filters_property | property | COMMENT
  static boolean explore_source_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "explore_source_item")) return false;
    boolean result_;
    result_ = column_property(builder_, level_ + 1);
    if (!result_) result_ = filters_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // EXPLORE_SOURCE COLON IDENTIFIER LBRACE explore_source_body RBRACE
  public static boolean explore_source_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "explore_source_property")) return false;
    if (!nextTokenIs(builder_, EXPLORE_SOURCE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, EXPLORE_SOURCE, COLON, IDENTIFIER, LBRACE);
    result_ = result_ && explore_source_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, EXPLORE_SOURCE_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // expression_element+
  public static boolean expression_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXPRESSION_CONTENT, "<expression content>");
    result_ = expression_element(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!expression_element(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "expression_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // template_expression
  //   | IDENTIFIER
  //   | STRING
  //   | NUMBER
  //   | DOT
  //   | COMMA
  //   | EQ | NE | LT | GT | LE | GE
  //   | PLUS | MINUS | STAR | SLASH | PERCENT
  //   | AND | OR
  //   | LPAREN | RPAREN
  static boolean expression_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_element")) return false;
    boolean result_;
    result_ = template_expression(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, EQ);
    if (!result_) result_ = consumeToken(builder_, NE);
    if (!result_) result_ = consumeToken(builder_, LT);
    if (!result_) result_ = consumeToken(builder_, GT);
    if (!result_) result_ = consumeToken(builder_, LE);
    if (!result_) result_ = consumeToken(builder_, GE);
    if (!result_) result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    if (!result_) result_ = consumeToken(builder_, STAR);
    if (!result_) result_ = consumeToken(builder_, SLASH);
    if (!result_) result_ = consumeToken(builder_, PERCENT);
    if (!result_) result_ = consumeToken(builder_, AND);
    if (!result_) result_ = consumeToken(builder_, OR);
    if (!result_) result_ = consumeToken(builder_, LPAREN);
    if (!result_) result_ = consumeToken(builder_, RPAREN);
    return result_;
  }

  /* ********************************************************** */
  // EXPRESSION COLON expression_content SEMICOLON SEMICOLON
  public static boolean expression_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_property")) return false;
    if (!nextTokenIs(builder_, EXPRESSION)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, EXPRESSION, COLON);
    result_ = result_ && expression_content(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, SEMICOLON, SEMICOLON);
    exit_section_(builder_, marker_, EXPRESSION_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // qualified_identifier STAR
  public static boolean field_pattern(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "field_pattern")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FIELD_PATTERN, "<field pattern>");
    result_ = qualified_identifier(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, STAR);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // FIELD COLON qualified_identifier SEMICOLON?
  public static boolean field_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "field_property")) return false;
    if (!nextTokenIs(builder_, FIELD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, FIELD, COLON);
    result_ = result_ && qualified_identifier(builder_, level_ + 1);
    result_ = result_ && field_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, FIELD_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean field_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "field_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // FIELDS COLON array_value SEMICOLON?
  public static boolean fields_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fields_property")) return false;
    if (!nextTokenIs(builder_, FIELDS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, FIELDS, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && fields_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, FIELDS_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean fields_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fields_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // !<<eof>> item_
  static boolean file_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_item")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = file_item_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && item_(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, LookMLParser::file_item_recover);
    return result_ || pinned_;
  }

  // !<<eof>>
  private static boolean file_item_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_item_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !eof(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !(VIEW | EXPLORE | DASHBOARD | LOOKML_DASHBOARD | TEST | DATAGROUP | ACCESS_GRANT | NAMED_VALUE_FORMAT | CONNECTION | PERSIST_WITH | WEEK_START_DAY | CASE_SENSITIVE | LABEL | INCLUDE | COMMENT | YAML_LIST_ITEM | YAML_DOCUMENT_START | <<eof>>)
  static boolean file_item_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_item_recover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !file_item_recover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // VIEW | EXPLORE | DASHBOARD | LOOKML_DASHBOARD | TEST | DATAGROUP | ACCESS_GRANT | NAMED_VALUE_FORMAT | CONNECTION | PERSIST_WITH | WEEK_START_DAY | CASE_SENSITIVE | LABEL | INCLUDE | COMMENT | YAML_LIST_ITEM | YAML_DOCUMENT_START | <<eof>>
  private static boolean file_item_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_item_recover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, VIEW);
    if (!result_) result_ = consumeToken(builder_, EXPLORE);
    if (!result_) result_ = consumeToken(builder_, DASHBOARD);
    if (!result_) result_ = consumeToken(builder_, LOOKML_DASHBOARD);
    if (!result_) result_ = consumeToken(builder_, TEST);
    if (!result_) result_ = consumeToken(builder_, DATAGROUP);
    if (!result_) result_ = consumeToken(builder_, ACCESS_GRANT);
    if (!result_) result_ = consumeToken(builder_, NAMED_VALUE_FORMAT);
    if (!result_) result_ = consumeToken(builder_, CONNECTION);
    if (!result_) result_ = consumeToken(builder_, PERSIST_WITH);
    if (!result_) result_ = consumeToken(builder_, WEEK_START_DAY);
    if (!result_) result_ = consumeToken(builder_, CASE_SENSITIVE);
    if (!result_) result_ = consumeToken(builder_, LABEL);
    if (!result_) result_ = consumeToken(builder_, INCLUDE);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    if (!result_) result_ = consumeToken(builder_, YAML_LIST_ITEM);
    if (!result_) result_ = consumeToken(builder_, YAML_DOCUMENT_START);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // filter_item*
  public static boolean filter_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filter_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FILTER_BODY, "<filter body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!filter_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "filter_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // FILTER COLON (IDENTIFIER | keyword) LBRACE property_list RBRACE
  public static boolean filter_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filter_definition")) return false;
    if (!nextTokenIs(builder_, FILTER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, FILTER, COLON);
    result_ = result_ && filter_definition_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LBRACE);
    result_ = result_ && property_list(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, FILTER_DEFINITION, result_);
    return result_;
  }

  // IDENTIFIER | keyword
  private static boolean filter_definition_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filter_definition_2")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // filters_property | COMMENT
  static boolean filter_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filter_item")) return false;
    if (!nextTokenIs(builder_, "", COMMENT, FILTERS)) return false;
    boolean result_;
    result_ = filters_property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // filter_spec (COMMA filter_spec)*
  public static boolean filter_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filter_list")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FILTER_LIST, "<filter list>");
    result_ = filter_spec(builder_, level_ + 1);
    result_ = result_ && filter_list_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (COMMA filter_spec)*
  private static boolean filter_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filter_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!filter_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "filter_list_1", pos_)) break;
    }
    return true;
  }

  // COMMA filter_spec
  private static boolean filter_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filter_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && filter_spec(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // (qualified_identifier | IDENTIFIER) COLON STRING
  public static boolean filter_spec(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filter_spec")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FILTER_SPEC, "<filter spec>");
    result_ = filter_spec_0(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, COLON, STRING);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // qualified_identifier | IDENTIFIER
  private static boolean filter_spec_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filter_spec_0")) return false;
    boolean result_;
    result_ = qualified_identifier(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    return result_;
  }

  /* ********************************************************** */
  // LBRACKET filter_list RBRACKET
  public static boolean filters_array(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filters_array")) return false;
    if (!nextTokenIs(builder_, LBRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && filter_list(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, FILTERS_ARRAY, result_);
    return result_;
  }

  /* ********************************************************** */
  // LBRACE filters_block_body RBRACE
  public static boolean filters_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filters_block")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACE);
    result_ = result_ && filters_block_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, FILTERS_BLOCK, result_);
    return result_;
  }

  /* ********************************************************** */
  // filters_block_item*
  public static boolean filters_block_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filters_block_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FILTERS_BLOCK_BODY, "<filters block body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!filters_block_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "filters_block_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // field_property | value_property | COMMENT
  static boolean filters_block_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filters_block_item")) return false;
    boolean result_;
    result_ = field_property(builder_, level_ + 1);
    if (!result_) result_ = value_property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // FILTERS COLON (filters_array | filters_block) SEMICOLON?
  public static boolean filters_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filters_property")) return false;
    if (!nextTokenIs(builder_, FILTERS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, FILTERS, COLON);
    result_ = result_ && filters_property_2(builder_, level_ + 1);
    result_ = result_ && filters_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, FILTERS_PROPERTY, result_);
    return result_;
  }

  // filters_array | filters_block
  private static boolean filters_property_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filters_property_2")) return false;
    boolean result_;
    result_ = filters_array(builder_, level_ + 1);
    if (!result_) result_ = filters_block(builder_, level_ + 1);
    return result_;
  }

  // SEMICOLON?
  private static boolean filters_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filters_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // FONT_COLOR COLON (STRING | IDENTIFIER | keyword) SEMICOLON?
  public static boolean font_color_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "font_color_property")) return false;
    if (!nextTokenIs(builder_, FONT_COLOR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, FONT_COLOR, COLON);
    result_ = result_ && font_color_property_2(builder_, level_ + 1);
    result_ = result_ && font_color_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, FONT_COLOR_PROPERTY, result_);
    return result_;
  }

  // STRING | IDENTIFIER | keyword
  private static boolean font_color_property_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "font_color_property_2")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    return result_;
  }

  // SEMICOLON?
  private static boolean font_color_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "font_color_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // form_param_item*
  public static boolean form_param_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "form_param_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FORM_PARAM_BODY, "<form param body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!form_param_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "form_param_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // name_property
  //   | type_property
  //   | label_property
  //   | required_property
  //   | default_property
  //   | property
  //   | COMMENT
  static boolean form_param_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "form_param_item")) return false;
    boolean result_;
    result_ = name_property(builder_, level_ + 1);
    if (!result_) result_ = type_property(builder_, level_ + 1);
    if (!result_) result_ = label_property(builder_, level_ + 1);
    if (!result_) result_ = required_property(builder_, level_ + 1);
    if (!result_) result_ = default_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // FORM_PARAM COLON LBRACE form_param_body RBRACE
  public static boolean form_param_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "form_param_property")) return false;
    if (!nextTokenIs(builder_, FORM_PARAM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, FORM_PARAM, COLON, LBRACE);
    result_ = result_ && form_param_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, FORM_PARAM_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // FORM_URL COLON STRING SEMICOLON?
  public static boolean form_url_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "form_url_property")) return false;
    if (!nextTokenIs(builder_, FORM_URL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, FORM_URL, COLON, STRING);
    result_ = result_ && form_url_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, FORM_URL_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean form_url_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "form_url_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // FROM COLON IDENTIFIER SEMICOLON?
  public static boolean from_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "from_property")) return false;
    if (!nextTokenIs(builder_, FROM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, FROM, COLON, IDENTIFIER);
    result_ = result_ && from_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, FROM_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean from_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "from_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // html_block_element* SQL_BLOCK_END
  public static boolean html_block_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "html_block_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HTML_BLOCK_CONTENT, "<html block content>");
    result_ = html_block_content_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SQL_BLOCK_END);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // html_block_element*
  private static boolean html_block_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "html_block_content_0")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!html_block_element(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "html_block_content_0", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // COMMENT | html_expression
  static boolean html_block_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "html_block_element")) return false;
    boolean result_;
    result_ = consumeToken(builder_, COMMENT);
    if (!result_) result_ = html_expression(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // template_expression
  //   | STRING
  //   | NUMBER
  //   | IDENTIFIER
  //   | sql_operator
  //   | DOT
  //   | LPAREN html_expression* RPAREN
  //   | LBRACE html_expression* RBRACE
  //   | SQL_CONTENT_TOKEN
  public static boolean html_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "html_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HTML_EXPRESSION, "<html expression>");
    result_ = template_expression(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = sql_operator(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = html_expression_6(builder_, level_ + 1);
    if (!result_) result_ = html_expression_7(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, SQL_CONTENT_TOKEN);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // LPAREN html_expression* RPAREN
  private static boolean html_expression_6(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "html_expression_6")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAREN);
    result_ = result_ && html_expression_6_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // html_expression*
  private static boolean html_expression_6_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "html_expression_6_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!html_expression(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "html_expression_6_1", pos_)) break;
    }
    return true;
  }

  // LBRACE html_expression* RBRACE
  private static boolean html_expression_7(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "html_expression_7")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACE);
    result_ = result_ && html_expression_7_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // html_expression*
  private static boolean html_expression_7_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "html_expression_7_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!html_expression(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "html_expression_7_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // HTML_BLOCK_START html_block_content
  public static boolean html_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "html_property")) return false;
    if (!nextTokenIs(builder_, HTML_BLOCK_START)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HTML_BLOCK_START);
    result_ = result_ && html_block_content(builder_, level_ + 1);
    exit_section_(builder_, marker_, HTML_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER (MINUS IDENTIFIER)+
  public static boolean hyphenated_identifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hyphenated_identifier")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    result_ = result_ && hyphenated_identifier_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, HYPHENATED_IDENTIFIER, result_);
    return result_;
  }

  // (MINUS IDENTIFIER)+
  private static boolean hyphenated_identifier_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hyphenated_identifier_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = hyphenated_identifier_1_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!hyphenated_identifier_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "hyphenated_identifier_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // MINUS IDENTIFIER
  private static boolean hyphenated_identifier_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hyphenated_identifier_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, MINUS, IDENTIFIER);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // ICON_URL COLON STRING SEMICOLON?
  public static boolean icon_url_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "icon_url_property")) return false;
    if (!nextTokenIs(builder_, ICON_URL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, ICON_URL, COLON, STRING);
    result_ = result_ && icon_url_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, ICON_URL_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean icon_url_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "icon_url_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // qualified_identifier (COMMA qualified_identifier)*
  public static boolean identifier_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "identifier_list")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IDENTIFIER_LIST, "<identifier list>");
    result_ = qualified_identifier(builder_, level_ + 1);
    result_ = result_ && identifier_list_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (COMMA qualified_identifier)*
  private static boolean identifier_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "identifier_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!identifier_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "identifier_list_1", pos_)) break;
    }
    return true;
  }

  // COMMA qualified_identifier
  private static boolean identifier_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "identifier_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && qualified_identifier(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER | keyword | YES | NO | TRUE_VALUE | FALSE_VALUE
  public static boolean identifier_part(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "identifier_part")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IDENTIFIER_PART, "<identifier part>");
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, YES);
    if (!result_) result_ = consumeToken(builder_, NO);
    if (!result_) result_ = consumeToken(builder_, TRUE_VALUE);
    if (!result_) result_ = consumeToken(builder_, FALSE_VALUE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // INCLUDE COLON STRING SEMICOLON?
  public static boolean include_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "include_statement")) return false;
    if (!nextTokenIs(builder_, INCLUDE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, INCLUDE, COLON, STRING);
    result_ = result_ && include_statement_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, INCLUDE_STATEMENT, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean include_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "include_statement_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // INCREMENT_KEY COLON STRING SEMICOLON?
  public static boolean increment_key_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "increment_key_property")) return false;
    if (!nextTokenIs(builder_, INCREMENT_KEY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, INCREMENT_KEY, COLON, STRING);
    result_ = result_ && increment_key_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, INCREMENT_KEY_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean increment_key_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "increment_key_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // INCREMENT_OFFSET COLON NUMBER SEMICOLON?
  public static boolean increment_offset_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "increment_offset_property")) return false;
    if (!nextTokenIs(builder_, INCREMENT_OFFSET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, INCREMENT_OFFSET, COLON, NUMBER);
    result_ = result_ && increment_offset_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, INCREMENT_OFFSET_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean increment_offset_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "increment_offset_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // INDEXES COLON array_value SEMICOLON?
  public static boolean indexes_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "indexes_property")) return false;
    if (!nextTokenIs(builder_, INDEXES)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, INDEXES, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && indexes_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, INDEXES_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean indexes_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "indexes_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // connection_statement 
  //   | persist_with_statement
  //   | week_start_day_statement
  //   | case_sensitive_statement
  //   | label_statement
  //   | datagroup_definition
  //   | access_grant_definition
  //   | named_value_format_definition
  //   | view_definition 
  //   | explore_definition 
  //   | dashboard_definition 
  //   | yaml_dashboard_document
  //   | test_definition
  //   | include_statement 
  //   | COMMENT
  static boolean item_(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "item_")) return false;
    boolean result_;
    result_ = connection_statement(builder_, level_ + 1);
    if (!result_) result_ = persist_with_statement(builder_, level_ + 1);
    if (!result_) result_ = week_start_day_statement(builder_, level_ + 1);
    if (!result_) result_ = case_sensitive_statement(builder_, level_ + 1);
    if (!result_) result_ = label_statement(builder_, level_ + 1);
    if (!result_) result_ = datagroup_definition(builder_, level_ + 1);
    if (!result_) result_ = access_grant_definition(builder_, level_ + 1);
    if (!result_) result_ = named_value_format_definition(builder_, level_ + 1);
    if (!result_) result_ = view_definition(builder_, level_ + 1);
    if (!result_) result_ = explore_definition(builder_, level_ + 1);
    if (!result_) result_ = dashboard_definition(builder_, level_ + 1);
    if (!result_) result_ = yaml_dashboard_document(builder_, level_ + 1);
    if (!result_) result_ = test_definition(builder_, level_ + 1);
    if (!result_) result_ = include_statement(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // join_item*
  public static boolean join_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "join_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, JOIN_BODY, "<join body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!join_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "join_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // JOIN COLON IDENTIFIER LBRACE join_body RBRACE
  public static boolean join_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "join_definition")) return false;
    if (!nextTokenIs(builder_, JOIN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, JOIN, COLON, IDENTIFIER, LBRACE);
    result_ = result_ && join_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, JOIN_DEFINITION, result_);
    return result_;
  }

  /* ********************************************************** */
  // from_property
  //     | sql_on_property 
  //     | sql_always_where_property
  //     | fields_property
  //     | required_joins_property
  //     | property
  //     | COMMENT
  static boolean join_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "join_item")) return false;
    boolean result_;
    result_ = from_property(builder_, level_ + 1);
    if (!result_) result_ = sql_on_property(builder_, level_ + 1);
    if (!result_) result_ = sql_always_where_property(builder_, level_ + 1);
    if (!result_) result_ = fields_property(builder_, level_ + 1);
    if (!result_) result_ = required_joins_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // NAME | TYPE | LABEL | VIEW_LABEL | GROUP_LABEL | DESCRIPTION | HIDDEN | PRIMARY_KEY | ALIAS
  //   | REQUIRED_ACCESS_GRANTS | SUGGESTIONS | RELATIONSHIP
  //   | SQL | SQL_TABLE_NAME | SQL_ON | SQL_WHERE | SQL_ALWAYS_WHERE | SQL_ALWAYS_FILTER | SQL_ALWAYS_HAVING | SQL_TRIGGER | SQL_LATITUDE | SQL_LONGITUDE
  //   | VIEW | EXPLORE | DASHBOARD | LOOKML_DASHBOARD 
  //   | DIMENSION | DIMENSION_GROUP | MEASURE | FILTER | PARAMETER | JOIN
  //   | FROM | CONNECTION | INCLUDE | PERSIST_WITH | WEEK_START_DAY | CASE_SENSITIVE
  //   | DATAGROUP | ACCESS_GRANT | NAMED_VALUE_FORMAT
  //   | MAX_CACHE_AGE | ALLOWED_VALUE | ALLOWED_VALUES | USER_ATTRIBUTE | VALUE_FORMAT | STRICT_VALUE_FORMAT
  //   | VIEW_NAME | ALWAYS_FILTER | CONDITIONALLY_FILTER | FILTERS | UNLESS | ACCESS_FILTER | FIELD | FIELDS | REQUIRED_JOINS
  //   | AGGREGATE_TABLE | QUERY | DIMENSIONS | MEASURES | TIMEZONE | MATERIALIZATION
  //   | DATAGROUP_TRIGGER | PARTITION_KEYS | CLUSTER_KEYS | INCREMENT_KEY | INCREMENT_OFFSET
  //   | DERIVED_TABLE | EXPLORE_SOURCE | COLUMN | PERSIST_FOR | INDEXES | FIELDS_HIDDEN_BY_DEFAULT
  //   | LINK | ACTION | URL | ICON_URL | FORM_URL | PARAM | FORM_PARAM | USER_ATTRIBUTE_PARAM
  //   | VALUE | DEFAULT | REQUIRED | HTML | CASE | WHEN | ELSE | SET | DRILL_FIELDS
  //   | TAGS | CAN_FILTER | ALPHA_SORT | TIERS | STYLE | CONVERT_TZ | DATATYPE | VALUE_FORMAT_NAME | PRECISION
  //   | GROUP_ITEM_LABEL | TIMEFRAMES | ALLOW_FILL | LIST_FIELD | SUGGEST_DIMENSION | APPROXIMATE | LISTENS_TO_FILTERS
  //   | APPROXIMATE_THRESHOLD | REQUIRED_FIELDS | ELEMENTS | ELEMENT | TITLE | MODEL | SORTS | LIMIT
  //   | SHOW_SINGLE_VALUE_TITLE | SINGLE_VALUE_TITLE | CONDITIONAL_FORMATTING | BACKGROUND_COLOR
  //   | FONT_COLOR | BOLD | LAYOUT | PREFERRED_VIEWER | REFRESH | UI_CONFIG | DISPLAY | OPTIONS
  //   | ALLOW_MULTIPLE_VALUES | DEFAULT_VALUE | GREATER_THAN | LESS_THAN | BETWEEN | EQUAL_TO | WHITE
  //   | PREFERRED_SLUG | COLUMN_LIMIT | STACKING | LEGEND_POSITION | X_AXIS_GRIDLINES | Y_AXIS_GRIDLINES
  //   | INTERPOLATION | SERIES_TYPES | SERIES_COLORS | COLOR_APPLICATION | INNER_RADIUS | TRELLIS | ORDERING
  //   | SHOW_DROPOFF | TABLE_THEME | HEADER_TEXT_ALIGNMENT | COMPARISON | COMPARISON_TYPE | COMPARISON_REVERSE_COLORS
  //   | SHOW_COMPARISON | SHOW_COMPARISON_LABEL | COMPARISON_LABEL | ENABLE_CONDITIONAL_FORMATTING
  //   | CONDITIONAL_FORMATTING_INCLUDE_TOTALS | CONDITIONAL_FORMATTING_INCLUDE_NULLS | VIS_CONFIG | HIDDEN_FIELDS
  //   | SERIES_LABELS | PIVOTS | DYNAMIC_FIELDS | QUERY_TIMEZONE | LISTEN | SHOW_VALUE_LABELS | LABEL_DENSITY
  //   | SHOW_VIEW_NAMES | Y_AXIS_COMBINED | SHOW_Y_AXIS_LABELS | SHOW_Y_AXIS_TICKS | Y_AXIS_TICK_DENSITY
  //   | Y_AXIS_TICK_DENSITY_CUSTOM | SHOW_X_AXIS_LABEL | SHOW_X_AXIS_TICKS | X_AXIS_SCALE | Y_AXIS_SCALE_MODE
  //   | X_AXIS_REVERSED | Y_AXIS_REVERSED | PLOT_SIZE_BY_FIELD | SHOW_NULL_POINTS | VALUE_LABELS | LABEL_TYPE
  //   | START_ANGLE | END_ANGLE | COLLECTION_ID | PALETTE_ID | STEPS | LIMIT_DISPLAYED_ROWS | LIMIT_DISPLAYED_ROWS_VALUES
  //   | POINT_STYLE | SHOW_NULL_LABELS | SHOW_TOTALS_LABELS | SHOW_SILHOUETTE | TOTALS_COLOR | SHOW_ROW_NUMBERS
  //   | TRANSPOSE | TRUNCATE_TEXT | HIDE_TOTALS | HIDE_ROW_TOTALS | SIZE_TO_FIT | HEADER_FONT_SIZE | ROWS_FONT_SIZE
  //   | MAP_PLOT_MODE | HEATMAP_GRIDLINES | HEATMAP_GRIDLINES_EMPTY | HEATMAP_OPACITY | SHOW_REGION_FIELD
  //   | DRAW_MAP_LABELS_ABOVE_DATA | MAP_TILE_PROVIDER | MAP_POSITION | MAP_SCALE_INDICATOR | MAP_PANNABLE
  //   | MAP_ZOOMABLE | MAP_MARKER_TYPE | MAP_MARKER_ICON_NAME | MAP_MARKER_RADIUS_MODE | MAP_MARKER_UNITS
  //   | MAP_MARKER_PROPORTIONAL_SCALE_TYPE | MAP_MARKER_COLOR_MODE | SHOW_LEGEND | QUANTIZE_MAP_VALUE_COLORS
  //   | REVERSE_MAP_VALUE_COLORS | TITLE_TEXT | SUBTITLE_TEXT | BODY_TEXT | CUSTOM_COLOR_ENABLED | CUSTOM_COLOR
  //   | FONT_SIZE | TEXT_COLOR | VALUE_FORMAT_NAME_PROPERTY | HIDDEN_POINTS_IF_NO | TRUNCATE_COLUMN_NAMES
  //   | ROW_TOTAL | CATEGORY | SHOW_HIDE | FIRST_LAST | NUM_ROWS | JS_LIBRARY_URL | DEPENDENCIES
  //   | SHOW_LEGEND_PROP | LEGEND_POSITION_PROP | COLOR_SCHEME | ANIMATION_DURATION | STRIKETHROUGH | ITALIC
  //   | CONSTRAINTS | MIN | MID | MAX | FILTER_EXPRESSION
  //   | ROW_PROPERTY | COL_PROPERTY | WIDTH_PROPERTY | HEIGHT_PROPERTY | FILL_FIELDS | TRUE_VALUE | FALSE_VALUE
  //   | X_AXIS_LABEL | X_AXIS_ZOOM | Y_AXIS_ZOOM | DEFAULTS_VERSION | SHOW_TOTALS | SHOW_ROW_TOTALS
  //   | SERIES_CELL_VISUALIZATIONS | HIDE_LEGEND | SWAP_AXES | SHOW_SQL_QUERY_MENU_OPTIONS
  //   | FIELD_FILTER | LABEL_VALUE_FORMAT | IS_ACTIVE | EXPRESSION | TABLE_CALCULATION | KIND_HINT | TYPE_HINT
  static boolean keyword(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "keyword")) return false;
    boolean result_;
    result_ = consumeToken(builder_, NAME);
    if (!result_) result_ = consumeToken(builder_, TYPE);
    if (!result_) result_ = consumeToken(builder_, LABEL);
    if (!result_) result_ = consumeToken(builder_, VIEW_LABEL);
    if (!result_) result_ = consumeToken(builder_, GROUP_LABEL);
    if (!result_) result_ = consumeToken(builder_, DESCRIPTION);
    if (!result_) result_ = consumeToken(builder_, HIDDEN);
    if (!result_) result_ = consumeToken(builder_, PRIMARY_KEY);
    if (!result_) result_ = consumeToken(builder_, ALIAS);
    if (!result_) result_ = consumeToken(builder_, REQUIRED_ACCESS_GRANTS);
    if (!result_) result_ = consumeToken(builder_, SUGGESTIONS);
    if (!result_) result_ = consumeToken(builder_, RELATIONSHIP);
    if (!result_) result_ = consumeToken(builder_, SQL);
    if (!result_) result_ = consumeToken(builder_, SQL_TABLE_NAME);
    if (!result_) result_ = consumeToken(builder_, SQL_ON);
    if (!result_) result_ = consumeToken(builder_, SQL_WHERE);
    if (!result_) result_ = consumeToken(builder_, SQL_ALWAYS_WHERE);
    if (!result_) result_ = consumeToken(builder_, SQL_ALWAYS_FILTER);
    if (!result_) result_ = consumeToken(builder_, SQL_ALWAYS_HAVING);
    if (!result_) result_ = consumeToken(builder_, SQL_TRIGGER);
    if (!result_) result_ = consumeToken(builder_, SQL_LATITUDE);
    if (!result_) result_ = consumeToken(builder_, SQL_LONGITUDE);
    if (!result_) result_ = consumeToken(builder_, VIEW);
    if (!result_) result_ = consumeToken(builder_, EXPLORE);
    if (!result_) result_ = consumeToken(builder_, DASHBOARD);
    if (!result_) result_ = consumeToken(builder_, LOOKML_DASHBOARD);
    if (!result_) result_ = consumeToken(builder_, DIMENSION);
    if (!result_) result_ = consumeToken(builder_, DIMENSION_GROUP);
    if (!result_) result_ = consumeToken(builder_, MEASURE);
    if (!result_) result_ = consumeToken(builder_, FILTER);
    if (!result_) result_ = consumeToken(builder_, PARAMETER);
    if (!result_) result_ = consumeToken(builder_, JOIN);
    if (!result_) result_ = consumeToken(builder_, FROM);
    if (!result_) result_ = consumeToken(builder_, CONNECTION);
    if (!result_) result_ = consumeToken(builder_, INCLUDE);
    if (!result_) result_ = consumeToken(builder_, PERSIST_WITH);
    if (!result_) result_ = consumeToken(builder_, WEEK_START_DAY);
    if (!result_) result_ = consumeToken(builder_, CASE_SENSITIVE);
    if (!result_) result_ = consumeToken(builder_, DATAGROUP);
    if (!result_) result_ = consumeToken(builder_, ACCESS_GRANT);
    if (!result_) result_ = consumeToken(builder_, NAMED_VALUE_FORMAT);
    if (!result_) result_ = consumeToken(builder_, MAX_CACHE_AGE);
    if (!result_) result_ = consumeToken(builder_, ALLOWED_VALUE);
    if (!result_) result_ = consumeToken(builder_, ALLOWED_VALUES);
    if (!result_) result_ = consumeToken(builder_, USER_ATTRIBUTE);
    if (!result_) result_ = consumeToken(builder_, VALUE_FORMAT);
    if (!result_) result_ = consumeToken(builder_, STRICT_VALUE_FORMAT);
    if (!result_) result_ = consumeToken(builder_, VIEW_NAME);
    if (!result_) result_ = consumeToken(builder_, ALWAYS_FILTER);
    if (!result_) result_ = consumeToken(builder_, CONDITIONALLY_FILTER);
    if (!result_) result_ = consumeToken(builder_, FILTERS);
    if (!result_) result_ = consumeToken(builder_, UNLESS);
    if (!result_) result_ = consumeToken(builder_, ACCESS_FILTER);
    if (!result_) result_ = consumeToken(builder_, FIELD);
    if (!result_) result_ = consumeToken(builder_, FIELDS);
    if (!result_) result_ = consumeToken(builder_, REQUIRED_JOINS);
    if (!result_) result_ = consumeToken(builder_, AGGREGATE_TABLE);
    if (!result_) result_ = consumeToken(builder_, QUERY);
    if (!result_) result_ = consumeToken(builder_, DIMENSIONS);
    if (!result_) result_ = consumeToken(builder_, MEASURES);
    if (!result_) result_ = consumeToken(builder_, TIMEZONE);
    if (!result_) result_ = consumeToken(builder_, MATERIALIZATION);
    if (!result_) result_ = consumeToken(builder_, DATAGROUP_TRIGGER);
    if (!result_) result_ = consumeToken(builder_, PARTITION_KEYS);
    if (!result_) result_ = consumeToken(builder_, CLUSTER_KEYS);
    if (!result_) result_ = consumeToken(builder_, INCREMENT_KEY);
    if (!result_) result_ = consumeToken(builder_, INCREMENT_OFFSET);
    if (!result_) result_ = consumeToken(builder_, DERIVED_TABLE);
    if (!result_) result_ = consumeToken(builder_, EXPLORE_SOURCE);
    if (!result_) result_ = consumeToken(builder_, COLUMN);
    if (!result_) result_ = consumeToken(builder_, PERSIST_FOR);
    if (!result_) result_ = consumeToken(builder_, INDEXES);
    if (!result_) result_ = consumeToken(builder_, FIELDS_HIDDEN_BY_DEFAULT);
    if (!result_) result_ = consumeToken(builder_, LINK);
    if (!result_) result_ = consumeToken(builder_, ACTION);
    if (!result_) result_ = consumeToken(builder_, URL);
    if (!result_) result_ = consumeToken(builder_, ICON_URL);
    if (!result_) result_ = consumeToken(builder_, FORM_URL);
    if (!result_) result_ = consumeToken(builder_, PARAM);
    if (!result_) result_ = consumeToken(builder_, FORM_PARAM);
    if (!result_) result_ = consumeToken(builder_, USER_ATTRIBUTE_PARAM);
    if (!result_) result_ = consumeToken(builder_, VALUE);
    if (!result_) result_ = consumeToken(builder_, DEFAULT);
    if (!result_) result_ = consumeToken(builder_, REQUIRED);
    if (!result_) result_ = consumeToken(builder_, HTML);
    if (!result_) result_ = consumeToken(builder_, CASE);
    if (!result_) result_ = consumeToken(builder_, WHEN);
    if (!result_) result_ = consumeToken(builder_, ELSE);
    if (!result_) result_ = consumeToken(builder_, SET);
    if (!result_) result_ = consumeToken(builder_, DRILL_FIELDS);
    if (!result_) result_ = consumeToken(builder_, TAGS);
    if (!result_) result_ = consumeToken(builder_, CAN_FILTER);
    if (!result_) result_ = consumeToken(builder_, ALPHA_SORT);
    if (!result_) result_ = consumeToken(builder_, TIERS);
    if (!result_) result_ = consumeToken(builder_, STYLE);
    if (!result_) result_ = consumeToken(builder_, CONVERT_TZ);
    if (!result_) result_ = consumeToken(builder_, DATATYPE);
    if (!result_) result_ = consumeToken(builder_, VALUE_FORMAT_NAME);
    if (!result_) result_ = consumeToken(builder_, PRECISION);
    if (!result_) result_ = consumeToken(builder_, GROUP_ITEM_LABEL);
    if (!result_) result_ = consumeToken(builder_, TIMEFRAMES);
    if (!result_) result_ = consumeToken(builder_, ALLOW_FILL);
    if (!result_) result_ = consumeToken(builder_, LIST_FIELD);
    if (!result_) result_ = consumeToken(builder_, SUGGEST_DIMENSION);
    if (!result_) result_ = consumeToken(builder_, APPROXIMATE);
    if (!result_) result_ = consumeToken(builder_, LISTENS_TO_FILTERS);
    if (!result_) result_ = consumeToken(builder_, APPROXIMATE_THRESHOLD);
    if (!result_) result_ = consumeToken(builder_, REQUIRED_FIELDS);
    if (!result_) result_ = consumeToken(builder_, ELEMENTS);
    if (!result_) result_ = consumeToken(builder_, ELEMENT);
    if (!result_) result_ = consumeToken(builder_, TITLE);
    if (!result_) result_ = consumeToken(builder_, MODEL);
    if (!result_) result_ = consumeToken(builder_, SORTS);
    if (!result_) result_ = consumeToken(builder_, LIMIT);
    if (!result_) result_ = consumeToken(builder_, SHOW_SINGLE_VALUE_TITLE);
    if (!result_) result_ = consumeToken(builder_, SINGLE_VALUE_TITLE);
    if (!result_) result_ = consumeToken(builder_, CONDITIONAL_FORMATTING);
    if (!result_) result_ = consumeToken(builder_, BACKGROUND_COLOR);
    if (!result_) result_ = consumeToken(builder_, FONT_COLOR);
    if (!result_) result_ = consumeToken(builder_, BOLD);
    if (!result_) result_ = consumeToken(builder_, LAYOUT);
    if (!result_) result_ = consumeToken(builder_, PREFERRED_VIEWER);
    if (!result_) result_ = consumeToken(builder_, REFRESH);
    if (!result_) result_ = consumeToken(builder_, UI_CONFIG);
    if (!result_) result_ = consumeToken(builder_, DISPLAY);
    if (!result_) result_ = consumeToken(builder_, OPTIONS);
    if (!result_) result_ = consumeToken(builder_, ALLOW_MULTIPLE_VALUES);
    if (!result_) result_ = consumeToken(builder_, DEFAULT_VALUE);
    if (!result_) result_ = consumeToken(builder_, GREATER_THAN);
    if (!result_) result_ = consumeToken(builder_, LESS_THAN);
    if (!result_) result_ = consumeToken(builder_, BETWEEN);
    if (!result_) result_ = consumeToken(builder_, EQUAL_TO);
    if (!result_) result_ = consumeToken(builder_, WHITE);
    if (!result_) result_ = consumeToken(builder_, PREFERRED_SLUG);
    if (!result_) result_ = consumeToken(builder_, COLUMN_LIMIT);
    if (!result_) result_ = consumeToken(builder_, STACKING);
    if (!result_) result_ = consumeToken(builder_, LEGEND_POSITION);
    if (!result_) result_ = consumeToken(builder_, X_AXIS_GRIDLINES);
    if (!result_) result_ = consumeToken(builder_, Y_AXIS_GRIDLINES);
    if (!result_) result_ = consumeToken(builder_, INTERPOLATION);
    if (!result_) result_ = consumeToken(builder_, SERIES_TYPES);
    if (!result_) result_ = consumeToken(builder_, SERIES_COLORS);
    if (!result_) result_ = consumeToken(builder_, COLOR_APPLICATION);
    if (!result_) result_ = consumeToken(builder_, INNER_RADIUS);
    if (!result_) result_ = consumeToken(builder_, TRELLIS);
    if (!result_) result_ = consumeToken(builder_, ORDERING);
    if (!result_) result_ = consumeToken(builder_, SHOW_DROPOFF);
    if (!result_) result_ = consumeToken(builder_, TABLE_THEME);
    if (!result_) result_ = consumeToken(builder_, HEADER_TEXT_ALIGNMENT);
    if (!result_) result_ = consumeToken(builder_, COMPARISON);
    if (!result_) result_ = consumeToken(builder_, COMPARISON_TYPE);
    if (!result_) result_ = consumeToken(builder_, COMPARISON_REVERSE_COLORS);
    if (!result_) result_ = consumeToken(builder_, SHOW_COMPARISON);
    if (!result_) result_ = consumeToken(builder_, SHOW_COMPARISON_LABEL);
    if (!result_) result_ = consumeToken(builder_, COMPARISON_LABEL);
    if (!result_) result_ = consumeToken(builder_, ENABLE_CONDITIONAL_FORMATTING);
    if (!result_) result_ = consumeToken(builder_, CONDITIONAL_FORMATTING_INCLUDE_TOTALS);
    if (!result_) result_ = consumeToken(builder_, CONDITIONAL_FORMATTING_INCLUDE_NULLS);
    if (!result_) result_ = consumeToken(builder_, VIS_CONFIG);
    if (!result_) result_ = consumeToken(builder_, HIDDEN_FIELDS);
    if (!result_) result_ = consumeToken(builder_, SERIES_LABELS);
    if (!result_) result_ = consumeToken(builder_, PIVOTS);
    if (!result_) result_ = consumeToken(builder_, DYNAMIC_FIELDS);
    if (!result_) result_ = consumeToken(builder_, QUERY_TIMEZONE);
    if (!result_) result_ = consumeToken(builder_, LISTEN);
    if (!result_) result_ = consumeToken(builder_, SHOW_VALUE_LABELS);
    if (!result_) result_ = consumeToken(builder_, LABEL_DENSITY);
    if (!result_) result_ = consumeToken(builder_, SHOW_VIEW_NAMES);
    if (!result_) result_ = consumeToken(builder_, Y_AXIS_COMBINED);
    if (!result_) result_ = consumeToken(builder_, SHOW_Y_AXIS_LABELS);
    if (!result_) result_ = consumeToken(builder_, SHOW_Y_AXIS_TICKS);
    if (!result_) result_ = consumeToken(builder_, Y_AXIS_TICK_DENSITY);
    if (!result_) result_ = consumeToken(builder_, Y_AXIS_TICK_DENSITY_CUSTOM);
    if (!result_) result_ = consumeToken(builder_, SHOW_X_AXIS_LABEL);
    if (!result_) result_ = consumeToken(builder_, SHOW_X_AXIS_TICKS);
    if (!result_) result_ = consumeToken(builder_, X_AXIS_SCALE);
    if (!result_) result_ = consumeToken(builder_, Y_AXIS_SCALE_MODE);
    if (!result_) result_ = consumeToken(builder_, X_AXIS_REVERSED);
    if (!result_) result_ = consumeToken(builder_, Y_AXIS_REVERSED);
    if (!result_) result_ = consumeToken(builder_, PLOT_SIZE_BY_FIELD);
    if (!result_) result_ = consumeToken(builder_, SHOW_NULL_POINTS);
    if (!result_) result_ = consumeToken(builder_, VALUE_LABELS);
    if (!result_) result_ = consumeToken(builder_, LABEL_TYPE);
    if (!result_) result_ = consumeToken(builder_, START_ANGLE);
    if (!result_) result_ = consumeToken(builder_, END_ANGLE);
    if (!result_) result_ = consumeToken(builder_, COLLECTION_ID);
    if (!result_) result_ = consumeToken(builder_, PALETTE_ID);
    if (!result_) result_ = consumeToken(builder_, STEPS);
    if (!result_) result_ = consumeToken(builder_, LIMIT_DISPLAYED_ROWS);
    if (!result_) result_ = consumeToken(builder_, LIMIT_DISPLAYED_ROWS_VALUES);
    if (!result_) result_ = consumeToken(builder_, POINT_STYLE);
    if (!result_) result_ = consumeToken(builder_, SHOW_NULL_LABELS);
    if (!result_) result_ = consumeToken(builder_, SHOW_TOTALS_LABELS);
    if (!result_) result_ = consumeToken(builder_, SHOW_SILHOUETTE);
    if (!result_) result_ = consumeToken(builder_, TOTALS_COLOR);
    if (!result_) result_ = consumeToken(builder_, SHOW_ROW_NUMBERS);
    if (!result_) result_ = consumeToken(builder_, TRANSPOSE);
    if (!result_) result_ = consumeToken(builder_, TRUNCATE_TEXT);
    if (!result_) result_ = consumeToken(builder_, HIDE_TOTALS);
    if (!result_) result_ = consumeToken(builder_, HIDE_ROW_TOTALS);
    if (!result_) result_ = consumeToken(builder_, SIZE_TO_FIT);
    if (!result_) result_ = consumeToken(builder_, HEADER_FONT_SIZE);
    if (!result_) result_ = consumeToken(builder_, ROWS_FONT_SIZE);
    if (!result_) result_ = consumeToken(builder_, MAP_PLOT_MODE);
    if (!result_) result_ = consumeToken(builder_, HEATMAP_GRIDLINES);
    if (!result_) result_ = consumeToken(builder_, HEATMAP_GRIDLINES_EMPTY);
    if (!result_) result_ = consumeToken(builder_, HEATMAP_OPACITY);
    if (!result_) result_ = consumeToken(builder_, SHOW_REGION_FIELD);
    if (!result_) result_ = consumeToken(builder_, DRAW_MAP_LABELS_ABOVE_DATA);
    if (!result_) result_ = consumeToken(builder_, MAP_TILE_PROVIDER);
    if (!result_) result_ = consumeToken(builder_, MAP_POSITION);
    if (!result_) result_ = consumeToken(builder_, MAP_SCALE_INDICATOR);
    if (!result_) result_ = consumeToken(builder_, MAP_PANNABLE);
    if (!result_) result_ = consumeToken(builder_, MAP_ZOOMABLE);
    if (!result_) result_ = consumeToken(builder_, MAP_MARKER_TYPE);
    if (!result_) result_ = consumeToken(builder_, MAP_MARKER_ICON_NAME);
    if (!result_) result_ = consumeToken(builder_, MAP_MARKER_RADIUS_MODE);
    if (!result_) result_ = consumeToken(builder_, MAP_MARKER_UNITS);
    if (!result_) result_ = consumeToken(builder_, MAP_MARKER_PROPORTIONAL_SCALE_TYPE);
    if (!result_) result_ = consumeToken(builder_, MAP_MARKER_COLOR_MODE);
    if (!result_) result_ = consumeToken(builder_, SHOW_LEGEND);
    if (!result_) result_ = consumeToken(builder_, QUANTIZE_MAP_VALUE_COLORS);
    if (!result_) result_ = consumeToken(builder_, REVERSE_MAP_VALUE_COLORS);
    if (!result_) result_ = consumeToken(builder_, TITLE_TEXT);
    if (!result_) result_ = consumeToken(builder_, SUBTITLE_TEXT);
    if (!result_) result_ = consumeToken(builder_, BODY_TEXT);
    if (!result_) result_ = consumeToken(builder_, CUSTOM_COLOR_ENABLED);
    if (!result_) result_ = consumeToken(builder_, CUSTOM_COLOR);
    if (!result_) result_ = consumeToken(builder_, FONT_SIZE);
    if (!result_) result_ = consumeToken(builder_, TEXT_COLOR);
    if (!result_) result_ = consumeToken(builder_, VALUE_FORMAT_NAME_PROPERTY);
    if (!result_) result_ = consumeToken(builder_, HIDDEN_POINTS_IF_NO);
    if (!result_) result_ = consumeToken(builder_, TRUNCATE_COLUMN_NAMES);
    if (!result_) result_ = consumeToken(builder_, ROW_TOTAL);
    if (!result_) result_ = consumeToken(builder_, CATEGORY);
    if (!result_) result_ = consumeToken(builder_, SHOW_HIDE);
    if (!result_) result_ = consumeToken(builder_, FIRST_LAST);
    if (!result_) result_ = consumeToken(builder_, NUM_ROWS);
    if (!result_) result_ = consumeToken(builder_, JS_LIBRARY_URL);
    if (!result_) result_ = consumeToken(builder_, DEPENDENCIES);
    if (!result_) result_ = consumeToken(builder_, SHOW_LEGEND_PROP);
    if (!result_) result_ = consumeToken(builder_, LEGEND_POSITION_PROP);
    if (!result_) result_ = consumeToken(builder_, COLOR_SCHEME);
    if (!result_) result_ = consumeToken(builder_, ANIMATION_DURATION);
    if (!result_) result_ = consumeToken(builder_, STRIKETHROUGH);
    if (!result_) result_ = consumeToken(builder_, ITALIC);
    if (!result_) result_ = consumeToken(builder_, CONSTRAINTS);
    if (!result_) result_ = consumeToken(builder_, MIN);
    if (!result_) result_ = consumeToken(builder_, MID);
    if (!result_) result_ = consumeToken(builder_, MAX);
    if (!result_) result_ = consumeToken(builder_, FILTER_EXPRESSION);
    if (!result_) result_ = consumeToken(builder_, ROW_PROPERTY);
    if (!result_) result_ = consumeToken(builder_, COL_PROPERTY);
    if (!result_) result_ = consumeToken(builder_, WIDTH_PROPERTY);
    if (!result_) result_ = consumeToken(builder_, HEIGHT_PROPERTY);
    if (!result_) result_ = consumeToken(builder_, FILL_FIELDS);
    if (!result_) result_ = consumeToken(builder_, TRUE_VALUE);
    if (!result_) result_ = consumeToken(builder_, FALSE_VALUE);
    if (!result_) result_ = consumeToken(builder_, X_AXIS_LABEL);
    if (!result_) result_ = consumeToken(builder_, X_AXIS_ZOOM);
    if (!result_) result_ = consumeToken(builder_, Y_AXIS_ZOOM);
    if (!result_) result_ = consumeToken(builder_, DEFAULTS_VERSION);
    if (!result_) result_ = consumeToken(builder_, SHOW_TOTALS);
    if (!result_) result_ = consumeToken(builder_, SHOW_ROW_TOTALS);
    if (!result_) result_ = consumeToken(builder_, SERIES_CELL_VISUALIZATIONS);
    if (!result_) result_ = consumeToken(builder_, HIDE_LEGEND);
    if (!result_) result_ = consumeToken(builder_, SWAP_AXES);
    if (!result_) result_ = consumeToken(builder_, SHOW_SQL_QUERY_MENU_OPTIONS);
    if (!result_) result_ = consumeToken(builder_, FIELD_FILTER);
    if (!result_) result_ = consumeToken(builder_, LABEL_VALUE_FORMAT);
    if (!result_) result_ = consumeToken(builder_, IS_ACTIVE);
    if (!result_) result_ = consumeToken(builder_, EXPRESSION);
    if (!result_) result_ = consumeToken(builder_, TABLE_CALCULATION);
    if (!result_) result_ = consumeToken(builder_, KIND_HINT);
    if (!result_) result_ = consumeToken(builder_, TYPE_HINT);
    return result_;
  }

  /* ********************************************************** */
  // LABEL COLON STRING SEMICOLON?
  public static boolean label_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_property")) return false;
    if (!nextTokenIs(builder_, LABEL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, LABEL, COLON, STRING);
    result_ = result_ && label_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, LABEL_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean label_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // LABEL COLON STRING SEMICOLON?
  public static boolean label_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_statement")) return false;
    if (!nextTokenIs(builder_, LABEL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, LABEL, COLON, STRING);
    result_ = result_ && label_statement_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, LABEL_STATEMENT, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean label_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_statement_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // link_item*
  public static boolean link_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LINK_BODY, "<link body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!link_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "link_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // url_property | icon_url_property | property | COMMENT
  static boolean link_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_item")) return false;
    boolean result_;
    result_ = url_property(builder_, level_ + 1);
    if (!result_) result_ = icon_url_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // LINK COLON LBRACE link_body RBRACE
  public static boolean link_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_property")) return false;
    if (!nextTokenIs(builder_, LINK)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, LINK, COLON, LBRACE);
    result_ = result_ && link_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, LINK_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // file_item*
  static boolean lookmlFile(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "lookmlFile")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!file_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "lookmlFile", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // materialization_item*
  public static boolean materialization_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "materialization_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MATERIALIZATION_BODY, "<materialization body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!materialization_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "materialization_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // datagroup_trigger_property
  //   | partition_keys_property
  //   | cluster_keys_property
  //   | increment_key_property
  //   | increment_offset_property
  //   | property
  //   | COMMENT
  static boolean materialization_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "materialization_item")) return false;
    boolean result_;
    result_ = datagroup_trigger_property(builder_, level_ + 1);
    if (!result_) result_ = partition_keys_property(builder_, level_ + 1);
    if (!result_) result_ = cluster_keys_property(builder_, level_ + 1);
    if (!result_) result_ = increment_key_property(builder_, level_ + 1);
    if (!result_) result_ = increment_offset_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // MATERIALIZATION COLON LBRACE materialization_body RBRACE
  public static boolean materialization_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "materialization_property")) return false;
    if (!nextTokenIs(builder_, MATERIALIZATION)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, MATERIALIZATION, COLON, LBRACE);
    result_ = result_ && materialization_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, MATERIALIZATION_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // MEASURE COLON (IDENTIFIER | keyword) LBRACE property_list RBRACE
  public static boolean measure_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "measure_definition")) return false;
    if (!nextTokenIs(builder_, MEASURE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, MEASURE, COLON);
    result_ = result_ && measure_definition_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LBRACE);
    result_ = result_ && property_list(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, MEASURE_DEFINITION, result_);
    return result_;
  }

  // IDENTIFIER | keyword
  private static boolean measure_definition_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "measure_definition_2")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // MEASURES COLON array_value SEMICOLON?
  public static boolean measures_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "measures_property")) return false;
    if (!nextTokenIs(builder_, MEASURES)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, MEASURES, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && measures_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, MEASURES_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean measures_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "measures_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // NAME COLON (STRING | IDENTIFIER) SEMICOLON?
  public static boolean name_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "name_property")) return false;
    if (!nextTokenIs(builder_, NAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, NAME, COLON);
    result_ = result_ && name_property_2(builder_, level_ + 1);
    result_ = result_ && name_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, NAME_PROPERTY, result_);
    return result_;
  }

  // STRING | IDENTIFIER
  private static boolean name_property_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "name_property_2")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    return result_;
  }

  // SEMICOLON?
  private static boolean name_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "name_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // named_value_format_item*
  public static boolean named_value_format_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_value_format_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NAMED_VALUE_FORMAT_BODY, "<named value format body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!named_value_format_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "named_value_format_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // NAMED_VALUE_FORMAT COLON IDENTIFIER LBRACE named_value_format_body RBRACE
  public static boolean named_value_format_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_value_format_definition")) return false;
    if (!nextTokenIs(builder_, NAMED_VALUE_FORMAT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, NAMED_VALUE_FORMAT, COLON, IDENTIFIER, LBRACE);
    result_ = result_ && named_value_format_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, NAMED_VALUE_FORMAT_DEFINITION, result_);
    return result_;
  }

  /* ********************************************************** */
  // property | COMMENT
  static boolean named_value_format_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_value_format_item")) return false;
    boolean result_;
    result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // !<<eof>>
  static boolean not_eof(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "not_eof")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !eof(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // OPTIONS COLON array_value SEMICOLON?
  public static boolean options_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "options_property")) return false;
    if (!nextTokenIs(builder_, OPTIONS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, OPTIONS, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && options_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, OPTIONS_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean options_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "options_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // param_item*
  public static boolean param_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "param_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PARAM_BODY, "<param body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!param_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "param_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // name_property | value_property | property | COMMENT
  static boolean param_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "param_item")) return false;
    boolean result_;
    result_ = name_property(builder_, level_ + 1);
    if (!result_) result_ = value_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // PARAM COLON LBRACE param_body RBRACE
  public static boolean param_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "param_property")) return false;
    if (!nextTokenIs(builder_, PARAM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, PARAM, COLON, LBRACE);
    result_ = result_ && param_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, PARAM_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // PARAMETER COLON (IDENTIFIER | keyword) LBRACE property_list RBRACE
  public static boolean parameter_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameter_definition")) return false;
    if (!nextTokenIs(builder_, PARAMETER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, PARAMETER, COLON);
    result_ = result_ && parameter_definition_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LBRACE);
    result_ = result_ && property_list(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, PARAMETER_DEFINITION, result_);
    return result_;
  }

  // IDENTIFIER | keyword
  private static boolean parameter_definition_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameter_definition_2")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // PARTITION_KEYS COLON array_value SEMICOLON?
  public static boolean partition_keys_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "partition_keys_property")) return false;
    if (!nextTokenIs(builder_, PARTITION_KEYS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, PARTITION_KEYS, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && partition_keys_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, PARTITION_KEYS_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean partition_keys_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "partition_keys_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // PERSIST_FOR COLON STRING SEMICOLON?
  public static boolean persist_for_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "persist_for_property")) return false;
    if (!nextTokenIs(builder_, PERSIST_FOR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, PERSIST_FOR, COLON, STRING);
    result_ = result_ && persist_for_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, PERSIST_FOR_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean persist_for_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "persist_for_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // PERSIST_WITH COLON IDENTIFIER SEMICOLON?
  public static boolean persist_with_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "persist_with_statement")) return false;
    if (!nextTokenIs(builder_, PERSIST_WITH)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, PERSIST_WITH, COLON, IDENTIFIER);
    result_ = result_ && persist_with_statement_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, PERSIST_WITH_STATEMENT, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean persist_with_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "persist_with_statement_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // property_name COLON property_value (SEMICOLON | SEMICOLON SEMICOLON)?
  public static boolean property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PROPERTY, "<property>");
    result_ = property_name(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && property_value(builder_, level_ + 1);
    result_ = result_ && property_3(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (SEMICOLON | SEMICOLON SEMICOLON)?
  private static boolean property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_3")) return false;
    property_3_0(builder_, level_ + 1);
    return true;
  }

  // SEMICOLON | SEMICOLON SEMICOLON
  private static boolean property_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = parseTokens(builder_, 0, SEMICOLON, SEMICOLON);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // link_property | action_property | case_property | filters_property | allowed_value_property | drill_fields_property | required_access_grants_property | suggestions_property | tags_property | timeframes_property | tiers_property | sql_property | sql_latitude_property | sql_longitude_property | sql_on_property | sql_always_where_property | html_property | property | COMMENT
  static boolean property_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_item")) return false;
    boolean result_;
    result_ = link_property(builder_, level_ + 1);
    if (!result_) result_ = action_property(builder_, level_ + 1);
    if (!result_) result_ = case_property(builder_, level_ + 1);
    if (!result_) result_ = filters_property(builder_, level_ + 1);
    if (!result_) result_ = allowed_value_property(builder_, level_ + 1);
    if (!result_) result_ = drill_fields_property(builder_, level_ + 1);
    if (!result_) result_ = required_access_grants_property(builder_, level_ + 1);
    if (!result_) result_ = suggestions_property(builder_, level_ + 1);
    if (!result_) result_ = tags_property(builder_, level_ + 1);
    if (!result_) result_ = timeframes_property(builder_, level_ + 1);
    if (!result_) result_ = tiers_property(builder_, level_ + 1);
    if (!result_) result_ = sql_property(builder_, level_ + 1);
    if (!result_) result_ = sql_latitude_property(builder_, level_ + 1);
    if (!result_) result_ = sql_longitude_property(builder_, level_ + 1);
    if (!result_) result_ = sql_on_property(builder_, level_ + 1);
    if (!result_) result_ = sql_always_where_property(builder_, level_ + 1);
    if (!result_) result_ = html_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // property_item*
  public static boolean property_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_list")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PROPERTY_LIST, "<property list>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!property_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "property_list", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // (IDENTIFIER | keyword) (PIPE (IDENTIFIER | keyword) (DOT (IDENTIFIER | keyword))*)?
  public static boolean property_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_name")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PROPERTY_NAME, "<property name>");
    result_ = property_name_0(builder_, level_ + 1);
    result_ = result_ && property_name_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // IDENTIFIER | keyword
  private static boolean property_name_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_name_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    return result_;
  }

  // (PIPE (IDENTIFIER | keyword) (DOT (IDENTIFIER | keyword))*)?
  private static boolean property_name_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_name_1")) return false;
    property_name_1_0(builder_, level_ + 1);
    return true;
  }

  // PIPE (IDENTIFIER | keyword) (DOT (IDENTIFIER | keyword))*
  private static boolean property_name_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_name_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, PIPE);
    result_ = result_ && property_name_1_0_1(builder_, level_ + 1);
    result_ = result_ && property_name_1_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // IDENTIFIER | keyword
  private static boolean property_name_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_name_1_0_1")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    return result_;
  }

  // (DOT (IDENTIFIER | keyword))*
  private static boolean property_name_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_name_1_0_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!property_name_1_0_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "property_name_1_0_2", pos_)) break;
    }
    return true;
  }

  // DOT (IDENTIFIER | keyword)
  private static boolean property_name_1_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_name_1_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && property_name_1_0_2_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // IDENTIFIER | keyword
  private static boolean property_name_1_0_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_name_1_0_2_0_1")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // STRING 
  //   | NUMBER 
  //   | IDENTIFIER 
  //   | keyword                    // Add keyword support
  //   | hyphenated_identifier
  //   | boolean_value
  //   | TRUE_VALUE
  //   | FALSE_VALUE
  //   | array_value 
  //   | template_expression DOT IDENTIFIER
  //   | template_expression
  //   | property_value_expression
  //   | LBRACE property_list RBRACE
  public static boolean property_value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PROPERTY_VALUE, "<property value>");
    result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    if (!result_) result_ = hyphenated_identifier(builder_, level_ + 1);
    if (!result_) result_ = boolean_value(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, TRUE_VALUE);
    if (!result_) result_ = consumeToken(builder_, FALSE_VALUE);
    if (!result_) result_ = array_value(builder_, level_ + 1);
    if (!result_) result_ = property_value_9(builder_, level_ + 1);
    if (!result_) result_ = template_expression(builder_, level_ + 1);
    if (!result_) result_ = property_value_expression(builder_, level_ + 1);
    if (!result_) result_ = property_value_12(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // template_expression DOT IDENTIFIER
  private static boolean property_value_9(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_value_9")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = template_expression(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, DOT, IDENTIFIER);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // LBRACE property_list RBRACE
  private static boolean property_value_12(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_value_12")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACE);
    result_ = result_ && property_list(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER (DOT IDENTIFIER)*
  public static boolean property_value_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_value_expression")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    result_ = result_ && property_value_expression_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, PROPERTY_VALUE_EXPRESSION, result_);
    return result_;
  }

  // (DOT IDENTIFIER)*
  private static boolean property_value_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_value_expression_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!property_value_expression_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "property_value_expression_1", pos_)) break;
    }
    return true;
  }

  // DOT IDENTIFIER
  private static boolean property_value_expression_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_value_expression_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DOT, IDENTIFIER);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // identifier_part (DOT identifier_part)*
  public static boolean qualified_identifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualified_identifier")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, QUALIFIED_IDENTIFIER, "<qualified identifier>");
    result_ = identifier_part(builder_, level_ + 1);
    result_ = result_ && qualified_identifier_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (DOT identifier_part)*
  private static boolean qualified_identifier_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualified_identifier_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!qualified_identifier_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "qualified_identifier_1", pos_)) break;
    }
    return true;
  }

  // DOT identifier_part
  private static boolean qualified_identifier_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qualified_identifier_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && identifier_part(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // query_item*
  public static boolean query_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "query_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, QUERY_BODY, "<query body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!query_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "query_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // dimensions_property | measures_property | timezone_property | property | COMMENT
  static boolean query_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "query_item")) return false;
    boolean result_;
    result_ = dimensions_property(builder_, level_ + 1);
    if (!result_) result_ = measures_property(builder_, level_ + 1);
    if (!result_) result_ = timezone_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // QUERY COLON LBRACE query_body RBRACE
  public static boolean query_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "query_property")) return false;
    if (!nextTokenIs(builder_, QUERY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, QUERY, COLON, LBRACE);
    result_ = result_ && query_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, QUERY_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // REQUIRED_ACCESS_GRANTS COLON array_value SEMICOLON?
  public static boolean required_access_grants_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "required_access_grants_property")) return false;
    if (!nextTokenIs(builder_, REQUIRED_ACCESS_GRANTS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, REQUIRED_ACCESS_GRANTS, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && required_access_grants_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, REQUIRED_ACCESS_GRANTS_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean required_access_grants_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "required_access_grants_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // REQUIRED_JOINS COLON array_value SEMICOLON?
  public static boolean required_joins_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "required_joins_property")) return false;
    if (!nextTokenIs(builder_, REQUIRED_JOINS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, REQUIRED_JOINS, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && required_joins_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, REQUIRED_JOINS_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean required_joins_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "required_joins_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // REQUIRED COLON boolean_value SEMICOLON?
  public static boolean required_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "required_property")) return false;
    if (!nextTokenIs(builder_, REQUIRED)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, REQUIRED, COLON);
    result_ = result_ && boolean_value(builder_, level_ + 1);
    result_ = result_ && required_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, REQUIRED_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean required_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "required_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // set_item*
  public static boolean set_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "set_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SET_BODY, "<set body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!set_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "set_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // SET COLON IDENTIFIER LBRACE set_body RBRACE
  public static boolean set_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "set_definition")) return false;
    if (!nextTokenIs(builder_, SET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, SET, COLON, IDENTIFIER, LBRACE);
    result_ = result_ && set_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, SET_DEFINITION, result_);
    return result_;
  }

  /* ********************************************************** */
  // fields_property | property | COMMENT
  static boolean set_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "set_item")) return false;
    boolean result_;
    result_ = fields_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // qualified_identifier (DESC | ASC | (COLON (IDENTIFIER | DESC | ASC)))?
  public static boolean sort_specification(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sort_specification")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SORT_SPECIFICATION, "<sort specification>");
    result_ = qualified_identifier(builder_, level_ + 1);
    result_ = result_ && sort_specification_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (DESC | ASC | (COLON (IDENTIFIER | DESC | ASC)))?
  private static boolean sort_specification_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sort_specification_1")) return false;
    sort_specification_1_0(builder_, level_ + 1);
    return true;
  }

  // DESC | ASC | (COLON (IDENTIFIER | DESC | ASC))
  private static boolean sort_specification_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sort_specification_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, DESC);
    if (!result_) result_ = consumeToken(builder_, ASC);
    if (!result_) result_ = sort_specification_1_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // COLON (IDENTIFIER | DESC | ASC)
  private static boolean sort_specification_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sort_specification_1_0_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COLON);
    result_ = result_ && sort_specification_1_0_2_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // IDENTIFIER | DESC | ASC
  private static boolean sort_specification_1_0_2_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sort_specification_1_0_2_1")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, DESC);
    if (!result_) result_ = consumeToken(builder_, ASC);
    return result_;
  }

  /* ********************************************************** */
  // SQL_ALWAYS_FILTER_START sql_block_content
  public static boolean sql_always_filter_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_always_filter_property")) return false;
    if (!nextTokenIs(builder_, SQL_ALWAYS_FILTER_START)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SQL_ALWAYS_FILTER_START);
    result_ = result_ && sql_block_content(builder_, level_ + 1);
    exit_section_(builder_, marker_, SQL_ALWAYS_FILTER_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // SQL_ALWAYS_HAVING_START sql_block_content
  public static boolean sql_always_having_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_always_having_property")) return false;
    if (!nextTokenIs(builder_, SQL_ALWAYS_HAVING_START)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SQL_ALWAYS_HAVING_START);
    result_ = result_ && sql_block_content(builder_, level_ + 1);
    exit_section_(builder_, marker_, SQL_ALWAYS_HAVING_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // SQL_ALWAYS_WHERE_START sql_block_content
  public static boolean sql_always_where_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_always_where_property")) return false;
    if (!nextTokenIs(builder_, SQL_ALWAYS_WHERE_START)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SQL_ALWAYS_WHERE_START);
    result_ = result_ && sql_block_content(builder_, level_ + 1);
    exit_section_(builder_, marker_, SQL_ALWAYS_WHERE_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // sql_block_element* SQL_BLOCK_END
  public static boolean sql_block_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_block_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SQL_BLOCK_CONTENT, "<sql block content>");
    result_ = sql_block_content_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SQL_BLOCK_END);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // sql_block_element*
  private static boolean sql_block_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_block_content_0")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!sql_block_element(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "sql_block_content_0", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // COMMENT | sql_expression
  static boolean sql_block_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_block_element")) return false;
    boolean result_;
    result_ = consumeToken(builder_, COMMENT);
    if (!result_) result_ = sql_expression(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // sql_field_reference
  //   | template_expression
  //   | STRING
  //   | NUMBER
  //   | IDENTIFIER
  //   | sql_operator
  //   | DOT
  //   | COMMA
  //   | LPAREN sql_expression* RPAREN
  //   | SQL_CONTENT_TOKEN
  public static boolean sql_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SQL_EXPRESSION, "<sql expression>");
    result_ = sql_field_reference(builder_, level_ + 1);
    if (!result_) result_ = template_expression(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = sql_operator(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = sql_expression_8(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, SQL_CONTENT_TOKEN);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // LPAREN sql_expression* RPAREN
  private static boolean sql_expression_8(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_expression_8")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAREN);
    result_ = result_ && sql_expression_8_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // sql_expression*
  private static boolean sql_expression_8_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_expression_8_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!sql_expression(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "sql_expression_8_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // (template_expression | IDENTIFIER) DOT IDENTIFIER
  public static boolean sql_field_reference(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_field_reference")) return false;
    if (!nextTokenIs(builder_, "<sql field reference>", DOLLAR, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SQL_FIELD_REFERENCE, "<sql field reference>");
    result_ = sql_field_reference_0(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, DOT, IDENTIFIER);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // template_expression | IDENTIFIER
  private static boolean sql_field_reference_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_field_reference_0")) return false;
    boolean result_;
    result_ = template_expression(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    return result_;
  }

  /* ********************************************************** */
  // SQL_LATITUDE COLON property_value (SEMICOLON | SEMICOLON SEMICOLON)?
  public static boolean sql_latitude_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_latitude_property")) return false;
    if (!nextTokenIs(builder_, SQL_LATITUDE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, SQL_LATITUDE, COLON);
    result_ = result_ && property_value(builder_, level_ + 1);
    result_ = result_ && sql_latitude_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, SQL_LATITUDE_PROPERTY, result_);
    return result_;
  }

  // (SEMICOLON | SEMICOLON SEMICOLON)?
  private static boolean sql_latitude_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_latitude_property_3")) return false;
    sql_latitude_property_3_0(builder_, level_ + 1);
    return true;
  }

  // SEMICOLON | SEMICOLON SEMICOLON
  private static boolean sql_latitude_property_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_latitude_property_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = parseTokens(builder_, 0, SEMICOLON, SEMICOLON);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // SQL_LONGITUDE COLON property_value (SEMICOLON | SEMICOLON SEMICOLON)?
  public static boolean sql_longitude_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_longitude_property")) return false;
    if (!nextTokenIs(builder_, SQL_LONGITUDE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, SQL_LONGITUDE, COLON);
    result_ = result_ && property_value(builder_, level_ + 1);
    result_ = result_ && sql_longitude_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, SQL_LONGITUDE_PROPERTY, result_);
    return result_;
  }

  // (SEMICOLON | SEMICOLON SEMICOLON)?
  private static boolean sql_longitude_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_longitude_property_3")) return false;
    sql_longitude_property_3_0(builder_, level_ + 1);
    return true;
  }

  // SEMICOLON | SEMICOLON SEMICOLON
  private static boolean sql_longitude_property_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_longitude_property_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SEMICOLON);
    if (!result_) result_ = parseTokens(builder_, 0, SEMICOLON, SEMICOLON);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // SQL_BLOCK_START sql_block_content
  public static boolean sql_on_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_on_property")) return false;
    if (!nextTokenIs(builder_, SQL_BLOCK_START)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SQL_BLOCK_START);
    result_ = result_ && sql_block_content(builder_, level_ + 1);
    exit_section_(builder_, marker_, SQL_ON_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // EQ | NE | LT | GT | LE | GE | PLUS | MINUS | STAR | SLASH | PERCENT | AND | OR
  static boolean sql_operator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_operator")) return false;
    boolean result_;
    result_ = consumeToken(builder_, EQ);
    if (!result_) result_ = consumeToken(builder_, NE);
    if (!result_) result_ = consumeToken(builder_, LT);
    if (!result_) result_ = consumeToken(builder_, GT);
    if (!result_) result_ = consumeToken(builder_, LE);
    if (!result_) result_ = consumeToken(builder_, GE);
    if (!result_) result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    if (!result_) result_ = consumeToken(builder_, STAR);
    if (!result_) result_ = consumeToken(builder_, SLASH);
    if (!result_) result_ = consumeToken(builder_, PERCENT);
    if (!result_) result_ = consumeToken(builder_, AND);
    if (!result_) result_ = consumeToken(builder_, OR);
    return result_;
  }

  /* ********************************************************** */
  // SQL_BLOCK_START sql_block_content
  public static boolean sql_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_property")) return false;
    if (!nextTokenIs(builder_, SQL_BLOCK_START)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SQL_BLOCK_START);
    result_ = result_ && sql_block_content(builder_, level_ + 1);
    exit_section_(builder_, marker_, SQL_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // SQL_TABLE_NAME COLON (STRING | template_expression | IDENTIFIER) SEMICOLON?
  public static boolean sql_table_name_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_table_name_property")) return false;
    if (!nextTokenIs(builder_, SQL_TABLE_NAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, SQL_TABLE_NAME, COLON);
    result_ = result_ && sql_table_name_property_2(builder_, level_ + 1);
    result_ = result_ && sql_table_name_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, SQL_TABLE_NAME_PROPERTY, result_);
    return result_;
  }

  // STRING | template_expression | IDENTIFIER
  private static boolean sql_table_name_property_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_table_name_property_2")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = template_expression(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    return result_;
  }

  // SEMICOLON?
  private static boolean sql_table_name_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_table_name_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // SQL_BLOCK_START sql_block_content
  public static boolean sql_trigger_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sql_trigger_property")) return false;
    if (!nextTokenIs(builder_, SQL_BLOCK_START)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SQL_BLOCK_START);
    result_ = result_ && sql_block_content(builder_, level_ + 1);
    exit_section_(builder_, marker_, SQL_TRIGGER_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // SUGGESTIONS COLON array_value SEMICOLON?
  public static boolean suggestions_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "suggestions_property")) return false;
    if (!nextTokenIs(builder_, SUGGESTIONS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, SUGGESTIONS, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && suggestions_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, SUGGESTIONS_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean suggestions_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "suggestions_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // TAGS COLON array_value SEMICOLON?
  public static boolean tags_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tags_property")) return false;
    if (!nextTokenIs(builder_, TAGS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, TAGS, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && tags_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, TAGS_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean tags_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tags_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // DOLLAR LBRACE template_reference RBRACE
  public static boolean template_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "template_expression")) return false;
    if (!nextTokenIs(builder_, DOLLAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DOLLAR, LBRACE);
    result_ = result_ && template_reference(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, TEMPLATE_EXPRESSION, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER (DOT IDENTIFIER)*
  public static boolean template_reference(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "template_reference")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    result_ = result_ && template_reference_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, TEMPLATE_REFERENCE, result_);
    return result_;
  }

  // (DOT IDENTIFIER)*
  private static boolean template_reference_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "template_reference_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!template_reference_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "template_reference_1", pos_)) break;
    }
    return true;
  }

  // DOT IDENTIFIER
  private static boolean template_reference_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "template_reference_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DOT, IDENTIFIER);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // test_item*
  public static boolean test_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "test_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TEST_BODY, "<test body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!test_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "test_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // TEST COLON IDENTIFIER LBRACE test_body RBRACE
  public static boolean test_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "test_definition")) return false;
    if (!nextTokenIs(builder_, TEST)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, TEST, COLON, IDENTIFIER, LBRACE);
    result_ = result_ && test_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, TEST_DEFINITION, result_);
    return result_;
  }

  /* ********************************************************** */
  // explore_source_property | assert_property | property | COMMENT
  static boolean test_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "test_item")) return false;
    boolean result_;
    result_ = explore_source_property(builder_, level_ + 1);
    if (!result_) result_ = assert_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // TIERS COLON array_value SEMICOLON?
  public static boolean tiers_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tiers_property")) return false;
    if (!nextTokenIs(builder_, TIERS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, TIERS, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && tiers_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, TIERS_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean tiers_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tiers_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // TIMEFRAMES COLON array_value SEMICOLON?
  public static boolean timeframes_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "timeframes_property")) return false;
    if (!nextTokenIs(builder_, TIMEFRAMES)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, TIMEFRAMES, COLON);
    result_ = result_ && array_value(builder_, level_ + 1);
    result_ = result_ && timeframes_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, TIMEFRAMES_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean timeframes_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "timeframes_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // TIMEZONE COLON STRING SEMICOLON?
  public static boolean timezone_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "timezone_property")) return false;
    if (!nextTokenIs(builder_, TIMEZONE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, TIMEZONE, COLON, STRING);
    result_ = result_ && timezone_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, TIMEZONE_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean timezone_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "timezone_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // TITLE COLON STRING SEMICOLON?
  public static boolean title_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "title_property")) return false;
    if (!nextTokenIs(builder_, TITLE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, TITLE, COLON, STRING);
    result_ = result_ && title_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, TITLE_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean title_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "title_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // TYPE COLON (IDENTIFIER | STRING | keyword) SEMICOLON?
  public static boolean type_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_property")) return false;
    if (!nextTokenIs(builder_, TYPE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, TYPE, COLON);
    result_ = result_ && type_property_2(builder_, level_ + 1);
    result_ = result_ && type_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, TYPE_PROPERTY, result_);
    return result_;
  }

  // IDENTIFIER | STRING | keyword
  private static boolean type_property_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_property_2")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    return result_;
  }

  // SEMICOLON?
  private static boolean type_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // ui_config_item*
  public static boolean ui_config_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ui_config_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, UI_CONFIG_BODY, "<ui config body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!ui_config_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "ui_config_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // type_property | display_property | options_property | property | COMMENT
  static boolean ui_config_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ui_config_item")) return false;
    boolean result_;
    result_ = type_property(builder_, level_ + 1);
    if (!result_) result_ = display_property(builder_, level_ + 1);
    if (!result_) result_ = options_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // UI_CONFIG COLON LBRACE ui_config_body RBRACE
  public static boolean ui_config_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ui_config_property")) return false;
    if (!nextTokenIs(builder_, UI_CONFIG)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, UI_CONFIG, COLON, LBRACE);
    result_ = result_ && ui_config_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, UI_CONFIG_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // UNLESS COLON LBRACKET identifier_list RBRACKET
  public static boolean unless_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unless_property")) return false;
    if (!nextTokenIs(builder_, UNLESS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, UNLESS, COLON, LBRACKET);
    result_ = result_ && identifier_list(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, UNLESS_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // URL COLON STRING SEMICOLON?
  public static boolean url_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "url_property")) return false;
    if (!nextTokenIs(builder_, URL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, URL, COLON, STRING);
    result_ = result_ && url_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, URL_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean url_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "url_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // user_attribute_param_item*
  public static boolean user_attribute_param_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "user_attribute_param_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, USER_ATTRIBUTE_PARAM_BODY, "<user attribute param body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!user_attribute_param_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "user_attribute_param_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // name_property | user_attribute_property | property | COMMENT
  static boolean user_attribute_param_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "user_attribute_param_item")) return false;
    boolean result_;
    result_ = name_property(builder_, level_ + 1);
    if (!result_) result_ = user_attribute_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // USER_ATTRIBUTE_PARAM COLON LBRACE user_attribute_param_body RBRACE
  public static boolean user_attribute_param_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "user_attribute_param_property")) return false;
    if (!nextTokenIs(builder_, USER_ATTRIBUTE_PARAM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, USER_ATTRIBUTE_PARAM, COLON, LBRACE);
    result_ = result_ && user_attribute_param_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, USER_ATTRIBUTE_PARAM_PROPERTY, result_);
    return result_;
  }

  /* ********************************************************** */
  // USER_ATTRIBUTE COLON IDENTIFIER SEMICOLON?
  public static boolean user_attribute_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "user_attribute_property")) return false;
    if (!nextTokenIs(builder_, USER_ATTRIBUTE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, USER_ATTRIBUTE, COLON, IDENTIFIER);
    result_ = result_ && user_attribute_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, USER_ATTRIBUTE_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean user_attribute_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "user_attribute_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // VALUE COLON STRING SEMICOLON?
  public static boolean value_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value_property")) return false;
    if (!nextTokenIs(builder_, VALUE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, VALUE, COLON, STRING);
    result_ = result_ && value_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, VALUE_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean value_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // view_item*
  public static boolean view_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "view_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, VIEW_BODY, "<view body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!view_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "view_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // VIEW COLON IDENTIFIER LBRACE view_body RBRACE
  public static boolean view_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "view_definition")) return false;
    if (!nextTokenIs(builder_, VIEW)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, VIEW, COLON, IDENTIFIER, LBRACE);
    result_ = result_ && view_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, VIEW_DEFINITION, result_);
    return result_;
  }

  /* ********************************************************** */
  // dimension_definition 
  //   | dimension_group_definition
  //   | measure_definition 
  //   | filter_definition
  //   | parameter_definition
  //   | set_definition
  //   | test_definition
  //   | drill_fields_property
  //   | sql_property
  //   | sql_table_name_property
  //   | sql_always_where_property
  //   | sql_always_filter_property
  //   | derived_table_property
  //   | html_property
  //   | property
  //   | COMMENT
  static boolean view_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "view_item")) return false;
    boolean result_;
    result_ = dimension_definition(builder_, level_ + 1);
    if (!result_) result_ = dimension_group_definition(builder_, level_ + 1);
    if (!result_) result_ = measure_definition(builder_, level_ + 1);
    if (!result_) result_ = filter_definition(builder_, level_ + 1);
    if (!result_) result_ = parameter_definition(builder_, level_ + 1);
    if (!result_) result_ = set_definition(builder_, level_ + 1);
    if (!result_) result_ = test_definition(builder_, level_ + 1);
    if (!result_) result_ = drill_fields_property(builder_, level_ + 1);
    if (!result_) result_ = sql_property(builder_, level_ + 1);
    if (!result_) result_ = sql_table_name_property(builder_, level_ + 1);
    if (!result_) result_ = sql_always_where_property(builder_, level_ + 1);
    if (!result_) result_ = sql_always_filter_property(builder_, level_ + 1);
    if (!result_) result_ = derived_table_property(builder_, level_ + 1);
    if (!result_) result_ = html_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // VIEW_NAME COLON IDENTIFIER SEMICOLON?
  public static boolean view_name_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "view_name_property")) return false;
    if (!nextTokenIs(builder_, VIEW_NAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, VIEW_NAME, COLON, IDENTIFIER);
    result_ = result_ && view_name_property_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, VIEW_NAME_PROPERTY, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean view_name_property_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "view_name_property_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // WEEK_START_DAY COLON day_of_week SEMICOLON?
  public static boolean week_start_day_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "week_start_day_statement")) return false;
    if (!nextTokenIs(builder_, WEEK_START_DAY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, WEEK_START_DAY, COLON);
    result_ = result_ && day_of_week(builder_, level_ + 1);
    result_ = result_ && week_start_day_statement_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, WEEK_START_DAY_STATEMENT, result_);
    return result_;
  }

  // SEMICOLON?
  private static boolean week_start_day_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "week_start_day_statement_3")) return false;
    consumeToken(builder_, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // when_item*
  public static boolean when_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "when_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, WHEN_BODY, "<when body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!when_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "when_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // WHEN COLON LBRACE when_body RBRACE
  public static boolean when_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "when_clause")) return false;
    if (!nextTokenIs(builder_, WHEN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, WHEN, COLON, LBRACE);
    result_ = result_ && when_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, WHEN_CLAUSE, result_);
    return result_;
  }

  /* ********************************************************** */
  // sql_property | label_property | property | COMMENT
  static boolean when_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "when_item")) return false;
    boolean result_;
    result_ = sql_property(builder_, level_ + 1);
    if (!result_) result_ = label_property(builder_, level_ + 1);
    if (!result_) result_ = property(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER STAR
  public static boolean wildcard_identifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "wildcard_identifier")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, STAR);
    exit_section_(builder_, marker_, WILDCARD_IDENTIFIER, result_);
    return result_;
  }

  /* ********************************************************** */
  // yaml_array_item (COMMA yaml_array_item)*
  public static boolean yaml_array_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_array_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_ARRAY_CONTENT, "<yaml array content>");
    result_ = yaml_array_item(builder_, level_ + 1);
    result_ = result_ && yaml_array_content_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (COMMA yaml_array_item)*
  private static boolean yaml_array_content_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_array_content_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!yaml_array_content_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "yaml_array_content_1", pos_)) break;
    }
    return true;
  }

  // COMMA yaml_array_item
  private static boolean yaml_array_content_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_array_content_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && yaml_array_item(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // field_pattern | wildcard_identifier | sort_specification | yaml_simple_value | yaml_flow_object
  public static boolean yaml_array_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_array_item")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_ARRAY_ITEM, "<yaml array item>");
    result_ = field_pattern(builder_, level_ + 1);
    if (!result_) result_ = wildcard_identifier(builder_, level_ + 1);
    if (!result_) result_ = sort_specification(builder_, level_ + 1);
    if (!result_) result_ = yaml_simple_value(builder_, level_ + 1);
    if (!result_) result_ = yaml_flow_object(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // yaml_item*
  public static boolean yaml_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_content")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_CONTENT, "<yaml content>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!yaml_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "yaml_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // DASHBOARD COLON IDENTIFIER yaml_content
  public static boolean yaml_dashboard_def(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_dashboard_def")) return false;
    if (!nextTokenIs(builder_, DASHBOARD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DASHBOARD, COLON, IDENTIFIER);
    result_ = result_ && yaml_content(builder_, level_ + 1);
    exit_section_(builder_, marker_, YAML_DASHBOARD_DEF, result_);
    return result_;
  }

  /* ********************************************************** */
  // YAML_DOCUMENT_START yaml_content
  public static boolean yaml_dashboard_document(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_dashboard_document")) return false;
    if (!nextTokenIs(builder_, YAML_DOCUMENT_START)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_DASHBOARD_DOCUMENT, null);
    result_ = consumeToken(builder_, YAML_DOCUMENT_START);
    pinned_ = result_; // pin = 1
    result_ = result_ && yaml_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // !(YAML_DOCUMENT_START | <<eof>>)
  static boolean yaml_dashboard_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_dashboard_recover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !yaml_dashboard_recover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // YAML_DOCUMENT_START | <<eof>>
  private static boolean yaml_dashboard_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_dashboard_recover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, YAML_DOCUMENT_START);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER | keyword | NUMBER | template_expression
  //   | LPAREN | RPAREN | DOT | COMMA
  //   | PLUS | MINUS | STAR | SLASH | PERCENT
  static boolean yaml_expression_part(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_expression_part")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = template_expression(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, LPAREN);
    if (!result_) result_ = consumeToken(builder_, RPAREN);
    if (!result_) result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    if (!result_) result_ = consumeToken(builder_, STAR);
    if (!result_) result_ = consumeToken(builder_, SLASH);
    if (!result_) result_ = consumeToken(builder_, PERCENT);
    return result_;
  }

  /* ********************************************************** */
  // yaml_expression_part+
  public static boolean yaml_expression_value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_expression_value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_EXPRESSION_VALUE, "<yaml expression value>");
    result_ = yaml_expression_part(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!yaml_expression_part(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "yaml_expression_value", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // LBRACKET yaml_array_content RBRACKET
  public static boolean yaml_flow_array(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_flow_array")) return false;
    if (!nextTokenIs(builder_, LBRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACKET);
    result_ = result_ && yaml_array_content(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACKET);
    exit_section_(builder_, marker_, YAML_FLOW_ARRAY, result_);
    return result_;
  }

  /* ********************************************************** */
  // LBRACE yaml_object_content RBRACE
  public static boolean yaml_flow_object(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_flow_object")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LBRACE);
    result_ = result_ && yaml_object_content(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RBRACE);
    exit_section_(builder_, marker_, YAML_FLOW_OBJECT, result_);
    return result_;
  }

  /* ********************************************************** */
  // yaml_flow_array | yaml_flow_object
  public static boolean yaml_flow_value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_flow_value")) return false;
    if (!nextTokenIs(builder_, "<yaml flow value>", LBRACE, LBRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_FLOW_VALUE, "<yaml flow value>");
    result_ = yaml_flow_array(builder_, level_ + 1);
    if (!result_) result_ = yaml_flow_object(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // yaml_property
  //   | yaml_list_entry
  //   | COMMENT
  //   | yaml_text_line
  static boolean yaml_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_item")) return false;
    boolean result_;
    result_ = yaml_property(builder_, level_ + 1);
    if (!result_) result_ = yaml_list_entry(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    if (!result_) result_ = yaml_text_line(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // yaml_property | yaml_flow_value | yaml_simple_value
  public static boolean yaml_item_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_item_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_ITEM_CONTENT, "<yaml item content>");
    result_ = yaml_property(builder_, level_ + 1);
    if (!result_) result_ = yaml_flow_value(builder_, level_ + 1);
    if (!result_) result_ = yaml_simple_value(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // YAML_LIST_ITEM yaml_item_content
  public static boolean yaml_list_entry(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_list_entry")) return false;
    if (!nextTokenIs(builder_, YAML_LIST_ITEM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, YAML_LIST_ITEM);
    result_ = result_ && yaml_item_content(builder_, level_ + 1);
    exit_section_(builder_, marker_, YAML_LIST_ENTRY, result_);
    return result_;
  }

  /* ********************************************************** */
  // YAML_LIST_ITEM yaml_dashboard_def
  public static boolean yaml_list_line(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_list_line")) return false;
    if (!nextTokenIs(builder_, YAML_LIST_ITEM)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_LIST_LINE, null);
    result_ = consumeToken(builder_, YAML_LIST_ITEM);
    pinned_ = result_; // pin = 1
    result_ = result_ && yaml_dashboard_def(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // yaml_text_line+
  public static boolean yaml_multiline_value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_multiline_value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_MULTILINE_VALUE, "<yaml multiline value>");
    result_ = yaml_text_line(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!yaml_text_line(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "yaml_multiline_value", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // yaml_object_property (COMMA yaml_object_property)*
  public static boolean yaml_object_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_object_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_OBJECT_CONTENT, "<yaml object content>");
    result_ = yaml_object_property(builder_, level_ + 1);
    result_ = result_ && yaml_object_content_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (COMMA yaml_object_property)*
  private static boolean yaml_object_content_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_object_content_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!yaml_object_content_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "yaml_object_content_1", pos_)) break;
    }
    return true;
  }

  // COMMA yaml_object_property
  private static boolean yaml_object_content_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_object_content_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && yaml_object_property(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // yaml_property_name COLON yaml_simple_value
  public static boolean yaml_object_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_object_property")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_OBJECT_PROPERTY, "<yaml object property>");
    result_ = yaml_property_name(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && yaml_simple_value(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // yaml_property_name COLON (yaml_value)?
  public static boolean yaml_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_property")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_PROPERTY, "<yaml property>");
    result_ = yaml_property_name(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && yaml_property_2(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (yaml_value)?
  private static boolean yaml_property_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_property_2")) return false;
    yaml_property_2_0(builder_, level_ + 1);
    return true;
  }

  // (yaml_value)
  private static boolean yaml_property_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_property_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = yaml_value(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // yaml_property_name_part ((DOT | PIPE) yaml_property_name_part)*
  public static boolean yaml_property_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_property_name")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_PROPERTY_NAME, "<yaml property name>");
    result_ = yaml_property_name_part(builder_, level_ + 1);
    result_ = result_ && yaml_property_name_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ((DOT | PIPE) yaml_property_name_part)*
  private static boolean yaml_property_name_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_property_name_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!yaml_property_name_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "yaml_property_name_1", pos_)) break;
    }
    return true;
  }

  // (DOT | PIPE) yaml_property_name_part
  private static boolean yaml_property_name_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_property_name_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = yaml_property_name_1_0_0(builder_, level_ + 1);
    result_ = result_ && yaml_property_name_part(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // DOT | PIPE
  private static boolean yaml_property_name_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_property_name_1_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, PIPE);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER
  //   | keyword
  //   | YES | NO | TRUE_VALUE | FALSE_VALUE  // Add boolean tokens
  //   | STAR IDENTIFIER STAR IDENTIFIER
  //   | STAR IDENTIFIER
  //   | IDENTIFIER STAR
  public static boolean yaml_property_name_part(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_property_name_part")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_PROPERTY_NAME_PART, "<yaml property name part>");
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, YES);
    if (!result_) result_ = consumeToken(builder_, NO);
    if (!result_) result_ = consumeToken(builder_, TRUE_VALUE);
    if (!result_) result_ = consumeToken(builder_, FALSE_VALUE);
    if (!result_) result_ = parseTokens(builder_, 0, STAR, IDENTIFIER, STAR, IDENTIFIER);
    if (!result_) result_ = parseTokens(builder_, 0, STAR, IDENTIFIER);
    if (!result_) result_ = parseTokens(builder_, 0, IDENTIFIER, STAR);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // STRING | NUMBER | IDENTIFIER | keyword | boolean_value
  //   | TRUE_VALUE | FALSE_VALUE | YES | NO
  //   | template_expression | field_pattern | wildcard_identifier | qualified_identifier
  //   | yaml_value_tokens
  //   | yaml_expression_value
  //   | yaml_text_line
  public static boolean yaml_simple_value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_simple_value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_SIMPLE_VALUE, "<yaml simple value>");
    result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    if (!result_) result_ = boolean_value(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, TRUE_VALUE);
    if (!result_) result_ = consumeToken(builder_, FALSE_VALUE);
    if (!result_) result_ = consumeToken(builder_, YES);
    if (!result_) result_ = consumeToken(builder_, NO);
    if (!result_) result_ = template_expression(builder_, level_ + 1);
    if (!result_) result_ = field_pattern(builder_, level_ + 1);
    if (!result_) result_ = wildcard_identifier(builder_, level_ + 1);
    if (!result_) result_ = qualified_identifier(builder_, level_ + 1);
    if (!result_) result_ = yaml_value_tokens(builder_, level_ + 1);
    if (!result_) result_ = yaml_expression_value(builder_, level_ + 1);
    if (!result_) result_ = yaml_text_line(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER | keyword | NUMBER | hyphenated_identifier
  static boolean yaml_string_part(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_string_part")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = hyphenated_identifier(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // yaml_text_token+
  public static boolean yaml_text_line(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_text_line")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_TEXT_LINE, "<yaml text line>");
    result_ = yaml_text_token(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!yaml_text_token(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "yaml_text_line", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER | keyword | NUMBER | STRING | DOT | COMMA | LBRACE | RBRACE | COLON | LPAREN | RPAREN | LBRACKET | RBRACKET | MINUS | PLUS | STAR
  //     | YAML_BLOCK_SCALAR_INDICATOR | DESC | ASC | SLASH | PERCENT | DOLLAR | LT | GT | LE | GE | NE | EQ | PIPE | YAML_TAG_INDICATOR | INLINE_CHAR | qualified_identifier
  //     | YES | NO | TRUE_VALUE | FALSE_VALUE
  static boolean yaml_text_token(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_text_token")) return false;
    boolean result_;
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, LBRACE);
    if (!result_) result_ = consumeToken(builder_, RBRACE);
    if (!result_) result_ = consumeToken(builder_, COLON);
    if (!result_) result_ = consumeToken(builder_, LPAREN);
    if (!result_) result_ = consumeToken(builder_, RPAREN);
    if (!result_) result_ = consumeToken(builder_, LBRACKET);
    if (!result_) result_ = consumeToken(builder_, RBRACKET);
    if (!result_) result_ = consumeToken(builder_, MINUS);
    if (!result_) result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, STAR);
    if (!result_) result_ = consumeToken(builder_, YAML_BLOCK_SCALAR_INDICATOR);
    if (!result_) result_ = consumeToken(builder_, DESC);
    if (!result_) result_ = consumeToken(builder_, ASC);
    if (!result_) result_ = consumeToken(builder_, SLASH);
    if (!result_) result_ = consumeToken(builder_, PERCENT);
    if (!result_) result_ = consumeToken(builder_, DOLLAR);
    if (!result_) result_ = consumeToken(builder_, LT);
    if (!result_) result_ = consumeToken(builder_, GT);
    if (!result_) result_ = consumeToken(builder_, LE);
    if (!result_) result_ = consumeToken(builder_, GE);
    if (!result_) result_ = consumeToken(builder_, NE);
    if (!result_) result_ = consumeToken(builder_, EQ);
    if (!result_) result_ = consumeToken(builder_, PIPE);
    if (!result_) result_ = consumeToken(builder_, YAML_TAG_INDICATOR);
    if (!result_) result_ = consumeToken(builder_, INLINE_CHAR);
    if (!result_) result_ = qualified_identifier(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, YES);
    if (!result_) result_ = consumeToken(builder_, NO);
    if (!result_) result_ = consumeToken(builder_, TRUE_VALUE);
    if (!result_) result_ = consumeToken(builder_, FALSE_VALUE);
    return result_;
  }

  /* ********************************************************** */
  // yaml_string_part+
  public static boolean yaml_unquoted_string(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_unquoted_string")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_UNQUOTED_STRING, "<yaml unquoted string>");
    result_ = yaml_string_part(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!yaml_string_part(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "yaml_unquoted_string", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // yaml_simple_value
  //   | yaml_flow_value
  //   | yaml_multiline_value
  //   | yaml_text_line
  //   | // empty for properties with nested content
  public static boolean yaml_value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_VALUE, "<yaml value>");
    result_ = yaml_simple_value(builder_, level_ + 1);
    if (!result_) result_ = yaml_flow_value(builder_, level_ + 1);
    if (!result_) result_ = yaml_multiline_value(builder_, level_ + 1);
    if (!result_) result_ = yaml_text_line(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, YAML_VALUE_4_0);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // yaml_unquoted_string
  public static boolean yaml_value_tokens(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "yaml_value_tokens")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, YAML_VALUE_TOKENS, "<yaml value tokens>");
    result_ = yaml_unquoted_string(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

}
