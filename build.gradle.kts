import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.grammarkit.tasks.GenerateParserTask

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.intellij.platform") version "2.6.0"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = "com.andriidev"
version = "2026.2.0"

repositories {
    mavenCentral()

    // IntelliJ Platform Gradle Plugin Repositories Extension
    intellijPlatform {
        defaultRepositories()
    }
}

// Configure Gradle IntelliJ Plugin
dependencies {
    // Use IntelliJ IDEA Community as the target platform (works for PyCharm too)
    intellijPlatform {
        intellijIdeaCommunity("2025.1")

        // Plugin Dependencies
        bundledPlugin("com.intellij.java")

        // Test Framework
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }

    implementation(kotlin("stdlib"))

    // Test dependencies
    testImplementation("junit:junit:4.13.2")
}

// Configuration for IntelliJ Platform
intellijPlatform {
    pluginConfiguration {
        name = "LookML Support"
        version = project.version.toString()

        ideaVersion {
            // Match intellijIdeaCommunity above (251 = 2025.1). For 253: use intellijIdea("2025.3") + newer IPL; not drop-in with Grammar-Kit 2022.3.2.2.
            sinceBuild = "251"
            untilBuild = provider { null } // No upper bound
        }

        vendor {
            name = "Andrii Kobtsev"
            email = "andrii.kobtsev@gmail.com"
            url = "https://github.com/andriikobtsev/lookml-plugin"
        }

        description = """
            <p><b>LookML Support</b> - Looker Modeling Language for IntelliJ-based IDEs (IntelliJ IDEA, PyCharm, and others). Minimum IDE: <b>2025.1</b> (build 251+).</p>

            <h3>Features</h3>
            <ul>
                <li><b>Code navigation</b> - go-to-definition, find usages, rename, and go-to-implementation/super for LookML fields and views, across extended and refined views and YAML dashboards.</li>
                <li><b>Code formatting</b> - <b>Reformat Code</b> for traditional LookML; YAML Looker dashboards use the same action plus an optional <b>Reformat YAML Dashboard</b> shortcut (see change notes for keys).</li>
                <li><b>YAML dashboard rewriter</b> - canonical layout and indentation when a valid license is active (evaluation/trial included).</li>
                <li><b>Code style</b> - <b>Settings | Editor | Code Style | LookML</b> (default 2-space indent).</li>
                <li><b>YAML dashboard completion</b> - 150+ properties with descriptions.</li>
                <li><b>Syntax highlighting</b> - keywords, strings, SQL blocks, field references.</li>
                <li><b>File types</b> - <code>.lkml</code>, <code>.lookml</code>.</li>
                <li><b>Commenting, brace matching, folding</b> - standard IDE editing aids.</li>
                <li><b>Manifest files</b> - <code>manifest.lkml</code> support (<code>project_name</code>, <code>constant</code>, <code>local_dependency</code>, <code>remote_dependency</code>, and more).</li>
                <li><b>Validation</b> - syntax errors for traditional LookML, plus schema-backed checks for YAML dashboard properties.</li>
            </ul>

            <p><a href="https://andriikobtsev.github.io/lookml-plugin/">Website</a> | <a href="https://andriikobtsev.github.io/lookml-plugin/eula.html">End User License Agreement</a></p>

            <h3>Supported syntax</h3>
            <ul>
                <li>Traditional LookML (views, explores, dimensions, measures, SQL blocks, templates)</li>
                <li>YAML-style Looker dashboards</li>
                <li>Wildcard field references (<code>users.basic*</code>, etc.)</li>
            </ul>

            <h3>Free and Pro</h3>
            <p><b>Free</b> (no license, no expiry): syntax highlighting, file type recognition, code folding, commenting, brace matching, quote handling, and color settings.</p>
            <p><b>Pro</b>: code completion (150+ dashboard properties plus in-<code>sql:</code> field suggestions), schema-backed dashboard validation, code formatting (<b>Reformat Code</b> and the YAML dashboard rewriter), and <b>code navigation</b> - go-to-definition, find usages, rename, and go-to-implementation/super across extended and refined views and YAML dashboards. Try Pro free during the evaluation period (length shown on the listing); after the trial, activate via <b>Help | Register</b> (JetBrains account + license). The free features keep working regardless.</p>

            <h3>Founding price</h3>
            <p>Pro is launching at an early one-time price. Buy once and own it for good, including every feature added next. The price increases as the plugin grows, so early buyers lock in the lowest price and get all future updates free.</p>
            <p><b>Discounts &amp; promos:</b> any current offers are posted at <a href="https://andriikobtsev.github.io/lookml-plugin/#offer">andriikobtsev.github.io/lookml-plugin/#offer</a> - check there before you buy.</p>

            <h3>Roadmap</h3>
            <p>Pro is actively developed, and a Pro license includes future Pro features as free updates. Next up: <b>Looker API validation</b> - run Looker's own LookML validator from the IDE and catch real errors before you push (requires a Looker connection). These are planned directions, not dated commitments; the price rises as they ship.</p>

            <h3>Privacy</h3>
            <p>The plugin does not add custom telemetry. License validation uses the standard JetBrains Marketplace / IDE licensing flow.</p>

            <h3>Source code &amp; license (GitHub)</h3>
            <p>Source is available under the <b>GNU Affero General Public License v3</b> (AGPL-3.0). Using or modifying the source is separate from the Marketplace binary: follow AGPL for the repository. For questions about alternate licensing of the source, contact the vendor.</p>
            <p>Vendor: <a href="mailto:andrii.kobtsev@gmail.com">andrii.kobtsev@gmail.com</a> | <a href="https://github.com/andriikobtsev/lookml-plugin">GitHub</a></p>
        """.trimIndent()

        changeNotes = """
            <p>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</p>
            <p><b>Stay up to date.</b> New features, the roadmap, pricing, and any current discounts &amp; promos
            are always posted on the <a href="https://andriikobtsev.github.io/lookml-plugin/">website</a> and the
            <a href="https://plugins.jetbrains.com/plugin/28971-lookml-support">Freemium &amp; Roadmap page</a> -
            check there so you never miss an update.</p>
            <p>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</p>

            <h3>2026.2.0 - Code navigation (Pro)</h3>
            <p><b>New Pro navigation for LookML fields and views</b>, across extended and refined views and
            YAML dashboards:</p>
            <ul>
                <li><b>Go to Definition</b> - jump from a <code>${'$'}{field}</code> / <code>view.field</code>
                    reference to its dimension, measure, set, constant, or <code>named_value_format</code>;
                    from <code>from:</code> / <code>view_name:</code> to the view; and from dashboard
                    fields/explores/models to their declarations.</li>
                <li><b>Find Usages</b> (Alt+F7) and <b>Rename</b> - project-wide, scoped to the view family
                    (base + refinements + extends), so unrelated same-named fields are left alone.</li>
                <li><b>Go to Implementation(s)</b> (Ctrl/Cmd+Alt+B) and <b>Go to Super</b> (Ctrl/Cmd+U) - move
                    between a base view and the views that extend or refine it; matching gutter icons too.</li>
                <li><b>Field completion in <code>sql:</code></b> - inside <code>${'$'}{...}</code>, suggests the
                    fields available to the view, including everything inherited up the <code>extends</code> chain.</li>
            </ul>
            <p><b>Formatting fixes:</b></p>
            <ul>
                <li>Function calls in <code>sql:</code> keep the name tight to the parenthesis
                    (<code>DATE_TRUNC(...)</code>, not <code>DATE_TRUNC (...)</code>), and there is no
                    space before a comma (<code>SELECT a, b</code>).</li>
                <li>Multi-line <code>sql:</code> blocks (e.g. a <code>derived_table</code> query) keep
                    their line breaks and indentation instead of collapsing onto one line.</li>
                <li><code>derived_table</code> and <code>explore_source</code> blocks are laid out one
                    property per line instead of collapsing onto a single line.</li>
            </ul>
            <p>Also: Pro features now turn on right after you activate a license, without needing an IDE
            restart.</p>
            <p>These are Pro features (free during the trial). All free editing features are unchanged.
            Known limitations: navigation inside Liquid expressions is not yet resolved, and Find Usages on a
            <code>dimension_group</code> does not yet list its generated timeframe fields (e.g. <code>created_month</code>).</p>
            <p><b>Existing users - thank you.</b> The productivity features are now Pro, but your free
            features keep working with no license. For pricing and any current offer, see the
            <a href="https://andriikobtsev.github.io/lookml-plugin/#offer">website</a>. Buy at the founding
            price and lock it in - future Pro features arrive as free updates.</p>

            <h3>2026.1.0</h3>
            <p><b>Important for existing users:</b> LookML Support is moving to a freemium model.
            <b>Code completion</b>, <b>dashboard validation</b>, and <b>code formatting</b> are becoming
            <b>Pro</b> features, with a free trial. The plugin stays free to install, and syntax
            highlighting and basic editing stay free forever. Start the trial or activate Pro via
            <b>Help | Register</b>.</p>
            <ul>
                <li><b>Pro (free trial included):</b> code completion (150+ properties), schema-backed dashboard validation, and formatting (<b>Reformat Code</b> <code>Cmd/Ctrl+Alt+L</code>, <b>Reformat YAML Dashboard</b> <code>Cmd/Ctrl+Alt+Shift+Y</code>).</li>
                <li><b>Free:</b> syntax highlighting, file type recognition, folding, commenting, brace matching, and quote handling.</li>
                <li><b>Founding offer:</b> Pro is available at an early one-time price. Buy once to own it for good and get all future features (go-to-definition planned) as free updates. The price rises as the plugin grows.</li>
            </ul>
            <p><b>Also new in this release:</b></p>
            <ul>
                <li><b>Manifest files:</b> <code>manifest.lkml</code> support for <code>project_name</code>, <code>constant</code>, <code>local_dependency</code>, <code>remote_dependency</code>, <code>override_constant</code>, and <code>new_lookml_runtime</code>.</li>
                <li><b>Better dashboard validation:</b> fewer false "unknown property" warnings; covers table calculations (<code>dynamic_fields</code>), filters, and text tiles.</li>
                <li><b>More robust dashboard formatting:</b> arrays stay arrays, block scalars and nested blocks (such as <code>dynamic_fields</code>) are preserved, and multi-line field lists are handled.</li>
                <li><b>Fixes:</b> dashboards with a leading comment before <code>---</code> now parse correctly; emoji in text tiles are supported; formatting runs only on an explicit reformat, not while typing.</li>
                <li><b>Code style:</b> LookML indent defaults in <b>Settings | Editor | Code Style | LookML</b>.</li>
                <li><b>Compatibility:</b> <code>sinceBuild 251</code> (IntelliJ 2025.1+).</li>
            </ul>

            <h3>Version 1.2.0 - Code Formatter</h3>
            <ul>
                <li><b>NEW:</b> Code formatter for traditional LookML (Cmd/Ctrl+Alt/Option+L)</li>
                <li><b>NEW:</b> SQL formatting - blocks on single line with proper spacing</li>
                <li><b>NEW:</b> Template expressions formatted: ${'$'}{TABLE} with no spaces</li>
                <li><b>NEW:</b> Smart 2-space indentation based on block structure</li>
                <li><b>FIXED:</b> Lexer now parses numbers correctly (prevents "100" becoming "1 0 0")</li>
                <li><b>FIXED:</b> SQL tokens no longer include trailing whitespace</li>
            </ul>

            <h3>Version 1.1.0 - YAML Dashboard Autocomplete</h3>
            <ul>
                <li><b>NEW:</b> YAML dashboard autocomplete with 150+ properties</li>
                <li><b>NEW:</b> Dashboard-level properties (title, layout, filters, elements, etc.)</li>
                <li><b>NEW:</b> Element properties (type, model, explore, fields, charts, tables, maps)</li>
                <li><b>NEW:</b> Filter properties (name, type, ui_config, listens_to_filters, etc.)</li>
                <li><b>NEW:</b> Visualization type suggestions (looker_line, looker_column, single_value, etc.)</li>
                <li><b>NEW:</b> Context-aware property suggestions based on YAML structure</li>
                <li>Type a few letters and press Ctrl+Space to see intelligent suggestions!</li>
            </ul>

            <h3>Version 1.0.0 - Initial Release</h3>
            <ul>
                <li>Full LookML syntax highlighting</li>
                <li>Traditional LookML parser with BNF grammar</li>
                <li>YAML dashboard support</li>
                <li>File type recognition for .lkml and .lookml</li>
                <li>Code commenting (line and block)</li>
                <li>Brace/bracket matching</li>
                <li>Smart quote handling</li>
                <li>Real-time error detection</li>
                <li>Code folding for major blocks</li>
                <li>Customizable color scheme</li>
            </ul>
        """.trimIndent()
    }

    // Publishing: create token at https://plugins.jetbrains.com/author/me > Profile > Access Tokens
    publishing {
        token = providers.environmentVariable("INTELLIJ_PUBLISH_TOKEN")
        channels = listOf("stable")
    }

    pluginVerification {
        // release-version 20262 matches plugin version 2026.2.0, so the Marketplace prefix rule is
        // satisfied and no mute is needed. Bump release-version whenever the 2026.<major> changes.
        ides {
            ide(IntelliJPlatformType.IntellijIdeaCommunity, "2025.1")
        }
    }

    // Paid plugins: avoid headless IDE registration during searchable options build
    buildSearchableOptions = false
}

// Configure Grammar-Kit to generate parser from our BNF
tasks.named<GenerateParserTask>("generateParser") {
    sourceFile.set(file("src/main/grammar/LookML.bnf"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToParser.set("com/yourcompany/lookml/parser/LookMLParser.java")
    pathToPsiRoot.set("com/yourcompany/lookml/psi")
    purgeOldFiles.set(true)
}

// Ensure parser is generated before compilation
tasks.compileKotlin {
    dependsOn("generateParser")
}
tasks.compileJava {
    dependsOn("generateParser")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
            freeCompilerArgs.add("-Xjsr305=strict")
        }
    }

    wrapper {
        gradleVersion = "8.9"
    }

    // Disable instrumentation task if it exists
    findByName("instrumentCode")?.enabled = false
    findByName("instrumentTestCode")?.enabled = false
}

// Add generated sources directory
sourceSets {
    main {
        java {
            srcDir("src/main/gen")
        }
    }
}

// Disable instrumentation after project evaluation
afterEvaluate {
    tasks.findByName("instrumentCode")?.enabled = false
    tasks.findByName("instrumentTestCode")?.enabled = false
}

// Task to quickly test parsing
tasks.register("testParsing") {
    description = "Test LookML parsing on comprehensive syntax file"
    group = "verification"

    doLast {
        println("To test parsing:")
        println("1. Make sure the plugin is built: ./gradlew buildPlugin")
        println("2. Open src/test/resources/comprehensive_syntax_test.lkml in IntelliJ")
        println("3. Look for red underlines indicating parsing errors")
        println("")
        println("Or run: ./gradlew test")
    }
}
