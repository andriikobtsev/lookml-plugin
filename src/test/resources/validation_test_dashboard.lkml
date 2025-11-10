# Dashboard file test - should validate as DASHBOARD type
dashboard: sales_overview {
  # Missing required title - should error
  
  layout: newspaper
  preferred_viewer: dashboards-next
  
  elements: {
    title: "Revenue by Month"
    type: looker_line
    explore: orders
    dimensions: [orders.created_month]
    measures: [orders.total_revenue]
    
    # More elements would go here
  }
  
  filters: {
    name: date_filter
    title: "Date Range"
    type: date_filter
    default_value: "7 days"
  }
}

# Valid dashboard with title
dashboard: marketing_dashboard {
  title: "Marketing Performance"
  
  elements: {
    title: "Campaign Performance"
    type: looker_column
  }
}

# This shouldn't be in a dashboard file - should warn
explore: orders {
  # Explores belong in model files
}