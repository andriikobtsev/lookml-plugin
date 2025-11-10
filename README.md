# LookML Plugin for IntelliJ Platform

A comprehensive plugin that adds **Looker Modeling Language (LookML)** support to IntelliJ-based IDEs including IntelliJ IDEA, PyCharm, and others.

## ✨ Features

### ✅ Working Features (v1.0)

- **Syntax Highlighting** - Keywords, strings, comments, SQL blocks, field references (traditional LookML)
- **File Type Recognition** - `.lkml` and `.lookml` files
- **Code Completion** - Basic keyword and property completion
- **Code Commenting** - Line and block comments (`Cmd/Ctrl + /`)
- **Brace Matching** - Automatic matching of `{}`, `[]`, `()`
- **Error Detection** - Syntax validation for traditional LookML
- **Code Folding** - Collapse/expand views, explores, measures, dashboards
- **Wildcard Field References** - Support for `users.basic*`, `detail*` syntax

### ⚠️ Known Limitations (v1.0)

- **Code formatter** - Needs improvement
- **YAML dashboard validation** - Partial support
- **Advanced IDE features** - Planned for future releases (navigation, refactoring)
- **Solo developer project** - Active development, bugs may exist

## 📥 Installation

### From IntelliJ Plugin Marketplace (Recommended)

1. Open **Settings/Preferences** → **Plugins**
2. Search for "**LookML Support**"
3. Click **Install**
4. Restart IDE

### Manual Installation

1. Download the latest release from [Releases](https://github.com/andriikobtsev/lookml-plugin/releases)
2. Open **Settings/Preferences** → **Plugins** → **⚙️** → **Install Plugin from Disk**
3. Select the downloaded `.zip` file
4. Restart IDE

## 🚀 Usage

Once installed, the plugin automatically activates when you open `.lkml` or `.lookml` files.

### Keyboard Shortcuts

- **Comment/Uncomment Line**: `Cmd/Ctrl + /`
- **Comment/Uncomment Block**: `Cmd/Ctrl + Shift + /`
- **Code Folding**: `Cmd/Ctrl + -/+` (collapse/expand)

### Customization

Customize syntax highlighting colors:
**Settings** → **Editor** → **Color Scheme** → **LookML**

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

## 🤝 Contributing

Contributions are welcome! Whether it's bug fixes, new features, or documentation improvements.

### How to Contribute

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Test thoroughly
5. Submit a pull request

### Development Setup

See [docs/TECHNICAL_GUIDE.md](docs/TECHNICAL_GUIDE.md) for development environment setup.

### Contribution License

By contributing, you agree that your contributions will be licensed under the same AGPL-3.0 license that covers this project. The original author retains the right to offer commercial licenses for the combined work.

## 📄 License

This project is **dual-licensed**:

- **🆓 AGPL-3.0** for open source use
- **💼 Commercial License** for proprietary/closed-source use

**For open source projects:** Free to use under AGPL-3.0. Modifications and derivative works must also be open sourced under AGPL-3.0.

**For commercial use:** If you need to use this plugin in closed-source or proprietary software, please contact us for commercial licensing options.

📧 **Commercial licensing**: andrii.kobtsev@gmail.com  
📖 **Full details**: [LICENSE.md](LICENSE.md)

## 🏗️ Built With

- IntelliJ Platform SDK
- Kotlin
- Grammar-Kit (BNF parser generator)

## 📊 Version History

**Current Version**: 1.0.0

See [CHANGELOG.md](CHANGELOG.md) for version history and planned features.

---

**Made with ❤️ by Andrii Kobtsev**

⭐ If you find this plugin helpful, please star the repo!

💬 Questions? Feedback? Open an issue or reach out!
