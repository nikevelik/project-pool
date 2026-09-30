# Instagram Screen Notifier: Design

## Purpose
Awareness. Show a notification when the user enters Instagram's **Home** screen or **Reels** tab. Icola only observes; it never blocks or alters Instagram.

## Requirements
- Fire a notification when the committed screen becomes **Home** or **Reels**.
- Entering any other screen (Profile, Explore, DMs, any state where the bottom tab bar is not visible) is silent, but updates the state, so re-entering Home fires again (Home → Profile → Home = 2 notifications).
- Opening Instagram, or returning to it from another app, counts as entering: it fires for the landing screen.
- "Reels screen" means the Reels bottom-bar tab only, not reel viewers opened from DMs, profiles or Explore.
- Scrolling within Home or Reels does not change state and does not fire.
- When Instagram leaves the foreground, tracker state resets so the next return fires.
- Notification presentation: high-importance channel (heads-up banner + sound), single fixed notification ID (a new one replaces the old one), alert on every post, auto-dismiss after a few seconds.

## Platform
- Kotlin, package `com.example.icola`, minSdk 29, targetSdk 37.
- Notification permission (`POST_NOTIFICATIONS`, Android 13+) is granted manually by the user. Missing permission is logged and the notification skipped. No `MainActivity` changes (out of scope).

## Components
- **InstagramDetectorService**: `AccessibilityService`. Receives events, filters, throttles, reads the tree, wires detector → tracker → notifier. No notification logic of its own.
- **InstagramScreenDetector**: pure mapping from a node tree to `Screen { HOME, REELS, OTHER }`.
  1. Look up Instagram's bottom-bar tab nodes by resource ID (`com.instagram.android:id/feed_tab`, `clips_tab`) and check `isSelected`.
  2. Fallback: a selected node whose content description or text starts with "Home" or "Reels" (depth-limited walk).
  3. Otherwise `OTHER`.
  IDs are unverified and must be confirmed on the user's Instagram version (see Testing).
- **ScreenTracker**: state machine. Input: detected `Screen`. Output: `NotifyHome`, `NotifyReels` or nothing. A new screen must be seen on two consecutive reads before it is committed (debounce for transitions and half-loaded trees). Has a `reset()` for when Instagram leaves the foreground.
- **TabNotifier**: creates the high-importance channel; posts the notification with a fixed ID, alert every time, and a timeout-based auto-dismiss. Checks the runtime permission.

## Data flow
Instagram event → filter (package, event types) → throttle (~300 ms) → `rootInActiveWindow` → `InstagramScreenDetector` → `ScreenTracker` → `TabNotifier` when the tracker emits a notify action.

## Error handling
- Null root or node, or an exception while parsing: log and skip the event.
- Recycle nodes (required on API 29); cap tree depth.
- Missing notification permission: log a warning, skip.

## Testing
- Unit tests for `ScreenTracker` (debounce, re-entry after OTHER, reset, no repeat while unchanged).
- Unit tests for `InstagramScreenDetector` against fake node trees (ID match, fallback, OTHER).
- First implementation step: a temporary debug dump of nodes with IDs or descriptions from Instagram, to confirm `feed_tab` and `clips_tab` on the user's device. Remove it after the IDs are confirmed.
- Manual check on device: open Instagram (fires), Home → Profile (silent), Profile → Home (fires), Home → Reels (fires), leave and return to Instagram (fires).

## Out of scope
- Permission or settings UI in `MainActivity`.
- Detecting reel viewers outside the Reels tab.
- Time tracking, blocking, or per-tab statistics.
