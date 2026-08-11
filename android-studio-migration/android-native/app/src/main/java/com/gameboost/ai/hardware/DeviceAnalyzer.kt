package com.gameboost.ai.hardware

interface DeviceAnalyzer {
    fun getManufacturer(): String
    fun getModel(): String
    fun getAndroidVersion(): String
    fun getApiLevel(): Int
    fun getCpuArchitecture(): String
    fun getCpuCores(): Int
    fun getTotalRam(): Long
    fun getAvailableRam(): Long
    fun getDisplayResolution(): String
    fun getRefreshRate(): Float
    fun getGpuRenderer(): String
    fun getThermalStatus(): Int
}
