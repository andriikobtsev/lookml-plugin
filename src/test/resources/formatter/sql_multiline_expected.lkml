# Test: multi-line sql: is preserved (not collapsed) - EXPECTED
view: v {
  derived_table: {
    sql:
      SELECT
        a,
        b
      FROM t
      WHERE a > 0 ;;
  }

  measure: total {
    type: sum
    sql: ${amount} ;;
  }
}
