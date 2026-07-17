package com.yourcompany.lookml.references

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiTreeUtil
import com.yourcompany.lookml.psi.LookMLArrayElement
import com.yourcompany.lookml.psi.LookMLArrayValue
import com.yourcompany.lookml.psi.LookMLDrillFieldsProperty
import com.yourcompany.lookml.psi.LookMLFieldPattern
import com.yourcompany.lookml.psi.LookMLFieldsProperty
import com.yourcompany.lookml.psi.LookMLFilterSpec
import com.yourcompany.lookml.psi.LookMLFromProperty
import com.yourcompany.lookml.psi.LookMLProperty
import com.yourcompany.lookml.psi.LookMLPropertyValue
import com.yourcompany.lookml.psi.LookMLQualifiedIdentifier
import com.yourcompany.lookml.psi.LookMLSqlBlockContent
import com.yourcompany.lookml.psi.LookMLTemplateReference
import com.yourcompany.lookml.psi.LookMLTypes
import com.yourcompany.lookml.psi.LookMLViewDefinition
import com.yourcompany.lookml.psi.LookMLViewNameProperty
import com.yourcompany.lookml.psi.LookMLWildcardIdentifier
import com.yourcompany.lookml.psi.LookMLYamlListEntry
import com.yourcompany.lookml.psi.LookMLYamlProperty
import com.yourcompany.lookml.psi.LookMLYamlSimpleValue
import com.yourcompany.lookml.psi.LookMLYamlTextLine
import com.yourcompany.lookml.psi.LookMLYamlValue

/**
 * Builds the [LookMLReference]s for a reference-host element (see [com.yourcompany.lookml.psi.impl.LookMLReferenceHostMixin]).
 *
 * This is the reference-layer counterpart of [LookMLGotoDeclarationHandler]; both delegate to the same
 * [LookMLResolve] core, and both resolve to the target's name leaf so Go to Definition dedupes. The
 * references additionally unlock Find Usages and Rename via [LookMLReference.isReferenceTo].
 *
 * Milestone A covers `${view.field}` template refs and field-list entries (bare/dotted fields, `set*`
 * wildcards, `extends` targets). Milestone C adds `filters:` keys, `from:`/`view_name:` view refs,
 * `value_format_name:` -> `named_value_format`, and `@{constant}` refs inside SQL blocks. Each of these
 * now also powers Find Usages and Rename (the handler keeps go-to working and dedupes against these).
 */
object LookMLReferences {

    private val EMPTY = emptyArray<PsiReference>()

    private val CONSTANT_REF = Regex("@\\{([^}]*)\\}")

    /** A `view.field` token (exactly two dot-separated identifier segments) inside a dashboard value. */
    private val FIELD_TOKEN = Regex("(?<![\\w.])([A-Za-z_]\\w*)\\.([A-Za-z_]\\w*)(?![\\w.])")

    /** `explore: name` inside a collapsed dashboard text-line blob. */
    private val EXPLORE_KV = Regex("\\bexplore\\s*:\\s*([A-Za-z_]\\w*)")

    /** `model: name` inside a collapsed dashboard text-line blob (leading identifier before any `@{...}`). */
    private val MODEL_KV = Regex("\\bmodel\\s*:\\s*([A-Za-z_]\\w*)")

    private val FIELD_LIST_OWNERS =
        setOf("drill_fields", "fields", "required_fields", "hidden_fields")

    /**
     * Dashboard (`.dashboard.lookml`) properties whose value carries `view.field` references.
     * `filters` is included for element-level filter maps: on a clean parse `filters:\n  view.field: v`
     * lands `view.field` as the `filters:` simple value (the map key), so it must be scanned like a field.
     */
    private val DASHBOARD_FIELD_OWNERS =
        setOf("fields", "sorts", "hidden_fields", "pivots", "filters")

    fun forHost(element: PsiElement): Array<PsiReference> =
        when (element) {
            is LookMLTemplateReference -> templateRefs(element)
            is LookMLArrayElement -> arrayElementRefs(element)
            is LookMLFilterSpec -> filterSpecRefs(element)
            is LookMLFromProperty -> viewRef(element, element.identifier)
            is LookMLViewNameProperty -> viewRef(element, element.identifier)
            is LookMLPropertyValue -> valueFormatRefs(element)
            is LookMLSqlBlockContent -> constantRefs(element)
            // Skip a simple value that wraps a text-line: the text-line host handles it (avoids doubling).
            is LookMLYamlSimpleValue ->
                if (PsiTreeUtil.findChildOfType(element, LookMLYamlTextLine::class.java) != null) {
                    EMPTY
                } else {
                    dashboardValueRefs(element)
                }
            is LookMLYamlTextLine -> dashboardBlobRefs(element)
            else -> EMPTY
        }

    // ─── filters: [ field: "value", view.field: "value" ] ───────────────────────

    /** The KEY of a `filters:` entry is a field reference (bare -> view family, or `view.field`). */
    private fun filterSpecRefs(spec: LookMLFilterSpec): Array<PsiReference> {
        val keyNode =
            spec.node.getChildren(null).firstOrNull {
                it.elementType == LookMLTypes.IDENTIFIER || it.psi is LookMLQualifiedIdentifier
            } ?: return EMPTY
        val project = spec.project
        val keyText = keyNode.text
        val keyStart = keyNode.textRange.startOffset - spec.textRange.startOffset
        val segments = keyText.split('.')
        if (segments.size >= 2) {
            val viewName = segments[0]
            val fieldName = segments[1]
            val fieldStart = keyStart + viewName.length + 1
            return arrayOf(
                LookMLReference(spec, TextRange(keyStart, keyStart + viewName.length)) {
                    LookMLResolve.findViewNameLeaf(project, LookMLResolve.realViewName(spec, viewName))
                },
                LookMLReference(spec, TextRange(fieldStart, fieldStart + fieldName.length)) {
                    LookMLResolve.resolveField(project, LookMLResolve.realViewName(spec, viewName), fieldName)
                },
            )
        }
        return arrayOf(
            LookMLReference(spec, TextRange(keyStart, keyStart + keyText.length)) {
                PsiTreeUtil.getParentOfType(spec, LookMLViewDefinition::class.java)
                    ?.let { LookMLResolve.resolveFieldInHierarchy(project, it, keyText) }
            },
        )
    }

    // ─── from: / view_name: -> view ─────────────────────────────────────────────

    private fun viewRef(host: PsiElement, id: PsiElement?): Array<PsiReference> {
        if (id == null) return EMPTY
        val name = id.text
        val range = id.textRange.shiftLeft(host.textRange.startOffset)
        return arrayOf(
            LookMLReference(host, range) { LookMLResolve.findViewNameLeaf(host.project, name) },
        )
    }

    // ─── value_format_name: my_format -> named_value_format ─────────────────────

    private fun valueFormatRefs(pv: LookMLPropertyValue): Array<PsiReference> {
        val property = pv.parent as? LookMLProperty ?: return EMPTY
        if (property.propertyName.text != "value_format_name") return EMPTY
        val name = pv.text
        if (name.isEmpty()) return EMPTY
        return arrayOf(
            LookMLReference(pv, TextRange(0, name.length)) {
                LookMLResolve.findNamedValueFormat(pv.project, name)
            },
        )
    }

    // ─── @{constant} inside SQL blocks -> constant: declaration ─────────────────

    private fun constantRefs(content: LookMLSqlBlockContent): Array<PsiReference> {
        val text = content.text
        if (!text.contains("@{")) return EMPTY
        val refs = ArrayList<PsiReference>()
        for (match in CONSTANT_REF.findAll(text)) {
            val group = match.groups[1] ?: continue
            val name = group.value.trim()
            if (name.isEmpty()) continue
            refs +=
                LookMLReference(content, TextRange(group.range.first, group.range.last + 1)) {
                    LookMLResolve.findConstant(content.project, name)
                }
        }
        return if (refs.isEmpty()) EMPTY else refs.toTypedArray()
    }

    // ─── ${view.field} / ${field} ───────────────────────────────────────────────

    private fun templateRefs(ref: LookMLTemplateReference): Array<PsiReference> {
        val project = ref.project
        val base = ref.textRange.startOffset
        val idNodes = ref.node.getChildren(null).filter { it.elementType == LookMLTypes.IDENTIFIER }

        // SQL-block lexing: `orders . id` as separate IDENTIFIER tokens.
        if (idNodes.size >= 2) {
            val viewNode = idNodes[0]
            val fieldNode = idNodes[1]
            val viewName = viewNode.text
            return arrayOf(
                LookMLReference(ref, viewNode.textRange.shiftLeft(base)) {
                    LookMLResolve.findViewNameLeaf(project, LookMLResolve.realViewName(ref, viewName))
                },
                LookMLReference(ref, fieldNode.textRange.shiftLeft(base)) {
                    LookMLResolve.resolveField(
                        project,
                        LookMLResolve.realViewName(ref, viewName),
                        fieldNode.text,
                    )
                },
            )
        }

        // expression: lexing - the whole `view.field` is one dotted IDENTIFIER token.
        val token = idNodes.firstOrNull() ?: return EMPTY
        val start = token.textRange.startOffset - base
        val parts = token.text.split('.')
        if (parts.size == 1) {
            val name = parts[0]
            if (name == "TABLE" || name == "SQL_TABLE_NAME") return EMPTY
            return arrayOf(
                LookMLReference(ref, TextRange(start, start + name.length)) {
                    PsiTreeUtil.getParentOfType(ref, LookMLViewDefinition::class.java)
                        ?.let { LookMLResolve.resolveFieldInHierarchy(project, it, name) }
                },
            )
        }
        val viewName = parts[0]
        val fieldName = parts[1]
        val fieldStart = start + viewName.length + 1
        return arrayOf(
            LookMLReference(ref, TextRange(start, start + viewName.length)) {
                LookMLResolve.findViewNameLeaf(project, LookMLResolve.realViewName(ref, viewName))
            },
            LookMLReference(ref, TextRange(fieldStart, fieldStart + fieldName.length)) {
                LookMLResolve.resolveField(project, LookMLResolve.realViewName(ref, viewName), fieldName)
            },
        )
    }

    // ─── Field-list entries: drill_fields / fields / extends / set* ─────────────

    private fun arrayElementRefs(el: LookMLArrayElement): Array<PsiReference> {
        val text = el.text
        // Nested `${...}` is handled by its own template_reference host; strings/numbers are not refs.
        if (text.contains("\${")) return EMPTY
        val firstType = el.node.firstChildNode?.elementType
        if (firstType == LookMLTypes.STRING || firstType == LookMLTypes.NUMBER) return EMPTY

        val project = el.project
        val owner = ownerName(el)

        if (owner == "extends") {
            return arrayOf(
                LookMLReference(el, TextRange(0, text.length)) {
                    val view = PsiTreeUtil.getParentOfType(el, LookMLViewDefinition::class.java)
                    if (view != null) {
                        LookMLResolve.findViewNameLeaf(project, text, exclude = view)
                    } else {
                        LookMLResolve.findExploreNameLeaf(project, text)
                    }
                },
            )
        }

        val isWildcard =
            PsiTreeUtil.findChildOfType(el, LookMLWildcardIdentifier::class.java) != null ||
                PsiTreeUtil.findChildOfType(el, LookMLFieldPattern::class.java) != null
        if (isWildcard) {
            val name = text.removeSuffix("*")
            val segments = name.split('.')
            return if (segments.size >= 2) {
                arrayOf(
                    LookMLReference(el, TextRange(segments[0].length + 1, name.length)) {
                        LookMLResolve.resolveSet(project, segments[0], segments[1])
                    },
                )
            } else {
                arrayOf(
                    LookMLReference(el, TextRange(0, segments[0].length)) {
                        PsiTreeUtil.getParentOfType(el, LookMLViewDefinition::class.java)
                            ?.let { LookMLResolve.resolveSetInHierarchy(project, it, segments[0]) }
                    },
                )
            }
        }

        if (owner !in FIELD_LIST_OWNERS) return EMPTY

        val segments = text.split('.')
        return if (segments.size >= 2) {
            val viewName = segments[0]
            val fieldName = segments[1]
            val fieldStart = viewName.length + 1
            arrayOf(
                LookMLReference(el, TextRange(0, viewName.length)) {
                    LookMLResolve.findViewNameLeaf(project, LookMLResolve.realViewName(el, viewName))
                },
                LookMLReference(el, TextRange(fieldStart, fieldStart + fieldName.length)) {
                    LookMLResolve.resolveField(project, LookMLResolve.realViewName(el, viewName), fieldName)
                },
            )
        } else {
            arrayOf(
                LookMLReference(el, TextRange(0, segments[0].length)) {
                    PsiTreeUtil.getParentOfType(el, LookMLViewDefinition::class.java)
                        ?.let { LookMLResolve.resolveFieldInHierarchy(project, it, segments[0]) }
                },
            )
        }
    }

    // ─── YAML dashboards: fields/sorts/hidden_fields/pivots + explore:/model: ───

    /**
     * References inside a pure-YAML dashboard value, dispatched by the owning property name:
     * - `explore: name` -> explore, `model: name` -> the model file (clean single-token values),
     * - `fields:`/`sorts:`/`hidden_fields:`/`pivots:` -> every `view.field` token in the value.
     *
     * NOTE on parsing: the YAML dashboard grammar does NOT build a flow-array here - `[a.b, c.d]` (and
     * often the following lines) collapse into one greedy `yaml_text_line`, with each `view.field` lexed as
     * a single dotted IDENTIFIER. So for field lists we SCAN the value text for `view.field` tokens rather
     * than relying on array/qualified-identifier nodes. This is robust to that parse and needs no grammar
     * change to the shipped YAML parser.
     */
    private fun dashboardValueRefs(value: LookMLYamlSimpleValue): Array<PsiReference> {
        val owner =
            (value.parent as? LookMLYamlValue)?.let { it.parent as? LookMLYamlProperty }
                ?.yamlPropertyName?.text ?: return EMPTY
        val project = value.project
        val text = value.text
        return when {
            owner == "explore" -> singleTokenRef(value, text) { LookMLResolve.findExploreNameLeaf(project, it) }
            owner == "model" -> singleTokenRef(value, text) { LookMLResolve.findModelFile(project, it) }
            owner in DASHBOARD_FIELD_OWNERS -> fieldTokenRefs(value, text, project, enclosingExploreName(value))
            else -> EMPTY
        }
    }

    /**
     * The `explore:` name of the dashboard element that owns [value], so structured `view.field` values
     * (fields / sorts / filters / ...) map their alias through the explore's `from:` / joins - the same way
     * the collapsed-blob path already does. On the flattened YAML parse the element's properties are largely
     * siblings, so we scan backwards to the element start (its `- ` list entry) for the nearest `explore:`.
     */
    private fun enclosingExploreName(value: PsiElement): String? {
        val owningProp = PsiTreeUtil.getParentOfType(value, LookMLYamlProperty::class.java) ?: return null
        var sib = owningProp.prevSibling
        while (sib != null) {
            when (sib) {
                is LookMLYamlProperty ->
                    if (sib.yamlPropertyName.text == "explore") return simpleValueText(sib)
                is LookMLYamlListEntry -> {
                    // Element boundary: an `explore:` may sit inside the list-entry's item content.
                    return PsiTreeUtil.findChildrenOfType(sib, LookMLYamlProperty::class.java)
                        .firstOrNull { it.yamlPropertyName.text == "explore" }
                        ?.let { simpleValueText(it) }
                }
            }
            sib = sib.prevSibling
        }
        return null
    }

    private fun simpleValueText(prop: LookMLYamlProperty): String? =
        PsiTreeUtil.findChildOfType(prop, LookMLYamlSimpleValue::class.java)?.text?.trim()?.takeIf { it.isNotEmpty() }

    /**
     * A collapsed dashboard text-line blob. Real dashboards routinely contain an unquoted multi-word value
     * (e.g. `name: Data as of`) which makes the YAML parser produce ONE greedy `yaml_text_line` that
     * swallows the following properties (`model:`, `explore:`, `fields:`, `listen:`, ...). The structured
     * PSI is therefore unreliable, so we scan the blob text directly for the things we care about:
     * `explore:`/`model:` values, `@{constant}` refs, and `view.field` tokens (fields/sorts/listen/etc).
     */
    private fun dashboardBlobRefs(line: LookMLYamlTextLine): Array<PsiReference> {
        val project = line.project
        val text = line.text
        // The element's `explore:` (if present in this blob) maps `alias.field` through joins / from:.
        val exploreName = EXPLORE_KV.find(text)?.groups?.get(1)?.value
        val refs = ArrayList<PsiReference>()
        for (m in EXPLORE_KV.findAll(text)) {
            val g = m.groups[1] ?: continue
            val name = g.value
            refs += LookMLReference(line, TextRange(g.range.first, g.range.last + 1)) {
                LookMLResolve.findExploreNameLeaf(project, name)
            }
        }
        for (m in MODEL_KV.findAll(text)) {
            val g = m.groups[1] ?: continue
            val name = g.value
            refs += LookMLReference(line, TextRange(g.range.first, g.range.last + 1)) {
                LookMLResolve.findModelFile(project, name)
            }
        }
        for (m in CONSTANT_REF.findAll(text)) {
            val g = m.groups[1] ?: continue
            val name = g.value.trim()
            if (name.isEmpty()) continue
            refs += LookMLReference(line, TextRange(g.range.first, g.range.last + 1)) {
                LookMLResolve.findConstant(project, name)
            }
        }
        for (m in FIELD_TOKEN.findAll(text)) {
            val vg = m.groups[1] ?: continue
            val fg = m.groups[2] ?: continue
            val alias = vg.value
            val fieldName = fg.value
            refs += LookMLReference(line, TextRange(vg.range.first, vg.range.last + 1)) {
                LookMLResolve.dashboardViewLeaf(project, exploreName, alias)
            }
            refs += LookMLReference(line, TextRange(fg.range.first, fg.range.last + 1)) {
                LookMLResolve.resolveDashboardField(project, exploreName, alias, fieldName)
            }
        }
        return if (refs.isEmpty()) EMPTY else refs.toTypedArray()
    }

    /** A clean single-identifier value (`explore:`/`model:`); rejects multi-token/greedy values. */
    private fun singleTokenRef(
        host: PsiElement,
        text: String,
        resolve: (String) -> PsiElement?,
    ): Array<PsiReference> {
        val start = text.indexOfFirst { !it.isWhitespace() }
        if (start < 0) return EMPTY
        val end = text.indexOfLast { !it.isWhitespace() } + 1
        val name = text.substring(start, end)
        if (name.isEmpty() || name.any { it.isWhitespace() }) return EMPTY
        return arrayOf(LookMLReference(host, TextRange(start, end)) { resolve(name) })
    }

    /**
     * Every `view.field` token in [text] -> a view ref + a field ref (find-usages / go-to / rename).
     * [exploreName] (the owning element's `explore:`) maps `alias.field` through the explore's `from:`/joins,
     * matching the collapsed-blob path. A `null` explore falls back to a literal view/field lookup.
     */
    private fun fieldTokenRefs(
        host: PsiElement,
        text: String,
        project: Project,
        exploreName: String?,
    ): Array<PsiReference> {
        val refs = ArrayList<PsiReference>()
        for (m in FIELD_TOKEN.findAll(text)) {
            val vg = m.groups[1] ?: continue
            val fg = m.groups[2] ?: continue
            val alias = vg.value
            val fieldName = fg.value
            refs += LookMLReference(host, TextRange(vg.range.first, vg.range.last + 1)) {
                LookMLResolve.dashboardViewLeaf(project, exploreName, alias)
            }
            refs += LookMLReference(host, TextRange(fg.range.first, fg.range.last + 1)) {
                LookMLResolve.resolveDashboardField(project, exploreName, alias, fieldName)
            }
        }
        return if (refs.isEmpty()) EMPTY else refs.toTypedArray()
    }

    /** The name of the property that owns [el] (e.g. `drill_fields`, `fields`, `extends`). */
    private fun ownerName(el: LookMLArrayElement): String? {
        val arrayValue = PsiTreeUtil.getParentOfType(el, LookMLArrayValue::class.java) ?: return null
        return when (arrayValue.parent) {
            is LookMLDrillFieldsProperty -> "drill_fields"
            is LookMLFieldsProperty -> "fields"
            else ->
                PsiTreeUtil.getParentOfType(arrayValue, LookMLProperty::class.java, true)
                    ?.propertyName
                    ?.text
        }
    }
}
