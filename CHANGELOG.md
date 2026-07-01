# Changelog

All notable changes to the LookML Plugin will be documented in this file.

## [2026.1.0] - 2026-07-01

First paid release (freemium). **Code formatting becomes a paid feature** after a free
evaluation period; all other features stay free. Activate via **Help | Register**.

### Paid (evaluation included)
- **LookML and YAML dashboard formatting** - IDE **Reformat Code** (`Cmd/Ctrl+Alt+L`) and the
  **Reformat YAML Dashboard** action (`Cmd/Ctrl+Alt+Shift+Y`), gated by `CheckLicense` in
  `LookMLFormattingModelBuilderV2`, `LookMLFormatAction`, and `ReformatYamlDashboardAction`
  (skipped in unit tests / headless).

### Free
- Syntax highlighting, code completion, folding, brace matching, commenting, and validation.

### Added
- **Manifest files** - `manifest.lkml` support for `project_name`, `constant`, `local_dependency`,
  `remote_dependency`, `override_constant`, and `new_lookml_runtime` (grammar, lexer, highlighting,
  completion).
- **LookML code style settings** - `LookMLLanguageCodeStyleSettingsProvider` (default 2-space indent).
- **Tests** - `YamlSemanticAnalyzerTest`, `YamlLeadingCommentTest`, and expanded `YamlDashboardRewriterTest`.

### Changed
- **Dashboard validation** - classify properties by indentation and validate item-level properties
  against the combined element / filter / `dynamic_fields` schemas; far fewer false
  "unknown property" warnings. Added missing element, filter, and table-calculation properties.
- **Dashboard formatting** - values are preserved verbatim instead of force-quoted (arrays stay
  arrays); multi-line flow arrays are joined; block scalars and nested blocks (`dynamic_fields`,
  `ui_config`, element-level `filters`, etc.) are preserved. Reformatting runs only on an explicit
  reformat, not on Enter / auto-indent.

### Fixed
- Dashboards with a leading `#` comment before `---` now parse as YAML (were parsed as traditional
  LookML). The `---` document start is recognized at any line start.
- Emoji (surrogate pairs) in text tiles no longer break parsing.

### Known limitations
- **YAML rewriter** - deeply nested structures are preserved verbatim rather than reformatted; a
  block-style element-level `filters:` map is distinguished from the dashboard `filters:` section
  by content shape (heuristic).
- **Traditional formatter** - still expects parseable files (syntax errors may limit formatting).

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
