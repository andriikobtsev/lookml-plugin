# Test: function calls in sql: keep the name tight to '(' - INPUT (messy)
view: metrics {
    dimension: bucket {
        type: string
        sql: DATE_TRUNC (${created_date}, MONTH) ;;
    }

    measure: total {
        type: sum
        sql: SUM (${amount}) ;;
    }
}
