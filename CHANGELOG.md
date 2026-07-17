# Changelog

All notable changes to the LookML Plugin will be documented in this file.

## [2026.2.0] - 2026-07-17

Adds a **Pro code-navigation layer** for LookML fields and views, plus in-`sql:` field completion
and two formatter fixes. All new capabilities are Pro (free during the trial); free editing features
are unchanged. Ships to existing 2026.1.x users (same `release-version`).

### Added (Pro)
- **Go to Definition** - from `${field}` / `view.field` references, `from:` / `view_name:`,
  `value_format_name:`, `@{constant}`, filter keys, and YAML dashboard fields/explores/models to
  their declarations, resolving aliases through explore `from:` and joins.
- **Find Usages** (Alt+F7) and **Rename** - project-wide, scoped to the view family
  (base + refinements + `extends`), so unrelated same-named fields are untouched. Reference
  resolution is index-backed and cached (`LookMLResolve` `ProjectIndex`).
- **Go to Implementation(s)** (Ctrl/Cmd+Alt+B) and **Go to Super** (Ctrl/Cmd+U), with matching
  parent/implementation gutter icons.
- **Field completion in `sql:`** - inside `${...}`, suggests the view's own and inherited fields.

### Fixed
- Function calls in `sql:` keep the name tight to the parenthesis (`DATE_TRUNC(...)`, not
  `DATE_TRUNC (...)`); SQL keywords before `(` keep their space.
- No space before a comma in `sql:` (`SELECT a, b`, not `SELECT a , b`).
- Multi-line `sql:` blocks (e.g. a `derived_table` query) keep their line breaks and indentation
  instead of collapsing onto one line.
- `derived_table` and `explore_source` blocks are laid out one property per line on reformat instead
  of collapsing onto a single line.
- Pro features now turn on immediately after activating a license mid-session (the license check no
  longer caches an "unlicensed" result for several minutes).

### Known limitations
- Navigation inside Liquid expressions (`{% ... %}`, `{{ ... }}`) is not yet resolved.
- Find Usages on a `dimension_group` does not yet list its generated timeframe fields (e.g.
  `created_month`, `created_date`); Go to Definition from those fields back to the group works.

## [2026.1.0] - 2026-07-01

First release with a Pro tier (freemium). **Code completion, dashboard validation, and code
formatting become Pro features** with a free trial; the plugin stays free to install and syntax
highlighting plus basic editing stay free. Activate Pro via **Help | Register**.

### Pro (free trial included)
- **Code completion** - gated in `LookMLCompletionContributor`.
- **Schema-backed dashboard validation** - gated in `YamlDashboardAnnotator` (basic parser syntax
  errors remain free).
- **LookML and YAML dashboard formatting** - IDE **Reformat Code** (`Cmd/Ctrl+Alt+L`) and the
  **Reformat YAML Dashboard** action (`Cmd/Ctrl+Alt+Shift+Y`), gated in
  `LookMLFormattingModelBuilderV2`, `LookMLFormatAction`, and `ReformatYamlDashboardAction`.
- License checks run through `CheckLicense` (skipped in unit tests / headless) and are cached with a
  short TTL so validation does not run on every editor event.

### Free
- Syntax highlighting, file type recognition, folding, commenting, brace matching, quote handling,
  and color settings.

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
