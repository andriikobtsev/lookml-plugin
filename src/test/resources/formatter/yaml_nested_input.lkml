# Test: YAML with nested objects - INPUT
---
- dashboard: complex_dashboard
title: "Complex Dashboard"
filters:
- name: date_filter
title:"Date Range"
type: field_filter
model: sales
explore: orders
field: orders.created_date
ui_config:
type: advanced
display: popover
elements:
- name: chart1
type: looker_column
fields: [orders.id]
listen:
Date Filter: orders.created_date
Status: orders.status
row: 0
col: 0
