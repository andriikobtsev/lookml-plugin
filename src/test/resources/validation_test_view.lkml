# View file test - should validate as VIEW type

view:users{dimension:id{sql:select * from users where id=1   ;;}}

view: users {
  sql_table_name: public.users ;;

  # Dimension without required type - should error
  dimension: id {
    primary_key: yes
    # Missing type
  }

  # Valid dimension
  # dimension: email {
    type: string
    sql: ${TABLE}.email   ;;
  }

  # Invalid type value - should error
  dimension: created {
    type: invalid_type
    sql: ${TABLE}.created_at   ;;
  }

  # Duplicate field name - should error
  dimension: email {
    type: string
    sql: lower(${TABLE}.email)   ;;
  }

  # Measure without type - should error
  measure: count {
    # Missing type
    drill_fields: [id, email]
  }

  # Invalid measure type - should error
  measure: total_revenue {
    type: number
    sql: ${TABLE}.revenue   ;;
  }

  # Valid measure with drill fields
  measure: total_orders {
    type: sum
    sql: ${TABLE}.order_count   ;;
    drill_fields: [id, email, created.date]
  }

  # Invalid drill_fields format - should error
  measure: avg_revenue {
    type: average
    sql: ${TABLE}.revenue   ;;
    drill_fields: "not an array"
  }

  # Hidden primary key - should warn
  dimension: secret_id {
    type: number
    primary_key: yes
    hidden: yes
  }

  # Invalid SQL reference - should error
  dimension: bad_reference {
    type: string
    sql: ${invalid.view.reference.with.too.many.parts}   ;;
  }
}

# This property shouldn't be at top level in view file - should warn
connection: "database"