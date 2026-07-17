package com.yourcompany.lookml.references

import com.intellij.codeInsight.CodeInsightActionHandler
import com.intellij.codeInsight.navigation.PsiTargetNavigator
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.PsiNavigateUtil
import com.yourcompany.lookml.license.LicenseConditions
import com.yourcompany.lookml.psi.LookMLViewDefinition

/**
 * "Go to Super" (Navigate | Go to Super Method/Class, Ctrl+U / Cmd+U) on a view -> the base view(s)
 * it `extends`, plus (for a refinement `+name`) the base view it refines. Keyboard counterpart of the
 * up-arrow gutter marker ([LookMLViewLineMarkerProvider]); reuses the same [LookMLResolve] data and
 * the same location-based dedup so a base reached via both `extends` and refinement is offered once.
 *
 * Pro-gated (silent when unlicensed, like the rest of the navigation layer).
 */
class LookMLGotoSuperHandler : CodeInsightActionHandler {

    override fun startInWriteAction(): Boolean = false

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        if (!LicenseConditions.allowPaidPluginFeatures()) return
        val caret = file.findElementAt(editor.caretModel.offset) ?: return
        val view = PsiTreeUtil.getParentOfType(caret, LookMLViewDefinition::class.java, false) ?: return
        val targets = superTargets(project, view)

        when (targets.size) {
            0 -> return
            1 -> PsiNavigateUtil.navigate(targets[0])
            else -> PsiTargetNavigator(targets).navigate(editor, "Go to Base View")
        }
    }

    companion object {
        /**
         * Base view name leaves for [view]: the views it `extends`, plus (for a refinement `+name`)
         * the base view it refines. Deduped by physical location, excluding the view itself.
         */
        fun superTargets(project: Project, view: LookMLViewDefinition): List<PsiElement> {
            val self = LookMLResolve.nameLeaf(view)
            val rawName = LookMLResolve.declName(view) ?: return emptyList()
            val isRefinement = rawName.startsWith("+")
            val baseName = rawName.removePrefix("+")

            val bases =
                LookMLResolve.extendsTargetNames(view)
                    .map { it.removePrefix("+") }
                    .flatMap { LookMLResolve.findViews(project, it) }
                    .toMutableList()
            if (isRefinement) bases.addAll(LookMLResolve.findViews(project, baseName))

            return bases
                .mapNotNull { LookMLResolve.nameLeaf(it) }
                .filter { it !== self }
                .distinctBy { it.containingFile?.viewProvider?.virtualFile?.path to it.textRange.startOffset }
        }
    }
}
