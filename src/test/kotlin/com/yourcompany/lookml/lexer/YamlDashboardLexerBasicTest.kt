package com.yourcompany.lookml.lexer

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Basic test for YamlDashboardLexer that doesn't depend on generated types.
 * This test can run even before parser generation.
 */
class YamlDashboardLexerBasicTest {
    
    @Test
    fun testBasicTokenization() {
        val code = "- dashboard: test_dashboard"
        val lexer = YamlDashboardLexer()
        
        lexer.start(code, 0, code.length, 0)
        
        // First token should be the dash
        assertNotNull(lexer.tokenType)
        assertEquals("-", code.substring(lexer.tokenStart, lexer.tokenEnd))
        
        // Advance through all tokens
        var tokenCount = 0
        while (lexer.tokenType != null) {
            tokenCount++
            lexer.advance()
        }
        
        // Should have at least 4 tokens: -, whitespace, dashboard, :, whitespace, identifier
        assert(tokenCount >= 6) { "Expected at least 6 tokens, got $tokenCount" }
    }
    
    @Test
    fun testPipeCharacterInPropertyName() {
        val code = """label|field.name: "value""""
        val lexer = YamlDashboardLexer()
        
        lexer.start(code, 0, code.length, 0)
        
        // First token should be the identifier with pipe
        assertNotNull(lexer.tokenType)
        val firstToken = code.substring(lexer.tokenStart, lexer.tokenEnd)
        assertEquals("label|field.name", firstToken)
        
        // Next should be colon
        lexer.advance()
        assertNotNull(lexer.tokenType)
        assertEquals(":", code.substring(lexer.tokenStart, lexer.tokenEnd))
        
        // Then whitespace
        lexer.advance()
        assertNotNull(lexer.tokenType)
        assertEquals(TokenType.WHITE_SPACE, lexer.tokenType)
        
        // Then string
        lexer.advance()
        assertNotNull(lexer.tokenType)
        assertEquals("\"value\"", code.substring(lexer.tokenStart, lexer.tokenEnd))
    }
    
    @Test
    fun testYamlDocumentStart() {
        val code = "---"
        val lexer = YamlDashboardLexer()
        
        lexer.start(code, 0, code.length, 0)
        
        assertNotNull(lexer.tokenType)
        assertEquals("---", code.substring(lexer.tokenStart, lexer.tokenEnd))
    }
    
    @Test
    fun testDescKeyword() {
        val code = "sorts: [field desc]"
        val lexer = YamlDashboardLexer()
        
        lexer.start(code, 0, code.length, 0)
        
        // Skip to 'desc' token
        var foundDesc = false
        while (lexer.tokenType != null) {
            val tokenText = code.substring(lexer.tokenStart, lexer.tokenEnd)
            if (tokenText == "desc") {
                foundDesc = true
                break
            }
            lexer.advance()
        }
        
        assert(foundDesc) { "Should have found 'desc' token" }
    }
}
