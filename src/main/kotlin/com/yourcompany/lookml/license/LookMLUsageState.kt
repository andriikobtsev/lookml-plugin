package com.yourcompany.lookml.license

import com.intellij.ide.util.PropertiesComponent
import java.time.LocalDate

/**
 * Application-level, persisted counters behind the review nudge and the once-per-version Pro notice.
 *
 * Persisted (not [com.intellij.openapi.util.Key] user data) on purpose: the previous in-memory
 * approach re-showed the license notification on every IDE restart, which read as nagging.
 */
object LookMLUsageState {

    private const val FIRST_USE_DAY = "lookml.usage.firstUseDay"
    private const val LAST_USE_DAY = "lookml.usage.lastUseDay"
    private const val DISTINCT_USE_DAYS = "lookml.usage.distinctUseDays"
    private const val FILE_OPENS = "lookml.usage.fileOpens"
    private const val REVIEW_ASK_COUNT = "lookml.review.askCount"
    private const val REVIEW_LAST_ASK_DAY = "lookml.review.lastAskDay"
    private const val REVIEW_FINISHED = "lookml.review.finished"
    private const val PRO_NOTICE_VERSION = "lookml.proNotice.shownForVersion"

    fun today(): Long = LocalDate.now().toEpochDay()

    private fun properties(): PropertiesComponent = PropertiesComponent.getInstance()

    private fun readDay(key: String): Long =
        properties().getValue(key)?.toLongOrNull() ?: UsageNudgeLogic.NO_DAY

    fun snapshot(): UsageSnapshot {
        val properties = properties()
        return UsageSnapshot(
            firstUseDay = readDay(FIRST_USE_DAY),
            distinctUseDays = properties.getInt(DISTINCT_USE_DAYS, 0),
            fileOpenCount = properties.getInt(FILE_OPENS, 0),
            askCount = properties.getInt(REVIEW_ASK_COUNT, 0),
            lastAskDay = readDay(REVIEW_LAST_ASK_DAY),
            finished = properties.getBoolean(REVIEW_FINISHED, false),
        )
    }

    fun recordLookMLFileOpened(): UsageSnapshot {
        val today = today()
        val lastUseDay = readDay(LAST_USE_DAY)
        val updated = UsageNudgeLogic.recordFileOpen(snapshot(), lastUseDay = lastUseDay, today = today)

        val properties = properties()
        properties.setValue(FIRST_USE_DAY, updated.firstUseDay.toString())
        properties.setValue(LAST_USE_DAY, today.toString())
        properties.setValue(DISTINCT_USE_DAYS, updated.distinctUseDays, 0)
        properties.setValue(FILE_OPENS, updated.fileOpenCount, 0)
        return updated
    }

    fun recordReviewAsked() {
        val properties = properties()
        properties.setValue(REVIEW_ASK_COUNT, snapshot().askCount + 1, 0)
        properties.setValue(REVIEW_LAST_ASK_DAY, today().toString())
    }

    /** Clicked either action: never ask again, since a real review cannot be detected. */
    fun finishReviewAsks() {
        properties().setValue(REVIEW_FINISHED, true)
    }

    /**
     * True once per plugin version, so a genuinely new release may inform the user a single time
     * instead of on every project open.
     */
    fun claimProNoticeFor(version: String): Boolean {
        val properties = properties()
        if (properties.getValue(PRO_NOTICE_VERSION) == version) {
            return false
        }
        properties.setValue(PRO_NOTICE_VERSION, version)
        return true
    }
}
