package com.yourcompany.lookml.license

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Review-ask policy. The properties that matter: never on first use, only after usage spread over
 * several days, at most twice ever, and never again once the user has acted on it.
 */
class UsageNudgeLogicTest {

    private val day0 = 20_000L

    private fun eligible(): UsageSnapshot =
        UsageSnapshot(
            firstUseDay = day0,
            distinctUseDays = UsageNudgeLogic.MIN_DISTINCT_USE_DAYS,
            fileOpenCount = UsageNudgeLogic.MIN_FILE_OPENS,
        )

    private val eligibleDay = day0 + UsageNudgeLogic.MIN_DAYS_SINCE_FIRST_USE

    @Test
    fun neverAsksOnFirstUse() {
        val firstOpen = UsageNudgeLogic.recordFileOpen(UsageSnapshot(), lastUseDay = UsageNudgeLogic.NO_DAY, today = day0)
        assertFalse(UsageNudgeLogic.shouldAskForReview(firstOpen, today = day0))
    }

    @Test
    fun asksOnceAllThresholdsAreMet() {
        assertTrue(UsageNudgeLogic.shouldAskForReview(eligible(), today = eligibleDay))
    }

    @Test
    fun waitsForEnoughElapsedDays() {
        assertFalse(UsageNudgeLogic.shouldAskForReview(eligible(), today = eligibleDay - 1))
    }

    @Test
    fun heavyUseInOneSittingIsNotEnough() {
        val bingeInOneDay = eligible().copy(distinctUseDays = 1, fileOpenCount = 500)
        assertFalse(UsageNudgeLogic.shouldAskForReview(bingeInOneDay, today = eligibleDay))
    }

    @Test
    fun occasionalUseWithoutEnoughOpensIsNotEnough() {
        val tooFewOpens = eligible().copy(fileOpenCount = UsageNudgeLogic.MIN_FILE_OPENS - 1)
        assertFalse(UsageNudgeLogic.shouldAskForReview(tooFewOpens, today = eligibleDay))
    }

    @Test
    fun actingOnTheAskEndsItForever() {
        val finished = eligible().copy(finished = true, askCount = 1, lastAskDay = eligibleDay)
        assertFalse(UsageNudgeLogic.shouldAskForReview(finished, today = eligibleDay + 10_000))
    }

    @Test
    fun aSilentCloseIsRetriedOnceAfterALongGap() {
        val askedOnce = eligible().copy(askCount = 1, lastAskDay = eligibleDay)
        assertFalse(
            UsageNudgeLogic.shouldAskForReview(
                askedOnce,
                today = eligibleDay + UsageNudgeLogic.MIN_DAYS_BETWEEN_ASKS - 1,
            ),
        )
        assertTrue(
            UsageNudgeLogic.shouldAskForReview(
                askedOnce,
                today = eligibleDay + UsageNudgeLogic.MIN_DAYS_BETWEEN_ASKS,
            ),
        )
    }

    @Test
    fun neverAsksMoreThanTwice() {
        val askedTwice = eligible().copy(askCount = UsageNudgeLogic.MAX_ASKS, lastAskDay = eligibleDay)
        assertFalse(UsageNudgeLogic.shouldAskForReview(askedTwice, today = eligibleDay + 10_000))
    }

    @Test
    fun aClockMovedBackwardsDoesNotAsk() {
        assertFalse(UsageNudgeLogic.shouldAskForReview(eligible(), today = day0 - 1))
    }

    @Test
    fun distinctDaysOnlyGrowWhenTheCalendarDayChanges() {
        val first = UsageNudgeLogic.recordFileOpen(UsageSnapshot(), lastUseDay = UsageNudgeLogic.NO_DAY, today = day0)
        assertEquals(day0, first.firstUseDay)
        assertEquals(1, first.distinctUseDays)
        assertEquals(1, first.fileOpenCount)

        val sameDay = UsageNudgeLogic.recordFileOpen(first, lastUseDay = day0, today = day0)
        assertEquals(1, sameDay.distinctUseDays)
        assertEquals(2, sameDay.fileOpenCount)

        val nextDay = UsageNudgeLogic.recordFileOpen(sameDay, lastUseDay = day0, today = day0 + 1)
        assertEquals(2, nextDay.distinctUseDays)
        assertEquals(3, nextDay.fileOpenCount)
        assertEquals(day0, nextDay.firstUseDay)
    }
}
