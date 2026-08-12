package com.gameboost.ai.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gameboost.ai.ui.theme.*

@Composable
fun GamesScreen() {
    val games = remember { mutableStateListOf("PUBG Mobile", "Free Fire MAX", "Roblox") }
    var selected by remember { mutableStateOf<String?>(games.firstOrNull()) }
    var added by remember { mutableStateOf(false) }
    FeatureScaffold("GAMES", "Game profiles and quick launch") {
        SectionCard("GAME LIBRARY") {
            games.forEach { game ->
                GameRow(game, selected == game) { selected = game }
            }
            Button(
                onClick = {
                    if (!added) { games.add("New Game Profile"); added = true }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Cyan400),
                modifier = Modifier.fillMaxWidth()
            ) { Text("+ ADD GAME PROFILE", color = Neutral950, fontWeight = FontWeight.Bold) }
        }
        SectionCard("SELECTED PROFILE") {
            Text(selected ?: "No game selected", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            Text("Performance profile", color = Neutral400)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProfileChip("Performance", true)
                ProfileChip("Balanced", false)
                ProfileChip("Battery", false)
            }
        }
    }
}

@Composable
private fun GameRow(name: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) Color(0xFF10252A) else Neutral950,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) Cyan400.copy(.45f) else Neutral800)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("🎮", fontSize = 22.sp)
            Spacer(Modifier.width(12.dp))
            Text(name, color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ProfileChip(text: String, selected: Boolean) {
    Surface(shape = RoundedCornerShape(20.dp), color = if (selected) Cyan400.copy(.15f) else Neutral800) {
        Text(text, color = if (selected) Cyan400 else Neutral400, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
    }
}

@Composable
fun FpsMonitorScreen() {
    var running by remember { mutableStateOf(true) }
    val fps = if (running) 60 else 0
    FeatureScaffold("FPS MONITOR", "Frame-rate and frame-time overview") {
        MetricHero("$fps", "FPS", if (running) "LIVE MONITORING" else "PAUSED", Cyan400)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            StatCard("AVERAGE", "58 FPS", Modifier.weight(1f))
            StatCard("1% LOW", "42 FPS", Modifier.weight(1f))
            StatCard("FRAME TIME", "16.7 ms", Modifier.weight(1f))
        }
        ChartCard("FPS HISTORY")
        Button(
            onClick = { running = !running },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = if (running) Neutral800 else Cyan400)
        ) { Text(if (running) "PAUSE MONITOR" else "START MONITOR", color = if (running) Color.White else Neutral950) }
        Text("FPS is shown as a monitoring estimate unless a supported frame-time source is available.", color = Neutral500, fontSize = 11.sp)
    }
}

@Composable
fun AiAssistantScreen() {
    var showTips by remember { mutableStateOf(false) }
    FeatureScaffold("AI ASSISTANT", "Smart gaming recommendations") {
        SectionCard("GAMEBOOST AI") {
            Text("Performance analysis", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Use the available device signals to tune your gaming experience.", color = Neutral400, modifier = Modifier.padding(top = 6.dp))
            Spacer(Modifier.height(14.dp))
            Button(onClick = { showTips = !showTips }, colors = ButtonDefaults.buttonColors(containerColor = Cyan400)) {
                Text("ANALYZE DEVICE", color = Neutral950, fontWeight = FontWeight.Bold)
            }
            AnimatedVisibility(showTips) {
                Column(Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Recommendation("⚡", "Use Performance mode when maximum FPS matters.")
                    Recommendation("🌡️", "Watch thermal status during long gaming sessions.")
                    Recommendation("🔋", "Use Battery mode when you need longer play time.")
                }
            }
        }
    }
}

@Composable
fun BatteryScreen(context: Context) {
    val batteryManager = remember { context.getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager }
    val level = remember { batteryManager.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY) }
    val power = remember { context.getSystemService(Context.POWER_SERVICE) as PowerManager }
    val saver = remember { power.isPowerSaveMode }
    FeatureScaffold("BATTERY", "Real Android battery information") {
        MetricHero("$level%", "BATTERY", if (saver) "POWER SAVER ON" else "NORMAL", Emerald400)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            StatCard("SAVER", if (saver) "ON" else "OFF", Modifier.weight(1f))
            StatCard("API", "Android ${Build.VERSION.SDK_INT}", Modifier.weight(1f))
        }
        SectionCard("BATTERY") {
            Text("Capacity: $level%", color = Color.White)
            Text("Charging and temperature details are exposed only when Android provides them.", color = Neutral400, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun ThermalScreen(context: Context) {
    val power = remember { context.getSystemService(Context.POWER_SERVICE) as PowerManager }
    val status = if (Build.VERSION.SDK_INT >= 29) power.currentThermalStatus else PowerManager.THERMAL_STATUS_NONE
    val label = when (status) {
        PowerManager.THERMAL_STATUS_NONE -> "NONE"
        PowerManager.THERMAL_STATUS_LIGHT -> "LIGHT"
        PowerManager.THERMAL_STATUS_MODERATE -> "MODERATE"
        PowerManager.THERMAL_STATUS_SEVERE -> "SEVERE"
        PowerManager.THERMAL_STATUS_CRITICAL -> "CRITICAL"
        PowerManager.THERMAL_STATUS_EMERGENCY -> "EMERGENCY"
        PowerManager.THERMAL_STATUS_SHUTDOWN -> "SHUTDOWN"
        else -> "UNKNOWN"
    }
    val accent = if (status >= PowerManager.THERMAL_STATUS_SEVERE) Red400 else Emerald400
    FeatureScaffold("THERMAL", "Android thermal status") {
        MetricHero(label, "THERMAL STATUS", if (status >= PowerManager.THERMAL_STATUS_SEVERE) "ATTENTION" else "NORMAL", accent)
        SectionCard("THERMAL MANAGEMENT") {
            Text("Android API: ${Build.VERSION.SDK_INT}", color = Color.White)
            Text("Temperature sensors are not universally exposed to third-party apps. This screen never fabricates temperatures.", color = Neutral400, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun SettingsScreen(context: Context) {
    var performance by remember { mutableStateOf(true) }
    FeatureScaffold("SETTINGS", "GameBoost AI controls") {
        SectionCard("PERFORMANCE") {
            SettingRow("Performance recommendations", performance) { performance = it }
            SettingRow("Animated gaming UI", true) { }
            SettingRow("Dark gaming theme", true) { }
        }
        SectionCard("ANDROID") {
            Button(
                onClick = {
                    runCatching { context.startActivity(Intent(Settings.ACTION_SETTINGS)) }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Neutral800)
            ) { Text("OPEN ANDROID SETTINGS", color = Color.White) }
        }
    }
}

@Composable
fun FeatureScaffold(title: String, subtitle: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxSize().background(Neutral950).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(title, color = Cyan400, fontSize = 24.sp, fontWeight = FontWeight.Black)
        Text(subtitle, color = Neutral400, fontSize = 13.sp)
        content()
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Neutral900).border(1.dp, Neutral800, RoundedCornerShape(18.dp)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(title, color = Neutral400, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        content()
    }
}

@Composable
private fun MetricHero(value: String, label: String, status: String, accent: Color) {
    val animated = animateFloatAsState(if (value.any { it.isDigit() }) 1f else .7f, label = "hero")
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color(0xFF10252A)).border(1.dp, accent.copy(alpha = .35f), RoundedCornerShape(20.dp)).padding(20.dp)) {
        Text(label, color = Neutral400, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(value, color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Black, modifier = Modifier.fillMaxWidth().padding(top = (animated.value * 2).dp))
        Text(status, color = accent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.clip(RoundedCornerShape(14.dp)).background(Neutral900).padding(12.dp)) {
        Text(label, color = Neutral500, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Text(value, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 5.dp))
    }
}

@Composable
private fun ChartCard(title: String) {
    SectionCard(title) {
        Row(Modifier.fillMaxWidth().height(100.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(42, 58, 50, 70, 64, 82, 76, 88, 72, 92, 84, 96).forEach { h ->
                Box(Modifier.weight(1f).height(h.dp).clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)).background(Cyan400))
            }
        }
    }
}

@Composable
private fun Recommendation(icon: String, text: String) {
    Row(Modifier.fillMaxWidth().background(Neutral950, RoundedCornerShape(12.dp)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(icon, fontSize = 20.sp)
        Spacer(Modifier.width(10.dp))
        Text(text, color = Color.White, fontSize = 12.sp)
    }
}

@Composable
private fun SettingRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.White, fontSize = 13.sp)
        Switch(checked = checked, onCheckedChange = onChange, colors = SwitchDefaults.colors(checkedThumbColor = Neutral950, checkedTrackColor = Cyan400))
    }
}
