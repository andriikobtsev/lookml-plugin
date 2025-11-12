# Changelog

All notable changes to the LookML Plugin will be documented in this file.

## [1.2.0] - 2025-11-12

### Added
- **Code Formatter** - Automatic code formatting for traditional LookML files
- **SQL Formatting** - SQL blocks formatted on single line with proper spacing
- **Template Expression Formatting** - `${TABLE}` formatted with no internal spaces
- **Operator Spacing** - Proper spaces around operators: `${a} = ${b}`
- **Structure-Based Indentation** - 2-space indentation based on block nesting

### Fixed
- **Lexer**: SQL tokens no longer include trailing whitespace
- **Lexer**: Numbers in SQL blocks now parsed as complete tokens (prevents `100` becoming `1 0 0`)
- **Formatter**: Literals (numbers, strings) preserved exactly as-is

### Technical Improvements
- Implemented `LookMLFormattingModelBuilderV2` with PSI-based formatting
- Created `LookMLBlock` for structure and indentation logic
- Created `SqlPropertyBlock` for SQL-specific inline formatting
- Updated plugin.xml to register formatter
- Added formatter test files in `src/test/resources/formatter/`

### Known Limitations
- YAML dashboard formatting not yet implemented (planned for v1.2.1)
- Formatter requires files to be parseable (no syntax errors)

## [1.1.0] - 2025-11-11

### Added
- **YAML Dashboard Autocomplete** - Comprehensive autocomplete for YAML dashboard files
- **150+ Properties** - Dashboard, element, filter, and chart properties with descriptions
- **Dashboard Properties** - title, description, layout, filters, elements, refresh, crossfilter_enabled, and more
- **Element Properties** - type, model, explore, fields, row, col, width, height, series_colors, conditional_formatting, and 80+ more
- **Filter Properties** - name, type, ui_config, model, explore, field, listens_to_filters, and more
- **Visualization Types** - All Looker chart types (looker_line, looker_column, single_value, etc.) plus marketplace visualizations
- **Context-Aware Suggestions** - Shows relevant properties based on dashboard structure
- **Value Suggestions** - Autocomplete for type, layout, and filter type values

### Technical Improvements
- Created `YamlDashboardProperties` with comprehensive property definitions
- Implemented `YamlDashboardCompletionProvider` for YAML-specific completion
- Enhanced `LookMLCompletionContributor` to support YAML dashboard files
- Added support for field-specific properties with pipe syntax (label|field.name)
- Documentation added in `docs/YAML_AUTOCOMPLETE.md`

## [1.0.0] - 2025-11-10

### Added
- Initial release
- Full syntax highlighting for LookML
- File type recognition for `.lkml` and `.lookml` files
- Code commenting (line and block)
- Brace matching for `{}`, `[]`, `()`
- Error detection and validation
- Code folding for major blocks
- YAML dashboard support
- Wildcard field references (`users.basic*`, `detail*`)
- Customizable color scheme

### Known Limitations
- Solo developer project in active development
- Some advanced IDE features planned for future releases
- Bug reports and feedback welcome

---

## Upcoming Features

See [docs/DEVELOPMENT_ROADMAP.md](docs/DEVELOPMENT_ROADMAP.md) for planned features.
