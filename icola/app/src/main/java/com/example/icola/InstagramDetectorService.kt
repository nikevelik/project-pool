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
    private var lastEventTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private val recheck = Runnable { evaluate() }
    private var rechecksLeft = 0

    override fun onServiceConnected() {
        super.onServiceConnected()
        OverlayService.goHome = { performGlobalAction(GLOBAL_ACTION_HOME) }
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
                            // Don't leave the overlay covering the launcher or another app.
                            OverlayService.hide(this)
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
        // Only when entering Home/Reels, so a dismissed overlay stays gone until re-entry.
        if (tracker.onDetected(screen) != null) OverlayService.show(this)
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

    override fun onInterrupt() {
        handler.removeCallbacks(recheck)
        tracker.reset()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        handler.removeCallbacks(recheck)
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
        const val MAX_DEPTH = 25
    }
}
