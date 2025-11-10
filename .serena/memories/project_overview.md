# LookML Plugin Project Overview

## Purpose
A comprehensive IntelliJ Platform plugin that adds **Looker Modeling Language (LookML)** support to IntelliJ-based IDEs including PyCharm, IntelliJ IDEA, and others.

## Tech Stack
- **Kotlin** - Primary language for plugin development
- **Java 21** - Target JDK version for IntelliJ Platform 2025.1  
- **IntelliJ Platform 2025.1** - Base platform for plugin development
- **Grammar-Kit Plugin** - Used for BNF grammar parsing and code generation
- **Gradle** - Build system with Kotlin DSL

## Key Features
- File type recognition for `.lkml` and `.lookml` files
- Dual-lexer architecture (traditional LookML + YAML dashboards)
- Syntax parsing via comprehensive BNF grammar
- Syntax highlighting with customizable colors
- Code commenting, brace matching, quote handling
- Error detection with real-time validation
- Support for wildcard field references and complex SQL blocks

## Architecture
The plugin uses a sophisticated **dual-lexer system**:
1. **LookMLLexerAdapter** - Smart router detecting file type
2. **BNF-Generated Lexer** - Handles traditional LookML syntax
3. **YamlDashboardLexer** - Handles YAML dashboard files
4. **Detection Logic** - Automatically chooses lexer based on content patterns

## Build System
- **Gradle 8.9** with Kotlin DSL
- **Grammar-Kit integration** for automatic parser generation
- **IntelliJ Platform Gradle Plugin** for plugin lifecycle management