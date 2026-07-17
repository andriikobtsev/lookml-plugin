package com.yourcompany.lookml.license

import com.intellij.openapi.application.ApplicationManager

/**
 * Whether Pro (paid) features may run. Respects unit tests and headless runs.
 * [CheckLicense.isLicensed] null means the licensing facade is not ready yet - do not block.
 *
 * The result is cached in memory, and [CheckLicense.isLicensed] does certificate validation on every
 * call, which the JetBrains guidance says must not run too often. Two different TTLs:
 * - a positive (licensed) result is cached for [POSITIVE_TTL_MS] - licensed users hit hot paths
 *   (completion, annotator, formatter, find-usages) constantly, so this avoids repeated crypto; an
 *   expiring trial is still caught within this window.
 * - a negative (unlicensed) result is cached for only [NEGATIVE_TTL_MS] - so a license activated
 *   mid-session (Help | Register), or a transient "not ready yet" answer during startup, is picked up
 *   within seconds instead of blocking Pro features for minutes. Revalidating at most every
 *   [NEGATIVE_TTL_MS] for unlicensed users is still well within "not too often".
 * In-memory, so an IDE restart always re-validates from scratch.
 */
object LicenseConditions {

    private const val POSITIVE_TTL_MS = 5 * 60 * 1000L
    private const val NEGATIVE_TTL_MS = 15 * 1000L

    @Volatile private var cachedAllowed: Boolean = false
    @Volatile private var cachedAtMs: Long = 0L

    fun allowPaidPluginFeatures(): Boolean {
        val app = ApplicationManager.getApplication()
        if (app.isUnitTestMode || app.isHeadlessEnvironment) {
            return true
        }
        val now = System.currentTimeMillis()
        if (isCacheFresh(now, cachedAtMs, cachedAllowed)) {
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

    /**
     * True when the cached decision from [cachedAtMs] is still valid at [now]. A licensed (`true`)
     * result stays valid for [POSITIVE_TTL_MS]; an unlicensed (`false`) result only for
     * [NEGATIVE_TTL_MS], so a freshly activated license takes effect quickly. `cachedAtMs == 0` means
     * nothing cached yet.
     */
    internal fun isCacheFresh(now: Long, cachedAtMs: Long, cachedAllowed: Boolean): Boolean {
        if (cachedAtMs == 0L) return false
        val ttl = if (cachedAllowed) POSITIVE_TTL_MS else NEGATIVE_TTL_MS
        return now - cachedAtMs < ttl
    }
}
