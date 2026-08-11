package com.gameboost.ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gameboost.ai.ui.theme.Cyan400
import com.gameboost.ai.ui.theme.Neutral400
import com.gameboost.ai.ui.theme.Neutral800
import com.gameboost.ai.ui.theme.Neutral900
import com.gameboost.ai.ui.theme.Neutral950
import com.gameboost.ai.viewmodel.MainViewModel

@Composable
fun MonitorScreen(viewModel: MainViewModel) {
    val stats by viewModel.deviceStats.collectAsState()
    val ramUsage by viewModel.ramUsage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Neutral950)
            .padding(16.dp)
    ) {
        Text("Performance Monitor", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Real-time native device statistics.", color = Neutral400, fontSize = 14.sp)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // RAM Monitor
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Neutral900)
                .border(1.dp, Neutral800, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("RAM Usage (Detected)", color = Color.White, fontWeight = FontWeight.Medium)
                    Text("${ramUsage.toInt()}%", color = Cyan400, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(Neutral950)) {
                    Box(modifier = Modifier.fillMaxWidth(ramUsage / 100f).height(8.dp).background(Cyan400))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Total: ${String.format("%.1f", stats?.totalRamGb ?: 0.0)} GB", color = Neutral400, fontSize = 12.sp)
            }
        }
    }
}
