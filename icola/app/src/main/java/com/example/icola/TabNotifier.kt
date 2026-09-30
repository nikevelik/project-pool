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
