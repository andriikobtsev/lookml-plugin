# LookML Plugin for IntelliJ Platform

A comprehensive plugin that adds **Looker Modeling Language (LookML)** support to IntelliJ-based IDEs including IntelliJ IDEA, PyCharm, and others.

## ✨ Features

### ✅ Working Features (2026.1.0)

- **Code Formatter** - **Traditional LookML** and **YAML dashboards** via Code > Reformat Code (`Cmd/Ctrl+Alt+L`)
- **YAML Dashboard Rewriter** - Same logic via **Reformat YAML Dashboard** (`Cmd/Ctrl+Alt+Shift+Y`); requires an active Marketplace license after the **evaluation period** (trial counts as licensed)
- **YAML Dashboard Autocomplete** - 150+ properties for YAML dashboards
- **Syntax Highlighting** - Keywords, strings, comments, SQL blocks, field references
- **File Type Recognition** - `.lkml` and `.lookml` files
- **Code Completion** - Context-aware completion for traditional LookML and YAML dashboards
- **Code Commenting** - Line and block comments (`Cmd/Ctrl + /`)
- **Brace Matching** - Automatic matching of `{}`, `[]`, `()`
- **Error Detection** - Syntax validation for traditional LookML and schema-backed checks for YAML dashboard properties
- **Manifest Files** - `manifest.lkml` support: `project_name`, `constant`, `local_dependency`, `remote_dependency`, `override_constant`, `new_lookml_runtime`
- **Code Folding** - Collapse/expand views, explores, measures, dashboards
- **Wildcard Field References** - Support for `users.basic*`, `detail*` syntax

### ⚠️ Known Limitations

- **YAML dashboard rewriter** - Deeply nested structures (`dynamic_fields`, `ui_config`, `listen`, element-level `filters`) are preserved verbatim rather than reformatted, so they are never mangled but also not re-indented (see `CHANGELOG.md`).
- **Traditional formatter** - Expects parseable files; severe syntax errors may limit formatting.
- **Advanced IDE features** - Navigation/refactoring planned for future releases.

## Pricing

**LookML Support is free to install.** Reading LookML (syntax highlighting) and basic editing are free; the productivity features (completion, validation, formatting) are **Pro**, with a free trial.

### Free vs Pro

| Capability | Free | Pro |
| --- | :---: | :---: |
| Syntax highlighting | ✅ | ✅ |
| File type recognition (`.lkml`, `.lookml`) | ✅ | ✅ |
| Folding, commenting, brace matching, quote handling | ✅ | ✅ |
| Color settings | ✅ | ✅ |
| Code completion (150+ dashboard properties) | - | ✅ |
| Dashboard validation (schema-backed property checks) | - | ✅ |
| Code formatting (**Reformat Code**, **Reformat YAML Dashboard**) | - | ✅ |

- **Free forever:** highlighting and basic editing - no license, no expiry.
- **Pro:** code completion, dashboard validation, and formatting. Try them free during the trial (length shown on the listing).
- **After the trial:** activate Pro via **Help | Register** (JetBrains account + license). The free features keep working regardless.

Basic syntax errors (for example an unclosed brace) are shown for free; the schema-backed dashboard property checks are the Pro validation.

## Roadmap

Pro is actively developed. **Buy Pro now at the founding price and you get future Pro features as free updates** (a Pro license includes lifetime updates). Planned directions:

- **Looker API validation** - run Looker's own LookML validator on your changes from inside the IDE and see real errors before you push (requires a Looker connection). This is authoritative validation, beyond the local schema checks.
- **Go-to-definition** - jump from a field reference to the dimension or measure it resolves to, correctly following extended and refined views.
- **Find usages and rename** for LookML fields.

These are planned directions, not dated commitments. Early Pro buyers lock in the founding price; the price increases as these features ship.

## 📥 Installation

### From IntelliJ Plugin Marketplace (Recommended)

1. Open **Settings/Preferences** > **Plugins**
2. Search for "**LookML Support**"
3. Click **Install**
4. Restart IDE

### Manual Installation

1. Download the latest release from [Releases](https://github.com/andriikobtsev/lookml-plugin/releases)
2. Open **Settings/Preferences** > **Plugins** > **⚙️** > **Install Plugin from Disk**
3. Select the downloaded `.zip` file
4. Restart IDE

## 🚀 Usage

Once installed, the plugin automatically activates when you open `.lkml` or `.lookml` files.

### YAML Dashboard Autocomplete 🆕

Get intelligent autocomplete for YAML dashboard files with **150+ properties**!

**How to use:**
1. Open a YAML dashboard file (starts with `---` or `- dashboard:`)
2. Start typing a property name (e.g., type `ti` for "title")
3. Press **`Ctrl+Space`** to see suggestions
4. Select from the list!

**What you get:**
- ✅ **Dashboard properties** - title, description, layout, filters, elements, etc.
- ✅ **Element properties** - type, model, explore, fields, row, col, width, height, etc.
- ✅ **Chart properties** - x_axis_gridlines, series_colors, stacking, legend_position, etc.
- ✅ **Table properties** - show_row_numbers, truncate_text, conditional_formatting, etc.
- ✅ **Filter properties** - name, type, ui_config, listens_to_filters, etc.
- ✅ **Visualization types** - looker_line, looker_column, single_value, table, etc.
- ✅ **150+ total properties** with descriptions!

### Code Formatter

Automatically format LookML files for consistent style!

#### Traditional LookML Files

**How to use:**
1. Open a traditional LookML file (views, explores, dimensions, etc.)
2. Press **`Cmd+Opt+L`** (Mac) or **`Ctrl+Alt+L`** (Windows/Linux)
3. Or: Code > Reformat Code

**What it does:**
- ✅ **Proper indentation** - 2 spaces per nesting level
- ✅ **SQL formatting** - SQL blocks on single line: `sql: ${TABLE}.id ;;`
- ✅ **Template expressions** - No spaces inside: `${TABLE}` not `$ { TABLE }`
- ✅ **Operator spacing** - Spaces around operators: `${a} = ${b}`
- ✅ **Preserves literals** - Numbers and strings stay intact

**Example:**
```lkml
# Before formatting:
view: users {
dimension: id {
type: number
sql: $ { TABLE } . user_id ;;
}
}

# After formatting:
view: users {
  dimension: id {
    type: number
    sql: ${TABLE}.user_id ;;
  }
}
```

#### YAML Dashboard Files

**How to use (recommended):**
1. Open a YAML dashboard file (typically `---` and/or `- dashboard:`)
2. **Code > Reformat Code** - **`Cmd+Option+L`** (Mac) or **`Ctrl+Alt+L`** (Windows/Linux)

**Alternative:** **Reformat YAML Dashboard** - **`Cmd+Option+Shift+Y`** / **`Ctrl+Alt+Shift+Y`**, or Find Action (`Cmd/Ctrl+Shift+A`) > "Reformat YAML Dashboard".

Both paths run the same **`YamlDashboardRewriter`** (canonical layout, 2-space indent, ordered sections).

**Example:**
```yaml
# Before formatting:
---
- dashboard : test
title : "Sales"
elements : - name : chart1
type : looker_line
row : 0

# After formatting:
---
- dashboard: test
  title: "Sales"
  elements:
  - name: chart1
    type: looker_line
    row: 0
```

Values keep their original form (strings, numbers, and arrays are not force-quoted); block
scalars and nested blocks such as `dynamic_fields` are preserved as-is.

### YAML Dashboard Autocomplete

Get intelligent autocomplete for YAML dashboard files with **150+ properties**!

**How to use:**
1. Open a YAML dashboard file (starts with `---` or `- dashboard:`)
2. Start typing a property name (e.g., type `ti` for "title")
3. Press **`Ctrl+Space`** to see suggestions
4. Select from the list!

**Example:**
```yaml
---
- dashboard: sales_dashboard
  # Type "ti" and press Ctrl+Space > see "title"
  title: "Sales Dashboard"

  elements:
  # Type "ty" and press Ctrl+Space > see "type"
  - type: looker_column
    # Type "show_" and press Ctrl+Space > see all "show_*" properties!
    show_view_names: false
```

### Traditional LookML Completion

Traditional LookML files also have autocomplete for:
- **Top-level keywords**: `view:`, `explore:`, `dashboard:`, etc.
- **View properties**: `dimension:`, `measure:`, `filter:`, etc.
- **Field properties**: `type:`, `sql:`, `label:`, etc.
- **Type values**: Dimension types, measure types, relationship types

### Keyboard Shortcuts

- **Code Completion**: `Ctrl+Space` (shows available keywords and properties)
- **Comment/Uncomment Line**: `Cmd/Ctrl + /`
- **Comment/Uncomment Block**: `Cmd/Ctrl + Shift + /`
- **Code Folding**: `Cmd/Ctrl + -/+` (collapse/expand)

### Customization

Customize syntax highlighting colors:
**Settings** > **Editor** > **Color Scheme** > **LookML**

## 📝 Supported Syntax

### Traditional LookML
```lookml
view: users {
  dimension: id {
    type: number
    primary_key: yes
    sql: ${TABLE}.user_id ;;
  }
  
  measure: count {
    type: count
    drill_fields: [id, name, detail*]
  }
}

explore: user_analysis {
  join: orders {
    sql_on: ${users.id} = ${orders.user_id} ;;
  }
}
```

### YAML Dashboards
```yaml
---
- dashboard: sales_overview
  title: "Sales Performance Dashboard"
  elements:
  - title: Revenue Trend
    type: looker_line
    filters:
      orders.created_date: 30 days
```

## 🐛 Support & Feedback

Found a bug or have a feature request?

- **Bug Reports**: [GitHub Issues](https://github.com/andriikobtsev/lookml-plugin/issues)
- **Feature Requests**: [GitHub Issues](https://github.com/andriikobtsev/lookml-plugin/issues)
- **Questions**: andrii.kobtsev@gmail.com

Your feedback helps improve the plugin!

## 📄 License & distribution (read this carefully)

Three different things are easy to confuse:

1. **JetBrains Marketplace plugin (the `.zip` you install from the Marketplace)**  
   Use is governed by **JetBrains Marketplace terms** and your **purchase or subscription**. This is how most users get the plugin.

2. **Source code in this GitHub repository**  
   Licensed under the **GNU Affero General Public License v3.0** ([LICENSE](LICENSE) - **AGPL-3.0**, not "plain GPL"). If you modify and distribute the source (or run a modified version as a network service), AGPL obligations apply.

3. **Alternate / commercial licensing of the source**  
   If you need a **different** license for the **code** (not the Marketplace binary), contact the author.

📧 **andrii.kobtsev@gmail.com**

## 🔒 Privacy

The plugin does **not** implement custom analytics. **License checks** use the standard JetBrains / IDE Marketplace flow.

## 🏗️ Built With

- IntelliJ Platform SDK
- Kotlin
- Grammar-Kit (BNF parser generator)

## 📊 Version History

**Current Version**: **2026.1.0** (see `build.gradle.kts` and Marketplace listing)

### What's New in 2026.1.0
- **Freemium model:** code completion, dashboard validation, and formatting are now **Pro** (free trial); syntax highlighting and basic editing stay free forever
- **Manifest file support:** `project_name`, `constant`, `local_dependency`, `remote_dependency`, `override_constant`, `new_lookml_runtime`
- **Better dashboard validation:** far fewer false "unknown property" warnings; covers table calculations, filters, and text tiles
- **More robust dashboard formatting:** arrays stay arrays, block scalars and nested blocks are preserved, multi-line field lists handled
- **Fixes:** dashboards with a leading comment before `---` parse correctly; emoji in text tiles supported; formatting runs only on an explicit reformat, not while typing

### Earlier: v1.2.0
- **Code Formatter** for traditional LookML (PSI-based)
- **SQL / template** spacing and **lexer** fixes

### What's New in v1.1.0
- 🆕 **YAML Dashboard Autocomplete** - 150+ properties with descriptions
- 🆕 **Dashboard Properties** - title, layout, filters, elements, and more
- 🆕 **Element Properties** - type, fields, charts, tables, maps (80+ properties)
- 🆕 **Filter Properties** - Comprehensive filter configuration options
- 🆕 **Visualization Types** - All Looker chart types plus marketplace visualizations
- ✨ **Context-Aware** - Shows relevant properties based on where you're typing

See [CHANGELOG.md](CHANGELOG.md) for complete version history.

---

**Made with ❤️ by Andrii Kobtsev**

## Support
⭐ If you find this plugin helpful, please star the repo!  
💝 You can also [support future development](https://paypal.me/andriikobtsev).

💬 Questions? Feedback? Open an issue or reach out!
