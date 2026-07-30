package com.yourcompany.lookml.license

/**
 * Usage snapshot the review nudge decides on. Days are epoch days so the arithmetic is calendar-safe
 * and trivially testable; [firstUseDay] is [NO_DAY] until the first LookML file is opened.
 */
data class UsageSnapshot(
    val firstUseDay: Long = UsageNudgeLogic.NO_DAY,
    val distinctUseDays: Int = 0,
    val fileOpenCount: Int = 0,
    val askCount: Int = 0,
    val lastAskDay: Long = UsageNudgeLogic.NO_DAY,
    val finished: Boolean = false,
)

/**
 * When to ask for a Marketplace review.
 *
 * Whether a user actually left a review is not observable (the Marketplace exposes no such API), so
 * "asked and clicked" is treated as done and the ask never repeats. A silent close is allowed exactly
 * one retry, [MIN_DAYS_BETWEEN_ASKS] days later, so a balloon dismissed by reflex is not the only shot.
 */
object UsageNudgeLogic {

    const val NO_DAY: Long = -1L

    /** Deliberately loose: real usage over more than one sitting, not a first-run prompt. */
    const val MIN_DAYS_SINCE_FIRST_USE: Int = 7
    const val MIN_DISTINCT_USE_DAYS: Int = 3
    const val MIN_FILE_OPENS: Int = 10

    const val MAX_ASKS: Int = 2
    const val MIN_DAYS_BETWEEN_ASKS: Int = 60

    fun shouldAskForReview(snapshot: UsageSnapshot, today: Long): Boolean {
        if (snapshot.finished || snapshot.askCount >= MAX_ASKS) {
            return false
        }
        if (snapshot.firstUseDay == NO_DAY || today < snapshot.firstUseDay) {
            return false
        }
        if (today - snapshot.firstUseDay < MIN_DAYS_SINCE_FIRST_USE) {
            return false
        }
        if (snapshot.distinctUseDays < MIN_DISTINCT_USE_DAYS || snapshot.fileOpenCount < MIN_FILE_OPENS) {
            return false
        }
        if (snapshot.askCount > 0 && today - snapshot.lastAskDay < MIN_DAYS_BETWEEN_ASKS) {
            return false
        }
        return true
    }

    /**
     * Applies one LookML file open. [distinctUseDays] only grows when the calendar day changes, which
     * is what makes "used on N separate days" meaningful rather than a single long session.
     */
    fun recordFileOpen(snapshot: UsageSnapshot, lastUseDay: Long, today: Long): UsageSnapshot =
        snapshot.copy(
            firstUseDay = if (snapshot.firstUseDay == NO_DAY) today else snapshot.firstUseDay,
            distinctUseDays = if (lastUseDay == today) snapshot.distinctUseDays else snapshot.distinctUseDays + 1,
            fileOpenCount = snapshot.fileOpenCount + 1,
        )
}
