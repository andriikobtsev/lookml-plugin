# Plugin Capability Ideas & Development Notes

## 🚀 **Future Feature Ideas Spotted During Cleanup**

### **From plugin.xml (commented out but ready to implement):**
- Code Completion Contributor
- Validation Annotator (error highlighting)
- Code Formatter  
- Reference Contributor (navigation)
- Find Usages Provider
- Refactoring Support
- Replace Field Usage Action (with Ctrl+Shift+R)

### **From README analysis:**
- Structure View/Code Folding
- Live Templates (code snippets)
- Documentation hover
- Rename refactoring with cross-file updates

### **User-requested priorities:**
1. **Code Completion** - Very important next milestone
2. **Error Recovery** - Don't stop at first error, show multiple issues
3. **Structure View** - Code folding for views/explores/measures, YAML blocks
4. **Navigation** - Click field → go to definition, view name → file
5. **Multi-file support** - Handle project-wide references

## 🔧 **Technical Implementation Notes**

### **Completed Architecture:**
- Dual-lexer system (Traditional + YAML)
- Smart file detection based on content
- BNF grammar with 200+ tokens
- Full YAML dashboard support
- Wildcard field reference support
- Boolean value handling in all contexts

### **Next Implementation Strategy:**
1. **Code Completion** - Leverage existing PSI tree, add completion contributor
2. **Error Recovery** - Improve parser error handling in BNF grammar  
3. **Structure View** - Use PSI elements to build collapsible tree
4. **Navigation** - Implement reference resolution using existing PSI

### **Key Files for Future Development:**
- `plugin.xml` - Uncomment features as they're implemented
- `LookML.bnf` - Grammar improvements for error recovery
- `LookMLParserDefinition.kt` - Core parser configuration
- `psi/` directory - PSI element definitions for navigation/completion

## 📋 **Development Milestones**

### **Milestone 1: Enhanced Editing Experience**
- [x] Code completion for keywords and properties ✅ **COMPLETED**
- [ ] Better error recovery (multiple errors shown)
- [ ] Structure view with code folding

### **Milestone 2: Navigation & References**  
- [ ] Click-to-go-to-definition
- [ ] Find usages across files
- [ ] Hover documentation
- [ ] Project-wide field resolution

### **Milestone 3: Advanced Features**
- [ ] Code formatting
- [ ] Refactoring (rename with updates)
- [ ] Live templates
- [ ] Performance optimization for large files

This provides a clear roadmap for continued development!
