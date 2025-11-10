# LookML Code Completion Guide

## Overview

The LookML plugin now includes comprehensive code completion functionality that provides context-aware suggestions for LookML syntax, keywords, and properties.

## Features

### 🎯 **Top-Level Keywords**
When typing at the root level of a LookML file, the completion system suggests:
- `view:` - Creates a new view definition
- `explore:` - Creates a new explore definition  
- `dashboard:` - Creates a new dashboard definition
- `datagroup:` - Creates a new datagroup definition
- `access_grant:` - Creates a new access grant definition
- `connection:` - Sets the connection string
- `include:` - Includes other LookML files
- `label:` - Sets a top-level label

### 🔧 **Context-Aware Property Completion**
Within different LookML blocks, completion adapts to provide relevant properties:

#### View Properties
- `dimension:` - Creates a new dimension
- `measure:` - Creates a new measure
- `sql_table_name:` - Sets the SQL table name
- `derived_table:` - Creates a derived table
- `dimension_group:` - Creates a dimension group
- `filter:` - Creates a filter
- `parameter:` - Creates a parameter

#### Explore Properties
- `from:` - Sets the base view
- `join:` - Adds a join
- `always_filter:` - Sets always-applied filters
- `access_filter:` - Sets access-based filters
- `fields:` - Specifies included fields

#### Dashboard Properties
- `title:` - Sets dashboard title
- `elements:` - Defines dashboard elements
- `filters:` - Creates dashboard filters
- `layout:` - Sets dashboard layout

#### Common Properties (available in all contexts)
- `name:` - Sets the name
- `label:` - Sets the display label
- `description:` - Adds description
- `type:` - Sets the type
- `hidden:` - Controls visibility
- `required:` - Makes field required

### 📋 **Value Completion**
For specific properties, completion provides appropriate values:

#### Type Property
- **For dimensions**: `string`, `number`, `date`, `datetime`, `yesno`, `tier`, `location`
- **For measures**: `count`, `sum`, `average`, `min`, `max`, `percent_of_total`

#### Boolean Properties
- `yes` / `no`
- `true` / `false`

#### Relationship Types (for joins)
- `one_to_one`
- `one_to_many`
- `many_to_one`
- `many_to_many`

#### Week Start Days
- `monday`, `tuesday`, `wednesday`, `thursday`, `friday`, `saturday`, `sunday`

## Usage

### 🚀 **How to Use**

1. **Open any `.lkml` file** in your IntelliJ-based IDE
2. **Start typing** at any location
3. **Press `Ctrl+Space`** (or `Cmd+Space` on Mac) to trigger completion
4. **Select** from the suggested completions
5. **Press Enter** to insert the selected completion

### 💡 **Smart Insertion**

The completion system includes smart insertion features:

- **Automatic colons**: Property names automatically add `:` 
- **Block structure**: Block keywords like `view:` automatically add `name { }` structure
- **Cursor positioning**: Cursor is positioned optimally for continued typing

### 📝 **Example Workflow**

```lookml
# Type "vi" and press Ctrl+Space
view: my_view {
  # Type "di" and press Ctrl+Space  
  dimension: user_id {
    # Type "ty" and press Ctrl+Space
    type: string
    # Type "la" and press Ctrl+Space
    label: "User ID"
  }
}
```

## Technical Implementation

### 🔍 **Context Detection**
The completion system analyzes the PSI (Program Structure Interface) tree to determine:
- Current block context (view, explore, dashboard, etc.)
- Property vs. value position
- Parent element type

### 📊 **Completion Providers**
Three specialized providers handle different completion scenarios:

1. **TopLevelCompletionProvider**: Handles root-level keywords
2. **PropertyNameCompletionProvider**: Suggests property names within blocks
3. **PropertyValueCompletionProvider**: Provides values for specific properties

### 🎨 **Visual Enhancements**
- **Icons**: Different icons for keywords, properties, and values
- **Type information**: Shows contextual type information
- **Prioritization**: Most relevant suggestions appear first

## Future Enhancements

### 🔮 **Planned Features**
- **Field reference completion**: Autocomplete field names from referenced views
- **SQL snippet completion**: Smart SQL completion within SQL blocks
- **Cross-file references**: Completion for includes and view references
- **Custom completion**: User-defined completion patterns

### 🐛 **Known Limitations**
- Completion works best with properly structured LookML files
- YAML dashboard completion is context-sensitive
- Some complex nested structures may need manual completion

---

## Troubleshooting

### ❓ **Completion Not Working?**
1. Ensure the file has `.lkml` extension
2. Check that the LookML plugin is enabled
3. Verify IntelliJ's completion settings are enabled
4. Try rebuilding the project (`Build → Rebuild Project`)

### 🔧 **Performance Tips**
- Keep LookML files well-structured for best completion performance
- Use proper indentation for accurate context detection
- Close unused files to improve completion speed

---

**Note**: This completion system is designed to work with both traditional LookML syntax and YAML-style dashboard definitions, automatically detecting the appropriate context.