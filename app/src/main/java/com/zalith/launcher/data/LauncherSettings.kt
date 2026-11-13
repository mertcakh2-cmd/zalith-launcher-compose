package com.zalith.launcher.data

import android.content.Context

/** Persisted launcher preferences. These are profile preferences, not device-wide tuning. */
class LauncherSettings(context: Context) {
    private val preferences = context.getSharedPreferences("launcher_settings", Context.MODE_PRIVATE)

    var performanceMode: Boolean
        get() = preferences.getBoolean("performance_mode", true)
        set(value) = preferences.edit().putBoolean("performance_mode", value).apply()

    var showFps: Boolean
        get() = preferences.getBoolean("show_fps", false)
        set(value) = preferences.edit().putBoolean("show_fps", value).apply()

    var reducedAnimations: Boolean
        get() = preferences.getBoolean("reduced_animations", false)
        set(value) = preferences.edit().putBoolean("reduced_animations", value).apply()

    var memoryGb: Float
        get() = preferences.getFloat("memory_gb", 4f)
        set(value) = preferences.edit().putFloat("memory_gb", value.coerceIn(1f, 8f)).apply()
}
