package com.yourcompany.lookml.license

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile

/**
 * Drives both notices off "a LookML file was opened", so a developer who never touches LookML is
 * never interrupted. Replaces the previous project-open trigger.
 */
class LookMLFileOpenListener : FileEditorManagerListener {

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        val app = ApplicationManager.getApplication()
        if (app.isUnitTestMode || app.isHeadlessEnvironment) {
            return
        }
        if (!isLookMLFile(file)) {
            return
        }

        val project = source.project
        if (project.isDisposed) {
            return
        }

        val snapshot = LookMLUsageState.recordLookMLFileOpened()
        // At most one balloon per open: never stack the Pro notice and the review ask together.
        if (!LookMLNotices.showProNoticeOncePerVersion(project)) {
            LookMLNotices.maybeAskForReview(project, snapshot)
        }
    }

    private fun isLookMLFile(file: VirtualFile): Boolean =
        when (file.extension?.lowercase()) {
            "lkml", "lookml" -> true
            else -> false
        }
}
