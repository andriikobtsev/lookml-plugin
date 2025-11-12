explore: fdf {
    from: items
    view_name: test_view
    label: "Test"
    join: users {
        type: left_outer
    }
}
