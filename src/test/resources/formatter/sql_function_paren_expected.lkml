# Test: function calls in sql: keep the name tight to '(' - EXPECTED
view: metrics {
  dimension: bucket {
    type: string
    sql: DATE_TRUNC(${created_date}, MONTH) ;;
  }

  measure: total {
    type: sum
    sql: SUM(${amount}) ;;
  }
}
