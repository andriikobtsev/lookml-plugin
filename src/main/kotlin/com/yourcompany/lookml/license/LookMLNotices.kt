package com.yourcompany.lookml.license

import com.intellij.ide.BrowserUtil
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project

/**
 * The two non-blocking notices, both triggered by opening a LookML file rather than by opening a
 * project, so they only reach users who are actually editing LookML.
 */
object LookMLNotices {

    private const val NOTIFICATION_GROUP_ID = "com.andriidev.lookml.license"
    private const val PLUGIN_ID = "com.andriidev.lookml"
    private const val TITLE = "LookML Support"

    /**
     * Shown at most once per plugin version to unlicensed users. Names the navigation features, which
     * are the strongest reason to try Pro and were missing from the previous wording.
     */
    fun showProNoticeOncePerVersion(project: Project): Boolean {
        if (CheckLicense.isLicensed() != false) {
            return false
        }
        val version = PluginManagerCore.getPlugin(PluginId.getId(PLUGIN_ID))?.version ?: return false
        if (!LookMLUsageState.claimProNoticeFor(version)) {
            return false
        }
        val body =
            "Reading and basic editing are free. Pro adds code navigation, find usages, rename, " +
                "completion, dashboard validation, and formatting, free during the trial."
        notify(project, body) {
            addAction(
                NotificationAction.createSimple("Start free trial or activate") {
                    CheckLicense.requestLicense(body)
                },
            )
        }
        return true
    }

    /**
     * Shown once to users with real usage history (see [UsageNudgeLogic]). Ratings drive Marketplace
     * ranking, and the listing currently has none.
     */
    fun maybeAskForReview(project: Project, snapshot: UsageSnapshot) {
        if (!UsageNudgeLogic.shouldAskForReview(snapshot, LookMLUsageState.today())) {
            return
        }
        LookMLUsageState.recordReviewAsked()
        notify(project, "Finding LookML Support useful? A short review helps other developers find it.") {
            addAction(
                NotificationAction.createSimple("Write a review") {
                    LookMLUsageState.finishReviewAsks()
                    BrowserUtil.browse(CheckLicense.MARKETPLACE_REVIEWS_URL)
                },
            )
            addAction(
                NotificationAction.createSimple("Don't ask again") {
                    LookMLUsageState.finishReviewAsks()
                },
            )
        }
    }

    private fun notify(
        project: Project,
        body: String,
        configure: com.intellij.notification.Notification.() -> Unit,
    ) {
        val group =
            NotificationGroupManager.getInstance().getNotificationGroup(NOTIFICATION_GROUP_ID)
                ?: return
        group
            .createNotification(TITLE, body, NotificationType.INFORMATION)
            .apply(configure)
            .notify(project)
    }
}
