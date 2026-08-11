package com.gameboost.ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
            .padding(16.dp)
    ) {
        Text(
            text = "GAMEBOOST AI",
            color = Cyan400,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Optimize. Analyze. Play Better.",
            color = Neutral400,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (stats != null) {
            val score = OptimizationEngine.calculateGamingScore(stats!!)
            val tier = OptimizationEngine.getTier(score)

            // Score Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Neutral900)
                    .border(1.dp, Neutral800, RoundedCornerShape(16.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Text("GAMING SCORE", color = Neutral400, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("$score", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Black)
                        Text("/100", color = Neutral500, fontSize = 20.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
                    }
                    Text("Tier: $tier", color = Cyan400, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Device (Detected)", color = Neutral400, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        InfoBox("Model", stats!!.model, Modifier.weight(1f))
                        InfoBox("OS", stats!!.osVersion, Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoBox("GPU", stats!!.gpuRenderer, Modifier.fillMaxWidth())
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Status Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Neutral900)
                    .border(1.dp, Neutral800, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("CURRENT STATUS", color = Neutral400, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    StatusRow("Battery", "${stats!!.batteryLevel}%" + if(stats!!.isCharging) " (Charging)" else "")
                    StatusRow("Thermal Status", stats!!.thermalStatus)
                    StatusRow("CPU Cores", "${stats!!.cpuCores} Cores (Detected)")
                }
            }
        }
    }
}

@Composable
fun InfoBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Neutral950)
            .border(1.dp, Neutral800.copy(alpha=0.5f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(label, color = Neutral500, fontSize = 10.sp)
        Text(value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun StatusRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Neutral300, fontSize = 14.sp)
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

val Neutral300 = Color(0xFFD4D4D4)
