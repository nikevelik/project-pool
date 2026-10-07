package com.example.icola

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Shows a fullscreen "Instagram Blocked" overlay. Tapping the dimmed background or the close
 * button dismisses it and stops the service; the card itself swallows taps so only the button
 * is interactive. At most one overlay exists at a time.
 */
class OverlayService : Service() {

    private var windowManager: WindowManager? = null
    private var overlay: View? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Repeated start requests while the overlay is up are ignored (no second instance).
        if (overlay == null) show()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        removeOverlay()
        super.onDestroy()
    }

    private fun show() {
        if (!Settings.canDrawOverlays(this)) {
            Log.w(TAG, "SYSTEM_ALERT_WINDOW not granted; skipping overlay")
            stopSelf()
            return
        }
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val view = buildView()
        try {
            wm.addView(view, layoutParams())
        } catch (e: Exception) {
            Log.w(TAG, "Unable to show overlay", e)
            stopSelf()
            return
        }
        windowManager = wm
        overlay = view
    }

    private fun removeOverlay() {
        val view = overlay ?: return
        overlay = null
        try {
            windowManager?.removeView(view)
        } catch (e: IllegalArgumentException) {
            // Already detached.
        }
    }

    private fun close() {
        removeOverlay()
        stopSelf()
    }

    private fun layoutParams() = WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        PixelFormat.TRANSLUCENT
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
        }
    }

    private fun buildView(): View {
        val message = TextView(this).apply {
            text = getString(R.string.overlay_message)
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            setTypeface(typeface, Typeface.BOLD)
            gravity = Gravity.CENTER
        }

        val closeButton = TextView(this).apply {
            text = getString(R.string.overlay_close)
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setTypeface(typeface, Typeface.BOLD)
            gravity = Gravity.CENTER
            minHeight = dp(48)
            minWidth = dp(160)
            setPadding(dp(24), dp(12), dp(24), dp(12))
            background = GradientDrawable().apply {
                setColor(0xFFE1306C.toInt())
                cornerRadius = dp(24).toFloat()
            }
            isClickable = true
            setOnClickListener {
                goHome?.invoke()
                close()
            }
        }

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(32), dp(32), dp(32), dp(28))
            background = GradientDrawable().apply {
                setColor(0xFF1C1C1E.toInt())
                cornerRadius = dp(20).toFloat()
            }
            isClickable = true // swallows taps so only the button inside is interactive
            addView(message)
            addView(
                closeButton,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { topMargin = dp(24) }
            )
        }

        return FrameLayout(this).apply {
            setBackgroundColor(0xFF000000.toInt()) // opaque: Instagram must not show through
            addView(
                card,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            )
        }
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()

    companion object {
        private const val TAG = "OverlayService"

        /** Set by the accessibility service, which alone may send the user to the home screen. */
        @Volatile
        var goHome: (() -> Unit)? = null

        /** Shows the overlay unless it is already showing or the permission is missing. */
        fun show(context: Context) {
            if (!Settings.canDrawOverlays(context)) {
                Log.w(TAG, "SYSTEM_ALERT_WINDOW not granted; skipping overlay")
                return
            }
            try {
                context.startService(Intent(context, OverlayService::class.java))
            } catch (e: IllegalStateException) {
                Log.w(TAG, "Unable to start overlay service", e)
            }
        }

        fun hide(context: Context) {
            context.stopService(Intent(context, OverlayService::class.java))
        }
    }
}
