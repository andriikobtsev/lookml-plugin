# Suggested Commands for LookML Plugin Development

## Essential Development Commands

### Building and Running
```bash
# Run plugin in development IDE
./gradlew runIde

# Build plugin for distribution
./gradlew buildPlugin
# Output: build/distributions/

# Clean build (fixes mysterious errors)
./gradlew clean build

# Test parsing functionality
./gradlew testParsing
```

### Grammar Development (Critical)
```bash
# Generate parser from BNF grammar
# Method 1: Right-click src/main/grammar/LookML.bnf → "Generate Parser Code"
# Method 2: Use gradle task
./gradlew generateParser
```

### Testing
```bash
# Run unit tests
./gradlew test

# Manual testing - open these files in development IDE:
# - src/test/resources/comprehensive_syntax_test.lkml
# - src/test/resources/yaml_dashboard_test.lkml
```

## System Commands (Darwin/macOS)
```bash
# Make gradlew executable
chmod +x gradlew

# File operations
ls -la          # List files with details
find . -name "*.lkml"  # Find LookML files
grep -r "pattern" .    # Search for pattern
```

## Development Workflow
1. **Grammar Changes**: Edit `LookML.bnf` → Generate Parser → Clean Build
2. **Lexer Changes**: Edit `YamlDashboardLexer.kt` → Build (no regeneration needed)
3. **Test**: Run `./gradlew runIde` and test with comprehensive syntax files

## Critical Notes
- **Always regenerate parser** after BNF changes or you'll get mysterious errors
- **IntelliJ IDEA required** for plugin development (not PyCharm)
- **Grammar-Kit Plugin** must be installed in IntelliJ IDEA