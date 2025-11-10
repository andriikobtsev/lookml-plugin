# View Grammar Structure in LookML.bnf

## View Definition Rule
```bnf
view_definition ::= VIEW COLON IDENTIFIER LBRACE view_body RBRACE {
  methods=[getName]
}
```

## View Body Structure
```bnf
view_body ::= view_item*
```

## Current View Items (What can go inside a view block)
```bnf
private view_item ::= (
    dimension_definition 
  | dimension_group_definition
  | measure_definition 
  | filter_definition
  | parameter_definition
  | set_definition
  | drill_fields_property
  | sql_property
  | sql_table_name_property
  | sql_always_where_property
  | sql_always_filter_property
  | derived_table_property
  | html_property
  | property
  | COMMENT
)
```

## Test Block Support
**Test blocks are NOT currently supported within view bodies**. The `test_definition` rule exists but is only available in model-level contexts (top-level items), not within view bodies.

## Test Definition (exists but not in view_item)
```bnf
test_definition ::= TEST COLON IDENTIFIER LBRACE test_body RBRACE {
  methods=[getName]
}

test_body ::= test_item*

private test_item ::= (explore_source_property | assert_property | property | COMMENT)
```

## Key Finding
To add test block support within views, the `test_definition` rule needs to be added to the `view_item` alternatives list.