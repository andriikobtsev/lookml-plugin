package com.yourcompany.lookml.license

import com.intellij.openapi.application.ApplicationManager

/**
 * Whether paid-plugin features may run. Respects unit tests and headless runs.
 * [CheckLicense.isLicensed] null means licensing facade not ready yet — do not block.
 */
object LicenseConditions {

    fun allowPaidPluginFeatures(): Boolean {
        val app = ApplicationManager.getApplication()
        if (app.isUnitTestMode || app.isHeadlessEnvironment) {
            return true
        }
        return when (CheckLicense.isLicensed()) {
            null, true -> true
            false -> false
        }
    }
}
