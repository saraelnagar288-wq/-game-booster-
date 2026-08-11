package com.gameboost.ai.data

import com.gameboost.ai.data.models.FPSReport
import com.gameboost.ai.data.models.Game
import com.gameboost.ai.data.models.GameSettings
import com.gameboost.ai.data.models.OptimizationMode
import com.gameboost.ai.hardware.DeviceStats

object OptimizationEngine {
    fun calculateGamingScore(device: DeviceStats): Int {
        var score = 30
        
        if (device.cpuCores >= 8) score += 20
        else if (device.cpuCores >= 6) score += 15
        else if (device.cpuCores >= 4) score += 10
        else score += 5
        
        if (device.totalRamGb >= 8) score += 20
        else if (device.totalRamGb >= 6) score += 15
        else if (device.totalRamGb >= 4) score += 10
        else score += 5
        
        val gpu = device.gpuRenderer.lowercase()
        if (gpu.contains("adreno 7") || gpu.contains("mali-g715") || gpu.contains("rtx")) score += 30
        else if (gpu.contains("adreno 6") || gpu.contains("mali-g710")) score += 20
        else if (gpu.contains("mali-g5") || gpu.contains("adreno 5")) score += 10
        else score += 5
        
        return score.coerceIn(0, 100)
    }

    fun getTier(score: Int): String {
        return when {
            score <= 20 -> "Very Low"
            score <= 40 -> "Low"
            score <= 60 -> "Entry"
            score <= 75 -> "Mid-range"
            score <= 90 -> "High-end"
            else -> "Extreme"
        }
    }

    fun generateSettings(score: Int, game: Game, mode: OptimizationMode): GameSettings {
        var graphics = "Low"
        var fps = "30 FPS"
        var shadows = "Off"
        var antiAliasing = "Off"
        var effects = "Low"
        var postProcessing = "Low"
        var resolution = "720p"

        if (score >= 75) {
            graphics = "Ultra"
            fps = if (mode == OptimizationMode.MAX_FPS) "90/120 FPS (If Supported)" else "60 FPS"
            shadows = if (mode == OptimizationMode.MAX_FPS) "Medium" else "High"
            antiAliasing = "On (TAA)"
            effects = "High"
            postProcessing = "High"
            resolution = "1080p"
        } else if (score >= 50) {
            graphics = "Medium"
            fps = if (mode == OptimizationMode.MAX_FPS) "60 FPS" else "45 FPS"
            shadows = if (mode == OptimizationMode.MAX_FPS) "Off" else "Medium"
            antiAliasing = if (mode == OptimizationMode.MAX_QUALITY) "On (FXAA)" else "Off"
            effects = "Medium"
            postProcessing = "Medium"
            resolution = "HD+"
        } else {
            graphics = if (mode == OptimizationMode.MAX_QUALITY) "Medium" else "Smooth/Lowest"
            fps = if (mode == OptimizationMode.MAX_FPS) "45/60 FPS" else "30 FPS"
            shadows = "Off"
            antiAliasing = "Off"
            effects = "Low"
            postProcessing = "Low"
            resolution = "Standard"
        }

        return GameSettings(graphics, fps, shadows, antiAliasing, effects, postProcessing, resolution)
    }

    fun estimateFps(score: Int, game: Game, mode: OptimizationMode): FPSReport {
        var baseFps = 30
        
        if (score > 80) baseFps = 60
        else if (score > 55) baseFps = 45
        
        if (game.cpuIntensity == "Extreme") baseFps -= 10
        if (game.gpuIntensity == "Extreme") baseFps -= 10

        if (mode == OptimizationMode.MAX_FPS) baseFps += 15
        if (mode == OptimizationMode.MAX_QUALITY) baseFps -= 10

        baseFps = baseFps.coerceIn(15, 120)

        return FPSReport(
            estimatedFpsRange = "${(baseFps - 10).coerceAtLeast(15)}-${(baseFps + 5).coerceAtMost(120)} FPS",
            average = baseFps,
            low1Percent = (baseFps - 15).coerceAtLeast(10),
            stability = if (baseFps >= 50) "Good" else if (baseFps >= 30) "Acceptable" else "Poor",
            confidence = if (score == 30) "Low" else "Medium",
            thermalRisk = if (baseFps >= 60) "Moderate" else "Low"
        )
    }
}
