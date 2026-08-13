package com.gameboost.ai.hardware

import android.app.GameManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.PerformanceHintManager
import android.os.Process

/**
 * Official Android performance integration.
 * No CPU/GPU overclocking, kernel writes, or thermal-limit changes.
 */
class PerformanceController(context: Context) {
    private val appContext = context.applicationContext
    private var hintSession: PerformanceHintManager.Session? = null

    val performanceHintsSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            appContext.getSystemService(PerformanceHintManager::class.java) != null

    /**
     * True when this APK is declared as a game and the Android Game Mode API exists.
     * This is API support, not a claim that the OEM currently exposes a selectable mode.
     */
    val gameModeApiSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            appContext.getSystemService(GameManager::class.java) != null &&
            ((appContext.applicationInfo.category and ApplicationInfo.CATEGORY_GAME) == ApplicationInfo.CATEGORY_GAME)

    fun currentGameMode(): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return GameManager.GAME_MODE_UNSUPPORTED
        return try {
            val manager = appContext.getSystemService(GameManager::class.java)
                ?: return GameManager.GAME_MODE_UNSUPPORTED
            manager.gameMode
        } catch (_: Throwable) {
            GameManager.GAME_MODE_UNSUPPORTED
        }
    }

    fun gameModeLabel(): String = when (currentGameMode()) {
        GameManager.GAME_MODE_PERFORMANCE -> "Performance"
        GameManager.GAME_MODE_BATTERY -> "Battery"
        GameManager.GAME_MODE_CUSTOM -> "Custom"
        GameManager.GAME_MODE_STANDARD -> "Standard"
        else -> if (gameModeApiSupported) "Supported • Standard" else "Unavailable"
    }

    /** Target work duration adapts the official hint session to the selected Game Mode. */
    fun targetWorkDurationNanos(): Long = when (currentGameMode()) {
        GameManager.GAME_MODE_BATTERY -> 33_333_333L // ~30 Hz workload target
        else -> 16_666_666L // ~60 Hz workload target
    }

    fun startPerformanceHintSession(): Boolean {
        if (!performanceHintsSupported) return false
        return try {
            if (hintSession != null) return true
            val manager = appContext.getSystemService(PerformanceHintManager::class.java) ?: return false
            hintSession = manager.createHintSession(
                intArrayOf(Process.myTid()),
                targetWorkDurationNanos()
            )
            hintSession != null
        } catch (_: Throwable) {
            hintSession = null
            false
        }
    }

    fun reportFrameDuration(durationNanos: Long) {
        try {
            hintSession?.reportActualWorkDuration(durationNanos.coerceAtLeast(1L))
        } catch (_: Throwable) {
            // Ignore unsupported/invalid sessions safely.
        }
    }

    fun stopPerformanceHintSession() {
        try {
            hintSession?.close()
        } catch (_: Throwable) {
            // Ignore cleanup failures.
        } finally {
            hintSession = null
        }
    }
}
