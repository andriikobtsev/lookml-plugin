# Test: derived_table wraps one property per line - INPUT (collapsed)
view: v { derived_table: { sql: SELECT a, b FROM t ;; persist_for: "24 hours" datagroup_trigger: my_dg } }
