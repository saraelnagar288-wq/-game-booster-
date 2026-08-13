package com.gameboost.ai.ui.screens

import android.os.SystemClock
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gameboost.ai.hardware.DeviceAnalyzer
import com.gameboost.ai.hardware.DeviceStats
import com.gameboost.ai.hardware.PerformanceController
import com.gameboost.ai.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun PerformanceScreen() {
    val context = LocalContext.current
    val analyzer = remember { DeviceAnalyzer(context.applicationContext) }
    val controller = remember { PerformanceController(context.applicationContext) }
    var stats by remember { mutableStateOf<DeviceStats?>(null) }
    var hintActive by remember { mutableStateOf(false) }
    var cpuHistory by remember { mutableStateOf(emptyList<Float>()) }
    var ramHistory by remember { mutableStateOf(emptyList<Float>()) }
    var gameMode by remember { mutableStateOf(controller.gameModeLabel()) }
    var lastMark by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            val now = SystemClock.elapsedRealtimeNanos()
            if (hintActive && lastMark != 0L) controller.reportFrameDuration(now - lastMark)
            lastMark = now
            val value = analyzer.analyze()
            stats = value
            gameMode = controller.gameModeLabel()
            value.cpuUsagePercent?.let { cpuHistory = (cpuHistory + it).takeLast(30) }
            ramHistory = (ramHistory + value.ramUsagePercent).takeLast(30)
            delay(1000)
        }
    }
    DisposableEffect(Unit) { onDispose { controller.stopPerformanceHintSession() } }

    Column(Modifier.fillMaxSize().background(Neutral950).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Column {
                Text("PERFORMANCE", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Text("REAL DEVICE TELEMETRY", color = Cyan400, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.3.sp)
            }
            Surface(shape = CircleShape, color = Color(0xFF10272B), border = androidx.compose.foundation.BorderStroke(1.dp, Cyan400.copy(.45f))) {
                Text("AI", color = Cyan400, fontWeight = FontWeight.Black, modifier = Modifier.padding(12.dp))
            }
        }
        stats?.let { s ->
            val score = remember(s) {
                var v = 55
                s.cpuUsagePercent?.let { if (it < 35) v += 12 else if (it > 80) v -= 12 }
                if (s.ramUsagePercent < 60) v += 12 else if (s.ramUsagePercent > 85) v -= 12
                if (s.thermalStatus == "Cool" || s.thermalStatus == "Normal") v += 10
                if (s.refreshRate >= 90) v += 8
                v.coerceIn(0, 100)
            }
            val animated by animateFloatAsState(score / 100f, label = "score")
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent,
                border = androidx.compose.foundation.BorderStroke(1.dp, Cyan400.copy(.28f))
            ) {
                Box(Modifier.background(Brush.linearGradient(listOf(Color(0xFF102C31), Neutral900, Color(0xFF121417))), RoundedCornerShape(24.dp)).padding(20.dp)) {
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Column {
                            Text("GAMING SCORE", color = Neutral400, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.3.sp)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("$score", color = Color.White, fontSize = 50.sp, fontWeight = FontWeight.Black)
                                Text(" / 100", color = Neutral500, modifier = Modifier.padding(bottom = 9.dp))
                            }
                            Text("CPU • RAM • thermal • display", color = Neutral500, fontSize = 10.sp)
                        }
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(progress = { animated }, modifier = Modifier.size(86.dp), color = Cyan400, trackColor = Neutral800, strokeWidth = 8.dp)
                            Text("${(animated * 100).roundToInt()}", color = Cyan400, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
            Text("HARDWARE", color = Neutral400, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(10.dp)) {
                HardwareCard("CPU", s.socModel, "${s.socManufacturer} • ${s.cpuCores} cores • ${s.cpuArch}", s.cpuUsagePercent?.let { "${it.roundToInt()}% LOAD" } ?: "LOAD N/A", Modifier.weight(1f))
                HardwareCard("GPU", s.gpuRenderer, s.gpuVendor, "USAGE N/A", Modifier.weight(1f))
            }
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(10.dp)) {
                StatCard("RAM", "${s.ramUsagePercent.roundToInt()}%", "${gb(s.totalRamGb - s.availableRamGb)} / ${gb(s.totalRamGb)} GB", Modifier.weight(1f))
                StatCard("THERMAL", s.thermalStatus, "Android system status", Modifier.weight(1f))
            }
            ChartCard("CPU LOAD", cpuHistory, "Live CPU usage")
            ChartCard("RAM LOAD", ramHistory, "Live system memory usage")
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Neutral900,
                border = androidx.compose.foundation.BorderStroke(1.dp, Cyan400.copy(.18f))
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    Text("ANDROID PERFORMANCE", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Text("Official PerformanceHintManager + Android Game Mode integration.", color = Neutral500, fontSize = 10.sp)
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Column {
                            Text("Performance Hint", color = Neutral200, fontWeight = FontWeight.Bold)
                            Text(if (controller.performanceHintsSupported) "SUPPORTED" else "UNAVAILABLE", color = Cyan400, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                        Switch(checked = hintActive, enabled = controller.performanceHintsSupported, onCheckedChange = { enabled ->
                            hintActive = if (enabled) controller.startPerformanceHintSession() else false
                            if (!enabled) controller.stopPerformanceHintSession()
                        })
                    }
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Column {
                            Text("Game Mode", color = Neutral200, fontWeight = FontWeight.Bold)
                            Text("Android 12+ • system selected", color = Neutral500, fontSize = 9.sp)
                        }
                        Text(gameMode.uppercase(), color = Cyan400, fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }
                    Text("Supported modes: Standard • Performance • Battery. Mode selection is controlled by Android/OEM Game Dashboard; GameBoost AI reads the active mode and adapts its performance hints.", color = Neutral500, fontSize = 9.sp)
                }
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Neutral900,
                border = androidx.compose.foundation.BorderStroke(1.dp, Neutral800)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("GRAPHICS ENGINE", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Info("GPU vendor", s.gpuVendor)
                    Info("Renderer", s.gpuRenderer)
                    Info("Graphics API", s.graphicsApi)
                    Info("Refresh rate", "${s.refreshRate.roundToInt()} Hz")
                    Info("Android", "API ${s.apiLevel}")
                }
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFF10181A),
                border = androidx.compose.foundation.BorderStroke(1.dp, Cyan400.copy(.16f))
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("FPS MONITOR", color = Cyan400, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Text("FPS: NOT AVAILABLE FROM STANDARD APP APIs", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("GameBoost AI will not invent FPS values. Cross-app FPS requires a supported system/game overlay or instrumentation.", color = Neutral500, fontSize = 9.sp)
                }
            }
        } ?: Box(Modifier.fillMaxWidth().height(260.dp), Alignment.Center) { CircularProgressIndicator(color = Cyan400) }
    }
}

@Composable
private fun HardwareCard(title: String, value: String, sub: String, status: String, modifier: Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(18.dp), color = Neutral900, border = androidx.compose.foundation.BorderStroke(1.dp, Neutral800)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, color = Cyan400, fontSize = 10.sp, fontWeight = FontWeight.Black)
            Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 2)
            Text(sub, color = Neutral500, fontSize = 9.sp, maxLines = 2)
            Text(status, color = Cyan400, fontSize = 8.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, sub: String, modifier: Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(18.dp), color = Neutral900, border = androidx.compose.foundation.BorderStroke(1.dp, Neutral800)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(title, color = Neutral500, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Text(sub, color = Neutral500, fontSize = 9.sp)
        }
    }
}

@Composable
private fun ChartCard(title: String, values: List<Float>, caption: String) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), color = Neutral900, border = androidx.compose.foundation.BorderStroke(1.dp, Neutral800)) {
        Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black)
                Text(if (values.size >= 2) "LIVE" else "WAITING", color = Cyan400, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Box(Modifier.fillMaxWidth().height(95.dp).clip(RoundedCornerShape(12.dp)).background(Neutral950)) {
                if (values.size >= 2) {
                    Canvas(Modifier.fillMaxSize().padding(8.dp)) {
                        val path = Path()
                        values.forEachIndexed { i, v ->
                            val x = size.width * i / (values.size - 1)
                            val y = size.height - v.coerceIn(0f, 100f) / 100f * size.height
                            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        }
                        drawPath(path, Cyan400, style = androidx.compose.ui.graphics.drawscope.Stroke(4f, cap = StrokeCap.Round))
                    }
                } else Box(Modifier.fillMaxSize(), Alignment.Center) { Text("Waiting for data…", color = Neutral500, fontSize = 10.sp) }
            }
            Text(caption, color = Neutral500, fontSize = 9.sp)
        }
    }
}

@Composable
private fun Info(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        Text(label, color = Neutral500, fontSize = 10.sp)
        Text(value, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, maxLines = 2)
    }
}

private fun gb(value: Double) = String.format("%.1f", value.coerceAtLeast(0.0))
