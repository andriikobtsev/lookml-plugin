# Task Completion Workflow

## After Making Changes

### 1. Grammar Changes (BNF file modifications)
```bash
# CRITICAL: Always regenerate parser after BNF changes
# Method 1: Right-click src/main/grammar/LookML.bnf → "Generate Parser Code"
# Method 2: Use gradle task
./gradlew generateParser

# Clean build to avoid mysterious errors
./gradlew clean build
```

### 2. Code Changes (Kotlin files)
```bash
# Standard build
./gradlew build
```

### 3. Testing Requirements
```bash
# Run unit tests
./gradlew test

# Manual testing in development IDE
./gradlew runIde
# Then open: src/test/resources/comprehensive_syntax_test.lkml
# Then open: src/test/resources/yaml_dashboard_test.lkml
# Check for red underlines indicating parsing errors
```

### 4. Verification Checklist
- [ ] Grammar regenerated (if BNF changed)
- [ ] Clean build successful
- [ ] Unit tests pass
- [ ] No red underlines in test files
- [ ] Syntax highlighting works correctly
- [ ] No regression in existing functionality

## Distribution Build
```bash
# Build plugin for distribution
./gradlew buildPlugin
# Output: build/distributions/LookML-Support-1.0.0.zip
```

## Development Best Practices
- **Single change at a time** - one feature/fix per development cycle
- **Test early and often** - run tests after each significant change
- **Backup first** - commit working state before making changes
- **Use test files** - comprehensive syntax coverage for validation