# LookML Plugin - Technical Implementation Guide

## 🏗️ **Architecture Overview**

### **Core Components**

```
File Input → LookMLLexerAdapter → Parser → PSI Tree → IDE Features
              ↓
         [Detection Logic]
              ↓
    YamlDashboardLexer OR BNF-Generated Lexer
```

### **Lexer Architecture**
1. **LookMLLexerAdapter.kt** - Smart router that detects file type
2. **Detection Logic**: 
   - `---` start → YAML Dashboard Lexer
   - `- dashboard:` start → YAML Dashboard Lexer  
   - `- title:`, `- name:`, `- type:`, `- model:` start → YAML Dashboard Lexer
   - Everything else → BNF-Generated Lexer
3. **YamlDashboardLexer.kt** - Custom lexer for YAML syntax with LookML extensions
4. **BNF Grammar** - Generates traditional LookML lexer/parser

### **Critical Insight: Dual-Lexer System**
The plugin automatically chooses the right lexer based on file content. This is why:
- Traditional `.lkml` files work with standard grammar
- YAML dashboard files get special handling for `yes/no` values
- Both syntaxes can coexist in the same project

### **Parser Flow**
```
Input Text → Lexer → Token Stream → Parser → PSI Elements → IDE Features
                                      ↓
                               Generated from LookML.bnf
```

## 🔧 **Development Workflow**

### **Making Grammar Changes (Most Common)**
1. **Edit** `src/main/grammar/LookML.bnf`
2. **Regenerate** parser: Right-click BNF file → "Generate Parser Code"
3. **Clean build**: `./gradlew clean build`
4. **Test** with real LookML files

⚠️ **Critical**: Always regenerate after BNF changes or you'll get mysterious errors!

### **Making Lexer Changes (For YAML Features)**  
1. **Edit** `src/main/kotlin/com/yourcompany/lookml/lexer/YamlDashboardLexer.kt`
2. **Direct build**: `./gradlew build` (no regeneration needed)
3. **Test** with YAML dashboard files

### **Safe Change Process**
1. **Backup** - commit current working state first
2. **Single change** - one feature/fix at a time
3. **Test suite** - run with `comprehensive_syntax_test.lkml` and `yaml_dashboard_test.lkml`
4. **Regression check** - ensure no new red underlines in existing files

## 📁 **Key Files & Responsibilities**

### **Grammar & Parsing**
- `LookML.bnf` - **Core grammar rules** (add new syntax here)
- `LookMLParserDefinition.kt` - **Parser setup** (rarely modified)
- `LookMLLexerAdapter.kt` - **Lexer routing logic** (modify for new file types)
- `YamlDashboardLexer.kt` - **YAML-specific tokenization** (add YAML features here)

### **Language Features**
- `LookMLSyntaxHighlighter.kt` - **Token-to-color mapping** (modify for new highlighting)
- `LookMLBraceMatcher.kt` - **Bracket matching** (add new bracket types)
- `LookMLCommenter.kt` - **Comment handling** (rarely modified)

### **IDE Integration**
- `plugin.xml` - **Feature registration** (uncomment as features are added)
- `LookMLFileType.kt` - **File type definition** (rarely modified)
- `LookMLLanguage.kt` - **Language registration** (rarely modified)

### **Generated Files (Don't Edit)**
- `src/main/gen/` - **Auto-generated from BNF** (regenerated each build)

## 🐛 **Debugging Guide**

### **Parsing Errors**
1. **Red underlines** → Check grammar rules in BNF
2. **"Unexpected token"** → Check lexer tokenization  
3. **"X expected, got Y"** → Check rule alternatives/precedence

### **Real Examples From Recent Fixes:**

#### **Problem**: `fields: [users.basic*]` failed with "* unexpected"
**Root Cause**: `sort_specification` rule was consuming `users.basic` before `field_pattern` could match the wildcard
**Solution**: Reorder `array_element` alternatives - put `field_pattern` before `sort_specification`

#### **Problem**: `test_explore.is_within_period: yes` failed in YAML
**Root Cause**: YAML lexer processed `yes` as `INLINE_CHAR` instead of checking keywords first  
**Solution**: Add keyword checking before falling back to generic text parsing

#### **Problem**: `type: max` failed with "IDENTIFIER expected, got 'max'"
**Root Cause**: `max` tokenized as `MAX` keyword but `property_value` rule didn't include keywords
**Solution**: Add `keyword` alternative to `property_value` rule

### **Lexer Issues**
1. **Wrong highlighting** → Check `getKeywordToken()` in YAML lexer
2. **Not using YAML lexer** → Check detection logic in `LookMLLexerAdapter`
3. **Token conflicts** → Check token definitions in BNF

### **Testing Strategy**
```bash
# 1. Run development IDE
./gradlew runIde

# 2. Open test files:
src/test/resources/comprehensive_syntax_test.lkml
src/test/resources/yaml_dashboard_test.lkml

# 3. Check for red underlines
# 4. Test specific syntax you changed
# 5. Test edge cases: wildcards, boolean values, sort syntax
```

## 🎯 **Implementation Patterns**

### **Adding New Syntax Support**
1. **Identify context** - Traditional LookML or YAML?
2. **Add tokens** - Update BNF tokens section if needed
3. **Add grammar rules** - Create BNF rules for new syntax
4. **Add lexer support** - Update YAML lexer if needed
5. **Test** - Create test cases

### **Adding New Keywords**
1. **Add token** - `NEW_KEYWORD='new_keyword'` in BNF
2. **Add to keyword rule** - Include in `private keyword ::= (...)`  
3. **Add to YAML lexer** - Add mapping in `getKeywordToken()`
4. **Update grammar rules** - Add keyword to relevant alternatives
5. **Regenerate** parser

### **Fixing Precedence Issues**
- **Rule order matters** - Most specific rules first
- **Example**: `field_pattern` before `qualified_identifier` in arrays
- **Test edge cases** - Ensure new rules don't break existing syntax

## ⚠️ **Common Pitfalls**

### **Grammar Issues**
- **Left recursion** - Can cause infinite loops
- **Precedence order** - Wrong order breaks parsing (we learned this the hard way!)
- **Missing alternatives** - Parser rejects valid syntax
- **Token vs Rule conflicts** - Same name for token and rule causes issues

### **Lexer Issues**  
- **Token conflicts** - Two rules match same text
- **State management** - YAML lexer state gets confused
- **Keyword vs identifier** - Need explicit mapping in both BNF and YAML lexer
- **Context sensitivity** - Value after colon needs different handling

### **Build Issues**
- **Forget to regenerate** - BNF changes not reflected
- **Cache issues** - Clean build fixes mysterious errors
- **Dependency conflicts** - Check gradle sync

## 🚀 **Feature Implementation Guide**

### **Code Completion** (Next Priority)
1. **Implement** `LookMLCompletionContributor`
2. **Uncomment** in `plugin.xml`
3. **Use PSI** to determine context (inside view, measure, etc.)
4. **Provide suggestions** based on LookML spec and current context

### **Error Recovery** (High Impact)
1. **Modify grammar** - Add error recovery rules in BNF
2. **Use `pin` annotations** - Continue parsing after structural elements
3. **Add `recoverWhile`** - Define what to skip during recovery

### **Navigation** 
1. **Implement** `LookMLReferenceContributor`  
2. **Resolve references** using PSI tree
3. **Cross-file resolution** for view/field references
4. **Handle qualified identifiers** like `users.name`

### **Structure View**
1. **Implement** `LookMLStructureViewFactory`
2. **Extract PSI elements** (views, explores, measures)
3. **Create tree structure** for IDE sidebar
4. **Add icons** for different element types

## 🔍 **Advanced Debugging Techniques**

### **PSI Tree Inspection**
- **PsiViewer Plugin** - Install to see PSI structure
- **Debug breakpoints** - Set in parser to see token flow
- **Log token types** - Add debug output to lexer

### **Grammar Testing**
- **Minimal examples** - Create smallest failing case
- **Incremental changes** - Add one rule at a time
- **Grammar validation** - Check for conflicts with Grammar-Kit

### **Performance Profiling**
- **Large file testing** - Test with 1000+ line files
- **Memory usage** - Monitor during parsing
- **Regex optimization** - Simplify token patterns if needed

## 📊 **Current Architecture State**

### **Completed Features** ✅
- Full BNF grammar (200+ tokens)
- Dual-lexer system
- YAML dashboard support
- Wildcard field references
- Boolean value handling
- Sort specification syntax
- Comprehensive error detection

### **Ready Extension Points** 🔌
- PSI element definitions for navigation
- Syntax highlighter for new tokens
- Plugin.xml structure for new features
- Test infrastructure for validation

This guide provides everything needed for deep technical work while preserving the robust foundation already built! 🎯
