package com.yourcompany.lookml.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDocumentManager
import com.yourcompany.lookml.formatting.YamlDashboardRewriter
import com.yourcompany.lookml.license.LicenseConditions
import com.yourcompany.lookml.yaml.YamlDashboardFiles

/**
 * Action to reformat YAML dashboard files
 * Triggered manually via menu or keyboard shortcut
 */
class ReformatYamlDashboardAction : AnAction("Reformat YAML Dashboard") {

    private val log = logger<ReformatYamlDashboardAction>()

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        
        // Check if it's a YAML dashboard
        val text = psiFile.text
        if (!YamlDashboardFiles.isYamlDashboardContent(text)) {
            return
        }

        if (!LicenseConditions.allowPaidPluginFeatures()) {
            Messages.showWarningDialog(
                project,
                "LookML Support requires an active license after the evaluation period. Use Help | Register to activate.",
                "LookML Support",
            )
            return
        }

        // Reformat
        WriteCommandAction.runWriteCommandAction(project) {
            try {
                val reformatted = YamlDashboardRewriter.rewriteDashboard(psiFile)
                
                val document = PsiDocumentManager.getInstance(project).getDocument(psiFile)
                if (document != null) {
                    document.setText(reformatted)
                    PsiDocumentManager.getInstance(project).commitDocument(document)
                }
            } catch (e: Exception) {
                log.error("YamlDashboardRewriter failed", e)
            }
        }
    }
    
    override fun update(e: AnActionEvent) {
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        val text = psiFile?.text ?: ""
        
        // Show action only for YAML dashboard files
        val isYaml = YamlDashboardFiles.isYamlDashboardContent(text)
        val licensed = LicenseConditions.allowPaidPluginFeatures()
        e.presentation.isEnabled = isYaml && licensed
        e.presentation.isVisible = isYaml
    }
}
