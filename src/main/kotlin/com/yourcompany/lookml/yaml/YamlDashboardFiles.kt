package com.yourcompany.lookml.yaml

/** Heuristic: file content is a Looker YAML dashboard (not traditional LookML). */
object YamlDashboardFiles {

    fun isYamlDashboardContent(text: String): Boolean {
        val t = text.trimStart()
        return t.contains("- dashboard:") ||
            t.contains("- dashboard :") ||
            (t.contains("dashboard") && t.contains("elements:"))
    }
}
