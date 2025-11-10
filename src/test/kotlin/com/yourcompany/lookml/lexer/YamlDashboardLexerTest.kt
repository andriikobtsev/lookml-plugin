package com.yourcompany.lookml.lexer

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
// Commented out until parser is generated
// import com.yourcompany.lookml.psi.LookMLTypes
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Full tests for YamlDashboardLexer.
 * Uncomment and use after generating parser from BNF.
 */
class YamlDashboardLexerTest {
    
    @Test
    fun testPipeInPropertyName() {
        val code = """label|mart__investment_case_current.count_commercial_units_in_period: "# of Commercial Units""""
        val tokens = tokenize(code)
        
        // Uncomment after generating parser
        /*
        val expectedTokens = listOf(
            TokenInfo(LookMLTypes.IDENTIFIER, "label|mart__investment_case_current.count_commercial_units_in_period"),
            TokenInfo(LookMLTypes.COLON, ":"),
            TokenInfo(TokenType.WHITE_SPACE, " "),
            TokenInfo(LookMLTypes.STRING, "\"# of Commercial Units\"")
        )
        
        assertTokensEqual(expectedTokens, tokens)
        */
        
        // For now, just verify we get some tokens
        assert(tokens.isNotEmpty()) { "Should have tokenized the input" }
    }
    
    @Test
    fun testSortWithDesc() {
        val code = """sorts: [order_items.created_month desc, products.category]"""
        val tokens = tokenize(code)
        
        // Uncomment after generating parser
        /*
        val expectedTokens = listOf(
            TokenInfo(LookMLTypes.SORTS, "sorts"),
            TokenInfo(LookMLTypes.COLON, ":"),
            TokenInfo(TokenType.WHITE_SPACE, " "),
            TokenInfo(LookMLTypes.LBRACKET, "["),
            TokenInfo(LookMLTypes.IDENTIFIER, "order_items.created_month"),
            TokenInfo(TokenType.WHITE_SPACE, " "),
            TokenInfo(LookMLTypes.DESC, "desc"),
            TokenInfo(LookMLTypes.COMMA, ","),
            TokenInfo(TokenType.WHITE_SPACE, " "),
            TokenInfo(LookMLTypes.IDENTIFIER, "products.category"),
            TokenInfo(LookMLTypes.RBRACKET, "]")
        )
        
        assertTokensEqual(expectedTokens, tokens)
        */
        
        // For now, verify 'desc' is found
        val hasDesc = tokens.any { it.text == "desc" }
        assert(hasDesc) { "Should have found 'desc' token" }
    }
    
    @Test
    fun testYamlDocumentStart() {
        val code = """---
- dashboard: test_dashboard"""
        val tokens = tokenize(code)
        
        // Uncomment after generating parser
        /*
        val expectedTokens = listOf(
            TokenInfo(LookMLTypes.YAML_DOCUMENT_START, "---"),
            TokenInfo(TokenType.WHITE_SPACE, "\n"),
            TokenInfo(LookMLTypes.YAML_LIST_ITEM, "-"),
            TokenInfo(TokenType.WHITE_SPACE, " "),
            TokenInfo(LookMLTypes.DASHBOARD, "dashboard"),
            TokenInfo(LookMLTypes.COLON, ":"),
            TokenInfo(TokenType.WHITE_SPACE, " "),
            TokenInfo(LookMLTypes.IDENTIFIER, "test_dashboard")
        )
        
        assertTokensEqual(expectedTokens, tokens)
        */
        
        // Basic check
        assert(tokens.first().text == "---") { "Should start with document marker" }
    }
    
    @Test
    fun testListItem() {
        val code = """- title: Revenue by Month"""
        val tokens = tokenize(code)
        
        // Uncomment after generating parser
        /*
        val expectedTokens = listOf(
            TokenInfo(LookMLTypes.YAML_LIST_ITEM, "-"),
            TokenInfo(TokenType.WHITE_SPACE, " "),
            TokenInfo(LookMLTypes.TITLE, "title"),
            TokenInfo(LookMLTypes.COLON, ":"),
            TokenInfo(TokenType.WHITE_SPACE, " "),
            TokenInfo(LookMLTypes.IDENTIFIER, "Revenue"),
            TokenInfo(TokenType.WHITE_SPACE, " "),
            TokenInfo(LookMLTypes.IDENTIFIER, "by"),
            TokenInfo(TokenType.WHITE_SPACE, " "),
            TokenInfo(LookMLTypes.IDENTIFIER, "Month")
        )
        
        assertTokensEqual(expectedTokens, tokens)
        */
        
        assert(tokens.first().text == "-") { "Should start with list item marker" }
    }
    
    @Test
    fun testNestedProperties() {
        val code = """  filters:
    order_items.status: "-Cancelled,-Returned""""
        val tokens = tokenize(code)
        
        // Uncomment after generating parser
        /*
        val expectedTokens = listOf(
            TokenInfo(TokenType.WHITE_SPACE, "  "),
            TokenInfo(LookMLTypes.FILTERS, "filters"),
            TokenInfo(LookMLTypes.COLON, ":"),
            TokenInfo(TokenType.WHITE_SPACE, "\n    "),
            TokenInfo(LookMLTypes.IDENTIFIER, "order_items.status"),
            TokenInfo(LookMLTypes.COLON, ":"),
            TokenInfo(TokenType.WHITE_SPACE, " "),
            TokenInfo(LookMLTypes.STRING, "\"-Cancelled,-Returned\"")
        )
        
        assertTokensEqual(expectedTokens, tokens)
        */
        
        // Basic check for nested structure
        val hasFilters = tokens.any { it.text == "filters" }
        assert(hasFilters) { "Should have 'filters' token" }
    }
    
    private fun tokenize(code: String): List<TokenInfo> {
        val lexer = YamlDashboardLexer()
        val tokens = mutableListOf<TokenInfo>()
        
        lexer.start(code, 0, code.length, 0)
        
        while (lexer.tokenType != null) {
            tokens.add(TokenInfo(
                lexer.tokenType!!,
                code.substring(lexer.tokenStart, lexer.tokenEnd)
            ))
            lexer.advance()
        }
        
        return tokens
    }
    
    private fun assertTokensEqual(expected: List<TokenInfo>, actual: List<TokenInfo>) {
        assertEquals(expected.size, actual.size, "Token count mismatch")
        
        for (i in expected.indices) {
            assertEquals(expected[i].type, actual[i].type, 
                "Token type mismatch at position $i. Expected: ${expected[i].type}, Actual: ${actual[i].type}")
            assertEquals(expected[i].text, actual[i].text,
                "Token text mismatch at position $i. Expected: '${expected[i].text}', Actual: '${actual[i].text}'")
        }
    }
    
    data class TokenInfo(val type: IElementType, val text: String)
}
