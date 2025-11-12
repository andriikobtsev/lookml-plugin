# Test: SQL blocks - INPUT
view: orders {
    dimension: id {
        sql: ${TABLE}.order_id;;
    }

    dimension: user_name {
        sql: ${users.name};;
    }

    measure: total_revenue {
        sql: ${TABLE}.amount;;
    }
}

explore: order_analysis {
    join: users {
        sql_on: ${orders.user_id} = ${users.id};;
        type: left_outer
        relationship: many_to_one
    }
}
