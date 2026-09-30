package com.example.icola

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class InstagramDetectorService : AccessibilityService() {

    private val tracker = ScreenTracker()
    private lateinit var notifier: TabNotifier
    private var lastEventTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private val recheck = Runnable { evaluate() }
    private var rechecksLeft = 0
    private val poll = Runnable { pollTick() }
    private var polling = false
    private var pollMisses = 0

    override fun onServiceConnected() {
        super.onServiceConnected()
        notifier = TabNotifier(this).also { it.ensureChannel() }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val pkg = event.packageName?.toString() ?: return

        if (pkg != InstagramPackages.INSTAGRAM) {
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
                InstagramPackages.leavesInstagram(pkg, packageName)
            ) {
                // Overlays (keyboard, system UI) also fire this event; only reset if the
                // active window itself is another app, i.e. Instagram really left the foreground.
                val active = try {
                    rootInActiveWindow
                } catch (e: Exception) {
                    Log.w(TAG, "Could not get root node", e)
                    null
                }
                if (active != null) {
                    try {
                        val activePkg = active.packageName?.toString()
                        if (activePkg != null &&
                            InstagramPackages.leavesInstagram(activePkg, packageName)
                        ) {
                            tracker.reset()
                            stopPolling()
                        }
                    } finally {
                        @Suppress("DEPRECATION")
                        active.recycle()
                    }
                }
            }
            return
        }

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
            AccessibilityEvent.TYPE_VIEW_SELECTED,
            AccessibilityEvent.TYPE_VIEW_CLICKED -> Unit
            else -> return
        }

        ensurePolling()

        val now = System.currentTimeMillis()
        if (now - lastEventTime < THROTTLE_MS) {
            // Don't lose the last event of a burst: read again shortly.
            rechecksLeft = MAX_RECHECKS
            scheduleRecheck()
            return
        }
        lastEventTime = now
        rechecksLeft = MAX_RECHECKS
        evaluate()
    }

    private fun scheduleRecheck() {
        handler.removeCallbacks(recheck)
        handler.postDelayed(recheck, RECHECK_MS)
    }

    private fun evaluate() {
        val screen = currentScreen() ?: return
        Log.d(TAG, "detected=$screen")
        tracker.onDetected(screen)?.let { notifier.notify(it) }
        // A new screen needs a confirming read; don't wait for another event to supply it.
        if (tracker.hasPending && rechecksLeft > 0) {
            rechecksLeft--
            scheduleRecheck()
        }
    }

    /** The screen Instagram is showing right now, or null if it can't be read (or isn't active). */
    private fun currentScreen(): Screen? {
        val root = try {
            rootInActiveWindow
        } catch (e: Exception) {
            Log.w(TAG, "Could not get root node", e)
            null
        } ?: return null

        return try {
            if (root.packageName?.toString() != InstagramPackages.INSTAGRAM) null
            else InstagramScreenDetector.detect(snapshot(root, 0))
        } catch (e: Exception) {
            Log.w(TAG, "Failed to parse view hierarchy", e)
            null
        } finally {
            @Suppress("DEPRECATION")
            root.recycle()
        }
    }

    // Safety net: while Instagram is in use, notify every POLL_MS if on Home or Reels, even if
    // the same notification was shown before, in case the event-driven path missed one.
    private fun ensurePolling() {
        pollMisses = 0
        if (polling) return
        polling = true
        handler.postDelayed(poll, POLL_MS)
    }

    private fun stopPolling() {
        polling = false
        handler.removeCallbacks(poll)
    }

    private fun pollTick() {
        val screen = currentScreen()
        if (screen == null) {
            // Instagram may just be mid-transition; give up after a few unreadable ticks.
            if (++pollMisses >= MAX_POLL_MISSES) {
                stopPolling()
                return
            }
        } else {
            pollMisses = 0
            when (screen) {
                Screen.HOME -> notifier.notify(NotifyAction.HOME)
                Screen.REELS -> notifier.notify(NotifyAction.REELS)
                Screen.OTHER -> Unit
            }
        }
        handler.postDelayed(poll, POLL_MS)
    }

    override fun onInterrupt() {
        handler.removeCallbacks(recheck)
        stopPolling()
        tracker.reset()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        handler.removeCallbacks(recheck)
        stopPolling()
        tracker.reset()
        return super.onUnbind(intent)
    }

    private fun snapshot(node: AccessibilityNodeInfo, depth: Int): NodeSnapshot {
        val children = if (depth >= MAX_DEPTH) {
            emptyList()
        } else {
            buildList {
                for (i in 0 until node.childCount) {
                    val child = node.getChild(i) ?: continue
                    add(snapshot(child, depth + 1))
                    @Suppress("DEPRECATION")
                    child.recycle()
                }
            }
        }
        return NodeSnapshot(
            viewId = node.viewIdResourceName,
            label = (node.contentDescription ?: node.text)?.toString(),
            selected = node.isSelected,
            children = children
        )
    }

    private companion object {
        const val TAG = "InstagramDetector"
        const val THROTTLE_MS = 300L
        const val RECHECK_MS = 400L
        const val MAX_RECHECKS = 3
        const val POLL_MS = 5_000L
        const val MAX_POLL_MISSES = 3
        const val MAX_DEPTH = 25
    }
}
