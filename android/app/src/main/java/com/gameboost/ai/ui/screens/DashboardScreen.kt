package com.gameboost.ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gameboost.ai.data.OptimizationEngine
import com.gameboost.ai.ui.theme.*
import com.gameboost.ai.viewmodel.MainViewModel

@Composable
fun DashboardScreen(viewModel: MainViewModel) {
    val stats by viewModel.deviceStats.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Neutral950)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("GAMEBOOST", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Black)
                Text("AI GAMING CENTER", color = Cyan400, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
            }
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF10252A),
                border = androidx.compose.foundation.BorderStroke(1.dp, Cyan400.copy(alpha = .35f))
            ) {
                Text("● READY", color = Cyan400, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp))
            }
        }

        if (stats != null) {
            val score = OptimizationEngine.calculateGamingScore(stats!!)
            val tier = OptimizationEngine.getTier(score)

            // Hero gaming score card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF10272B), Neutral900, Color(0xFF15171A))),
                        RoundedCornerShape(22.dp)
                    )
                    .border(1.dp, Cyan400.copy(alpha = .25f), RoundedCornerShape(22.dp))
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("GAMING SCORE", color = Neutral400, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("$score", color = Color.White, fontSize = 52.sp, fontWeight = FontWeight.Black)
                                Text(" / 100", color = Neutral500, fontSize = 16.sp, modifier = Modifier.padding(bottom = 9.dp))
                            }
                        }
                        Surface(shape = RoundedCornerShape(14.dp), color = Cyan400.copy(alpha = .12f)) {
                            Text(tier.uppercase(), color = Cyan400, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
                        }
                    }
                    LinearProgressIndicator(
                        progress = { score / 100f },
                        modifier = Modifier.fillMaxWidth().height(7.dp),
                        color = Cyan400,
                        trackColor = Neutral800,
                    )
                    Text("Your device gaming readiness", color = Neutral400, fontSize = 12.sp)
                }
            }

            // Device card
            SectionTitle("DEVICE")
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Neutral900,
                border = androidx.compose.foundation.BorderStroke(1.dp, Neutral800)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(stats!!.model, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("${stats!!.osVersion} • ${stats!!.cpuCores} CPU cores", color = Neutral400, fontSize = 12.sp)
                    InfoBox("GPU", stats!!.gpuRenderer, Modifier.fillMaxWidth())
                }
            }

            // Live status grid
            SectionTitle("LIVE STATUS")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                MetricCard("BATTERY", "${stats!!.batteryLevel}%", if (stats!!.isCharging) "CHARGING" else "ON BATTERY", Modifier.weight(1f))
                MetricCard("THERMAL", stats!!.thermalStatus, "SYSTEM", Modifier.weight(1f))
            }

            // Quick actions UI
            SectionTitle("QUICK ACTIONS")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                QuickAction("⚡", "BOOST", Modifier.weight(1f))
                QuickAction("🎮", "GAMES", Modifier.weight(1f))
                QuickAction("📊", "MONITOR", Modifier.weight(1f))
            }

            Text(
                "Hardware values are reported only when Android exposes them; estimated values are labeled accordingly.",
                color = Neutral500,
                fontSize = 10.sp,
                lineHeight = 14.sp,
                modifier = Modifier.padding(horizontal = 2.dp, vertical = 4.dp)
            )
        } else {
            Box(Modifier.fillMaxWidth().height(180.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Cyan400)
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(title, color = Neutral400, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
}

@Composable
private fun MetricCard(label: String, value: String, caption: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Neutral900,
        border = androidx.compose.foundation.BorderStroke(1.dp, Neutral800)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(label, color = Neutral500, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(value, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text(caption, color = Cyan400, fontSize = 9.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun QuickAction(icon: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF101D20),
        border = androidx.compose.foundation.BorderStroke(1.dp, Cyan400.copy(alpha = .18f))
    ) {
        Column(modifier = Modifier.padding(vertical = 15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 22.sp)
            Spacer(Modifier.height(5.dp))
            Text(label, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun InfoBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Neutral950, RoundedCornerShape(12.dp))
            .border(1.dp, Neutral800.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(label, color = Neutral500, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Text(value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium, maxLines = 2)
    }
}
