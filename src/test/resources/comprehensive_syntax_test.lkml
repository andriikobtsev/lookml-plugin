# Comprehensive LookML Syntax Test File
# This file contains examples of all major LookML syntax patterns

# Model file syntax
connection: "database_connection"
persist_with: datagroup_name
week_start_day: monday
case_sensitive: no
label: "E-Commerce Analytics"  # Top-level label statement

# Includes
include: "views/*.view"
include: "/views/**/*.view"
include: "*.dashboard.lookml"


# Datagroup
datagroup: orders_datagroup {


  sql_trigger: SELECT MAX(created_at) FROM orders ;;
  max_cache_age: "4 hours"
  label: "Orders Datagroup"
  description: "Datagroup for orders data"
}

# Access grant
access_grant: can_view_financial_data {
  allowed_values: ["finance", "executive"]
  user_attribute: department
}

# Named value format
named_value_format: usd {
  value_format: "$#,##0.00"
  strict_value_format: yes
}

# Explore
explore: orders {
  from: order_items
                  view_name: orders
  label: "Orders Analysis"
  view_label: "Order Details"
  description: "Explore for analyzing orders"
  group_label: "E-Commerce"
  hidden: no
  
  # Explore-specific properties
  sql_always_where: ${orders.created_date} >= '2020-01-01' ;;
  sql_always_having: ${orders.total_revenue} > 100 ;;
  always_filter: {
    filters: [orders.created_date: "90 days"]
  }
  conditionally_filter: {
    filters: [orders.created_date: "7 days"]
    unless: [orders.user_id, orders.status]
  }
  
  # Access filter
  access_filter: {
    field: orders.region
    user_attribute: region
  }
  
  # Required access grants
  required_access_grants: [can_view_financial_data]
  
  # Joins
  join: users {
    type: left_outer
    sql_on: ${orders.user_id} = ${users.id} ;;
    relationship: many_to_one
    view_label: "Customer"
    fields: [users.basic*]
    required_joins: []
  }
  
  join: order_items {
    type: inner
    sql_on: ${orders.id} = ${order_items.order_id} ;;
    relationship: one_to_many
    sql_where: ${order_items.returned_date} IS NULL ;;
  }
  
  # Aggregate table
  aggregate_table: rollup_daily {
    query: {
      dimensions: [created_date]
      measures: [total_revenue, count]
      timezone: "America/Los_Angeles"
    }
    materialization: {
      datagroup_trigger: orders_datagroup
      partition_keys: ["created_date"]
      cluster_keys: ["region"]
      increment_key: "created_date"
      increment_offset: 3
    }
  }
}

# View
view: orders {
  sql_table_name: `project.dataset.orders` ;;
  # or derived table
  # derived_table: {
  #   sql: SELECT * FROM orders WHERE status = 'complete' ;;
  #   persist_for: "24 hours"
  #   cluster_keys: ["created_date", "region"]
  #   partition_keys: ["created_date"]
  #   datagroup_trigger: orders_datagroup
  # }
  
  # Drill fields set
  drill_fields: [detail*]
  
  # Dimensions
  dimension: id {
    type: string
    primary_key: yes
    sql: ${TABLE}.id ;;
    hidden: no
    label: "Order ID"
    view_label: "Order Info"
    group_label: "IDs"
    group_item_label: "Order"
    description: "Unique order identifier"
    tags: ["ids", "primary_keys"]
    can_filter: yes
    
    # Links
    link: {
      label: "Order Dashboard"
      url: "/dashboards/1?order_id={{ value }}"
      icon_url: "https://example.com/icon.png"
    }
    
    # Actions
    action: {
      label: "Send Order Email"
      url: "https://example.com/api/email"
      icon_url: "https://example.com/email-icon.png"
      form_url: "https://example.com/form"
      param: {
        name: "order_id"
        value: "{{ value }}"
      }
      form_param: {
        name: "email"
        type: string
        label: "Email Address"
        required: yes
        default: "{{ users.email._value }}"
      }
      user_attribute_param: {
        user_attribute: email
        name: "sender_email"
      }
    }
  }
  
  dimension: user_id {
    type: string
    sql: ${TABLE}.user_id ;;
    hidden: yes
  }
  
  # Test dimension with keyword name
  dimension: name {
    type: string
    sql: ${TABLE}.name ;;
    label: "Customer Name"
  }
  
  dimension: status {
    type: string
    sql: ${TABLE}.status ;;
    suggestions: ["pending", "complete", "cancelled"]
    
    # HTML
    html: 
      {% if value == 'complete' %}
        <span style="color: green;">{{ value }}</span>
      {% elsif value == 'cancelled' %}
        <span style="color: red;">{{ value }}</span>
      {% else %}
        <span style="color: orange;">{{ value }}</span>
      {% endif %} ;;
  }
  
  # Dimension with case
  dimension: status_group {
    type: string
    case: {
      when: {
        sql: ${status} IN ('complete', 'shipped') ;;
        label: "Fulfilled"
      }
      when: {
        sql: ${status} = 'cancelled' ;;
        label: "Cancelled"
      }
      else: "In Progress"
    }
    alpha_sort: yes
  }
  
  # Tier dimension
  dimension: order_size_tier {
    type: tier
    tiers: [0, 10, 50, 100, 500, 1000]
    style: integer
    sql: ${item_count} ;;
    value_format_name: decimal_0
  }
  
  # Location dimension
  dimension: location {
    type: location
    sql_latitude: ${TABLE}.latitude ;;
    sql_longitude: ${TABLE}.longitude ;;
  }
  
  # Dimension group
  dimension_group: created {
    type: time
    timeframes: [
      raw,
      time,
      date,
      week,
      month,
      quarter,
      year,
      hour_of_day,
      day_of_week,
      day_of_week_index,
      day_of_month,
      day_of_year,
      week_of_year,
      month_name,
      month_num,
      quarter_of_year,
      fiscal_month_num,
      fiscal_quarter,
      fiscal_quarter_of_year,
      fiscal_year
    ]
    convert_tz: yes
    datatype: datetime
    sql: ${TABLE}.created_at ;;
    
    # Allow fill
    allow_fill: yes
  }
  
  # Measures
  measure: count {
    type: count
    drill_fields: [detail*]
    value_format_name: decimal_0
    
    # Filters on measure
    filters: [status: "complete"]
    filters: {
      field: created_date
      value: "last 90 days"
    }
  }
  
  measure: total_revenue {
    type: sum
    sql: ${TABLE}.revenue ;;
    value_format_name: usd
    precision: 2
    
    # Required fields
    required_fields: [created_date]
  }
  
  measure: average_revenue {
    type: average
    sql: ${TABLE}.revenue ;;
    value_format: "$#,##0.00"
  }
  
  measure: unique_users {
    type: count_distinct
    sql: ${user_id} ;;
    
    # Approximate
    approximate: yes
    approximate_threshold: 100000
  }
  
  # Measure with custom aggregation
  measure: median_revenue {
    type: median
    sql: ${TABLE}.revenue ;;
  }
  
  # Number measure with custom SQL
  measure: conversion_rate {
    type: number
    sql: 1.0 * ${completed_orders} / NULLIF(${count}, 0) ;;
    value_format_name: percent_2
  }
  
  # List measure
  measure: status_list {
    type: list
    list_field: status
  }
  
  # Filtered measure
  measure: completed_orders {
    type: count
    filters: {
      field: status
      value: "complete"
    }
  }
  
  # Parameter
  parameter: date_granularity {
    type: unquoted
    allowed_value: {
      label: "Daily"
      value: "day"
    }
    allowed_value: {
      label: "Weekly"
      value: "week"
    }
    allowed_value: {
      label: "Monthly"
      value: "month"
    }
    default_value: "day"
  }
  
  # Dynamic dimension using parameter
  dimension: dynamic_date {
    type: string
    sql: 
      {% if date_granularity._parameter_value == 'day' %}
        ${created_date}
      {% elsif date_granularity._parameter_value == 'week' %}
        ${created_week}
      {% else %}
        ${created_month}
      {% endif %} ;;
  }
  
  # Filter
  filter: date_filter {
    type: date
    default_value: "last 30 days"
    suggest_dimension: created_date
  }
  
  # Set for drill fields
  set: detail {
    fields: [
      id,
      created_time,
      status,
      users.name,
      users.email,
      total_revenue
    ]
  }
}

# View with explore_source derived table
view: dt_portfolio_facts {
  fields_hidden_by_default: yes
  
  derived_table: {
    explore_source: peakside_v2 {
      column: mfh_portfolio_id { field: tr__mfh_portfolio_current.mfh_portfolio_id }
      column: min_construction_year { field: mart__mfh_current.min_construction_year }
      column: max_construction_year { field: mart__mfh_current.max_construction_year }
      column: entry_date { field: mart__investment_case_current.entry_date }
      column: bp_duration { field: mart__investment_case_bp.bp_duration }
    }
  }
  
  dimension: mfh_portfolio_id {
    type: string
    sql: ${TABLE}.mfh_portfolio_id ;;
  }
  
  dimension: min_construction_year {
    type: number
    sql: ${TABLE}.min_construction_year ;;
  }
  
  dimension: max_construction_year {
    type: number
    sql: ${TABLE}.max_construction_year ;;
  }
  
  dimension: entry_date {
    type: date
    sql: ${TABLE}.entry_date ;;
  }
  
  dimension: bp_duration {
    type: number
    sql: ${TABLE}.bp_duration ;;
  }
}

# Dashboard
dashboard: sales_overview {
  title: "Sales Overview Dashboard"
  layout: newspaper
  preferred_viewer: dashboards-next
  description: "Overview of sales performance"
  refresh: "1 hour"
  
  # Elements
  elements: [
    {
      title: "Total Revenue"
      name: total_revenue_element
      type: single_value
      model: ecommerce
      explore: orders
      dimensions: []
      measures: [orders.total_revenue]
      sorts: [orders.total_revenue desc]
      limit: 500
      
      # Visualization settings
      show_single_value_title: true
      single_value_title: "Revenue"
      value_format: "$#,##0"
      
      # Conditional formatting
      conditional_formatting: [{
        type: greater_than
        value: 1000000
        background_color: "#72D16D"
        font_color: white
        bold: true
      }]
    }
  ]
  
  # Filters
  filters: [
    {
      name: date_filter
      title: "Date Range"
      type: date_filter
      default_value: "30 days"
      allow_multiple_values: true
      required: false
      ui_config: {
        type: relative_timeframes
        display: inline
        options: []
      }
    }
  ]
}

# Test edge cases and special syntax

# Empty blocks
view: empty_view {
}

explore: empty_explore {
}

# Nested template expressions
view: test_complex_sql {
  dimension: complex_sql {
    sql: 
      CASE 
        WHEN ${TABLE}.status = 'complete' AND ${users.region} = 'US' THEN ${TABLE}.revenue * 1.1
        WHEN ${TABLE}.status = 'pending' THEN ${TABLE}.revenue * 0.9
      ELSE ${TABLE}.revenue
    END ;;
  }

  # Multiple semicolons in SQL
  dimension: multiple_semicolons {
    sql: ${TABLE}.field1 || ';' || ${TABLE}.field2 ;;
  }
}

# Complex derived table
view: complex_derived {
  derived_table: {
    sql:
      WITH base AS (
        SELECT 
          user_id,
          DATE(created_at) as created_date,
          SUM(revenue) as daily_revenue
        FROM orders
        WHERE {% condition date_filter %} created_at {% endcondition %}
        GROUP BY 1, 2
      ),
      cumulative AS (
        SELECT 
          *,
          SUM(daily_revenue) OVER (
            PARTITION BY user_id 
            ORDER BY created_date 
            ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
          ) as cumulative_revenue
        FROM base
      )
      SELECT * FROM cumulative
    ;;
    
    persist_for: "24 hours"
    indexes: ["user_id", "created_date"]
  }
  
  dimension: user_id {
    type: string
    sql: ${TABLE}.user_id ;;
  }
}

# Derived table with SQL comments
view: derived_with_comments {
  derived_table: {
    sql:
      /* 1) Commercial slice */
      SELECT
        src.mfh_investment_case_id,
        'Commercial' AS area_type,
        -- Sum of all commercial areas
        SUM(src.nra_m2) AS area_m2,
        /* Calculate percentage
           This is a multi-line comment */
        SUM(src.nra_m2) / NULLIF(SUM(SUM(src.nra_m2)) OVER (), 0) AS area_pct
      FROM
        source_table src
      WHERE
        src.area_type = 'COMMERCIAL' -- Filter for commercial only
      GROUP BY
        1, 2
    ;;
  }
  
  dimension: mfh_investment_case_id {
    type: string
    sql: ${TABLE}.mfh_investment_case_id ;;
  }
  
  dimension: area_type {
    type: string
    sql: ${TABLE}.area_type ;;
  }
  
  measure: total_area {
    type: sum
    sql: ${TABLE}.area_m2 ;;
  }
}

# Refinements
view: +orders {
  dimension: new_dimension {
    type: string
    sql: ${TABLE}.new_field ;;
  }
}

explore: +orders {
  join: new_table {
    sql_on: ${orders.id} = ${new_table.order_id} ;;
  }
}
