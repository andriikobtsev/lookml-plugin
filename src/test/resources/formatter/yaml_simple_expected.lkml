# Test: Simple YAML dashboard - EXPECTED (perfect)
---
- dashboard: test_dashboard
title: "Sales Dashboard"
layout: newspaper

elements:
- name: revenue_chart
title: "Revenue"
type: looker_line
model: sales
explore: orders
fields: [orders.created_month, orders.total_revenue]
row: 0
col: 0
width: 12
height: 6
