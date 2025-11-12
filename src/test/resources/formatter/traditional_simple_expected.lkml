# Test: Simple traditional LookML - EXPECTED (perfect)
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
