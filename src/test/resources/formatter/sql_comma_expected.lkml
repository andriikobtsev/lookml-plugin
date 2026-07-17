# Test: no space before a comma in sql: - EXPECTED
view: metrics {
  dimension: cols {
    type: string
    sql: SELECT a, b, c FROM t ;;
  }

  measure: grp {
    type: number
    sql: COALESCE(${a}, ${b}, 0) ;;
  }
}
