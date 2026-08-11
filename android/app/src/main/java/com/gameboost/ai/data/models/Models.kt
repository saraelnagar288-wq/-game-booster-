package com.gameboost.ai.data.models

data class Game(
    val id: String,
    val name: String,
    val packageName: String,
    val engine: String,
    val cpuIntensity: String,
    val gpuIntensity: String
)

enum class OptimizationMode {
    MAX_FPS, BALANCED, MAX_QUALITY, BATTERY_SAVER, THERMAL_SAFE
}

data class GameSettings(
    val graphics: String,
    val fps: String,
    val shadows: String,
    val antiAliasing: String,
    val effects: String,
    val postProcessing: String,
    val resolution: String
)

data class FPSReport(
    val estimatedFpsRange: String,
    val average: Int,
    val low1Percent: Int,
    val stability: String,
    val confidence: String,
    val thermalRisk: String
)
