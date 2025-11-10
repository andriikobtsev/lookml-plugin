# Code Style and Conventions

## File Structure
- **Main source**: `src/main/kotlin/com/yourcompany/lookml/`
- **Grammar**: `src/main/grammar/LookML.bnf`
- **Generated code**: `src/main/gen/` (don't edit directly!)
- **Test resources**: `src/test/resources/`

## Naming Conventions
- **Package**: `com.yourcompany.lookml`
- **Classes**: PascalCase (e.g., `LookMLSyntaxHighlighter`)
- **Files**: Match class names with `.kt` extension
- **Grammar rules**: snake_case (e.g., `view_definition`, `test_body`)

## Code Organization
- **Lexer**: `/lexer/` subpackage
- **Parser**: Generated in `/parser/` and `/psi/`
- **Highlighting**: `/highlighting/` subpackage
- **Completion**: `/completion/` subpackage

## Grammar Conventions
- **Tokens**: UPPER_CASE (e.g., `VIEW='view'`, `TEST='test'`)
- **Rules**: snake_case (e.g., `view_body`, `test_definition`)
- **Private rules**: Prefix with `private` for internal parsing rules
- **Rule structure**: `rule_name ::= pattern { optional_methods }`

## Generated Code Guidelines
- **Never edit** files in `src/main/gen/` directly
- **Always regenerate** parser after grammar changes
- **Use symbolic tools** for precise code modifications
- **Test thoroughly** after grammar changes