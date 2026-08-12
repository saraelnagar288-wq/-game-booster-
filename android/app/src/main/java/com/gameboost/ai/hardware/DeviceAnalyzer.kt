package com.gameboost.ai.hardware

import android.app.ActivityManager
import android.content.Context
import android.opengl.EGL14
import android.opengl.GLES20
import android.os.BatteryManager
import android.os.Build
import android.os.PowerManager
import android.view.WindowManager
import java.io.BufferedReader
import java.io.FileReader

/** Reads information exposed to a normal Android application. No root or hidden APIs. */
data class DeviceStats(
    val manufacturer: String,
    val model: String,
    val osVersion: String,
    val apiLevel: Int,
    val socManufacturer: String,
    val socModel: String,
    val cpuCores: Int,
    val cpuArch: String,
    val cpuUsagePercent: Float?,
    val totalRamGb: Double,
    val availableRamGb: Double,
    val ramUsagePercent: Float,
    val batteryLevel: Int,
    val isCharging: Boolean,
    val thermalStatus: String,
    val displayResolution: String,
    val refreshRate: Float,
    val gpuVendor: String,
    val gpuRenderer: String,
    val graphicsApi: String
)

class DeviceAnalyzer(private val context: Context) {
    private var previousCpu: CpuSnapshot? = null

    fun analyze(): DeviceStats {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        am.getMemoryInfo(memoryInfo)
        val totalRam = memoryInfo.totalMem.toDouble() / (1024.0 * 1024.0 * 1024.0)
        val availRam = memoryInfo.availMem.toDouble() / (1024.0 * 1024.0 * 1024.0)
        val ramUsage = if (memoryInfo.totalMem > 0L) {
            ((memoryInfo.totalMem - memoryInfo.availMem).toDouble() / memoryInfo.totalMem * 100.0)
                .toFloat().coerceIn(0f, 100f)
        } else 0f

        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        val isCharging = batteryManager.isCharging

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val thermalStatus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            when (powerManager.currentThermalStatus) {
                PowerManager.THERMAL_STATUS_NONE -> "Cool"
                PowerManager.THERMAL_STATUS_LIGHT -> "Normal"
                PowerManager.THERMAL_STATUS_MODERATE -> "Warm"
                PowerManager.THERMAL_STATUS_SEVERE -> "Hot"
                PowerManager.THERMAL_STATUS_CRITICAL -> "Critical"
                PowerManager.THERMAL_STATUS_EMERGENCY -> "Emergency"
                PowerManager.THERMAL_STATUS_SHUTDOWN -> "Shutdown imminent"
                else -> "Unknown"
            }
        } else "Unavailable"

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        @Suppress("DEPRECATION")
        val display = windowManager.defaultDisplay
        val refreshRate = display.refreshRate
        val metrics = android.util.DisplayMetrics()
        @Suppress("DEPRECATION")
        display.getRealMetrics(metrics)

        val gpu = GpuInfo.read()
        return DeviceStats(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            osVersion = "Android ${Build.VERSION.RELEASE}",
            apiLevel = Build.VERSION.SDK_INT,
            socManufacturer = Build.SOC_MANUFACTURER.ifBlank { "Unavailable" },
            socModel = Build.SOC_MODEL.ifBlank { Build.HARDWARE.ifBlank { "Unavailable" } },
            cpuCores = Runtime.getRuntime().availableProcessors(),
            cpuArch = Build.SUPPORTED_ABIS.firstOrNull() ?: "Unavailable",
            cpuUsagePercent = readCpuUsage(),
            totalRamGb = totalRam,
            availableRamGb = availRam,
            ramUsagePercent = ramUsage,
            batteryLevel = batteryLevel.coerceIn(0, 100),
            isCharging = isCharging,
            thermalStatus = thermalStatus,
            displayResolution = "${metrics.widthPixels} × ${metrics.heightPixels}",
            refreshRate = refreshRate,
            gpuVendor = gpu.vendor,
            gpuRenderer = gpu.renderer,
            graphicsApi = gpu.graphicsApi
        )
    }

    private fun readCpuUsage(): Float? {
        return try {
            val line = BufferedReader(FileReader("/proc/stat")).use { it.readLine() }
                ?: return null
            if (!line.startsWith("cpu ")) return null
            val values = line.trim().split(Regex("\\s+")).drop(1).mapNotNull { it.toLongOrNull() }
            if (values.size < 4) return null
            val current = CpuSnapshot(values.sum(), values[3] + (values.getOrNull(4) ?: 0L))
            val previous = previousCpu
            previousCpu = current
            if (previous == null) return null
            val totalDelta = current.total - previous.total
            val idleDelta = current.idle - previous.idle
            if (totalDelta <= 0L) return null
            ((totalDelta - idleDelta).toDouble() / totalDelta * 100.0).toFloat().coerceIn(0f, 100f)
        } catch (_: Exception) {
            null
        }
    }

    private data class CpuSnapshot(val total: Long, val idle: Long)
}

private data class GpuInfo(val vendor: String, val renderer: String, val graphicsApi: String) {
    companion object {
        fun read(): GpuInfo {
            var display = EGL14.EGL_NO_DISPLAY
            var eglContext = EGL14.EGL_NO_CONTEXT
            var surface = EGL14.EGL_NO_SURFACE
            return try {
                display = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
                if (display == EGL14.EGL_NO_DISPLAY) return unavailable()
                val version = IntArray(2)
                if (!EGL14.eglInitialize(display, version, 0, version, 1)) return unavailable()
                val configAttribs = intArrayOf(
                    EGL14.EGL_RENDERABLE_TYPE, 4,
                    EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT,
                    EGL14.EGL_RED_SIZE, 8, EGL14.EGL_GREEN_SIZE, 8,
                    EGL14.EGL_BLUE_SIZE, 8, EGL14.EGL_NONE
                )
                val configs = arrayOfNulls<android.opengl.EGLConfig>(1)
                val count = IntArray(1)
                if (!EGL14.eglChooseConfig(display, configAttribs, 0, configs, 0, 1, count, 0)) return unavailable()
                val config = configs[0] ?: return unavailable()
                val contextAttribs = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE)
                eglContext = EGL14.eglCreateContext(display, config, EGL14.EGL_NO_CONTEXT, contextAttribs, 0)
                if (eglContext == EGL14.EGL_NO_CONTEXT) return unavailable()
                val surfaceAttribs = intArrayOf(EGL14.EGL_WIDTH, 1, EGL14.EGL_HEIGHT, 1, EGL14.EGL_NONE)
                surface = EGL14.eglCreatePbufferSurface(display, config, surfaceAttribs, 0)
                if (surface == EGL14.EGL_NO_SURFACE) return unavailable()
                if (!EGL14.eglMakeCurrent(display, surface, surface, eglContext)) return unavailable()
                val vendor = GLES20.glGetString(GLES20.GL_VENDOR)?.trim().orEmpty()
                val renderer = GLES20.glGetString(GLES20.GL_RENDERER)?.trim().orEmpty()
                val versionString = GLES20.glGetString(GLES20.GL_VERSION)?.trim().orEmpty()
                if (renderer.isBlank()) unavailable() else GpuInfo(
                    vendor.ifBlank { "Unavailable" },
                    renderer,
                    if (versionString.isBlank()) "OpenGL ES" else "OpenGL ES · $versionString"
                )
            } catch (_: Throwable) {
                unavailable()
            } finally {
                if (display != EGL14.EGL_NO_DISPLAY) {
                    if (surface != EGL14.EGL_NO_SURFACE) EGL14.eglDestroySurface(display, surface)
                    if (eglContext != EGL14.EGL_NO_CONTEXT) EGL14.eglDestroyContext(display, eglContext)
                    EGL14.eglTerminate(display)
                }
            }
        }

        private fun unavailable() = GpuInfo("Unavailable", "GPU information unavailable", "Unavailable")
    }
}
