package com.yourcompany.lookml.license

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Cache-freshness policy for the Pro gate. The key property: a negative (unlicensed) result must go
 * stale quickly so a license activated mid-session (Help | Register) is picked up within seconds,
 * while a positive (licensed) result may be cached longer to avoid repeated certificate validation.
 */
class LicenseConditionsTest {

    private val second = 1000L
    private val minute = 60 * second

    @Test
    fun nothingCachedIsNeverFresh() {
        assertFalse(LicenseConditions.isCacheFresh(now = 5 * minute, cachedAtMs = 0L, cachedAllowed = true))
        assertFalse(LicenseConditions.isCacheFresh(now = 5 * minute, cachedAtMs = 0L, cachedAllowed = false))
    }

    @Test
    fun unlicensedResultGoesStaleWithinSeconds() {
        val cachedAt = 100_000L
        // Just after caching: still fresh.
        assertTrue(LicenseConditions.isCacheFresh(cachedAt + 5 * second, cachedAt, cachedAllowed = false))
        // After the short negative window: stale, so a just-activated license is re-checked.
        assertFalse(LicenseConditions.isCacheFresh(cachedAt + 30 * second, cachedAt, cachedAllowed = false))
    }

    @Test
    fun licensedResultStaysCachedForMinutes() {
        val cachedAt = 100_000L
        // A licensed user keeps Pro without re-validating on every hot-path call.
        assertTrue(LicenseConditions.isCacheFresh(cachedAt + 30 * second, cachedAt, cachedAllowed = true))
        assertTrue(LicenseConditions.isCacheFresh(cachedAt + 4 * minute, cachedAt, cachedAllowed = true))
        // But an expiring trial is still caught within the positive window.
        assertFalse(LicenseConditions.isCacheFresh(cachedAt + 6 * minute, cachedAt, cachedAllowed = true))
    }
}
