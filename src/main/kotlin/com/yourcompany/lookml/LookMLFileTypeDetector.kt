package com.yourcompany.lookml

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile

enum class LookMLFileSubType {
    MODEL,      // Contains connection, includes, explores, etc.
    VIEW,       // Contains view definitions with dimensions/measures
    EXPLORE,    // Standalone explore file
    DASHBOARD,  // Dashboard definitions
    MANIFEST,   // Project manifest file
    UNKNOWN     // Mixed or unknown content
}

object LookMLFileTypeDetector {
    
    fun detectFileType(file: PsiFile): LookMLFileSubType {
        val text = file.text
        return detectFromContent(text)
    }
    
    fun detectFileType(file: VirtualFile): LookMLFileSubType {
        if (file.name == "manifest.lkml") {
            return LookMLFileSubType.MANIFEST
        }
        
        // For virtual files, we'd need to read content
        // This is a simplified version
        return LookMLFileSubType.UNKNOWN
    }
    
    fun detectFromContent(content: String): LookMLFileSubType {
        // Remove comments for analysis
        val cleanContent = content
            .replace(Regex("#.*$", RegexOption.MULTILINE), "")
            .replace(Regex("/\\*.*?\\*/", RegexOption.DOT_MATCHES_ALL), "")
        
        // Count different top-level elements
        val hasConnection = cleanContent.contains(Regex("\\bconnection\\s*:"))
        val hasIncludes = cleanContent.contains(Regex("\\binclude\\s*:"))
        val hasExplore = cleanContent.contains(Regex("\\bexplore\\s*:"))
        val hasView = cleanContent.contains(Regex("\\bview\\s*:"))
        val hasDashboard = cleanContent.contains(Regex("\\b(dashboard|element|filter|layout)\\s*:"))
        val hasDatagroup = cleanContent.contains(Regex("\\bdatagroup\\s*:"))
        val hasAccessGrant = cleanContent.contains(Regex("\\baccess_grant\\s*:"))
        
        // Model files typically have connection and/or includes at the top level
        if (hasConnection || (hasIncludes && (hasExplore || hasDatagroup || hasAccessGrant))) {
            return LookMLFileSubType.MODEL
        }
        
        // Dashboard files have dashboard-specific elements
        if (hasDashboard) {
            return LookMLFileSubType.DASHBOARD
        }
        
        // View files primarily contain view definitions
        if (hasView && !hasExplore && !hasConnection) {
            return LookMLFileSubType.VIEW
        }
        
        // Explore-only files (rare but possible)
        if (hasExplore && !hasView && !hasConnection) {
            return LookMLFileSubType.EXPLORE
        }
        
        return LookMLFileSubType.UNKNOWN
    }
    
    /**
     * Get allowed top-level properties for a file type
     */
    fun getAllowedTopLevelProperties(fileType: LookMLFileSubType): Set<String> {
        return when (fileType) {
            LookMLFileSubType.MODEL -> setOf(
                "connection", "include", "explore", "view", "datagroup", 
                "access_grant", "map_layer", "named_value_format", "fiscal_month_offset",
                "persist_for", "persist_with", "week_start_day", "case_sensitive"
            )
            LookMLFileSubType.VIEW -> setOf(
                "view", "include"
            )
            LookMLFileSubType.EXPLORE -> setOf(
                "explore", "include"
            )
            LookMLFileSubType.DASHBOARD -> setOf(
                "dashboard", "include"
            )
            LookMLFileSubType.MANIFEST -> setOf(
                "project_name", "application", "local_dependency", "remote_dependency",
                "new_lookml_runtime", "constant", "includes", "models"
            )
            LookMLFileSubType.UNKNOWN -> emptySet() // Allow anything for unknown files
        }
    }
    
    /**
     * Get required properties for specific contexts
     */
    fun getRequiredProperties(context: String): Set<String> {
        return when (context) {
            "dimension", "measure" -> setOf("type")
            "join" -> setOf("sql_on", "relationship")
            "dashboard" -> setOf("title")
            else -> emptySet()
        }
    }
    
    /**
     * Get valid values for enum-like properties
     */
    fun getValidPropertyValues(property: String): Set<String>? {
        return when (property) {
            "type" -> setOf(
                "string", "number", "date", "date_time", "date_raw", "date_month_name",
                "date_day_of_week", "date_fiscal_quarter", "yesno", "zipcode", "tier",
                "time", "duration", "location", "distance", "list", "count", "count_distinct",
                "sum", "average", "median", "percentile", "max", "min", "date_month_num",
                "date_quarter", "date_fiscal_month_num", "date_day_of_month", "date_week",
                "sum_distinct", "percent_of_total", "percent_of_previous"
            )
            "relationship" -> setOf("one_to_one", "one_to_many", "many_to_one", "many_to_many")
            "join_type", "type" -> setOf("left_outer", "inner", "full_outer", "cross")
            "interval" -> setOf("day", "hour", "minute", "second", "week", "month", "quarter", "year")
            "style" -> setOf("integer", "decimal", "percent", "currency", "scientific")
            "value_format", "value_format_name" -> setOf(
                "decimal_0", "decimal_1", "decimal_2", "percent_0", "percent_1", "percent_2",
                "usd_0", "usd", "eur_0", "eur", "gbp_0", "gbp"
            )
            else -> null
        }
    }
}