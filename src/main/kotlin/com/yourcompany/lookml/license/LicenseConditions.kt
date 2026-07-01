package com.yourcompany.lookml.license

import com.intellij.openapi.application.ApplicationManager

/**
 * Whether Pro (paid) features may run. Respects unit tests and headless runs.
 * [CheckLicense.isLicensed] null means the licensing facade is not ready yet - do not block.
 *
 * The result is cached in memory with a short TTL: the gate is called from hot paths (completion,
 * annotator, formatter model), and [CheckLicense.isLicensed] does certificate validation on every
 * call, which the JetBrains guidance says must not run too often. The TTL still catches a trial that
 * expires (or a license activated/removed) mid-session without needing an IDE restart. In-memory, so
 * a restart re-validates for free.
 */
object LicenseConditions {

    private const val CACHE_TTL_MS = 5 * 60 * 1000L

    @Volatile private var cachedAllowed: Boolean = false
    @Volatile private var cachedAtMs: Long = 0L

    fun allowPaidPluginFeatures(): Boolean {
        val app = ApplicationManager.getApplication()
        if (app.isUnitTestMode || app.isHeadlessEnvironment) {
            return true
        }
        val now = System.currentTimeMillis()
        val cachedAt = cachedAtMs
        if (cachedAt != 0L && now - cachedAt < CACHE_TTL_MS) {
            return cachedAllowed
        }
        val licensed = CheckLicense.isLicensed()
        if (licensed == null) {
            // Licensing facade not ready yet: allow, but do not cache so we re-check once it inits.
            return true
        }
        cachedAllowed = licensed
        cachedAtMs = now
        return licensed
    }
}
