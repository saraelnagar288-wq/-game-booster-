package com.gameboost.ai.data

interface OptimizationEngine {
    fun calculateGamingScore(device: Any): Int
    fun estimateFps(device: Any, gameProfile: Any, mode: String): Any
    fun generateRecommendations(device: Any, gameProfile: Any, mode: String): Any
}
