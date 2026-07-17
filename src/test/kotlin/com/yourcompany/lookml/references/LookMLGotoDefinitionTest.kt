package com.yourcompany.lookml.references

import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction
import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Milestone 1: Go to Definition for `${...}` field/view references.
 *
 * Uses the real navigation entry point (`GotoDeclarationAction.findTargetElement`) so the plugin.xml
 * registration and handler are exercised end to end.
 */
class LookMLGotoDefinitionTest : BasePlatformTestCase() {

    private fun targetAtCaret(): PsiElement? =
        GotoDeclarationAction.findTargetElement(project, myFixture.editor, myFixture.caretOffset)

    fun testSameViewFieldRef() {
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
              measure: total {
                type: sum
                sql: ${'$'}{i<caret>d} ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("same-view field ref should resolve", target)
        assertEquals("id", target!!.text)
    }

    fun testCrossFileViewFieldRef() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "explore.lkml",
            """
            explore: e {
              sql_always_where: ${'$'}{orders.i<caret>d} = 1 ;;
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("cross-file field ref should resolve", target)
        assertEquals("id", target!!.text)
        assertEquals("orders.lkml", target.containingFile.name)
    }

    fun testViewSegmentResolvesToView() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "explore.lkml",
            """
            explore: e {
              sql_always_where: ${'$'}{ord<caret>ers.id} = 1 ;;
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("view segment should resolve", target)
        assertEquals("orders", target!!.text)
    }

    fun testTablePlaceholderNotResolved() {
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              dimension: id {
                type: number
                sql: ${'$'}{TAB<caret>LE}.id ;;
              }
            }
            """.trimIndent(),
        )
        assertNull("\${'$'}{TABLE} must not navigate", targetAtCaret())
    }

    fun testDrillFieldQualifiedRef() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              measure: total {
                type: sum
                sql: ${'$'}{TABLE}.total ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "users.lkml",
            """
            view: users {
              dimension: id {
                type: number
                drill_fields: [orders.to<caret>tal]
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("drill_fields view.field should resolve", target)
        assertEquals("total", target!!.text)
        assertEquals("orders.lkml", target.containingFile.name)
    }

    fun testDrillFieldQualifiedViewSegment() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              measure: total {
                type: sum
                sql: ${'$'}{TABLE}.total ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "users.lkml",
            """
            view: users {
              dimension: id {
                type: number
                drill_fields: [ord<caret>ers.total]
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("clicking the view segment should resolve to the view", target)
        assertEquals("orders", target!!.text)
    }

    fun testDrillFieldBareRef() {
        myFixture.configureByText(
            "users.lkml",
            """
            view: users {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
              dimension: name {
                type: string
                drill_fields: [i<caret>d]
                sql: ${'$'}{TABLE}.name ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("bare drill field should resolve in same view", target)
        assertEquals("id", target!!.text)
    }

    fun testExtendsResolvesToView() {
        myFixture.addFileToProject(
            "base.lkml",
            """
            view: base_orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "child.lkml",
            """
            view: orders {
              extends: [base_ord<caret>ers]
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("extends target should resolve to base view", target)
        assertEquals("base_orders", target!!.text)
        assertEquals("base.lkml", target.containingFile.name)
    }

    fun testInheritedFieldSameViewRef() {
        myFixture.addFileToProject(
            "base.lkml",
            """
            view: base_orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              extends: [base_orders]
              measure: total {
                type: sum
                sql: ${'$'}{i<caret>d} ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("inherited field should resolve via extends", target)
        assertEquals("id", target!!.text)
        assertEquals("base.lkml", target.containingFile.name)
    }

    fun testInheritedFieldMultiLevel() {
        myFixture.addFileToProject(
            "grand.lkml",
            """
            view: grand {
              dimension: gid {
                type: number
                sql: ${'$'}{TABLE}.gid ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject(
            "mid.lkml",
            """
            view: middle {
              extends: [grand]
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "child.lkml",
            """
            view: child {
              extends: [middle]
              measure: m {
                type: count
                drill_fields: [gi<caret>d]
                sql: ${'$'}{TABLE}.x ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("field from a grandparent view should resolve", target)
        assertEquals("gid", target!!.text)
        assertEquals("grand.lkml", target.containingFile.name)
    }

    fun testInheritedQualifiedFieldAcrossExtends() {
        myFixture.addFileToProject(
            "reporting.lkml",
            """
            view: orders_reporting {
              measure: total {
                type: sum
                sql: ${'$'}{TABLE}.total ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              extends: [orders_reporting]
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "users.lkml",
            """
            view: users {
              dimension: id {
                type: number
                drill_fields: [orders.to<caret>tal]
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("qualified ref to an inherited field should resolve", target)
        assertEquals("total", target!!.text)
        assertEquals("reporting.lkml", target.containingFile.name)
    }

    fun testExtendsGutterMarkers() {
        myFixture.addFileToProject(
            "base.lkml",
            """
            view: base_orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              extends: [base_orders]
            }
            """.trimIndent(),
        )
        myFixture.doHighlighting()
        val gutters = myFixture.findAllGutters()
        assertTrue("expected an extends gutter marker on the view", gutters.isNotEmpty())
    }

    fun testSetInternalFieldRef() {
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
              set: detail {
                fields: [i<caret>d]
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("field inside a set's fields should resolve", target)
        assertEquals("id", target!!.text)
    }

    fun testDrillFieldSetWildcard() {
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              set: detail {
                fields: [id, name]
              }
              dimension: id {
                type: number
                drill_fields: [deta<caret>il*]
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("set wildcard in drill_fields should resolve to the set", target)
        assertEquals("detail", target!!.text)
    }

    fun testQualifiedSetWildcard() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              set: detail {
                fields: [id]
              }
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "users.lkml",
            """
            view: users {
              dimension: uid {
                type: number
                drill_fields: [orders.deta<caret>il*]
                sql: ${'$'}{TABLE}.uid ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("qualified set wildcard should resolve to the set", target)
        assertEquals("detail", target!!.text)
        assertEquals("orders.lkml", target.containingFile.name)
    }

    fun testValueFormatNameNavigatesToDefinition() {
        myFixture.addFileToProject(
            "formats.lkml",
            """
            named_value_format: eur_0 {
              value_format: "€0"
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              measure: total {
                type: sum
                value_format_name: eur<caret>_0
                sql: ${'$'}{TABLE}.total ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("value_format_name should resolve to the named_value_format", target)
        assertEquals("eur_0", target!!.text)
        assertEquals("formats.lkml", target.containingFile.name)
    }

    fun testRefinementFieldMergedIntoBase() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders_refine.lkml",
            """
            view: +orders {
              measure: total {
                type: sum
                sql: ${'$'}{i<caret>d} ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("a refinement should see the base view's fields", target)
        assertEquals("id", target!!.text)
        assertEquals("orders.lkml", target.containingFile.name)
    }

    fun testRefinementNameNavigatesToBase() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders_refine.lkml",
            """
            view: +ord<caret>ers {
              measure: total {
                type: sum
                sql: ${'$'}{id} ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("clicking a refinement name should jump to the base view", target)
        assertEquals("orders", target!!.text)
        assertEquals("orders.lkml", target.containingFile.name)
    }

    fun testFromValueNavigatesToView() {
        myFixture.addFileToProject(
            "orders_base.lkml",
            """
            view: orders_base {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "explore.lkml",
            """
            explore: order_items {
              join: orders {
                from: orders_b<caret>ase
                sql_on: ${'$'}{orders.id} = 1 ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("from: value should resolve to the view", target)
        assertEquals("orders_base", target!!.text)
        assertEquals("orders_base.lkml", target.containingFile.name)
    }

    fun testJoinAliasFieldResolvesThroughFrom() {
        myFixture.addFileToProject(
            "orders_base.lkml",
            """
            view: orders_base {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "explore.lkml",
            """
            explore: order_items {
              join: o {
                from: orders_base
                sql_on: ${'$'}{o.i<caret>d} = 1 ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("a join alias field should resolve through from:", target)
        assertEquals("id", target!!.text)
        assertEquals("orders_base.lkml", target.containingFile.name)
    }

    fun testIncludeNavigatesToFile() {
        myFixture.addFileToProject(
            "views/orders.view.lkml",
            """
            view: orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders.model.lkml",
            """
            include: "/views/orde<caret>rs.view.lkml"

            explore: orders {}
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("include path should resolve to the file", target)
        assertEquals("orders.view.lkml", target!!.containingFile.name)
    }

    fun testIncludeGlobNavigatesToFiles() {
        myFixture.addFileToProject(
            "views/orders.view.lkml",
            """
            view: orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders.model.lkml",
            """
            include: "/views/*.view<caret>.lkml"

            explore: orders {}
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("include glob should resolve to at least one file", target)
        assertEquals("orders.view.lkml", target!!.containingFile.name)
    }

    fun testSqlOnQualifiedRef() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "explore.lkml",
            """
            explore: e {
              join: orders {
                sql_on: ${'$'}{orders.i<caret>d} = 1 ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("sql_on qualified ref should resolve", target)
        assertEquals("id", target!!.text)
        assertEquals("orders.lkml", target.containingFile.name)
    }

    fun testSqlOnRefWithLiquidPresent() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: mart__investment_case {
              dimension: mfh_investment_case_id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "explore.lkml",
            """
            explore: e {
              join: investment_case_bp {
                sql_on: ${'$'}{mart__investment_case.mfh_investment_c<caret>ase_id} = ${'$'}{investment_case_bp.mfh_investment_case_id}
                AND DATE_TRUNC(${'$'}{investment_case_bp.bp_month}, MONTH) =
                {% if mfh_vacancy.as_of_month._is_filtered %}
                DATE_TRUNC(DATE(TIMESTAMP({% date_start mfh_vacancy.as_of_month %})), MONTH)
                {% else %}
                DATE_TRUNC(CURRENT_DATE('Europe/Berlin'), MONTH)
                {% endif %} ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("a ref should still resolve even when the sql_on contains nested Liquid", target)
        assertEquals("mfh_investment_case_id", target!!.text)
    }

    fun testAssertExpressionRef() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders.model.lkml",
            """
            test: t {
              assert: a {
                expression: ${'$'}{orders.i<caret>d} > 0 ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("assert expression ref should resolve", target)
        assertEquals("id", target!!.text)
        assertEquals("orders.lkml", target.containingFile.name)
    }

    fun testTestExploreSourceResolvesToExplore() {
        myFixture.addFileToProject(
            "explore.lkml",
            """
            explore: order_items {}
            """.trimIndent(),
        )
        myFixture.configureByText(
            "tests.model.lkml",
            """
            test: t {
              explore_source: order_it<caret>ems {
                column: c {}
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("explore_source should resolve to the explore", target)
        assertEquals("order_items", target!!.text)
        assertEquals("explore.lkml", target.containingFile.name)
    }

    fun testTestColumnFieldResolves() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              dimension: id {
                type: number
                sql: ${'$'}{TABLE}.id ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "tests.model.lkml",
            """
            test: t {
              explore_source: orders {
                column: c { field: orders.i<caret>d }
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("column field ref should resolve", target)
        assertEquals("id", target!!.text)
        assertEquals("orders.lkml", target.containingFile.name)
    }

    fun testConstantRefNavigatesToManifest() {
        myFixture.addFileToProject(
            "manifest.lkml",
            """
            constant: gcp_project {
              value: "my-project"
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders.view.lkml",
            """
            view: v {
              sql_table_name: @{gcp_pro<caret>ject}.dataset.tbl ;;
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("@{constant} should resolve to the constant declaration", target)
        assertEquals("gcp_project", target!!.text)
        assertEquals("manifest.lkml", target.containingFile.name)
    }

    fun testConstantInsideBacktickString() {
        myFixture.addFileToProject(
            "manifest.lkml",
            """
            constant: gcp_project {
              value: "my-project"
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders.view.lkml",
            """
            view: v {
              sql_table_name: `@{gcp_pro<caret>ject}.dataset.tbl` ;;
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("@{constant} inside a backtick string should resolve", target)
        assertEquals("gcp_project", target!!.text)
        assertEquals("manifest.lkml", target.containingFile.name)
    }

    fun testSecondConstantInsideBacktickString() {
        myFixture.addFileToProject(
            "manifest.lkml",
            """
            constant: gcp_project {
              value: "p"
            }
            constant: mfh_dataset_name {
              value: "d"
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders.view.lkml",
            """
            view: v {
              sql_table_name: `@{gcp_project}.@{mfh_data<caret>set_name}.tbl` ;;
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("the second @{constant} in a backtick string should resolve", target)
        assertEquals("mfh_dataset_name", target!!.text)
    }

    fun testSecondConstantRefNavigates() {
        myFixture.addFileToProject(
            "manifest.lkml",
            """
            constant: gcp_project {
              value: "p"
            }
            constant: mfh_dataset_name {
              value: "d"
            }
            """.trimIndent(),
        )
        myFixture.configureByText(
            "orders.view.lkml",
            """
            view: v {
              sql_table_name: @{gcp_project}.@{mfh_data<caret>set_name}.tbl ;;
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("the second @{constant} should also resolve", target)
        assertEquals("mfh_dataset_name", target!!.text)
    }

    fun testDimensionGroupTimeframeField() {
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              dimension_group: created {
                type: time
                timeframes: [date, month]
                sql: ${'$'}{TABLE}.created ;;
              }
              measure: m {
                type: count
                sql: ${'$'}{created_da<caret>te} ;;
              }
            }
            """.trimIndent(),
        )
        val target = targetAtCaret()
        assertNotNull("timeframe field should resolve to its dimension_group", target)
        assertEquals("created", target!!.text)
    }
}
