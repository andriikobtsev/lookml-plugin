package com.yourcompany.lookml.references

import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Milestone D (reference layer): references inside pure-YAML dashboards (`.dashboard.lookml`).
 *
 * Only the meaningful parts are covered (owner decision): `fields:` / `sorts:` / `hidden_fields:` /
 * `pivots:` entries -> `view.field`, `explore:` -> explore, `model:` -> the model file. UI-only
 * properties are ignored. Each of these now powers Go to Definition + Find Usages (+ Rename for fields).
 */
class LookMLDashboardReferenceTest : BasePlatformTestCase() {

    private fun usageCountAtCaret(): Int = myFixture.findUsages(myFixture.elementAtCaret).size

    private fun ordersView() =
        """
        view: orders {
          dimension: created_month {
            type: string
            sql: ${'$'}{TABLE}.created_month ;;
          }
        }
        """.trimIndent()

    fun testDashboardFieldUsages() {
        // Single definition of the view/dimension (in the file under caret), referenced from the dashboard.
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              dimension: created_mo<caret>nth {
                type: string
                sql: ${'$'}{TABLE}.created_month ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject(
            "board.dashboard.lkml",
            """
            ---
            - dashboard: d
              elements:
              - title: e1
                explore: orders_explore
                fields: [orders.created_month]
                sorts: [orders.created_month desc]
            """.trimIndent(),
        )
        // `fields:` entry + `sorts:` entry reference the dimension.
        assertEquals("dashboard fields + sorts count as usages", 2, usageCountAtCaret())
    }

    fun testDashboardExploreUsages() {
        myFixture.addFileToProject(
            "orders.lkml",
            """
            view: orders {
              dimension: created_month {
                type: string
                sql: ${'$'}{TABLE}.created_month ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.configureByText("sales.model.lkml", "explore: orders_exp<caret>lore {}")
        myFixture.addFileToProject(
            "board.dashboard.lkml",
            """
            ---
            - dashboard: d
              elements:
              - title: e1
                explore: orders_explore
                fields: [orders.created_month]
            """.trimIndent(),
        )
        assertEquals("dashboard explore: counts as a usage", 1, usageCountAtCaret())
    }

    fun testDashboardFieldGoTo() {
        myFixture.addFileToProject("orders.lkml", ordersView())
        myFixture.configureByText(
            "board.dashboard.lkml",
            """
            ---
            - dashboard: d
              elements:
              - title: e1
                fields: [orders.created_mo<caret>nth]
            """.trimIndent(),
        )
        val target = myFixture.getReferenceAtCaretPosition()?.resolve()
        assertNotNull("dashboard field resolves to the dimension", target)
        assertEquals("created_month", target!!.text)
    }

    fun testDashboardFieldUsagesWithoutTripleDash() {
        // Common Looker form: the file starts directly with `- dashboard:` (no leading `---`).
        myFixture.configureByText(
            "orders.lkml",
            """
            view: orders {
              dimension: created_mo<caret>nth {
                type: string
                sql: ${'$'}{TABLE}.created_month ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject(
            "board.dashboard.lookml",
            """
            - dashboard: d
              elements:
              - title: e1
                explore: orders_explore
                fields: [orders.created_month]
                sorts: [orders.created_month desc]
            """.trimIndent(),
        )
        assertEquals("no-'---' dashboard fields + sorts count as usages", 2, usageCountAtCaret())
    }

    fun testDashboardFieldGoToWithoutTripleDash() {
        myFixture.addFileToProject("orders.lkml", ordersView())
        myFixture.configureByText(
            "board.dashboard.lookml",
            """
            - dashboard: d
              elements:
              - title: e1
                fields: [orders.created_mo<caret>nth]
            """.trimIndent(),
        )
        val target = myFixture.getReferenceAtCaretPosition()?.resolve()
        assertNotNull("no-'---' dashboard field resolves to the dimension", target)
        assertEquals("created_month", target!!.text)
    }

    // A realistic dashboard: an unquoted multi-word value (`name: Data as of`) makes the YAML parser
    // collapse the element into one greedy text-line blob. References must still work via blob scanning.
    private fun collapsedDashboard() =
        """
        ---
        - dashboard: vacancy_overview
          title: Vacancy Overview
          elements:
          - title: ''
            name: Data as of
            model: mfh@{model_suffix}
            explore: mfh_vacancy
            type: single_value
            fields: [mfh_vacancy.data_as_of]
            listen:
              As of Month: mfh_vacancy.data_as_of
          - title: Vacancy Overview
            name: Vacancy Overview
            explore: mfh_vacancy
            type: looker_grid
            fields: [primary_building_location.asset_main_address_rcs]
            sorts: [primary_building_location.asset_main_address_rcs]
        """.trimIndent()

    fun testCollapsedDashboardFieldUsages() {
        myFixture.configureByText(
            "views.lkml",
            """
            view: primary_building_location {
              dimension: asset_main_address_r<caret>cs {
                type: string
                sql: ${'$'}{TABLE}.x ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject("board.dashboard.lookml", collapsedDashboard())
        // `fields:` + `sorts:` in the second (collapsed) element.
        assertEquals("collapsed dashboard field usages", 2, usageCountAtCaret())
    }

    fun testCollapsedDashboardExploreUsages() {
        myFixture.addFileToProject(
            "views.lkml",
            "view: mfh_vacancy { dimension: data_as_of { type: string sql: ${'$'}{TABLE}.y ;; } }",
        )
        myFixture.configureByText("model.lkml", "explore: mfh_vac<caret>ancy {}")
        myFixture.addFileToProject("board.dashboard.lookml", collapsedDashboard())
        // Two `explore: mfh_vacancy` element headers.
        assertEquals("collapsed dashboard explore usages", 2, usageCountAtCaret())
    }

    fun testCollapsedDashboardFieldGoTo() {
        myFixture.addFileToProject(
            "views.lkml",
            """
            view: primary_building_location {
              dimension: asset_main_address_rcs {
                type: string
                sql: ${'$'}{TABLE}.x ;;
              }
            }
            """.trimIndent(),
        )
        val file = myFixture.addFileToProject("board.dashboard.lookml", collapsedDashboard())
        myFixture.configureFromExistingVirtualFile(file.virtualFile)
        val offset = file.text.indexOf("primary_building_location.asset_main_address_rcs]") +
            "primary_building_location.".length + 3
        val ref = myFixture.file.findReferenceAt(offset)
        val target = ref?.resolve()
        assertNotNull("collapsed dashboard field resolves to the dimension", target)
        assertEquals("asset_main_address_rcs", target!!.text)
    }

    // Dashboard fields use the explore's namespace: `alias.field`, where alias is the explore name or a
    // join alias mapped to a real view via `from:`. Resolution must go through the explore.
    private fun aliasModel() =
        """
        explore: mfh_vacancy {
          from: mfh_vacancy_base
          join: primary_building_location {
            from: mfh_building_with_location
            relationship: many_to_one
            sql_on: 1=1 ;;
          }
        }
        view: mfh_vacancy_base {
          filter: as_of_month {
            type: string
          }
        }
        view: mfh_building_with_location {
          dimension: asset_main_address_rcs {
            type: string
            sql: ${'$'}{TABLE}.x ;;
          }
        }
        """.trimIndent()

    private fun aliasDashboard() =
        """
        ---
        - dashboard: d
          elements:
          - title: ''
            name: Vacancy Overview
            explore: mfh_vacancy
            fields: [primary_building_location.asset_main_address_rcs]
            listen:
              As of Month: mfh_vacancy.as_of_month
        """.trimIndent()

    fun testDashboardJoinAliasFieldGoTo() {
        myFixture.addFileToProject("mfh_vacancy.model.lkml", aliasModel())
        val file = myFixture.addFileToProject("board.dashboard.lookml", aliasDashboard())
        myFixture.configureFromExistingVirtualFile(file.virtualFile)
        val offset = file.text.indexOf("asset_main_address_rcs]") + 3
        val target = myFixture.file.findReferenceAt(offset)?.resolve()
        assertNotNull("join-alias field resolves through from:", target)
        assertEquals("asset_main_address_rcs", target!!.text)
    }

    fun testDashboardExploreBaseFilterFieldGoTo() {
        myFixture.addFileToProject("mfh_vacancy.model.lkml", aliasModel())
        val file = myFixture.addFileToProject("board.dashboard.lookml", aliasDashboard())
        myFixture.configureFromExistingVirtualFile(file.virtualFile)
        val offset = file.text.indexOf("mfh_vacancy.as_of_month") + "mfh_vacancy.".length + 2
        val target = myFixture.file.findReferenceAt(offset)?.resolve()
        assertNotNull("explore-base filter field resolves through from:", target)
        assertEquals("as_of_month", target!!.text)
    }

    fun testDashboardAliasFieldUsages() {
        myFixture.addFileToProject("board.dashboard.lookml", aliasDashboard())
        myFixture.configureByText(
            "mfh_vacancy.model.lkml",
            """
            explore: mfh_vacancy {
              from: mfh_vacancy_base
              join: primary_building_location {
                from: mfh_building_with_location
                relationship: many_to_one
                sql_on: 1=1 ;;
              }
            }
            view: mfh_vacancy_base {
              filter: as_of_month {
                type: string
              }
            }
            view: mfh_building_with_location {
              dimension: asset_main_address_r<caret>cs {
                type: string
                sql: ${'$'}{TABLE}.x ;;
              }
            }
            """.trimIndent(),
        )
        assertEquals("dashboard field via join alias counts as a usage", 1, usageCountAtCaret())
    }

    // Reproduces the reported miss: a measure used in a realistic dashboard element that also carries
    // dynamic_fields (table calc `expression: mean(${alias.field})`), filters, and viz-config keys that
    // embed the field name (`style_alias.field`, `series_labels`, ...). Find Usages must still find the
    // real `fields:` + `${...}` usages through the collapsed text-line blob.
    private fun noisyModel() =
        """
        explore: peakside_v2 {
          join: mart__actuals_estarcore {
            relationship: many_to_one
            sql_on: 1=1 ;;
          }
        }
        view: mart__actuals_estarcore {
          measure: weighted_vacancy_rate {
            type: number
            sql: 1 ;;
            value_format_name: percent_2
          }
        }
        """.trimIndent()

    private fun noisyDashboard() =
        """
        ---
        - dashboard: portfolio_overview_operating_results
          title: Portfolio Overview
          elements:
          - title: Vacancy rate
            name: Vacancy rate
            model: peakside-report_v2@{model_suffix}
            explore: peakside_v2
            type: marketplace_viz_multiple_value::multiple_value-marketplace
            fields: [mart__actuals_estarcore.weighted_vacancy_rate, peakside_v2.date_month_month]
            filters:
              peakside_v2.is_within_period: Yes
            sorts: [peakside_v2.date_month_month desc]
            limit: 500
            column_limit: 50
            dynamic_fields:
            - category: table_calculation
              expression: mean(${'$'}{mart__actuals_estarcore.weighted_vacancy_rate})
              label: Actual avg within period
            style_mart__actuals_estarcore.weighted_vacancy_rate: normal
            show_title_mart__actuals_estarcore.weighted_vacancy_rate: true
        """.trimIndent()

    fun testListenSingleValueFieldUsages() {
        myFixture.addFileToProject(
            "board.dashboard.lookml",
            """
            ---
            - dashboard: d
              elements:
              - title: t
                explore: mfh_vacancy
                listen:
                  As of Month: mfh_vacancy.data_as_of
            """.trimIndent(),
        )
        myFixture.configureByText(
            "mfh_vacancy.lkml",
            """
            view: mfh_vacancy {
              dimension: data_as_o<caret>f {
                type: string
                sql: ${'$'}{TABLE}.d ;;
              }
            }
            """.trimIndent(),
        )
        assertEquals("field used in a listen: single value counts as a usage", 1, usageCountAtCaret())
    }

    fun testNoisyDashboardMeasureUsages() {
        myFixture.addFileToProject("board.dashboard.lookml", noisyDashboard())
        myFixture.configureByText("peakside_v2.model.lkml", noisyModel().replaceFirst("weighted_vacancy_rate", "weighted_vacancy_r<caret>ate"))
        val count = usageCountAtCaret()
        assertTrue("measure used in dashboard fields + table-calc expression (got $count)", count >= 2)
    }

    fun testElementFilterKeyUsageCollapsed() {
        // The element `filters:` MAP key `alias.field: value` is a field usage. Here an unquoted
        // multi-word value (`name: Data as of`) collapses the element into one greedy text-line; the
        // blob scanner must still pick the filter key up.
        myFixture.configureByText(
            "spine.lkml",
            """
            view: cal {
              dimension: is_within_pe<caret>riod {
                type: yesno
                sql: ${'$'}{TABLE}.x ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject("model.lkml", "explore: cal {}")
        myFixture.addFileToProject(
            "board.dashboard.lkml",
            """
            ---
            - dashboard: d
              elements:
              - title: e1
                name: Data as of
                explore: cal
                fields: [cal.other]
                filters:
                  cal.is_within_period: Yes
            """.trimIndent(),
        )
        assertEquals("element filters: key counts as a usage", 1, usageCountAtCaret())
    }

    fun testElementFilterKeyUsageStructured() {
        // Same, but no collapse trigger - the field must still be found.
        myFixture.configureByText(
            "spine.lkml",
            """
            view: cal {
              dimension: is_within_pe<caret>riod {
                type: yesno
                sql: ${'$'}{TABLE}.x ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject("model.lkml", "explore: cal {}")
        myFixture.addFileToProject(
            "board.dashboard.lkml",
            """
            ---
            - dashboard: d
              elements:
              - title: e1
                explore: cal
                filters:
                  cal.is_within_period: Yes
            """.trimIndent(),
        )
        assertEquals("structured element filters: key counts as a usage", 1, usageCountAtCaret())
    }

    fun testExploreQualifiedFieldResolvesThroughJoin() {
        // `explore.field` where the field lives in a JOINED view (not the explore's base): resolution
        // must scan the explore's participating views. The dashboard qualifies by the explore name.
        myFixture.configureByText(
            "cal.lkml",
            """
            view: cal {
              dimension: is_within_pe<caret>riod {
                type: yesno
                sql: ${'$'}{TABLE}.x ;;
              }
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject("base.lkml", "view: base_v {\n  dimension: id { type: number }\n}")
        myFixture.addFileToProject(
            "model.lkml",
            """
            explore: peakside {
              view_name: base_v
              join: cal { relationship: many_to_one }
            }
            """.trimIndent(),
        )
        myFixture.addFileToProject(
            "board.dashboard.lkml",
            """
            ---
            - dashboard: d
              elements:
              - title: e1
                name: Data as of
                explore: peakside
                filters:
                  peakside.is_within_period: Yes
            """.trimIndent(),
        )
        assertEquals("explore-qualified filter field resolves via a join", 1, usageCountAtCaret())
    }

    fun testDashboardModelGoTo() {
        myFixture.addFileToProject("sales.model.lkml", "explore: orders_explore {}")
        myFixture.configureByText(
            "board.dashboard.lkml",
            """
            ---
            - dashboard: d
              elements:
              - title: e1
                model: sa<caret>les
                explore: orders_explore
            """.trimIndent(),
        )
        val target = myFixture.getReferenceAtCaretPosition()?.resolve()
        assertNotNull("dashboard model: resolves to the model file", target)
        assertEquals("sales.model.lkml", target!!.containingFile.name)
    }
}
