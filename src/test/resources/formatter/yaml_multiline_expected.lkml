---
- dashboard: ml_dash
  title: "ML"
  elements:
  - name: chart_a
    type: looker_column
    model: sales
    explore: orders
    fields: [orders.a, orders.b, orders.c]
    pivots: [orders.status]
    row: 0
    col: 0
    width: 12
    height: 6
  - name: text_a
    type: text
    title_text: ''
    body_text: |-
      <div style="color: #1f77b4;">
        <h3>Hello</h3>
      </div>
    row: 6
    col: 0
    width: 24
    height: 2
