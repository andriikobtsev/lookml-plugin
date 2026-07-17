package com.yourcompany.lookml.references

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.yourcompany.lookml.psi.LookMLTypes
import com.yourcompany.lookml.psi.LookMLViewDefinition

/**
 * Gutter markers on view declarations, mirroring Kotlin/Java's override markers:
 * - up arrow on a view that `extends` (or refines, `+name`) others -> jump to the base view(s),
 * - down arrow on a view that is extended or refined by others -> jump to those view(s).
 *
 * Placed as slow markers (they scan the project for extenders/refinements) on the view's name leaf.
 */
class LookMLViewLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(
        elements: List<PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>,
    ) {
        // Pro-gated: extends/refinement navigation gutter markers require a license.
        if (!com.yourcompany.lookml.license.LicenseConditions.allowPaidPluginFeatures()) return
        for (element in elements) {
            if (element.node?.elementType != LookMLTypes.IDENTIFIER) continue
            val view = element.parent as? LookMLViewDefinition ?: continue
            if (LookMLResolve.nameLeaf(view) !== element) continue

            val project = element.project
            val rawName = element.text
            val isRefinement = rawName.startsWith("+")
            val baseName = rawName.removePrefix("+")

            // Up: bases this view extends, plus (for a refinement) the base view it refines.
            val parents =
                LookMLResolve.extendsTargetNames(view)
                    .map { it.removePrefix("+") }
                    .flatMap { LookMLResolve.findViews(project, it) }
                    .toMutableList()
            if (isRefinement) parents.addAll(LookMLResolve.findViews(project, baseName))
            val parentLeaves = distinctLeaves(parents, element)
            if (parentLeaves.isNotEmpty()) {
                result.add(
                    NavigationGutterIconBuilder.create(AllIcons.Gutter.OverridingMethod)
                        .setTargets(parentLeaves)
                        .setTooltipText(if (isRefinement) "Refines / extends: go to base view" else "Extends: go to base view")
                        .createLineMarkerInfo(element),
                )
            }

            // Down: views that extend this one, plus (for a base view) its refinements.
            val children =
                LookMLResolve.findViewsExtending(project, baseName).toMutableList()
            if (!isRefinement) children.addAll(LookMLResolve.findRefinements(project, baseName))
            val childLeaves = distinctLeaves(children, element)
            if (childLeaves.isNotEmpty()) {
                result.add(
                    NavigationGutterIconBuilder.create(AllIcons.Gutter.OverridenMethod)
                        .setTargets(childLeaves)
                        .setTooltipText("Extended / refined by")
                        .createLineMarkerInfo(element),
                )
            }
        }
    }

    /**
     * Name leaves of [views], excluding [self], deduped by physical location. Dedup prevents a target
     * from appearing twice (e.g. the same base reached via both an `extends` entry and the refinement
     * relationship), which showed up as a chooser popup with two identical entries.
     */
    private fun distinctLeaves(views: List<LookMLViewDefinition>, self: PsiElement): List<PsiElement> =
        views
            .mapNotNull { LookMLResolve.nameLeaf(it) }
            .filter { it !== self }
            .distinctBy { it.containingFile?.viewProvider?.virtualFile?.path to it.textRange.startOffset }
}
