package com.example.icola

/**
 * Decides when entering a screen should notify. A new screen must be read twice in a row
 * before it is committed, so transitional or half-loaded trees don't cause false alerts.
 */
class ScreenTracker {

    private var committed: Screen? = null
    private var pending: Screen? = null

    /** True while a new screen has been seen once and is waiting for its confirming read. */
    val hasPending: Boolean get() = pending != null

    fun onDetected(screen: Screen): NotifyAction? {
        if (screen == committed) {
            pending = null
            return null
        }
        if (screen != pending) {
            pending = screen
            return null
        }
        committed = screen
        pending = null
        return when (screen) {
            Screen.HOME -> NotifyAction.HOME
            Screen.REELS -> NotifyAction.REELS
            Screen.OTHER -> null
        }
    }

    /** Call when Instagram leaves the foreground so the next entry fires again. */
    fun reset() {
        committed = null
        pending = null
    }
}
