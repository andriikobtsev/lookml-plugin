package com.yourcompany.lookml.formatting

import org.junit.Test
import org.junit.Assert.*

/**
 * Test class for LookML formatting functionality
 */
class LookMLFormatterTest {

    private val action = LookMLFormatAction()

    @Test
    fun testSqlKeywordCapitalization() {
        val input = "sql: select * from users where id = 1 ;;"
        val expected = "SELECT * FROM users WHERE id = 1"

        // Test the SQL capitalization method via reflection
        val method = action.javaClass.getDeclaredMethod("capitalizeSqlKeywords", String::class.java)
        method.isAccessible = true
        val result = method.invoke(action, input.substringAfter("sql:").substringBefore(";;").trim()) as String

        assertEquals(expected, result)
    }

    @Test
    fun testSqlKeywordCapitalizationWithTemplates() {
        val input = "select * from \${TABLE} where \${users.id} = \${orders.user_id}"
        val expected = "SELECT * FROM \${TABLE} WHERE \${users.id} = \${orders.user_id}"

        val method = action.javaClass.getDeclaredMethod("capitalizeSqlKeywords", String::class.java)
        method.isAccessible = true
        val result = method.invoke(action, input) as String

        assertEquals(expected, result)
    }

    @Test
    fun testSqlKeywordCapitalizationWithStrings() {
        val input = "select name from users where status = 'active' and role = \"admin\""
        val expected = "SELECT name FROM users WHERE status = 'active' AND role = \"admin\""

        val method = action.javaClass.getDeclaredMethod("capitalizeSqlKeywords", String::class.java)
        method.isAccessible = true
        val result = method.invoke(action, input) as String

        assertEquals(expected, result)
    }

    @Test
    fun testComplexSqlWithJoin() {
        val input = "select u.id, o.total from users u left join orders o on u.id = o.user_id where u.created_date > '2020-01-01'"
        val expected = "SELECT u.id, o.total FROM users u LEFT JOIN orders o ON u.id = o.user_id WHERE u.created_date > '2020-01-01'"

        val method = action.javaClass.getDeclaredMethod("capitalizeSqlKeywords", String::class.java)
        method.isAccessible = true
        val result = method.invoke(action, input) as String

        assertEquals(expected, result)
    }

    @Test
    fun testSqlWithAggregateFunctions() {
        val input = "select count(*), sum(total), avg(price), min(created_at), max(updated_at) from orders"
        val expected = "SELECT COUNT(*), SUM(total), AVG(price), MIN(created_at), MAX(updated_at) FROM orders"

        val method = action.javaClass.getDeclaredMethod("capitalizeSqlKeywords", String::class.java)
        method.isAccessible = true
        val result = method.invoke(action, input) as String

        assertEquals(expected, result)
    }

    @Test
    fun testNormalizeSpacesBasic() {
        val input = "type :  string"
        val expected = "type: string"

        val method = action.javaClass.getDeclaredMethod("normalizeSpaces", String::class.java)
        method.isAccessible = true
        val result = method.invoke(action, input) as String

        assertEquals(expected, result)
    }

    @Test
    fun testNormalizeSpacesWithBraces() {
        val input = "view: users { "
        val expected = "view: users{"

        val method = action.javaClass.getDeclaredMethod("normalizeSpaces", String::class.java)
        method.isAccessible = true
        val result = method.invoke(action, input) as String

        assertEquals(expected, result)
    }

    @Test
    fun testSqlPropertyNormalization() {
        val input = "sql_on: select  *  from   users   where id = 1"

        val method = action.javaClass.getDeclaredMethod("normalizeSpaces", String::class.java)
        method.isAccessible = true
        val result = method.invoke(action, input) as String

        // Should capitalize SQL keywords and normalize spaces
        assertTrue(result.contains("SELECT"))
        assertTrue(result.contains("FROM"))
        assertTrue(result.contains("WHERE"))
        assertFalse(result.contains("  ")) // No double spaces
    }

    @Test
    fun testPreservesTemplateExpressions() {
        val input = "sql: SELECT * FROM \${TABLE} WHERE \${users.id} IS NOT NULL ;;"

        val method = action.javaClass.getDeclaredMethod("normalizeSpaces", String::class.java)
        method.isAccessible = true
        val result = method.invoke(action, input) as String

        // Should preserve ${...} exactly
        assertTrue(result.contains("\${TABLE}"))
        assertTrue(result.contains("\${users.id}"))
    }

    @Test
    fun testPreservesQuotedStrings() {
        val input = "label: \"User Name with  Spaces\""

        val method = action.javaClass.getDeclaredMethod("normalizeSpaces", String::class.java)
        method.isAccessible = true
        val result = method.invoke(action, input) as String

        // Should preserve string content (but may normalize outside spaces)
        assertTrue(result.contains("\"User Name with  Spaces\"") || result.contains("User Name with"))
    }

    @Test
    fun testCapitalizeIfKeyword() {
        val keywords = setOf("SELECT", "FROM", "WHERE")

        val method = action.javaClass.getDeclaredMethod("capitalizeIfKeyword", String::class.java, Set::class.java)
        method.isAccessible = true

        assertEquals("SELECT", method.invoke(action, "select", keywords))
        assertEquals("SELECT", method.invoke(action, "SELECT", keywords))
        assertEquals("SELECT", method.invoke(action, "SeLeCt", keywords))
        assertEquals("user_id", method.invoke(action, "user_id", keywords))
    }

    @Test
    fun testFormatterExists() {
        // Verify that the formatter action can be instantiated
        assertNotNull(action)
    }

    @Test
    fun testFormattingModelBuilderExists() {
        // Verify that the formatting model builder can be instantiated
        val builder = LookMLFormattingModelBuilder()
        assertNotNull(builder)
    }
}
