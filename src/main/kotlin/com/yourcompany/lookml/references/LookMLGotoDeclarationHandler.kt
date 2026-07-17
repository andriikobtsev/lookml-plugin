package com.yourcompany.lookml.references

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.yourcompany.lookml.psi.LookMLArrayValue
import com.yourcompany.lookml.psi.LookMLDrillFieldsProperty
import com.yourcompany.lookml.psi.LookMLExploreDefinition
import com.yourcompany.lookml.psi.LookMLExploreSourceProperty
import com.yourcompany.lookml.psi.LookMLFieldPattern
import com.yourcompany.lookml.psi.LookMLFieldsProperty
import com.yourcompany.lookml.psi.LookMLFromProperty
import com.yourcompany.lookml.psi.LookMLIncludeStatement
import com.yourcompany.lookml.psi.LookMLJoinDefinition
import com.yourcompany.lookml.psi.LookMLProperty
import com.yourcompany.lookml.psi.LookMLQualifiedIdentifier
import com.yourcompany.lookml.psi.LookMLTemplateReference
import com.yourcompany.lookml.psi.LookMLTypes
import com.yourcompany.lookml.psi.LookMLViewDefinition
import com.yourcompany.lookml.psi.LookMLViewNameProperty
import com.yourcompany.lookml.psi.LookMLWildcardIdentifier

/**
 * Go to Definition for LookML references (Track A).
 *
 * Handles Cmd/Ctrl+click, Cmd/Ctrl+B, F3 on:
 * - `${view.field}` / `${field}` in sql/html (`${TABLE}`, `${SQL_TABLE_NAME}` are ignored),
 * - dotted `view.field` references in field lists and `field:` (drill_fields, fields, filters, ...),
 * - bare field names in field lists (drill_fields, fields, required_fields, hidden_fields),
 * - `set*` / `view.set*` wildcards and `value_format_name:`,
 * - `extends: [...]` targets -> the base view/explore,
 * - refinement names (`view: +orders`) -> the base view,
 * - explore/join names, `from:` and `view_name:` values -> the underlying view,
 * - `include: "path"` -> the included file(s).
 *
 * Field/set lookups follow the full scope: the view's own family (base + `+` refinements) plus its
 * `extends` chain. Inside an explore, join aliases and `from:`/`view_name:` are mapped to the real view.
 *
 * Note on lexing: inside `${...}` the segments are separate tokens (IDENTIFIER `.` IDENTIFIER), but in
 * field lists the whole `view.field` is a SINGLE IDENTIFIER token. Both are handled here.
 *
 * Resolution is reference-free (a [GotoDeclarationHandler] needs no `ContributedReferenceHost`). Real
 * [com.intellij.psi.PsiReference]s - which also power Find Usages / Rename - are added later via a
 * grammar mixin on the same [LookMLResolve] core.
 */
class LookMLGotoDeclarationHandler : GotoDeclarationHandler {

    override fun getGotoDeclarationTargets(
        sourceElement: PsiElement?,
        offset: Int,
        editor: Editor?,
    ): Array<PsiElement>? {
        if (sourceElement == null) return null
        // Pro-gated: Go to Definition is a paid feature. Silent (this also fires on Ctrl/Cmd-hover).
        if (!com.yourcompany.lookml.license.LicenseConditions.allowPaidPluginFeatures()) return null
        val type = sourceElement.node.elementType
        LOG.info("LookMLGoto: called type=$type text=[${sourceElement.text.take(40)}] offset=$offset")
        if (type == LookMLTypes.STRING) {
            val files = resolveInclude(sourceElement)
            if (files.isNotEmpty()) return files
            // `@{constant}` embedded in a (often backtick-quoted) string, e.g. sql_table_name.
            val c = resolveConstantAt(sourceElement, offset)
            LOG.info("LookMLGoto: string -> constant=${c?.text}")
            return c?.let { arrayOf(it) }
        }
        if (type == LookMLTypes.SQL_CONTENT_TOKEN) {
            val c = resolveConstantAt(sourceElement, offset)
            LOG.info("LookMLGoto: sql_content_token -> constant=${c?.text}")
            return c?.let { arrayOf(it) }
        }
        if (type != LookMLTypes.IDENTIFIER) return null
        val tref = PsiTreeUtil.getParentOfType(sourceElement, LookMLTemplateReference::class.java)
        LOG.info("LookMLGoto: identifier; templateRefParent=${tref != null} parentChain=${parentChain(sourceElement)}")
        val target =
            resolveTemplate(sourceElement, offset)
                ?: resolveDeclarationName(sourceElement)
                ?: resolveFromLike(sourceElement)
                ?: resolveExploreSource(sourceElement)
                ?: resolveValueFormatName(sourceElement)
                ?: resolveSetReference(sourceElement)
                ?: resolveIdentifierToken(sourceElement, offset)
        LOG.info("LookMLGoto: resolved target=${target?.text} in ${target?.containingFile?.name}")
        return target?.let { arrayOf(it) }
    }

    private fun parentChain(e: PsiElement): String {
        val sb = StringBuilder()
        var p: PsiElement? = e.parent
        var depth = 0
        while (p != null && depth < 6) {
            sb.append(p.node?.elementType).append(" > ")
            p = p.parent
            depth++
        }
        return sb.toString()
    }

    /**
     * `@{constant}` -> the `constant:` declaration in the manifest. The `@{...}` may be a standalone SQL
     * token or embedded in a string (e.g. a backtick-quoted `sql_table_name`), so we find the `@{...}`
     * span under the caret [offset] within [source]'s text.
     */
    private fun resolveConstantAt(source: PsiElement, offset: Int): PsiElement? {
        val text = source.text
        val local = (offset - source.textRange.startOffset).coerceIn(0, text.length)
        for (match in CONSTANT_REF.findAll(text)) {
            // caret anywhere within `@{...}` (inclusive of the closing brace).
            if (local >= match.range.first && local <= match.range.last + 1) {
                val name = match.groupValues[1].trim()
                if (name.isNotEmpty()) return LookMLResolve.findConstant(source.project, name)
            }
        }
        // Fallback: a token that is exactly one constant ref (caret offset unreliable).
        val single = CONSTANT_REF.matchEntire(text.trim())
        val name = single?.groupValues?.get(1)?.trim()
        return if (!name.isNullOrEmpty()) LookMLResolve.findConstant(source.project, name) else null
    }

    /** `include: "path/file.lkml"` -> the included file(s) (supports a trailing `*` glob). */
    private fun resolveInclude(source: PsiElement): Array<PsiElement> {
        val include = PsiTreeUtil.getParentOfType(source, LookMLIncludeStatement::class.java)
        if (include == null || include.string !== source) return emptyArray()
        val files = LookMLResolve.resolveInclude(source.project, source.text)
        return Array(files.size) { files[it] }
    }

    /**
     * Clicking a declaration's own name: a refinement (`view: +orders`) jumps to its base view; a join
     * or explore name jumps to the underlying view (through `from:`/`view_name:` when present).
     */
    private fun resolveDeclarationName(source: PsiElement): PsiElement? {
        val project = source.project
        when (val parent = source.parent) {
            is LookMLViewDefinition -> {
                if (LookMLResolve.nameLeaf(parent) !== source) return null
                val text = source.text
                return if (text.startsWith("+")) {
                    LookMLResolve.findViewNameLeaf(project, text.removePrefix("+"))
                } else {
                    null
                }
            }
            is LookMLJoinDefinition -> {
                if (LookMLResolve.nameLeaf(parent) !== source) return null
                val real = LookMLResolve.realViewName(source, source.text)
                return LookMLResolve.findViewNameLeaf(project, real)
            }
            is LookMLExploreDefinition -> {
                if (LookMLResolve.nameLeaf(parent) !== source) return null
                val real = LookMLResolve.realViewName(source, source.text)
                return LookMLResolve.findViewNameLeaf(project, real)
            }
            else -> return null
        }
    }

    /** The value of a `from:` / `view_name:` property -> the referenced view. */
    private fun resolveFromLike(source: PsiElement): PsiElement? {
        val parent = source.parent
        val isTarget =
            (parent is LookMLFromProperty && parent.identifier === source) ||
                (parent is LookMLViewNameProperty && parent.identifier === source)
        if (!isTarget) return null
        return LookMLResolve.findViewNameLeaf(source.project, source.text)
    }

    /** `value_format_name: my_format` -> the `named_value_format` declaration (first match). */
    private fun resolveValueFormatName(source: PsiElement): PsiElement? {
        val property = PsiTreeUtil.getParentOfType(source, LookMLProperty::class.java) ?: return null
        if (property.propertyName.text != "value_format_name") return null
        if (PsiTreeUtil.isAncestor(property.propertyName, source, false)) return null
        return LookMLResolve.findNamedValueFormat(source.project, source.text)
    }

    /** A `set*` / `view.set*` wildcard in a field list -> the `set` declaration. */
    private fun resolveSetReference(source: PsiElement): PsiElement? {
        val isWildcard =
            PsiTreeUtil.getParentOfType(source, LookMLWildcardIdentifier::class.java) != null ||
                PsiTreeUtil.getParentOfType(source, LookMLFieldPattern::class.java) != null
        if (!isWildcard) return null
        val project = source.project
        val segments = source.text.split('.')
        return if (segments.size >= 2) {
            LookMLResolve.resolveSet(project, segments[0], segments[1])
        } else {
            PsiTreeUtil.getParentOfType(source, LookMLViewDefinition::class.java)
                ?.let { LookMLResolve.resolveSetInHierarchy(project, it, segments[0]) }
        }
    }

    /**
     * `${view.field}` / `${field}` under a template_reference. Two lexings occur: in SQL blocks the
     * segments are separate `IDENTIFIER . IDENTIFIER` tokens; in `expression:` (and similar) the whole
     * `view.field` is a single dotted IDENTIFIER token. Both are handled.
     */
    private fun resolveTemplate(source: PsiElement, offset: Int): PsiElement? {
        val templateRef =
            PsiTreeUtil.getParentOfType(source, LookMLTemplateReference::class.java) ?: return null
        val idNodes =
            templateRef.node.getChildren(null).filter { it.elementType == LookMLTypes.IDENTIFIER }
        val index = idNodes.indexOfFirst { it === source.node }
        if (index < 0) return null
        val project = source.project
        if (idNodes.size == 1) {
            val parts = idNodes[0].text.split('.')
            if (parts.size == 1) {
                val name = parts[0]
                return if (name == "TABLE" || name == "SQL_TABLE_NAME") {
                    null
                } else {
                    PsiTreeUtil.getParentOfType(templateRef, LookMLViewDefinition::class.java)
                        ?.let { LookMLResolve.resolveFieldInHierarchy(project, it, name) }
                }
            }
            val viewName = LookMLResolve.realViewName(source, parts[0])
            return if (clickedSegment(source, offset, parts) == 0) {
                LookMLResolve.findViewNameLeaf(project, viewName)
            } else {
                LookMLResolve.resolveField(project, viewName, parts[1])
            }
        }
        return if (index == 0) {
            LookMLResolve.findViewNameLeaf(project, LookMLResolve.realViewName(source, idNodes[0].text))
        } else {
            LookMLResolve.resolveField(
                project,
                LookMLResolve.realViewName(source, idNodes[0].text),
                idNodes[1].text,
            )
        }
    }

    /** `explore_source: my_explore` (in a `test:` block) -> the explore (or a same-named view). */
    private fun resolveExploreSource(source: PsiElement): PsiElement? {
        val property = source.parent as? LookMLExploreSourceProperty ?: return null
        if (LookMLResolve.nameLeaf(property) !== source) return null
        val project = source.project
        return LookMLResolve.findExploreNameLeaf(project, source.text)
            ?: LookMLResolve.findViewNameLeaf(project, source.text)
    }

    /**
     * A single IDENTIFIER token in a field list, `extends`, or `field:`. The token text may itself be
     * `view.field` (dots are part of the token here), so we split on `.` and use the caret [offset] to
     * decide which segment was clicked.
     */
    private fun resolveIdentifierToken(source: PsiElement, offset: Int): PsiElement? {
        val text = source.text
        val segments = text.split('.')
        val project = source.project
        val arrayValue = PsiTreeUtil.getParentOfType(source, LookMLArrayValue::class.java)
        val owner = arrayValue?.let { ownerName(it) }

        if (owner == "extends") {
            val view = PsiTreeUtil.getParentOfType(source, LookMLViewDefinition::class.java)
            if (view != null) {
                return LookMLResolve.findViewNameLeaf(project, text, exclude = view)
            }
            val explore = PsiTreeUtil.getParentOfType(source, LookMLExploreDefinition::class.java)
            return if (explore != null) LookMLResolve.findExploreNameLeaf(project, text) else null
        }

        val inFieldList = owner in FIELD_LIST_OWNERS
        val inQualified =
            PsiTreeUtil.getParentOfType(source, LookMLQualifiedIdentifier::class.java) != null
        if (!inFieldList && !inQualified) return null

        return if (segments.size >= 2) {
            val viewName = LookMLResolve.realViewName(source, segments[0])
            val fieldName = segments[1]
            if (clickedSegment(source, offset, segments) == 0) {
                LookMLResolve.findViewNameLeaf(project, viewName)
            } else {
                LookMLResolve.resolveField(project, viewName, fieldName)
            }
        } else {
            PsiTreeUtil.getParentOfType(source, LookMLViewDefinition::class.java)
                ?.let { LookMLResolve.resolveFieldInHierarchy(project, it, segments[0]) }
        }
    }

    /** Which dot-separated segment of [source] the caret [offset] sits on. */
    private fun clickedSegment(source: PsiElement, offset: Int, segments: List<String>): Int {
        val local = (offset - source.textRange.startOffset).coerceIn(0, source.textLength)
        var consumed = 0
        for ((i, segment) in segments.withIndex()) {
            consumed += segment.length
            if (local <= consumed) return i
            consumed += 1 // the '.' separator
        }
        return segments.size - 1
    }

    /** The name of the property that owns [arrayValue] (e.g. `drill_fields`, `fields`, `extends`). */
    private fun ownerName(arrayValue: LookMLArrayValue): String? =
        when (val parent = arrayValue.parent) {
            is LookMLDrillFieldsProperty -> "drill_fields"
            is LookMLFieldsProperty -> "fields"
            else ->
                PsiTreeUtil.getParentOfType(arrayValue, LookMLProperty::class.java, true)
                    ?.propertyName
                    ?.text
        }

    private companion object {
        private val LOG = Logger.getInstance(LookMLGotoDeclarationHandler::class.java)
        private val CONSTANT_REF = Regex("@\\{([^}]*)\\}")
        private val FIELD_LIST_OWNERS =
            setOf("drill_fields", "fields", "required_fields", "hidden_fields")
    }
}
