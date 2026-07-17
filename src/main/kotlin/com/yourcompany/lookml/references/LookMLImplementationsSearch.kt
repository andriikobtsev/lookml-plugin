package com.yourcompany.lookml.references

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.PsiElement
import com.intellij.util.Processor
import com.yourcompany.lookml.license.LicenseConditions
import com.yourcompany.lookml.psi.LookMLViewDefinition

/**
 * "Go to Implementation(s)" (Ctrl/Cmd+Alt+B) on a view -> every view that `extends` or refines it,
 * project-wide. This is the keyboard-action counterpart of the down-arrow gutter marker
 * ([LookMLViewLineMarkerProvider]) and reuses the same [LookMLResolve] hierarchy data.
 *
 * Registered under the `definitionsSearch` extension point, whose executors receive the definition
 * [PsiElement] directly as the query parameter.
 *
 * Pro-gated (silent when unlicensed, like the rest of the navigation layer).
 */
class LookMLImplementationsSearch :
    QueryExecutorBase<PsiElement, PsiElement>(true) {

    override fun processQuery(
        element: PsiElement,
        consumer: Processor<in PsiElement>,
    ) {
        if (!LicenseConditions.allowPaidPluginFeatures()) return
        val view = viewFor(element) ?: return
        val project = element.project
        val baseName = LookMLResolve.canonicalViewName(view) ?: return
        val self = LookMLResolve.nameLeaf(view)

        val impls = LinkedHashSet<PsiElement>()
        for (v in LookMLResolve.findViewsExtending(project, baseName)) {
            LookMLResolve.nameLeaf(v)?.let { if (it !== self) impls.add(it) }
        }
        for (v in LookMLResolve.findRefinements(project, baseName)) {
            LookMLResolve.nameLeaf(v)?.let { if (it !== self) impls.add(it) }
        }
        for (impl in impls) {
            if (!consumer.process(impl)) return
        }
    }

    /** The [LookMLViewDefinition] when [element] is a view declaration or its name leaf; else null. */
    private fun viewFor(element: PsiElement): LookMLViewDefinition? {
        (element as? LookMLViewDefinition)?.let { return it }
        val parent = element.parent as? LookMLViewDefinition ?: return null
        return if (LookMLResolve.nameLeaf(parent) === element) parent else null
    }
}
