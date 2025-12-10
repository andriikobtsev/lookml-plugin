package com.yourcompany.lookml.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiDocumentManager
import com.yourcompany.lookml.formatting.YamlDashboardRewriter

/**
 * Action to reformat YAML dashboard files
 * Triggered manually via menu or keyboard shortcut
 */
class ReformatYamlDashboardAction : AnAction("Reformat YAML Dashboard") {
    
    override fun getActionUpdateThread() = ActionUpdateThread.BGT
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        
        // Check if it's a YAML dashboard
        val text = psiFile.text
        if (!isYamlDashboard(text)) {
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
                e.printStackTrace()
            }
        }
    }
    
    override fun update(e: AnActionEvent) {
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        val text = psiFile?.text ?: ""
        
        // Show action only for YAML dashboard files
        e.presentation.isEnabled = isYamlDashboard(text)
        e.presentation.isVisible = isYamlDashboard(text)
    }
    
    private fun isYamlDashboard(text: String): Boolean {
        return text.contains("- dashboard:") ||
               text.contains("- dashboard :") ||
               (text.contains("dashboard") && text.contains("elements:"))
    }
}
