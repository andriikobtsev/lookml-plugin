# Model file test - should validate as MODEL type
connection: "database"

include: "*.view.lkml"
include: "*.explore.lkml"

# This should trigger validation error - invalid property for model file
invalid_property: "test"

datagroup: orders_datagroup {
  sql_trigger: SELECT MAX(created_at) FROM orders ;;
  max_cache_age: "24 hours"
}

access_grant: can_see_revenue {
  user_attribute: department
  allowed_values: ["finance", "executive"]
}

explore: orders {
  # Missing required relationship property - should error
  join: users {
    sql_on: ${orders.user_id} = ${users.id} ;;
    # Missing relationship
  }
  
  # Valid join
  join: products {
    sql_on: ${orders.product_id} = ${products.id} ;;
    relationship: many_to_one
    type: left_outer  # Should validate enum value
  }
  
  # Invalid join type - should error
  join: categories {
    sql_on: ${products.category_id} = ${categories.id} ;;
    relationship: many_to_one
    type: invalid_type
  }
}