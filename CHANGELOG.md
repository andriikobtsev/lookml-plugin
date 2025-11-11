# Changelog

All notable changes to the LookML Plugin will be documented in this file.

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
