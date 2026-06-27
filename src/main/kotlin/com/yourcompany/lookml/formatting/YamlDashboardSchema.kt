package com.yourcompany.lookml.formatting

enum class ValueType {
    STRING,   // Simple string value
    LIST,     // List of objects (followed by - items)
    MAP,      // Map/object (followed by key: value properties)
    UNKNOWN   // Unknown or unspecified
}

enum class ObjectType {
    DASHBOARD,
    ELEMENT,
    DASHBOARD_FILTER,
    ELEMENT_FILTER,  // For future nested filters
    UNKNOWN
}

data class SemanticInfo(
    val objectType: ObjectType,      // What object does this property belong to
    val propertyName: String?,       // Name of the property
    val valueType: ValueType,        // Type of value this property expects
    val level: Int                   // Nesting level (0 = dashboard, 1 = element, etc.)
)

object YamlDashboardSchema {

    // Dashboard object properties (top-level only)
    private val DASHBOARD_PROPERTIES = mapOf(
        "dashboard" to ValueType.STRING,
        "title" to ValueType.STRING,
        "layout" to ValueType.STRING,
        "tile_size" to ValueType.STRING,
        "auto_run" to ValueType.STRING,
        "load_configuration" to ValueType.STRING,
        "lookml_link_id" to ValueType.STRING,
        "show_filters_bar" to ValueType.STRING,
        "show_title" to ValueType.STRING,
        "refresh_interval" to ValueType.STRING,
        "refresh" to ValueType.STRING,
        "preferred_viewer" to ValueType.STRING,
        "description" to ValueType.STRING,
        "preferred_slug" to ValueType.STRING,
        "extends" to ValueType.STRING,
        "extension" to ValueType.STRING,
        "crossfilter_enabled" to ValueType.STRING,
        "elements" to ValueType.LIST,
        "filters" to ValueType.LIST,
        "listen" to ValueType.MAP,
        "rows" to ValueType.LIST  // For grid layout
    )

    // Element object properties (inside elements list)
    // This is a COMPREHENSIVE list from Looker documentation + your real-world examples
    private val ELEMENT_PROPERTIES = mapOf(
        // ===== BASIC ELEMENT PROPERTIES =====
        "title" to ValueType.STRING,
        "name" to ValueType.STRING,
        "model" to ValueType.STRING,
        "explore" to ValueType.STRING,
        "type" to ValueType.STRING,
        "note_state" to ValueType.STRING,
        "note_display" to ValueType.STRING,
        "note_text" to ValueType.STRING,
        "rich_content_json" to ValueType.STRING,  // For buttons
        
        // ===== POSITIONING (CRITICAL - newspaper layout) =====
        "row" to ValueType.STRING,
        "col" to ValueType.STRING,
        "width" to ValueType.STRING,
        "height" to ValueType.STRING,
        "top" to ValueType.STRING,  // For static layout
        "left" to ValueType.STRING,  // For static layout
        
        // ===== QUERY PARAMETERS =====
        "fields" to ValueType.UNKNOWN,  // Can be list or string
        "fill_fields" to ValueType.UNKNOWN,
        "filters" to ValueType.MAP,  // Element filters are MAP, not LIST
        "filter_expression" to ValueType.STRING,
        "sorts" to ValueType.UNKNOWN,
        "limit" to ValueType.STRING,
        "column_limit" to ValueType.STRING,
        "total" to ValueType.STRING,
        "row_total" to ValueType.STRING,
        "subtotals" to ValueType.UNKNOWN,
        "query_timezone" to ValueType.STRING,
        "dynamic_fields" to ValueType.UNKNOWN,
        "pivots" to ValueType.UNKNOWN,
        "dimensions" to ValueType.UNKNOWN,
        "measures" to ValueType.UNKNOWN,
        "merged_queries" to ValueType.LIST,
        
        // ===== CHART LAYOUT & APPEARANCE =====
        "stacking" to ValueType.STRING,
        "show_value_labels" to ValueType.STRING,
        "label_density" to ValueType.STRING,
        "label_value_format" to ValueType.STRING,
        "legend_position" to ValueType.STRING,
        "hide_legend" to ValueType.STRING,
        "point_style" to ValueType.STRING,
        "show_null_points" to ValueType.STRING,
        "show_null_labels" to ValueType.STRING,
        "interpolation" to ValueType.STRING,
        
        // ===== AXES & GRIDLINES =====
        "x_axis_gridlines" to ValueType.STRING,
        "y_axis_gridlines" to ValueType.STRING,
        "show_view_names" to ValueType.STRING,
        "show_y_axis_labels" to ValueType.STRING,
        "show_y_axis_ticks" to ValueType.STRING,
        "y_axis_tick_density" to ValueType.STRING,
        "y_axis_tick_density_custom" to ValueType.STRING,
        "show_x_axis_label" to ValueType.STRING,
        "show_x_axis_ticks" to ValueType.STRING,
        "x_axis_scale" to ValueType.STRING,
        "y_axis_scale_mode" to ValueType.STRING,
        "x_axis_reversed" to ValueType.STRING,
        "y_axis_reversed" to ValueType.STRING,
        "x_axis_label" to ValueType.STRING,
        "y_axis_label" to ValueType.STRING,
        "x_axis_zoom" to ValueType.STRING,
        "y_axis_zoom" to ValueType.STRING,
        "y_axes" to ValueType.LIST,  // Complex axis configuration
        "x_axis_label_rotation" to ValueType.STRING,
        "plot_size_by_field" to ValueType.STRING,
        
        // ===== COLORS & STYLING =====
        "color_application" to ValueType.MAP,
        "series_types" to ValueType.MAP,
        "series_colors" to ValueType.MAP,
        "series_labels" to ValueType.MAP,
        "series_point_styles" to ValueType.MAP,
        "series_cell_visualizations" to ValueType.MAP,
        "color_range" to ValueType.LIST,
        "color_by_type" to ValueType.STRING,
        "custom_color_enabled" to ValueType.STRING,
        "custom_color" to ValueType.STRING,
        "font_color" to ValueType.STRING,
        "background_color" to ValueType.STRING,
        
        // ===== TABLE-SPECIFIC =====
        "show_row_numbers" to ValueType.STRING,
        "transpose" to ValueType.STRING,
        "truncate_text" to ValueType.STRING,
        "hide_totals" to ValueType.STRING,
        "hide_row_totals" to ValueType.STRING,
        "show_totals" to ValueType.STRING,
        "show_row_totals" to ValueType.STRING,
        "show_totals_labels" to ValueType.STRING,
        "totals_color" to ValueType.STRING,
        "size_to_fit" to ValueType.STRING,
        "table_theme" to ValueType.STRING,
        "enable_conditional_formatting" to ValueType.STRING,
        "header_text_alignment" to ValueType.STRING,
        "header_font_size" to ValueType.STRING,
        "header_font_color" to ValueType.STRING,
        "header_background_color" to ValueType.STRING,
        "rows_font_size" to ValueType.STRING,
        "conditional_formatting_include_totals" to ValueType.STRING,
        "conditional_formatting_include_nulls" to ValueType.STRING,
        "conditional_formatting" to ValueType.LIST,
        "truncate_header" to ValueType.STRING,
        "minimum_column_width" to ValueType.STRING,
        "truncate_column_names" to ValueType.STRING,
        
        // ===== SHOW/HIDE OPTIONS =====
        "show_sql_query_menu_options" to ValueType.STRING,
        "show_comparison" to ValueType.STRING,
        "show_comparison_label" to ValueType.STRING,
        "show_single_value_title" to ValueType.STRING,
        "show_silhouette" to ValueType.STRING,
        "hidden_fields" to ValueType.LIST,
        "hidden_points_if_no" to ValueType.LIST,
        "hidden_pivots" to ValueType.MAP,
        "hidden_series" to ValueType.LIST,
        
        // ===== SINGLE VALUE CHART =====
        "single_value_title" to ValueType.STRING,
        "comparison_type" to ValueType.STRING,
        "comparison_reverse_colors" to ValueType.STRING,
        "comparison_label" to ValueType.STRING,
        
        // ===== PIE/DONUT CHARTS =====
        "value_labels" to ValueType.STRING,
        "label_type" to ValueType.STRING,
        "inner_radius" to ValueType.STRING,
        "start_angle" to ValueType.STRING,
        "end_angle" to ValueType.STRING,
        
        // ===== MAP VISUALIZATIONS =====
        "map_plot_mode" to ValueType.STRING,
        "heatmap_gridlines" to ValueType.STRING,
        "heatmap_gridlines_empty" to ValueType.STRING,
        "heatmap_opacity" to ValueType.STRING,
        "show_region_field" to ValueType.STRING,
        "draw_map_labels_above_data" to ValueType.STRING,
        "map_tile_provider" to ValueType.STRING,
        "map_position" to ValueType.STRING,
        "map_scale_indicator" to ValueType.STRING,
        "map_pannable" to ValueType.STRING,
        "map_zoomable" to ValueType.STRING,
        "map_marker_type" to ValueType.STRING,
        "map_marker_icon_name" to ValueType.STRING,
        "map_marker_radius_mode" to ValueType.STRING,
        "map_marker_units" to ValueType.STRING,
        "map_marker_proportional_scale_type" to ValueType.STRING,
        "map_marker_color_mode" to ValueType.STRING,
        "show_legend" to ValueType.STRING,
        "quantize_map_value_colors" to ValueType.STRING,
        "reverse_map_value_colors" to ValueType.STRING,
        
        // ===== ADVANCED VISUALIZATION OPTIONS =====
        "ordering" to ValueType.STRING,
        "show_dropoff" to ValueType.STRING,
        "trellis" to ValueType.STRING,
        "limit_displayed_rows" to ValueType.STRING,
        "limit_displayed_rows_values" to ValueType.MAP,
        "reference_lines" to ValueType.LIST,
        "trend_lines" to ValueType.LIST,
        
        // ===== FORMATTING =====
        "font_size" to ValueType.STRING,
        "value_format" to ValueType.STRING,
        "defaults_version" to ValueType.STRING,
        
        // ===== LISTEN (DASHBOARD FILTERS) =====
        "listen" to ValueType.MAP,
        
        // ===== CUSTOM VISUALIZATION OPTIONS =====
        // These are from various custom visualizations and real dashboards
        "up_color" to ValueType.STRING,
        "down_color" to ValueType.STRING,
        "total_color" to ValueType.STRING,
        "toColor" to ValueType.LIST,
        "value_titles" to ValueType.STRING,
        "font_size_value" to ValueType.STRING,
        "font_size_label" to ValueType.STRING,
        "leftAxisLabelVisible" to ValueType.STRING,
        "leftAxisLabel" to ValueType.STRING,
        "rightAxisLabelVisible" to ValueType.STRING,
        "rightAxisLabel" to ValueType.STRING,
        "smoothedBars" to ValueType.STRING,
        "orientation" to ValueType.STRING,
        "labelPosition" to ValueType.STRING,
        "percentType" to ValueType.STRING,
        "percentPosition" to ValueType.STRING,
        "valuePosition" to ValueType.STRING,
        "labelColorEnabled" to ValueType.STRING,
        "labelColor" to ValueType.STRING,
        "charts_across" to ValueType.STRING,
        "groupBars" to ValueType.STRING,
        "labelSize" to ValueType.STRING,
        "showLegend" to ValueType.STRING,
        
        // ===== ADDITIONAL CHART-SPECIFIC =====
        "color_picker" to ValueType.LIST,
        "formatting_override" to ValueType.STRING,
        "rounded" to ValueType.STRING,
        "outline" to ValueType.STRING,
        "label_year" to ValueType.STRING,
        "label_month" to ValueType.STRING,
        "viz_show_legend" to ValueType.STRING,
        "focus_tooltip" to ValueType.STRING,
        "outline_weight" to ValueType.STRING,
        "cell_color" to ValueType.STRING,
        "outline_color" to ValueType.STRING,
        "cell_reducer" to ValueType.STRING,
        "axis_label_color" to ValueType.STRING,
        
        // ===== GAUGE/RADIAL CHARTS =====
        "arm_length" to ValueType.STRING,
        "arm_weight" to ValueType.STRING,
        "spinner_length" to ValueType.STRING,
        "spinner_weight" to ValueType.STRING,
        "target_length" to ValueType.STRING,
        "target_gap" to ValueType.STRING,
        "target_weight" to ValueType.STRING,
        "range_min" to ValueType.STRING,
        "range_max" to ValueType.STRING,
        "value_label_type" to ValueType.STRING,
        "value_label_font" to ValueType.STRING,
        "value_label_padding" to ValueType.STRING,
        "target_source" to ValueType.STRING,
        "target_label_type" to ValueType.STRING,
        "target_label_font" to ValueType.STRING,
        "label_font_size" to ValueType.STRING,
        "spinner_type" to ValueType.STRING,
        "fill_color" to ValueType.STRING,
        "spinner_color" to ValueType.STRING,
        "range_color" to ValueType.STRING,
        "gauge_fill_type" to ValueType.STRING,
        "fill_colors" to ValueType.LIST,
        "viz_trellis_by" to ValueType.STRING,
        "trellis_cols" to ValueType.STRING,
        "angle" to ValueType.STRING,
        "cutout" to ValueType.STRING,
        "range_x" to ValueType.STRING,
        "range_y" to ValueType.STRING,
        "target_label_padding" to ValueType.STRING,
        
        // ===== COLUMN/BAR CHART SPACING =====
        "column_spacing_ratio" to ValueType.STRING,
        "column_group_spacing_ratio" to ValueType.STRING,
        
        // ===== SWAP/TRANSFORM OPTIONS =====
        "swap_axes" to ValueType.STRING,
        
        // ===== ADDITIONAL OPTIONS FROM REAL DASHBOARDS =====
        "y_axis_combined" to ValueType.STRING,
        "is_active" to ValueType.STRING,
        "bold" to ValueType.STRING,
        "italic" to ValueType.STRING,
        "strikethrough" to ValueType.STRING,
        
        // ===== VIS_CONFIG (CATCH-ALL FOR CUSTOM VIZ) =====
        "vis_config" to ValueType.MAP
    )

    // Dashboard filter object properties
    private val DASHBOARD_FILTER_PROPERTIES = mapOf(
        "name" to ValueType.STRING,
        "title" to ValueType.STRING,
        "type" to ValueType.STRING,
        "default_value" to ValueType.STRING,
        "allow_multiple_values" to ValueType.STRING,
        "required" to ValueType.STRING,
        "model" to ValueType.STRING,
        "explore" to ValueType.STRING,
        "field" to ValueType.STRING,
        "listens_to_filters" to ValueType.LIST,
        "ui_config" to ValueType.MAP
    )

    // UI config properties (nested under ui_config:)
    private val UI_CONFIG_PROPERTIES = mapOf(
        "type" to ValueType.STRING,
        "display" to ValueType.STRING,
        "options" to ValueType.MAP
    )

    /**
     * Get value type for a property in a given object context
     */
    fun getValueType(objectType: ObjectType, propertyName: String): ValueType {
        return when (objectType) {
            ObjectType.DASHBOARD -> DASHBOARD_PROPERTIES[propertyName] ?: ValueType.UNKNOWN
            ObjectType.ELEMENT -> ELEMENT_PROPERTIES[propertyName] ?: ValueType.UNKNOWN
            ObjectType.DASHBOARD_FILTER -> DASHBOARD_FILTER_PROPERTIES[propertyName] ?: ValueType.UNKNOWN
            else -> ValueType.UNKNOWN
        }
    }

    /**
     * Determine object type from list entry key
     */
    fun getObjectTypeFromKey(key: String?): ObjectType {
        return when (key) {
            "dashboard" -> ObjectType.DASHBOARD
            "title", "name" -> ObjectType.ELEMENT  // List entries in elements use title or name
            else -> ObjectType.UNKNOWN
        }
    }
    
    /**
     * Check if a property should belong to an element (not dashboard level)
     */
    fun isElementProperty(propertyName: String): Boolean {
        return ELEMENT_PROPERTIES.containsKey(propertyName)
    }
    
    /**
     * Check if a property should belong to dashboard (not element level)
     */
    fun isDashboardProperty(propertyName: String): Boolean {
        return DASHBOARD_PROPERTIES.containsKey(propertyName)
    }
}
