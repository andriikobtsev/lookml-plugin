package com.yourcompany.lookml.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.yourcompany.lookml.LookMLIcons
import javax.swing.Icon

class LookMLColorSettingsPage : ColorSettingsPage {
    override fun getIcon(): Icon = LookMLIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = LookMLSyntaxHighlighter()

    override fun getDemoText(): String = """
        # This is a comment
        include: "*.view"
        
        view: user_facts {
          sql_table_name: public.user_facts ;;
          
          dimension: user_id {
            type: number
            primary_key: yes
            sql: ${'$'}{TABLE}.user_id ;;
          }
          
          dimension_group: created {
            type: time
            timeframes: [date, week, month, year]
            sql: ${'$'}{TABLE}.created_at ;;
          }
          
          measure: total_orders {
            type: count_distinct
            sql: ${'$'}{TABLE}.order_id ;;
            description: "Total number of orders"
          }
          
          filter: date_filter {
            type: date
          }
        }
        
        explore: orders {
          label: "Order Analysis"
          
          join: users {
            type: left_outer
            relationship: many_to_one
            sql_on: ${'$'}{orders.user_id} = ${'$'}{users.id} ;;
          }
        }
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "LookML"

    companion object {
        private val DESCRIPTORS = arrayOf(
            AttributesDescriptor("Keywords", TextAttributesKey.createTextAttributesKey("LOOKML_KEYWORD")),
            AttributesDescriptor("Properties", TextAttributesKey.createTextAttributesKey("LOOKML_PROPERTY")),
            AttributesDescriptor("Strings", TextAttributesKey.createTextAttributesKey("LOOKML_STRING")),
            AttributesDescriptor("Numbers", TextAttributesKey.createTextAttributesKey("LOOKML_NUMBER")),
            AttributesDescriptor("Comments", TextAttributesKey.createTextAttributesKey("LOOKML_COMMENT")),
            AttributesDescriptor("SQL blocks", TextAttributesKey.createTextAttributesKey("LOOKML_SQL")),
            AttributesDescriptor("Braces", TextAttributesKey.createTextAttributesKey("LOOKML_BRACES")),
            AttributesDescriptor("Brackets", TextAttributesKey.createTextAttributesKey("LOOKML_BRACKETS")),
            AttributesDescriptor("Operators", TextAttributesKey.createTextAttributesKey("LOOKML_OPERATOR")),
            AttributesDescriptor("Bad characters", TextAttributesKey.createTextAttributesKey("LOOKML_BAD_CHARACTER"))
        )
    }
}
