package com.example.icola

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ScreenTrackerTest {

    private val tracker = ScreenTracker()

    @Test
    fun firstScreenNeedsTwoConsecutiveReads() {
        assertNull(tracker.onDetected(Screen.HOME))
        assertEquals(NotifyAction.HOME, tracker.onDetected(Screen.HOME))
    }

    @Test
    fun unchangedScreenDoesNotFireAgain() {
        tracker.onDetected(Screen.HOME)
        tracker.onDetected(Screen.HOME)
        assertNull(tracker.onDetected(Screen.HOME))
        assertNull(tracker.onDetected(Screen.HOME))
    }

    @Test
    fun homeToReelsFiresReels() {
        tracker.onDetected(Screen.HOME)
        tracker.onDetected(Screen.HOME)
        assertNull(tracker.onDetected(Screen.REELS))
        assertEquals(NotifyAction.REELS, tracker.onDetected(Screen.REELS))
    }

    @Test
    fun otherScreenIsSilent() {
        assertNull(tracker.onDetected(Screen.OTHER))
        assertNull(tracker.onDetected(Screen.OTHER))
    }

    @Test
    fun reenteringHomeAfterOtherFiresAgain() {
        tracker.onDetected(Screen.HOME)
        assertEquals(NotifyAction.HOME, tracker.onDetected(Screen.HOME))
        tracker.onDetected(Screen.OTHER)
        assertNull(tracker.onDetected(Screen.OTHER))
        assertNull(tracker.onDetected(Screen.HOME))
        assertEquals(NotifyAction.HOME, tracker.onDetected(Screen.HOME))
    }

    @Test
    fun flappingBetweenReadsNeverCommits() {
        tracker.onDetected(Screen.HOME)
        tracker.onDetected(Screen.HOME)
        assertNull(tracker.onDetected(Screen.REELS))
        assertNull(tracker.onDetected(Screen.HOME))
        assertNull(tracker.onDetected(Screen.REELS))
        assertNull(tracker.onDetected(Screen.HOME))
    }

    @Test
    fun singleReadOfNewScreenStaysPending() {
        tracker.onDetected(Screen.HOME)
        tracker.onDetected(Screen.HOME)
        assertNull(tracker.onDetected(Screen.REELS))
        // no further events: nothing fires until a second read arrives
    }

    @Test
    fun resetMakesNextEntryFireAgain() {
        tracker.onDetected(Screen.HOME)
        tracker.onDetected(Screen.HOME)
        tracker.reset()
        assertNull(tracker.onDetected(Screen.HOME))
        assertEquals(NotifyAction.HOME, tracker.onDetected(Screen.HOME))
    }
}
