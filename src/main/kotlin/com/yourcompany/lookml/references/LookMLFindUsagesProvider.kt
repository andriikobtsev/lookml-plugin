package com.yourcompany.lookml.references

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.yourcompany.lookml.lexer.LookMLLexerAdapter
import com.yourcompany.lookml.license.LicenseConditions
import com.yourcompany.lookml.psi.LookMLAccessGrantDefinition
import com.yourcompany.lookml.psi.LookMLConstantDefinition
import com.yourcompany.lookml.psi.LookMLDatagroupDefinition
import com.yourcompany.lookml.psi.LookMLDimensionDefinition
import com.yourcompany.lookml.psi.LookMLDimensionGroupDefinition
import com.yourcompany.lookml.psi.LookMLExploreDefinition
import com.yourcompany.lookml.psi.LookMLFilterDefinition
import com.yourcompany.lookml.psi.LookMLJoinDefinition
import com.yourcompany.lookml.psi.LookMLMeasureDefinition
import com.yourcompany.lookml.psi.LookMLNamedElement
import com.yourcompany.lookml.psi.LookMLNamedValueFormatDefinition
import com.yourcompany.lookml.psi.LookMLParameterDefinition
import com.yourcompany.lookml.psi.LookMLSetDefinition
import com.yourcompany.lookml.psi.LookMLTypes
import com.yourcompany.lookml.psi.LookMLViewDefinition

/**
 * Enables Find Usages (Alt+F7) and the shared Rename infrastructure for LookML declarations.
 *
 * The words scanner feeds the identifier index off the real parser lexer, so dotted references like
 * `orders.total` (a single IDENTIFIER token) are split into `orders` / `total` and each declaration name
 * becomes findable. Usages themselves come from [LookMLReference.isReferenceTo].
 */
class LookMLFindUsagesProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner =
        DefaultWordsScanner(
            LookMLLexerAdapter(),
            TokenSet.create(LookMLTypes.IDENTIFIER),
            TokenSet.create(LookMLTypes.COMMENT),
            TokenSet.create(LookMLTypes.STRING),
        )

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean =
        LicenseConditions.allowPaidPluginFeatures() && psiElement is LookMLNamedElement

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String =
        when (element) {
            is LookMLViewDefinition -> "view"
            is LookMLExploreDefinition -> "explore"
            is LookMLDimensionDefinition -> "dimension"
            is LookMLDimensionGroupDefinition -> "dimension group"
            is LookMLMeasureDefinition -> "measure"
            is LookMLFilterDefinition -> "filter"
            is LookMLParameterDefinition -> "parameter"
            is LookMLSetDefinition -> "set"
            is LookMLJoinDefinition -> "join"
            is LookMLConstantDefinition -> "constant"
            is LookMLDatagroupDefinition -> "datagroup"
            is LookMLAccessGrantDefinition -> "access grant"
            is LookMLNamedValueFormatDefinition -> "value format"
            else -> "identifier"
        }

    override fun getDescriptiveName(element: PsiElement): String =
        (element as? LookMLNamedElement)?.name ?: element.text

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String =
        getDescriptiveName(element)
}
