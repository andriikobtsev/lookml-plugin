package com.yourcompany.lookml.formatting

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.yourcompany.lookml.psi.*

class LookMLFoldingBuilder : FoldingBuilderEx() {
    
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        
        // Find all foldable elements
        val foldableElements = PsiTreeUtil.findChildrenOfAnyType(root, 
            LookMLViewDefinition::class.java,
            LookMLExploreDefinition::class.java,
            LookMLDimensionDefinition::class.java,
            LookMLMeasureDefinition::class.java,
            LookMLParameterDefinition::class.java,
            LookMLTestDefinition::class.java,
            LookMLJoinDefinition::class.java
        )
        
        for (element in foldableElements) {
            val range = getFoldingRange(element)
            if (range != null && range.length > 0) {
                descriptors.add(FoldingDescriptor(element.node, range))
            }
        }
        
        return descriptors.toTypedArray()
    }
    
    private fun getFoldingRange(element: PsiElement): TextRange? {
        val text = element.text
        val startOffset = element.textRange.startOffset
        
        // Find opening brace
        val openBraceIndex = text.indexOf('{')
        if (openBraceIndex == -1) return null
        
        // Find closing brace
        val closeBraceIndex = text.lastIndexOf('}')
        if (closeBraceIndex == -1 || closeBraceIndex <= openBraceIndex) return null
        
        val startRange = startOffset + openBraceIndex
        val endRange = startOffset + closeBraceIndex + 1
        
        return TextRange(startRange, endRange)
    }
    
    override fun getPlaceholderText(node: ASTNode): String? {
        return when (node.psi) {
            is LookMLViewDefinition -> "{ view content... }"
            is LookMLExploreDefinition -> "{ explore content... }"
            is LookMLDimensionDefinition -> "{ dimension content... }"
            is LookMLMeasureDefinition -> "{ measure content... }"
            is LookMLParameterDefinition -> "{ parameter content... }"
            is LookMLTestDefinition -> "{ test content... }"
            is LookMLJoinDefinition -> "{ join content... }"
            else -> "{ ... }"
        }
    }
    
    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return false // Don't collapse by default
    }
}