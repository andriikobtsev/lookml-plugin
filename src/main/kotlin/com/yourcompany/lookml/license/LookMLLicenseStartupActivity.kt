package com.yourcompany.lookml.license

import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.util.Key
import com.intellij.util.Alarm

/**
 * On project open, shows a sticky notification when the plugin has no valid Marketplace license.
 * Retries briefly while [com.intellij.ui.LicensingFacade] is not yet initialized ([CheckLicense.isLicensed] null).
 */
class LookMLLicenseStartupActivity : StartupActivity.DumbAware {

    override fun runActivity(project: Project) {
        val app = ApplicationManager.getApplication()
        if (app.isUnitTestMode || app.isHeadlessEnvironment) {
            return
        }
        scheduleCheck(project, attempt = 0)
    }

    private fun scheduleCheck(project: Project, attempt: Int) {
        when (val licensed = CheckLicense.isLicensed()) {
            null -> {
                if (attempt >= MAX_FACADE_RETRIES) {
                    return
                }
                val alarm = Alarm(Alarm.ThreadToUse.SWING_THREAD, project)
                alarm.addRequest(
                    { scheduleCheck(project, attempt + 1) },
                    RETRY_DELAY_MS,
                    ModalityState.nonModal(),
                )
            }
            false -> maybeNotifyUnlicensed(project)
            true -> { /* licensed or unknown handled above */ }
        }
    }

    private fun maybeNotifyUnlicensed(project: Project) {
        if (project.getUserData(NOTIFIED_KEY) == true) {
            return
        }
        project.putUserData(NOTIFIED_KEY, true)

        val group =
            NotificationGroupManager.getInstance().getNotificationGroup(NOTIFICATION_GROUP_ID)
                ?: return
        val notification =
            group
                .createNotification(
                    TITLE,
                    BODY,
                    NotificationType.INFORMATION,
                )
                .apply {
                    addAction(
                        NotificationAction.createSimple(ACTION_LABEL) {
                            CheckLicense.requestLicense(BODY)
                        },
                    )
                }
        Notifications.Bus.notify(notification, project)
    }

    private companion object {
        private const val NOTIFICATION_GROUP_ID = "com.andriidev.lookml.license"
        private const val RETRY_DELAY_MS = 500
        private const val MAX_FACADE_RETRIES = 40

        private val NOTIFIED_KEY = Key.create<Boolean>("lookml.license.banner.shown")

        private const val TITLE = "LookML Support"
        private const val BODY =
            "Reading and basic editing are free. The Pro features (code completion, dashboard validation, and formatting) are free during the trial; after that, activate with Help | Register."
        private const val ACTION_LABEL = "Start free trial or activate…"
    }
}
