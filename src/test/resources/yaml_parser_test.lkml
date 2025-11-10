---
# Simple YAML Dashboard Test File
# This file tests the key YAML parsing improvements

- dashboard: parser_test_dashboard
  title: "YAML Parser Test"
  description: |
    This dashboard tests various YAML syntax features
    that were previously causing parsing errors.
  layout: newspaper
  preferred_viewer: dashboards-next
  
  # Test: Boolean values (true/false)
  refresh: 1 day
  filters:
  - name: date_filter
    title: Date Filter
    type: field_filter
    default_value: 7 day
    allow_multiple_values: true
    required: false
    ui_config:
      type: advanced
      display: popover
    model: test_model
    explore: test_explore
    field: test_table.created_date
    
  elements:
  # Test: Basic element with sorts containing desc
  - title: Revenue by Month
    name: Revenue by Month
    model: ecommerce
    explore: orders
    type: looker_line
    fields: [orders.created_month, orders.total_revenue]
    sorts: [orders.created_month desc]
    limit: 500
    
    # Test: Property names with pipes
    series_labels:
      orders.total_revenue: "Total Revenue"
    
    # Test: Nested properties
    x_axis_gridlines: false
    y_axis_gridlines: true
    show_view_names: false
    show_y_axis_labels: true
    show_y_axis_ticks: true
    y_axis_tick_density: default
    y_axis_tick_density_custom: 5
    
    # Test: Row/col/width/height properties
    row: 0
    col: 0
    width: 12
    height: 8
    
  # Test: Element with conditional formatting
  - title: Sales Table
    name: Sales Table
    type: looker_grid
    fields: [products.category, orders.count, orders.total_revenue]
    sorts: [orders.total_revenue desc]
    limit: 10
    
    # Test: Complex nested structure
    conditional_formatting: [
      {
        type: greater_than,
        value: 1000,
        background_color: "#3EB0D5",
        font_color: white,
        bold: true
      }
    ]
    
    # Test: Boolean values
    show_totals: true
    show_row_totals: true
    show_row_numbers: false
    
    row: 8
    col: 0
    width: 12
    height: 6

# Test: Property with pipe in name
  - title: Units by Category
    name: Units by Category
    fields: [products.category, label|inventory.units_in_stock: "Units in Stock"]
    
    # Test: Multiple sorts with desc/asc
    sorts: [
      products.category asc,
      label|inventory.units_in_stock desc
    ]
    
    row: 14
    col: 0
    width: 12
    height: 6
