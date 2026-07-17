package com.yourcompany.lookml.references

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.TokenType
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import com.yourcompany.lookml.LookMLFileType
import com.yourcompany.lookml.psi.LookMLArrayElement
import com.yourcompany.lookml.psi.LookMLArrayValue
import com.yourcompany.lookml.psi.LookMLConstantDefinition
import com.yourcompany.lookml.psi.LookMLDimensionGroupDefinition
import com.yourcompany.lookml.psi.LookMLExploreDefinition
import com.yourcompany.lookml.psi.LookMLJoinDefinition
import com.yourcompany.lookml.psi.LookMLNamedValueFormatDefinition
import com.yourcompany.lookml.psi.LookMLTypes
import com.yourcompany.lookml.psi.LookMLViewDefinition

/**
 * Track A resolution core: turn LookML names into their declaration PSI, project-wide.
 *
 * Milestone 1 scope: field references (`${view.field}`, `${field}`) and the view segment. Extends and
 * refinements are intentionally NOT followed here - that arrives with the inheritance chunk.
 */
object LookMLResolve {

    /** The name token of a declaration: the first meaningful leaf after its `:`. Null if unnamed. */
    fun nameLeaf(def: PsiElement): PsiElement? {
        var node = def.node.firstChildNode
        var seenColon = false
        while (node != null) {
            val type = node.elementType
            if (seenColon && type != TokenType.WHITE_SPACE && type != LookMLTypes.COMMENT) {
                // A `{` here means the declaration has no name.
                return if (type == LookMLTypes.LBRACE) null else node.psi
            }
            if (type == LookMLTypes.COLON) seenColon = true
            node = node.treeNext
        }
        return null
    }

    fun declName(def: PsiElement): String? = nameLeaf(def)?.text

    // ─── Project-wide name index (cached; rebuilt only on PSI change) ───────────────

    /**
     * All name-keyed declarations in the project, built in a single file scan and cached until the PSI
     * changes. Every hot lookup (find views/explores/constants/formats, "extended by") reads this map in
     * O(1) instead of rescanning every LookML file, which is what made Find Usages slow (resolve() and
     * isReferenceTo run many times per search).
     */
    private class ProjectIndex(
        val viewsByName: Map<String, List<LookMLViewDefinition>>,
        val exploresByName: Map<String, List<LookMLExploreDefinition>>,
        val constantsByName: Map<String, List<LookMLConstantDefinition>>,
        val formatsByName: Map<String, List<LookMLNamedValueFormatDefinition>>,
        val extendedBy: Map<String, List<LookMLViewDefinition>>,
    )

    private val INDEX_KEY = Key.create<CachedValue<ProjectIndex>>("lookml.projectIndex")

    private fun index(project: Project): ProjectIndex =
        CachedValuesManager.getManager(project).getCachedValue(
            project,
            INDEX_KEY,
            {
                val views = HashMap<String, MutableList<LookMLViewDefinition>>()
                val explores = HashMap<String, MutableList<LookMLExploreDefinition>>()
                val constants = HashMap<String, MutableList<LookMLConstantDefinition>>()
                val formats = HashMap<String, MutableList<LookMLNamedValueFormatDefinition>>()
                val extendedBy = HashMap<String, MutableList<LookMLViewDefinition>>()
                val scope = GlobalSearchScope.allScope(project)
                val manager = PsiManager.getInstance(project)
                for (virtualFile in FileTypeIndex.getFiles(LookMLFileType, scope)) {
                    val psiFile = manager.findFile(virtualFile) ?: continue
                    for (view in PsiTreeUtil.findChildrenOfType(psiFile, LookMLViewDefinition::class.java)) {
                        declName(view)?.let { views.getOrPut(it) { ArrayList() }.add(view) }
                        for (parent in extendsTargetNames(view)) {
                            extendedBy.getOrPut(parent.removePrefix("+")) { ArrayList() }.add(view)
                        }
                    }
                    for (explore in PsiTreeUtil.findChildrenOfType(psiFile, LookMLExploreDefinition::class.java)) {
                        declName(explore)?.let { explores.getOrPut(it) { ArrayList() }.add(explore) }
                    }
                    for (constant in PsiTreeUtil.findChildrenOfType(psiFile, LookMLConstantDefinition::class.java)) {
                        declName(constant)?.let { constants.getOrPut(it) { ArrayList() }.add(constant) }
                    }
                    for (format in
                        PsiTreeUtil.findChildrenOfType(psiFile, LookMLNamedValueFormatDefinition::class.java)) {
                        declName(format)?.let { formats.getOrPut(it) { ArrayList() }.add(format) }
                    }
                }
                CachedValueProvider.Result.create(
                    ProjectIndex(views, explores, constants, formats, extendedBy),
                    PsiModificationTracker.MODIFICATION_COUNT,
                )
            },
            false,
        )

    /** All views named [name] anywhere in the project (project-wide resolution). */
    fun findViews(project: Project, name: String): List<LookMLViewDefinition> =
        index(project).viewsByName[name].orEmpty()

    /** A refinement is declared with a leading `+` (e.g. `view: +orders`); this strips it. */
    fun canonicalViewName(view: LookMLViewDefinition): String? = declName(view)?.removePrefix("+")

    /**
     * The full family for a view [name]: the base view `name` plus every refinement `+name`, anywhere
     * in the project. Refinements merge extra fields/sets into the base, so field/set lookups must see
     * them all.
     */
    fun findViewFamily(project: Project, name: String): List<LookMLViewDefinition> {
        val byName = index(project).viewsByName
        return byName[name].orEmpty() + byName["+$name"].orEmpty()
    }

    /** Refinements (`+name`) of the view [name], anywhere in the project. */
    fun findRefinements(project: Project, name: String): List<LookMLViewDefinition> =
        findViews(project, "+$name")

    /** All explores named [name] anywhere in the project. */
    fun findExplores(project: Project, name: String): List<LookMLExploreDefinition> =
        index(project).exploresByName[name].orEmpty()

    fun findViewNameLeaf(project: Project, name: String, exclude: LookMLViewDefinition? = null): PsiElement? {
        val views = findViews(project, name)
        val chosen = views.firstOrNull { it !== exclude } ?: views.firstOrNull()
        return chosen?.let { nameLeaf(it) }
    }

    fun findExploreNameLeaf(project: Project, name: String): PsiElement? =
        findExplores(project, name).firstOrNull()?.let { nameLeaf(it) }

    /** Field-like declarations directly inside a view (no inheritance in Milestone 1). */
    fun fieldDecls(view: LookMLViewDefinition): List<PsiElement> {
        val body = view.viewBody
        val list = ArrayList<PsiElement>()
        list.addAll(body.dimensionDefinitionList)
        list.addAll(body.dimensionGroupDefinitionList)
        list.addAll(body.measureDefinitionList)
        list.addAll(body.filterDefinitionList)
        list.addAll(body.parameterDefinitionList)
        return list
    }

    /**
     * Resolve [fieldName] within [view]. Handles dimension_group timeframe fields, e.g. a reference
     * `${created_date}` resolves to `dimension_group: created`.
     */
    fun resolveFieldInView(view: LookMLViewDefinition, fieldName: String): PsiElement? {
        for (def in fieldDecls(view)) {
            val name = declName(def) ?: continue
            if (name == fieldName) return nameLeaf(def)
            if (def is LookMLDimensionGroupDefinition && fieldName.startsWith(name + "_")) {
                return nameLeaf(def)
            }
        }
        return null
    }

    fun resolveField(project: Project, viewName: String, fieldName: String): PsiElement? {
        for (view in viewHierarchyByName(project, viewName)) {
            resolveFieldInView(view, fieldName)?.let { return it }
        }
        return null
    }

    // ─── Inheritance (extends) ────────────────────────────────────────────────────

    /** Names a view `extends` (its direct parents). */
    fun extendsTargetNames(view: LookMLViewDefinition): List<String> {
        val prop =
            view.viewBody.propertyList.firstOrNull { it.propertyName.text == "extends" }
                ?: return emptyList()
        val arrayValue = PsiTreeUtil.findChildOfType(prop, LookMLArrayValue::class.java)
            ?: return emptyList()
        return PsiTreeUtil.findChildrenOfType(arrayValue, LookMLArrayElement::class.java)
            .map { it.text.trim() }
            .filter { it.isNotEmpty() }
    }

    /**
     * The complete field/set scope for [start]: its own family (base + refinements), plus everything
     * it `extends`, transitively. Handles multiple parents, refinements, and cycles.
     */
    fun viewHierarchy(project: Project, start: LookMLViewDefinition): List<LookMLViewDefinition> {
        val name = canonicalViewName(start) ?: return listOf(start)
        return viewHierarchyByName(project, name)
    }

    /** Same as [viewHierarchy] but keyed by a view [name] (used when starting from a bare reference). */
    fun viewHierarchyByName(project: Project, name: String): List<LookMLViewDefinition> {
        val orderedViews = LinkedHashSet<LookMLViewDefinition>()
        val seenNames = HashSet<String>()
        val queue = ArrayDeque<String>()
        queue.add(name)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (!seenNames.add(current)) continue
            for (view in findViewFamily(project, current)) {
                orderedViews.add(view)
                for (parentName in extendsTargetNames(view)) queue.add(parentName.removePrefix("+"))
            }
        }
        return orderedViews.toList()
    }

    /** Resolve a field starting at [start] and walking up the `extends` chain. */
    fun resolveFieldInHierarchy(
        project: Project,
        start: LookMLViewDefinition,
        fieldName: String,
    ): PsiElement? {
        for (view in viewHierarchy(project, start)) {
            resolveFieldInView(view, fieldName)?.let { return it }
        }
        return null
    }

    /** A completion candidate for a `${...}` field reference: the field [name], its [kind]
     *  (dimension/measure/dimension_group/filter/parameter), and the [ownerView] it is declared in. */
    data class FieldSuggestion(val name: String, val kind: String, val ownerView: String)

    /**
     * Every field visible from [start] via `${...}`: its own fields plus all inherited fields up the
     * `extends` chain (and refinement family), deduped by name keeping the nearest declaration. Backs
     * upstream-family field completion inside `sql:` blocks.
     */
    fun familyFieldSuggestions(project: Project, start: LookMLViewDefinition): List<FieldSuggestion> {
        val out = LinkedHashMap<String, FieldSuggestion>()
        val startOwner = canonicalViewName(start) ?: "?"
        // Seed with the enclosing view's own fields straight from its PSI. This works even while the
        // current file has a transient parse error (e.g. the half-typed `${}` that triggered
        // completion), which would otherwise keep the view out of the project index.
        collectFieldsInto(start, startOwner, out)

        // Walk parents (other files, well-formed) via the cached index.
        val queue = ArrayDeque(extendsTargetNames(start).map { it.removePrefix("+") })
        val seen = hashSetOf(startOwner)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (!seen.add(current)) continue
            for (view in findViewFamily(project, current)) {
                collectFieldsInto(view, current, out)
                for (parent in extendsTargetNames(view)) queue.add(parent.removePrefix("+"))
            }
        }
        return out.values.toList()
    }

    private fun collectFieldsInto(
        view: LookMLViewDefinition,
        owner: String,
        out: MutableMap<String, FieldSuggestion>,
    ) {
        for (def in fieldDecls(view)) {
            val name = declName(def) ?: continue
            out.putIfAbsent(name, FieldSuggestion(name, fieldKeyword(def), owner))
        }
    }

    /** [familyFieldSuggestions] starting from a bare view [name] (for cross-view `${view.field}`). */
    fun familyFieldSuggestionsFor(project: Project, name: String): List<FieldSuggestion> {
        val start =
            findViews(project, name).firstOrNull()
                ?: findViews(project, "+$name").firstOrNull()
                ?: return emptyList()
        return familyFieldSuggestions(project, start)
    }

    /** The declaring keyword of a field def (the token before its `:`), e.g. `dimension`, `measure`. */
    private fun fieldKeyword(def: PsiElement): String {
        var node = def.node.firstChildNode
        while (node != null) {
            val type = node.elementType
            if (type == LookMLTypes.COLON) break
            if (type != TokenType.WHITE_SPACE && type != LookMLTypes.COMMENT) return node.text
            node = node.treeNext
        }
        return "field"
    }

    // ─── Sets ─────────────────────────────────────────────────────────────────────

    /** Resolve a `set` named [setName] in [start] or up its `extends` chain. */
    fun resolveSetInHierarchy(
        project: Project,
        start: LookMLViewDefinition,
        setName: String,
    ): PsiElement? {
        for (view in viewHierarchy(project, start)) {
            for (set in view.viewBody.setDefinitionList) {
                if (declName(set) == setName) return nameLeaf(set)
            }
        }
        return null
    }

    fun resolveSet(project: Project, viewName: String, setName: String): PsiElement? {
        for (view in viewHierarchyByName(project, viewName)) {
            for (set in view.viewBody.setDefinitionList) {
                if (declName(set) == setName) return nameLeaf(set)
            }
        }
        return null
    }

    // ─── Named value formats (model/manifest level) ────────────────────────────────

    /** Resolve a `named_value_format` named [name] anywhere in the project (first match). */
    fun findNamedValueFormat(project: Project, name: String): PsiElement? =
        index(project).formatsByName[name]?.firstOrNull()?.let { nameLeaf(it) }

    /** Resolve a `constant` named [name] anywhere in the project (manifest.lkml; first match). */
    fun findConstant(project: Project, name: String): PsiElement? =
        index(project).constantsByName[name]?.firstOrNull()?.let { nameLeaf(it) }

    /** Views that directly `extends` a view named [name] (the "extended by" direction). */
    fun findViewsExtending(project: Project, name: String): List<LookMLViewDefinition> =
        index(project).extendedBy[name].orEmpty()

    // ─── Explore aliases (from: / view_name: / join aliases) ───────────────────────

    /**
     * Inside an explore, a join name is an alias and `from:` points at the real view; the explore base
     * likewise resolves through `view_name:`/`from:`. Given [alias] as used at [context], return the real
     * underlying view name. Falls back to [alias] itself (Looker's convention: name == view when no
     * `from:`). Outside an explore this is a no-op.
     */
    fun realViewName(context: PsiElement, alias: String): String {
        val explore =
            PsiTreeUtil.getParentOfType(context, LookMLExploreDefinition::class.java) ?: return alias
        return realViewNameInExplore(explore, alias)
    }

    /** Alias -> real underlying view within a specific [explore] (base via view_name/from, joins via from). */
    fun realViewNameInExplore(explore: LookMLExploreDefinition, alias: String): String {
        if (declName(explore) == alias) return exploreBaseTarget(explore) ?: alias
        for (join in explore.exploreBody.joinDefinitionList) {
            if (declName(join) == alias) return joinFromTarget(join) ?: alias
        }
        return alias
    }

    // ─── Dashboard field resolution (alias.field through an explore's joins / from:) ──

    /**
     * The real view a dashboard [alias] maps to. Dashboard fields are `alias.field`, where `alias` is an
     * explore name or a join alias in the element's `explore:` - NOT necessarily a raw view (Looker maps it
     * through `from:` / `view_name:`). [exploreName] is that element's `explore:` value. Falls back to the
     * alias itself when the explore/alias has no mapping (Looker's convention: alias == view).
     */
    fun dashboardRealView(project: Project, exploreName: String?, alias: String): String {
        val explore = exploreName?.let { findExplores(project, it).firstOrNull() } ?: return alias
        return realViewNameInExplore(explore, alias)
    }

    /** Resolve a dashboard `alias.field` to its declaration, mapping [alias] through [exploreName]. */
    fun resolveDashboardField(
        project: Project,
        exploreName: String?,
        alias: String,
        field: String,
    ): PsiElement? {
        val realView = dashboardRealView(project, exploreName, alias)
        resolveField(project, realView, field)?.let { return it }
        if (realView != alias) resolveField(project, alias, field)?.let { return it }
        // Fallback: the qualifier is often the explore name (e.g. `ardian_v2_rcs.is_within_period`) while the
        // field actually lives in a view reached through the explore's `from:`/`view_name:` or a join. When
        // the direct alias mapping misses, scan every view participating in the explore and take the first
        // that declares the field. Scoped to the explore's own views, so it does not match unrelated views.
        val explore = exploreName?.let { findExplores(project, it).firstOrNull() } ?: return null
        for (viewName in exploreViewNames(explore)) {
            resolveField(project, viewName, field)?.let { return it }
        }
        return null
    }

    /** Every view participating in [explore]: the base (via view_name/from, else the explore's own name)
     *  plus each join (via `from:`, else the join's own name). */
    private fun exploreViewNames(explore: LookMLExploreDefinition): List<String> {
        val names = LinkedHashSet<String>()
        (exploreBaseTarget(explore) ?: declName(explore))?.let { names.add(it) }
        for (join in explore.exploreBody.joinDefinitionList) {
            (joinFromTarget(join) ?: declName(join))?.let { names.add(it) }
        }
        return names.toList()
    }

    /** Name leaf of the view a dashboard [alias] points at (real view via the explore, else the alias). */
    fun dashboardViewLeaf(project: Project, exploreName: String?, alias: String): PsiElement? {
        val realView = dashboardRealView(project, exploreName, alias)
        return findViewNameLeaf(project, realView)
            ?: if (realView != alias) findViewNameLeaf(project, alias) else null
    }

    private fun exploreBaseTarget(explore: LookMLExploreDefinition): String? {
        val body = explore.exploreBody
        body.viewNamePropertyList.firstOrNull()?.let { return it.identifier.text }
        body.fromPropertyList.firstOrNull()?.let { return it.identifier.text }
        return null
    }

    private fun joinFromTarget(join: LookMLJoinDefinition): String? =
        join.joinBody.fromPropertyList.firstOrNull()?.identifier?.text

    // ─── include: "path" ───────────────────────────────────────────────────────────

    /**
     * Resolve a raw `include:` value (still quoted) to the LookML file(s) it points at. Looker paths are
     * project-root-relative (leading `/`) and may end with a `*` glob; we match by path suffix and
     * support a simple `*` wildcard, returning every matching file.
     */
    fun resolveInclude(project: Project, rawValue: String): List<PsiFile> {
        val cleaned = rawValue.trim().trim('"', '\'').removePrefix("/")
        if (cleaned.isEmpty()) return emptyList()
        val scope = GlobalSearchScope.allScope(project)
        val manager = PsiManager.getInstance(project)
        val files = FileTypeIndex.getFiles(LookMLFileType, scope)
        val result = ArrayList<PsiFile>()
        if (cleaned.contains('*')) {
            val body = cleaned.split('*').joinToString("[^/]*") { Regex.escape(it) }
            val regex = Regex(".*/$body")
            for (virtualFile in files) {
                val path = virtualFile.path.replace('\\', '/')
                if (regex.matches(path)) manager.findFile(virtualFile)?.let { result.add(it) }
            }
        } else {
            val leaf = cleaned.substringAfterLast('/')
            for (virtualFile in files) {
                val path = virtualFile.path.replace('\\', '/')
                if (path == cleaned || path.endsWith("/$cleaned")) {
                    manager.findFile(virtualFile)?.let { result.add(it) }
                }
            }
            if (result.isEmpty()) {
                for (virtualFile in files) {
                    if (virtualFile.name == leaf) manager.findFile(virtualFile)?.let { result.add(it) }
                }
            }
        }
        return result
    }

    /** `model: my_model` (in a dashboard) -> the `my_model.model.lkml` (or `.model.lookml`) file. */
    fun findModelFile(project: Project, name: String): PsiElement? {
        val clean = name.trim().trim('"', '\'')
        if (clean.isEmpty()) return null
        val scope = GlobalSearchScope.allScope(project)
        val manager = PsiManager.getInstance(project)
        for (virtualFile in FileTypeIndex.getFiles(LookMLFileType, scope)) {
            if (virtualFile.name == "$clean.model.lkml" || virtualFile.name == "$clean.model.lookml") {
                return manager.findFile(virtualFile)
            }
        }
        return null
    }
}
