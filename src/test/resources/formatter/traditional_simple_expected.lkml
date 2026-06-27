# Test: Simple traditional LookML - EXPECTED (formatted; 2-space indent)
view: users {
  dimension: id {
    type: number
    primary_key: yes
    sql: ${TABLE}.user_id ;;
  }

  measure: count {
    type: count
    drill_fields: [id, name]
  }
}
