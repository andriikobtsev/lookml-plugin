# Test: YAML edge cases - INPUT
---
- dashboard: edge_cases
description: |
This is multiline
and should be preserved
title: "Test"
elements:
- name: chart1
type:looker_grid
fields: [field1, field2, field3]
series_colors:
orders.total: "#FF0000"
users.count: "#00FF00"
conditional_formatting:[{
type: between, value: [0, 100]
}]
label|mart__long_field_name.count: "Custom Label"
y_axes: [{
label: '', orientation: left
}]
