package com.gameboost.ai.hardware

import android.app.GameManager
import android.content.Context
import android.os.Build
import android.os.PerformanceHintManager
import android.os.Process

/**
 * Uses official Android performance APIs when the device exposes them.
 * It never changes CPU/GPU clocks or kernel governors directly.
 */
class PerformanceController(context: Context) {
    private val appContext = context.applicationContext
    private var hintSession: PerformanceHintManager.Session? = null

    val performanceHintsSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            appContext.getSystemService(PerformanceHintManager::class.java) != null

    fun startPerformanceHintSession(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return false
        return try {
            val manager = appContext.getSystemService(PerformanceHintManager::class.java) ?: return false
            if (hintSession != null) return true
            hintSession = manager.createHintSession(
                intArrayOf(Process.myTid()),
                16_666_666L
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
            // Unsupported or invalid sessions are safely ignored.
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

    fun gameModeLabel(): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return "Unsupported"
        return try {
            val manager = appContext.getSystemService(GameManager::class.java) ?: return "Unavailable"
            when (manager.gameMode) {
                GameManager.GAME_MODE_PERFORMANCE -> "Performance"
                GameManager.GAME_MODE_BATTERY -> "Battery"
                GameManager.GAME_MODE_CUSTOM -> "Custom"
                GameManager.GAME_MODE_STANDARD -> "Standard"
                GameManager.GAME_MODE_UNSUPPORTED -> "Unsupported"
                else -> "System controlled"
            }
        } catch (_: Throwable) {
            "System controlled"
        }
    }
}
