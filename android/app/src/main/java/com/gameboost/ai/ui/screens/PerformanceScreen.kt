package com.gameboost.ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clip
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gameboost.ai.ui.theme.*

@Composable
fun PerformanceScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Neutral950)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("PERFORMANCE", color = Cyan400, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Gaming performance overview", color = Neutral400, fontSize = 14.sp)

        MetricCard("FPS", "60", "Estimated", Cyan400)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            SmallMetricCard("Average FPS", "58", Modifier.weight(1f))
            SmallMetricCard("1% Low", "42", Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            SmallMetricCard("Frame Time", "16.7 ms", Modifier.weight(1f))
            SmallMetricCard("Refresh Rate", "—", Modifier.weight(1f))
        }

        SectionCard("SYSTEM") {
            PerformanceRow("CPU", "Detected")
            PerformanceRow("GPU", "Detected")
            PerformanceRow("RAM", "Available")
            PerformanceRow("Thermal", "System data")
        }

        SectionCard("FPS GRAPH") {
            Row(
                modifier = Modifier.fillMaxWidth().height(110.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                val bars = listOf(72, 82, 66, 90, 76, 86, 64, 80, 92, 78, 88, 70)
                bars.forEach { height ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(height.dp)
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            .background(Cyan400)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("FPS values are estimated unless measured by a supported Android API.", color = Neutral500, fontSize = 11.sp)
        }

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Cyan400),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("OPTIMIZE PERFORMANCE", color = Neutral950, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun MetricCard(title: String, value: String, status: String, accent: Color) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Neutral900)
            .border(1.dp, Neutral800, RoundedCornerShape(18.dp)).padding(20.dp)
    ) {
        Text(title, color = Neutral400, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Text(value, color = Color.White, fontSize = 46.sp, fontWeight = FontWeight.Black)
        Text(status, color = accent, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SmallMetricCard(title: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.clip(RoundedCornerShape(14.dp)).background(Neutral900).padding(14.dp)) {
        Text(title, color = Neutral500, fontSize = 11.sp)
        Spacer(Modifier.height(4.dp))
        Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Neutral900).padding(16.dp)) {
        Text(title, color = Neutral400, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun PerformanceRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Neutral200, fontSize = 14.sp)
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
