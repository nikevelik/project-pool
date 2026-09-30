# Instagram Screen Notifier Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Show a heads-up notification when the user enters Instagram's Home screen or Reels tab.

**Architecture:** An `AccessibilityService` snapshots Instagram's node tree into plain data (`NodeSnapshot`). A pure `InstagramScreenDetector` maps the snapshot to `Screen { HOME, REELS, OTHER }`. A pure `ScreenTracker` debounces and decides when to notify. `TabNotifier` posts the notification. Detector and tracker are unit-tested on the JVM; the service and notifier are verified on device.

**Tech Stack:** Kotlin, Android `AccessibilityService`, `NotificationCompat`, JUnit4 (`libs.junit`, already a dependency).

**Spec:** `docs/superpowers/specs/2026-09-30-instagram-screen-notifier-design.md`

## Global Constraints

- Package `com.example.icola`; minSdk 29, targetSdk 37.
- Instagram package: `com.instagram.android`; tab IDs `com.instagram.android:id/feed_tab` (Home) and `com.instagram.android:id/clips_tab` (Reels), unverified until Task 4.
- Fire on entering Home or Reels only; entering any other screen is silent but updates state.
- Reels means the Reels bottom-bar tab only.
- A new screen must be seen on two consecutive reads before it is committed.
- Notification: high-importance channel, single fixed notification ID, alert on every post, auto-dismiss after a few seconds.
- No `MainActivity` changes; missing `POST_NOTIFICATIONS` is logged and skipped.
- Recycle child nodes (API 29); cap tree depth.

## Review Focus

- Events stop after one read of a new screen (throttle drops the last event): the state stays pending and no notification fires. Accepted trade-off of the debounce; covered by a tracker test that pins the behavior.
- A → B → A flapping between reads must never commit B (tracker test).
- Our own heads-up notification produces `com.android.systemui` events; they must not reset the tracker, or every notification would re-trigger itself (`InstagramPackages` test).
- Nodes with null label and null view ID (common in Instagram trees) must not crash and must map to OTHER (detector test).
- Tab bar present but no tab selected must give OTHER, not the previous screen (detector test).

---

### Task 1: ScreenTracker and package policy

**Files:**
- Create: `app/src/main/java/com/example/icola/Screen.kt`
- Create: `app/src/main/java/com/example/icola/ScreenTracker.kt`
- Create: `app/src/main/java/com/example/icola/InstagramPackages.kt`
- Test: `app/src/test/java/com/example/icola/ScreenTrackerTest.kt`
- Test: `app/src/test/java/com/example/icola/InstagramPackagesTest.kt`

**Interfaces:**
- Produces: `enum class Screen { HOME, REELS, OTHER }`, `enum class NotifyAction { HOME, REELS }`, `class ScreenTracker { fun onDetected(screen: Screen): NotifyAction?; fun reset() }`, `object InstagramPackages { const val INSTAGRAM: String; fun leavesInstagram(pkg: String, ownPackage: String): Boolean }`.

- [ ] **Step 1: Write the failing tests**

`ScreenTrackerTest.kt`:
```kotlin
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
```

`InstagramPackagesTest.kt`:
```kotlin
package com.example.icola

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InstagramPackagesTest {

    private val own = "com.example.icola"

    @Test
    fun instagramItselfIsNotLeaving() {
        assertFalse(InstagramPackages.leavesInstagram(InstagramPackages.INSTAGRAM, own))
    }

    @Test
    fun systemUiIsIgnoredSoOurOwnNotificationDoesNotResetState() {
        assertFalse(InstagramPackages.leavesInstagram("com.android.systemui", own))
    }

    @Test
    fun ownPackageIsIgnored() {
        assertFalse(InstagramPackages.leavesInstagram(own, own))
    }

    @Test
    fun launcherAndOtherAppsCountAsLeaving() {
        assertTrue(InstagramPackages.leavesInstagram("com.sec.android.app.launcher", own))
        assertTrue(InstagramPackages.leavesInstagram("com.whatsapp", own))
    }
}
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `./gradlew :app:testDebugUnitTest --tests "com.example.icola.ScreenTrackerTest" --tests "com.example.icola.InstagramPackagesTest"`
Expected: FAIL (compilation error, unresolved reference `ScreenTracker`, `Screen`, `InstagramPackages`).

- [ ] **Step 3: Write the implementation**

`Screen.kt`:
```kotlin
package com.example.icola

enum class Screen { HOME, REELS, OTHER }

enum class NotifyAction { HOME, REELS }
```

`ScreenTracker.kt`:
```kotlin
package com.example.icola

/**
 * Decides when entering a screen should notify. A new screen must be read twice in a row
 * before it is committed, so transitional or half-loaded trees don't cause false alerts.
 */
class ScreenTracker {

    private var committed: Screen? = null
    private var pending: Screen? = null

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
```

`InstagramPackages.kt`:
```kotlin
package com.example.icola

object InstagramPackages {
    const val INSTAGRAM = "com.instagram.android"

    // Our own heads-up notification is reported by SystemUI; it must not reset tracker state.
    private val IGNORED = setOf("com.android.systemui")

    fun leavesInstagram(pkg: String, ownPackage: String): Boolean =
        pkg != INSTAGRAM && pkg != ownPackage && pkg !in IGNORED
}
```

- [ ] **Step 4: Run tests to verify they pass**

Run: `./gradlew :app:testDebugUnitTest --tests "com.example.icola.ScreenTrackerTest" --tests "com.example.icola.InstagramPackagesTest"`
Expected: PASS (12 tests).

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/example/icola/Screen.kt app/src/main/java/com/example/icola/ScreenTracker.kt app/src/main/java/com/example/icola/InstagramPackages.kt app/src/test/java/com/example/icola/ScreenTrackerTest.kt app/src/test/java/com/example/icola/InstagramPackagesTest.kt
git commit -m "feat: add screen tracker and Instagram package policy"
```

---

### Task 2: InstagramScreenDetector

**Files:**
- Create: `app/src/main/java/com/example/icola/NodeSnapshot.kt`
- Create: `app/src/main/java/com/example/icola/InstagramScreenDetector.kt`
- Test: `app/src/test/java/com/example/icola/InstagramScreenDetectorTest.kt`

**Interfaces:**
- Consumes: `Screen` from Task 1.
- Produces: `data class NodeSnapshot(val viewId: String?, val label: String?, val selected: Boolean, val children: List<NodeSnapshot> = emptyList())`, `object InstagramScreenDetector { const val HOME_TAB_ID: String; const val REELS_TAB_ID: String; fun detect(root: NodeSnapshot): Screen }`.

- [ ] **Step 1: Write the failing test**

`InstagramScreenDetectorTest.kt`:
```kotlin
package com.example.icola

import org.junit.Assert.assertEquals
import org.junit.Test

class InstagramScreenDetectorTest {

    private fun node(
        viewId: String? = null,
        label: String? = null,
        selected: Boolean = false,
        vararg children: NodeSnapshot
    ) = NodeSnapshot(viewId, label, selected, children.toList())

    private fun tabBar(homeSelected: Boolean, reelsSelected: Boolean) = node(
        children = arrayOf(
            node(InstagramScreenDetector.HOME_TAB_ID, "Home", homeSelected),
            node("com.instagram.android:id/search_tab", "Search and explore", false),
            node(InstagramScreenDetector.REELS_TAB_ID, "Reels", reelsSelected),
            node("com.instagram.android:id/profile_tab", "Profile", false)
        )
    )

    @Test
    fun selectedHomeTabIsHome() {
        assertEquals(Screen.HOME, InstagramScreenDetector.detect(tabBar(true, false)))
    }

    @Test
    fun selectedReelsTabIsReels() {
        assertEquals(Screen.REELS, InstagramScreenDetector.detect(tabBar(false, true)))
    }

    @Test
    fun tabBarWithNothingSelectedIsOther() {
        assertEquals(Screen.OTHER, InstagramScreenDetector.detect(tabBar(false, false)))
    }

    @Test
    fun nonHomeNonReelsTabSelectedIsOther() {
        val root = node(
            children = arrayOf(
                node(InstagramScreenDetector.HOME_TAB_ID, "Home", false),
                node("com.instagram.android:id/profile_tab", "Profile", true)
            )
        )
        assertEquals(Screen.OTHER, InstagramScreenDetector.detect(root))
    }

    @Test
    fun deeplyNestedTabIsFound() {
        val root = node(children = arrayOf(node(children = arrayOf(node(children = arrayOf(
            node(InstagramScreenDetector.REELS_TAB_ID, null, true)
        ))))))
        assertEquals(Screen.REELS, InstagramScreenDetector.detect(root))
    }

    @Test
    fun labelFallbackWorksWhenIdsAreUnknown() {
        val home = node(children = arrayOf(node("x:id/renamed", "Home", true)))
        val reels = node(children = arrayOf(node("x:id/renamed", "Reels", true)))
        assertEquals(Screen.HOME, InstagramScreenDetector.detect(home))
        assertEquals(Screen.REELS, InstagramScreenDetector.detect(reels))
    }

    @Test
    fun labelFallbackIsCaseInsensitivePrefixMatch() {
        val root = node(children = arrayOf(node(null, "REELS, tab 3 of 5", true)))
        assertEquals(Screen.REELS, InstagramScreenDetector.detect(root))
    }

    @Test
    fun unselectedLabelledNodeIsIgnored() {
        val root = node(children = arrayOf(node(null, "Home", false)))
        assertEquals(Screen.OTHER, InstagramScreenDetector.detect(root))
    }

    @Test
    fun nodesWithNullIdAndNullLabelDoNotCrash() {
        val root = node(children = arrayOf(node(null, null, true), node(null, null, false)))
        assertEquals(Screen.OTHER, InstagramScreenDetector.detect(root))
    }

    @Test
    fun idMatchWinsOverLabelMatch() {
        val root = node(
            children = arrayOf(
                node(null, "Reels", true),
                node(InstagramScreenDetector.HOME_TAB_ID, null, true)
            )
        )
        assertEquals(Screen.HOME, InstagramScreenDetector.detect(root))
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew :app:testDebugUnitTest --tests "com.example.icola.InstagramScreenDetectorTest"`
Expected: FAIL (unresolved reference `NodeSnapshot`, `InstagramScreenDetector`).

- [ ] **Step 3: Write the implementation**

`NodeSnapshot.kt`:
```kotlin
package com.example.icola

/** Plain-data copy of an accessibility node, so detection can run without the Android framework. */
data class NodeSnapshot(
    val viewId: String?,
    val label: String?,
    val selected: Boolean,
    val children: List<NodeSnapshot> = emptyList()
)
```

`InstagramScreenDetector.kt`:
```kotlin
package com.example.icola

object InstagramScreenDetector {

    const val HOME_TAB_ID = "${InstagramPackages.INSTAGRAM}:id/feed_tab"
    const val REELS_TAB_ID = "${InstagramPackages.INSTAGRAM}:id/clips_tab"

    fun detect(root: NodeSnapshot): Screen = byId(root) ?: byLabel(root) ?: Screen.OTHER

    private fun byId(node: NodeSnapshot): Screen? {
        if (node.selected) {
            when (node.viewId) {
                HOME_TAB_ID -> return Screen.HOME
                REELS_TAB_ID -> return Screen.REELS
            }
        }
        return node.children.firstNotNullOfOrNull { byId(it) }
    }

    /** Fallback for renamed IDs: a selected node labelled "Home" or "Reels". */
    private fun byLabel(node: NodeSnapshot): Screen? {
        if (node.selected) {
            val label = node.label?.lowercase()
            when {
                label == null -> Unit
                label.startsWith("reels") -> return Screen.REELS
                label.startsWith("home") -> return Screen.HOME
            }
        }
        return node.children.firstNotNullOfOrNull { byLabel(it) }
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew :app:testDebugUnitTest --tests "com.example.icola.InstagramScreenDetectorTest"`
Expected: PASS (10 tests).

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/example/icola/NodeSnapshot.kt app/src/main/java/com/example/icola/InstagramScreenDetector.kt app/src/test/java/com/example/icola/InstagramScreenDetectorTest.kt
git commit -m "feat: add Instagram screen detector"
```

---

### Task 3: Notifier, service and manifest wiring

**Files:**
- Create: `app/src/main/java/com/example/icola/TabNotifier.kt`
- Create: `app/src/main/java/com/example/icola/InstagramDetectorService.kt`
- Create: `app/src/main/res/xml/accessibility_service_config.xml`
- Modify: `app/src/main/res/values/strings.xml`
- Modify: `app/src/main/AndroidManifest.xml`

**Interfaces:**
- Consumes: `Screen`, `NotifyAction`, `ScreenTracker`, `InstagramPackages`, `NodeSnapshot`, `InstagramScreenDetector` from Tasks 1-2.
- Produces: `class TabNotifier(context: Context) { fun ensureChannel(); fun notify(action: NotifyAction) }` and the registered `InstagramDetectorService`.

- [ ] **Step 1: Write `TabNotifier.kt`**

```kotlin
package com.example.icola

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class TabNotifier(private val context: Context) {

    fun ensureChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Instagram screens",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Announces when you enter Instagram Home or Reels" }
        context.getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }

    fun notify(action: NotifyAction) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w(TAG, "POST_NOTIFICATIONS not granted; skipping notification")
            return
        }

        val text = when (action) {
            NotifyAction.HOME -> "Entered Instagram Home"
            NotifyAction.REELS -> "Entered Instagram Reels"
        }
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Icola")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setOnlyAlertOnce(false)
            .setAutoCancel(true)
            .setTimeoutAfter(TIMEOUT_MS)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            Log.w(TAG, "Unable to post notification", e)
        }
    }

    private companion object {
        const val TAG = "TabNotifier"
        const val CHANNEL_ID = "instagram_screens"
        const val NOTIFICATION_ID = 1001
        const val TIMEOUT_MS = 4_000L
    }
}
```

- [ ] **Step 2: Write `InstagramDetectorService.kt`**

The `DEBUG_DUMP` block is temporary and is removed in Task 4.

```kotlin
package com.example.icola

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class InstagramDetectorService : AccessibilityService() {

    private val tracker = ScreenTracker()
    private lateinit var notifier: TabNotifier
    private var lastEventTime = 0L

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
                tracker.reset()
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
        if (now - lastEventTime < THROTTLE_MS) return
        lastEventTime = now

        val root = try {
            rootInActiveWindow
        } catch (e: Exception) {
            Log.w(TAG, "Could not get root node", e)
            null
        } ?: return

        try {
            if (root.packageName?.toString() != InstagramPackages.INSTAGRAM) return
            val snapshot = snapshot(root, 0)
            if (DEBUG_DUMP) dump(snapshot, 0)
            val screen = InstagramScreenDetector.detect(snapshot)
            Log.d(TAG, "detected=$screen")
            tracker.onDetected(screen)?.let { notifier.notify(it) }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to parse view hierarchy", e)
        } finally {
            @Suppress("DEPRECATION")
            root.recycle()
        }
    }

    override fun onInterrupt() {
        tracker.reset()
    }

    override fun onUnbind(intent: Intent?): Boolean {
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

    // TEMPORARY (removed in Task 4): logs tab-like and selected nodes to confirm Instagram's IDs.
    private fun dump(node: NodeSnapshot, depth: Int) {
        if (node.selected || node.viewId?.contains("tab") == true) {
            Log.d(TAG, "DUMP ${"  ".repeat(depth)}id=${node.viewId} label=${node.label} selected=${node.selected}")
        }
        node.children.forEach { dump(it, depth + 1) }
    }

    private companion object {
        const val TAG = "InstagramDetector"
        const val THROTTLE_MS = 300L
        const val MAX_DEPTH = 25
        const val DEBUG_DUMP = true
    }
}
```

- [ ] **Step 3: Write `res/xml/accessibility_service_config.xml`**

No `packageNames` filter, because the service must see events from other apps to know Instagram left the foreground.

```xml
<?xml version="1.0" encoding="utf-8"?>
<accessibility-service xmlns:android="http://schemas.android.com/apk/res/android"
    android:accessibilityEventTypes="typeWindowStateChanged|typeWindowContentChanged|typeViewSelected|typeViewClicked"
    android:accessibilityFeedbackType="feedbackGeneric"
    android:accessibilityFlags="flagReportViewIds"
    android:canRetrieveWindowContent="true"
    android:description="@string/accessibility_service_description"
    android:notificationTimeout="100" />
```

- [ ] **Step 4: Update `strings.xml` and `AndroidManifest.xml`**

In `strings.xml`, add inside `<resources>`:
```xml
    <string name="accessibility_service_description">Notifies you when you enter Instagram\'s Home or Reels screen.</string>
```

In `AndroidManifest.xml`, add before `<application`:
```xml
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```
and add after the closing `</activity>` (inside `<application>`):
```xml
        <service
            android:name=".InstagramDetectorService"
            android:exported="false"
            android:label="@string/app_name"
            android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE">
            <intent-filter>
                <action android:name="android.accessibilityservice.AccessibilityService" />
            </intent-filter>
            <meta-data
                android:name="android.accessibilityservice"
                android:resource="@xml/accessibility_service_config" />
        </service>
```

- [ ] **Step 5: Build and run all unit tests**

Run: `./gradlew :app:assembleDebug :app:testDebugUnitTest`
Expected: BUILD SUCCESSFUL, all unit tests pass.

- [ ] **Step 6: Commit**

```bash
git add app/src/main
git commit -m "feat: add Instagram detector service and notifier"
```

---

### Task 4: Confirm IDs on device and remove the debug dump

**Files:**
- Modify: `app/src/main/java/com/example/icola/InstagramDetectorService.kt`
- Modify (only if IDs differ): `app/src/main/java/com/example/icola/InstagramScreenDetector.kt`, `app/src/test/java/com/example/icola/InstagramScreenDetectorTest.kt`

**Interfaces:**
- Consumes: the service from Task 3.

- [ ] **Step 1: Install and enable**

Run: `./gradlew :app:installDebug`
Then on the phone: Settings → Accessibility → Icola → On. Ensure notifications are allowed for Icola.

- [ ] **Step 2: Read Instagram's real tab IDs**

Run: `adb logcat -s InstagramDetector:D`
Open Instagram; tap Home, then Reels. Look at the `DUMP` lines. Expected: the selected Home tab shows `id=com.instagram.android:id/feed_tab` and the selected Reels tab `id=com.instagram.android:id/clips_tab`. If the IDs differ, update `HOME_TAB_ID` / `REELS_TAB_ID` in `InstagramScreenDetector.kt`, and the same constants used in `InstagramScreenDetectorTest.kt` follow automatically since the tests reference the constants.

- [ ] **Step 3: Manual behavior check**

Confirm each, with a heads-up banner and sound where a notification is expected:
- Open Instagram: notification for the landing screen (Home or Reels).
- Home → Profile: silent. Profile → Home: notification.
- Home → Reels: notification. Reels → Home: notification.
- Leave Instagram (go to launcher) and return: notification.
- Scroll within Home: no repeat notification.

- [ ] **Step 4: Remove the debug dump**

In `InstagramDetectorService.kt`, delete the `if (DEBUG_DUMP) dump(snapshot, 0)` line, the entire `dump` function with its `// TEMPORARY` comment, and the `DEBUG_DUMP` constant.

- [ ] **Step 5: Verify build and tests**

Run: `./gradlew :app:assembleDebug :app:testDebugUnitTest`
Expected: BUILD SUCCESSFUL, all tests pass.

- [ ] **Step 6: Commit**

```bash
git add app/src
git commit -m "chore: confirm Instagram tab IDs and remove debug dump"
```
