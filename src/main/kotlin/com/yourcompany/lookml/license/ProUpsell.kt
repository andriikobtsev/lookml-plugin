package com.yourcompany.lookml.license

import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

/**
 * Shared Pro upsell surfaces. Turns an unlicensed Pro-feature attempt into a trial/activate
 * prompt instead of silence, without nagging.
 */
object ProUpsell {

    private const val NOTIFICATION_GROUP_ID = "com.andriidev.lookml.license"
    private const val BALLOON_SHOWN_KEY = "lookml.pro.upsell.balloon.shown"

    /** Modal prompt for explicit user actions (menu / shortcut on a Pro action). */
    fun showDialog(project: Project, featureLabel: String) {
        val choice =
            Messages.showYesNoDialog(
                project,
                "$featureLabel is a Pro feature. Start a free trial or activate Pro to use it.",
                "LookML Support - Pro Feature",
                "Get Pro",
                "Not Now",
                Messages.getInformationIcon(),
            )
        if (choice == Messages.YES) {
            CheckLicense.requestLicense("Activate LookML Support Pro or start a free trial to use $featureLabel.")
        }
    }

    /**
     * Non-intrusive balloon for the built-in Reformat Code path (formatting runs mid-edit, so a dialog
     * would be disruptive). Persisted, so it does not return on every IDE restart.
     */
    fun showBalloonOnce(project: Project) {
        val properties = PropertiesComponent.getInstance()
        if (properties.getBoolean(BALLOON_SHOWN_KEY, false)) {
            return
        }
        properties.setValue(BALLOON_SHOWN_KEY, true)

        val group =
            NotificationGroupManager.getInstance().getNotificationGroup(NOTIFICATION_GROUP_ID)
                ?: return
        group
            .createNotification(
                "LookML Support - Pro Feature",
                "Code formatting is a Pro feature. Start a free trial or activate Pro to format LookML.",
                NotificationType.INFORMATION,
            )
            .apply {
                addAction(
                    NotificationAction.createSimple("Get Pro") {
                        CheckLicense.requestLicense(
                            "Activate LookML Support Pro or start a free trial to use code formatting.",
                        )
                    },
                )
            }
            .notify(project)
    }
}
