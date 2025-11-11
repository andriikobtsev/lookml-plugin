package com.yourcompany.lookml.completion

/**
 * Comprehensive list of LookML YAML dashboard properties
 * Based on Looker documentation: https://cloud.google.com/looker/docs/reference/param-lookml-dashboard
 */
object YamlDashboardProperties {

    /**
     * Top-level dashboard properties
     */
    val DASHBOARD_PROPERTIES = mapOf(
        "dashboard" to "Dashboard name (letters, numbers, dashes, underscores)",
        "title" to "Dashboard display title",
        "description" to "Dashboard description",
        "layout" to "Layout type: newspaper, grid, static, or tile",
        "preferred_viewer" to "Preferred viewer (deprecated)",
        "extends" to "Base dashboard to extend from",
        "extension" to "Set to 'required' to mandate extension",

        // Display options
        "tile_size" to "Pixel size for tiles (default: 160)",
        "width" to "Total dashboard width in pixels (default: 1200)",
        "refresh" to "Auto-refresh interval (e.g., '2 hours')",
        "auto_run" to "Auto-run on open (yes/no, default: yes)",

        // Features
        "crossfilter_enabled" to "Enable cross-filtering (yes/no)",
        "enable_viz_full_screen" to "Allow full-screen views (yes/no, default: yes)",

        // Filter bar
        "filters_bar_collapsed" to "Filter bar collapsed by default (yes/no)",
        "filters_location_top" to "Filter bar at top (yes/no, default: yes)",

        // Collections
        "filters" to "Dashboard-level filters",
        "elements" to "Dashboard visualization elements",
        "rows" to "Element rows for grid layouts",
        "embed_style" to "Embedded dashboard appearance"
    )

    /**
     * Dashboard element properties (go inside elements array)
     */
    val ELEMENT_PROPERTIES = mapOf(
        // Core identification
        "name" to "Element unique name",
        "title" to "Element display title",
        "type" to "Visualization type",

        // Query definition
        "model" to "Model name",
        "explore" to "Explore name",
        "fields" to "List of fields [view.field1, view.field2]",
        "filters" to "Element-specific filters",
        "sorts" to "Sort specification [field desc/asc]",
        "limit" to "Row limit (number)",
        "column_limit" to "Column limit for pivots",
        "total" to "Show totals (yes/no)",
        "row_total" to "Show row totals (right/left/no)",
        "subtotals" to "Show subtotals [field1, field2]",

        // Positioning (for grid/tile/static layouts)
        "row" to "Row position (number)",
        "col" to "Column position (number)",
        "width" to "Element width",
        "height" to "Element height",

        // Query options
        "query_timezone" to "Query timezone",
        "listen" to "Listen to dashboard filters",
        "dynamic_fields" to "Dynamic field definitions",
        "pivots" to "Pivot fields",
        "fill_fields" to "Fields to fill in",

        // Display options
        "show_view_names" to "Show view names (yes/no)",
        "show_row_numbers" to "Show row numbers (yes/no)",
        "transpose" to "Transpose table (yes/no)",
        "truncate_text" to "Truncate text (yes/no)",
        "hide_totals" to "Hide totals (yes/no)",
        "hide_row_totals" to "Hide row totals (yes/no)",
        "size_to_fit" to "Size to fit (yes/no)",
        "table_theme" to "Table theme",
        "limit_displayed_rows" to "Limit displayed rows (yes/no)",
        "enable_conditional_formatting" to "Enable conditional formatting (yes/no)",

        // Chart-specific
        "x_axis_gridlines" to "Show X axis gridlines (yes/no)",
        "y_axis_gridlines" to "Show Y axis gridlines (yes/no)",
        "show_y_axis_labels" to "Show Y axis labels (yes/no)",
        "show_y_axis_ticks" to "Show Y axis ticks (yes/no)",
        "y_axis_tick_density" to "Y axis tick density",
        "y_axis_tick_density_custom" to "Custom Y axis tick density",
        "show_x_axis_label" to "Show X axis label (yes/no)",
        "show_x_axis_ticks" to "Show X axis ticks (yes/no)",
        "y_axis_scale_mode" to "Y axis scale mode (linear/log)",
        "x_axis_reversed" to "Reverse X axis (yes/no)",
        "y_axis_reversed" to "Reverse Y axis (yes/no)",
        "plot_size_by_field" to "Field to size plot by",
        "trellis" to "Trellis/faceting configuration",

        // Series customization
        "series_types" to "Series visualization types",
        "series_colors" to "Series color mapping",
        "series_labels" to "Series label mapping",
        "series_point_styles" to "Point styles for series",

        // Value formatting
        "value_labels" to "Value label display",
        "label_density" to "Label density",
        "x_axis_scale" to "X axis scale type",
        "y_axis_combined" to "Combine Y axes (yes/no)",
        "show_null_points" to "Show null points (yes/no)",
        "interpolation" to "Line interpolation",

        // Single value specific
        "single_value_title" to "Single value title",
        "show_single_value_title" to "Show single value title (yes/no)",
        "show_comparison" to "Show comparison (yes/no)",
        "comparison_type" to "Comparison type",
        "comparison_reverse_colors" to "Reverse comparison colors (yes/no)",
        "comparison_label" to "Comparison label",

        // Styling
        "font_size" to "Font size",
        "text_color" to "Text color",
        "custom_color_enabled" to "Enable custom color (yes/no)",
        "custom_color" to "Custom color value",
        "show_value_labels" to "Show value labels (yes/no)",
        "show_percent_labels" to "Show percent labels (yes/no)",

        // Maps
        "map_plot_mode" to "Map plot mode",
        "heatmap_gridlines" to "Heatmap gridlines (yes/no)",
        "heatmap_gridlines_empty" to "Empty heatmap gridlines (yes/no)",
        "heatmap_opacity" to "Heatmap opacity",
        "show_region_field" to "Show region field (yes/no)",
        "show_value_field" to "Show value field (yes/no)",
        "map_type" to "Map type",
        "map_tile_provider" to "Map tile provider",
        "map_position" to "Map position",
        "map_scale_indicator" to "Map scale indicator",
        "map_pannable" to "Map pannable (yes/no)",
        "map_zoomable" to "Map zoomable (yes/no)",

        // Note/text tiles
        "note_text" to "Note/text content",
        "note_state" to "Note state (expanded/collapsed)",
        "note_display" to "Note display mode",

        // Advanced
        "hidden_fields" to "Fields to hide",
        "hidden_pivots" to "Pivots to hide",
        "color_application" to "Color application settings",
        "ordering" to "Ordering specification",
        "show_totals_labels" to "Show totals labels (yes/no)",
        "show_silhouette" to "Show silhouette (yes/no)",
        "totals_color" to "Totals color",
        "defaults_version" to "Defaults version (0 or 1)",

        // Additional table properties
        "header_text_alignment" to "Header text alignment (left/center/right)",
        "header_font_size" to "Header font size",
        "rows_font_size" to "Rows font size",
        "conditional_formatting_include_totals" to "Include totals in conditional formatting",
        "conditional_formatting_include_nulls" to "Include nulls in conditional formatting",
        "show_sql_query_menu_options" to "Show SQL query menu options",
        "show_totals" to "Show totals (yes/no)",
        "show_row_totals" to "Show row totals (yes/no)",
        "truncate_header" to "Truncate header (yes/no)",
        "minimum_column_width" to "Minimum column width",
        "series_cell_visualizations" to "Cell visualizations per series",
        "series_collapsed" to "Collapsed series",
        "conditional_formatting" to "Conditional formatting rules",
        "column_order" to "Column order array",
        "series_value_format" to "Value format per series",

        // Chart properties
        "stacking" to "Stacking mode (normal/percent/none)",
        "legend_position" to "Legend position (center/left/right)",
        "point_style" to "Point style (none/circle/square)",
        "show_value_labels" to "Show value labels (yes/no)",
        "show_null_labels" to "Show null labels (yes/no)",
        "x_axis_zoom" to "Enable X axis zoom (yes/no)",
        "y_axis_zoom" to "Enable Y axis zoom (yes/no)",
        "y_axes" to "Y axes configuration",

        // Pie/Donut specific
        "label_type" to "Label type (labPer/lab/per/value/legend/labVal)",

        // Text tile specific
        "title_text" to "Title text for text tile",
        "subtitle_text" to "Subtitle text",
        "body_text" to "Body text content",

        // Marketplace visualizations
        "font_size_main" to "Main font size",
        "orientation" to "Orientation (horizontal/vertical)",
        "theme" to "Theme (traditional/modern/etc)",
        "customTheme" to "Custom theme definition",
        "showTooltip" to "Show tooltip (yes/no)",
        "showHighlight" to "Show highlight (yes/no)",
        "rowSubtotals" to "Row subtotals (yes/no)",
        "colSubtotals" to "Column subtotals (yes/no)",
        "spanRows" to "Span rows (yes/no)",
        "spanCols" to "Span columns (yes/no)",
        "calculateOthers" to "Calculate others (yes/no)",
        "sortColumnsBy" to "Sort columns by",
        "useViewName" to "Use view name (yes/no)",
        "useHeadings" to "Use headings (yes/no)",
        "useShortName" to "Use short name (yes/no)",
        "useUnit" to "Use unit (yes/no)",
        "groupVarianceColumns" to "Group variance columns (yes/no)",
        "genericLabelForSubtotals" to "Generic label for subtotals (yes/no)",
        "indexColumn" to "Index column (yes/no)",
        "transposeTable" to "Transpose table (yes/no)",
        "minWidthForIndexColumns" to "Min width for index columns (yes/no)",
        "headerFontSize" to "Header font size",
        "bodyFontSize" to "Body font size",
        "subtotalDepth" to "Subtotal depth",
        "dividers" to "Show dividers (yes/no)",
        "title_hidden" to "Hide title (yes/no)",

        // Properties with pipe syntax (field-specific)
        "label" to "Label (can use label|field.name syntax)",
        "heading" to "Heading (can use heading|field.name syntax)",
        "hide" to "Hide field (can use hide|field.name syntax)",
        "style" to "Style (can use style|field.name syntax)",
        "reportIn" to "Report in (can use reportIn|field.name syntax)",
        "unit" to "Unit (can use unit|field.name syntax)",
        "comparison" to "Comparison (can use comparison|field.name syntax)",
        "switch" to "Switch (can use switch|field.name syntax)",
        "var_num" to "Variance number (can use var_num|field.name syntax)",
        "var_pct" to "Variance percent (can use var_pct|field.name syntax)",

        // Additional styling
        "show_title" to "Show title (can be field-specific)",
        "title_override" to "Title override (can be field-specific)",
        "title_placement" to "Title placement (can be field-specific)",
        "value_format" to "Value format",
        "show_comparison" to "Show comparison (can be field-specific)",

        // Advanced chart options
        "preferred_slug" to "Preferred slug for dashboard URL"
    )

    /**
     * Filter properties (inside filters array)
     */
    val FILTER_PROPERTIES = mapOf(
        "name" to "Filter name",
        "title" to "Filter title",
        "type" to "Filter type: field_filter, date_filter, number_filter, string_filter",
        "default_value" to "Default filter value",
        "allow_multiple_values" to "Allow multiple values (yes/no)",
        "required" to "Filter is required (yes/no)",
        "ui_config" to "UI configuration object",
        "model" to "Model for field filter",
        "explore" to "Explore for field filter",
        "field" to "Field for field filter",
        "listens_to_filters" to "Filters this filter listens to (array)"
    )

    /**
     * UI config properties (inside filter ui_config)
     */
    val UI_CONFIG_PROPERTIES = mapOf(
        "type" to "UI type: checkboxes, button_group, advanced, etc.",
        "display" to "Display mode: inline, popover, overflow"
    )

    /**
     * Visualization types for 'type' property
     */
    val VISUALIZATION_TYPES = listOf(
        // Standard Looker visualizations
        "looker_line",
        "looker_area",
        "looker_column",
        "looker_bar",
        "looker_scatter",
        "looker_pie",
        "looker_donut_multiples",
        "looker_funnel",
        "looker_timeline",
        "looker_waterfall",
        "looker_boxplot",
        "single_value",
        "looker_single_record",
        "table",
        "looker_grid",
        "text",
        "button",

        // Maps
        "looker_map",
        "looker_geo_coordinates",
        "looker_geo_choropleth",
        "looker_google_map",

        // Marketplace visualizations (commonly used)
        "marketplace_viz_multiple_value::multiple_value-marketplace",
        "marketplace_viz_report_table::report_table-marketplace"
    )

    /**
     * Layout types for 'layout' property
     */
    val LAYOUT_TYPES = listOf(
        "newspaper",
        "grid",
        "static",
        "tile"
    )

    /**
     * Filter types for 'type' property in filters
     */
    val FILTER_TYPES = listOf(
        "field_filter",
        "date_filter",
        "number_filter",
        "string_filter"
    )
}
