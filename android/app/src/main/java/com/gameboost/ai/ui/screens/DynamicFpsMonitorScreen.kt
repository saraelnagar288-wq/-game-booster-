package com.gameboost.ai.ui.screens

import android.view.Choreographer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gameboost.ai.ui.theme.Cyan400
import com.gameboost.ai.ui.theme.Neutral400
import com.gameboost.ai.ui.theme.Neutral500
import com.gameboost.ai.ui.theme.Neutral800
import com.gameboost.ai.ui.theme.Neutral900
import com.gameboost.ai.ui.theme.Neutral950
import kotlin.math.roundToInt

@Composable
fun DynamicFpsMonitorScreen() {
    var running by remember { mutableStateOf(true) }
    var currentFps by remember { mutableFloatStateOf(0f) }
    var frameTimeMs by remember { mutableFloatStateOf(0f) }
    val history = remember { mutableStateListOf<Float>() }

    DisposableEffect(running) {
        if (!running) {
            onDispose { }
        } else {
            val choreographer = Choreographer.getInstance()
            var lastFrameNanos = 0L
            var lastPublishedNanos = 0L

            val callback = object : Choreographer.FrameCallback {
                override fun doFrame(frameTimeNanos: Long) {
                    if (!running) return
                    if (lastFrameNanos != 0L) {
                        val deltaNanos = frameTimeNanos - lastFrameNanos
                        if (deltaNanos > 0L) {
                            val ms = (deltaNanos / 1_000_000f).coerceIn(1f, 1000f)
                            val fps = (1000f / ms).coerceIn(1f, 240f)
                            currentFps = fps
                            frameTimeMs = ms
                            if (frameTimeNanos - lastPublishedNanos >= 250_000_000L) {
                                history.add(fps)
                                if (history.size > 24) history.removeAt(0)
                                lastPublishedNanos = frameTimeNanos
                            }
                        }
                    }
                    lastFrameNanos = frameTimeNanos
                    choreographer.postFrameCallback(this)
                }
            }
            choreographer.postFrameCallback(callback)
            onDispose { choreographer.removeFrameCallback(callback) }
        }
    }

    val average = if (history.isEmpty()) currentFps else history.average().toFloat()
    val low = history.minOrNull() ?: currentFps
    val bars = if (history.isEmpty()) listOf(currentFps.coerceAtLeast(1f)) else history.toList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Neutral950)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("FPS MONITOR", color = Cyan400, fontSize = 24.sp)
        Text("Real-time UI frame pacing monitor", color = Neutral400, fontSize = 13.sp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF10252A))
                .border(1.dp, Cyan400.copy(alpha = .35f), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Text("FPS", color = Neutral400, fontSize = 11.sp)
            Text(if (running) currentFps.roundToInt().coerceIn(0, 240).toString() else "0", color = Color.White, fontSize = 42.sp)
            Text(if (running) "LIVE FRAME PACING" else "PAUSED", color = Cyan400, fontSize = 12.sp)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            Metric("AVERAGE", "${average.roundToInt()} FPS")
            Metric("LOW", "${low.roundToInt()} FPS")
            Metric("FRAME TIME", "${"%.1f".format(frameTimeMs)} ms")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Neutral900)
                .border(1.dp, Neutral800, RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Text("LIVE FPS HISTORY", color = Neutral400, fontSize = 11.sp)
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth().height(130.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                bars.forEach { value ->
                    val height = (value.coerceIn(1f, 120f) / 120f * 110f).dp
                    Spacer(
                        Modifier
                            .weight(1f)
                            .height(height)
                            .background(Cyan400.copy(alpha = .75f), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    )
                }
            }
        }

        Button(
            onClick = { running = !running },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = if (running) Neutral800 else Cyan400)
        ) {
            Text(if (running) "PAUSE MONITOR" else "START MONITOR", color = if (running) Color.White else Neutral950)
        }

        Text(
            "This measures GameBoost AI's own rendered frames. Exact FPS of another game requires a supported system/game telemetry source; the app does not invent a game FPS value.",
            color = Neutral500,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun RowScope.Metric(label: String, value: String) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(Neutral900)
            .border(1.dp, Neutral800, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Text(label, color = Neutral500, fontSize = 9.sp)
        Spacer(Modifier.height(5.dp))
        Text(value, color = Color.White, fontSize = 13.sp)
    }
}
