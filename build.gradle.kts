import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.grammarkit.tasks.GenerateParserTask

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.intellij.platform") version "2.6.0"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = "com.andriidev"
version = "1.1.0"

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
            sinceBuild = "251"
            untilBuild = provider { null } // No upper bound
        }

        vendor {
            name = "Andrii Kobtsev"
            email = "andrii.kobtsev@gmail.com"
            url = "https://github.com/andriikobtsev/lookml-plugin"
        }

        description = """
            <p>Comprehensive <b>Looker Modeling Language (LookML)</b> support for IntelliJ-based IDEs.</p>
            
            <h3>Features:</h3>
            <ul>
                <li><b>Syntax Highlighting</b> - Keywords, strings, SQL blocks, field references</li>
                <li><b>File Type Recognition</b> - .lkml and .lookml files</li>
                <li><b>Code Commenting</b> - Line and block comments (Cmd/Ctrl + /)</li>
                <li><b>Brace Matching</b> - Automatic matching for {}, [], ()</li>
                <li><b>YAML Dashboard Support</b> - Full parsing of YAML-style dashboards</li>
                <li><b>Error Detection</b> - Real-time syntax validation</li>
                <li><b>Code Folding</b> - Collapse/expand views, explores, measures</li>
            </ul>
            
            <h3>Supported Syntax:</h3>
            <ul>
                <li>Traditional LookML (views, explores, dimensions, measures)</li>
                <li>YAML dashboards with full property support</li>
                <li>SQL blocks with template expressions</li>
                <li>Wildcard field references (users.basic*, detail*)</li>
                <li>Boolean values (yes/no, true/false)</li>
            </ul>
            
            <h3>License:</h3>
            <p>Dual-licensed under <b>AGPL-3.0</b> (open source) and <b>Commercial License</b> (proprietary use).</p>
            <p>For commercial licensing inquiries: andrii.kobtsev@gmail.com</p>
        """.trimIndent()

        changeNotes = """
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

    // Publishing configuration
    publishing {
        token = providers.environmentVariable("INTELLIJ_PUBLISH_TOKEN")
        channels = listOf("stable")
    }
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
