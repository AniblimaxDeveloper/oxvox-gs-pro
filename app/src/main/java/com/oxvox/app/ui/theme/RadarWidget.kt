package com.oxvox.app.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun RadarWidget() {
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "angle"
    )

    Canvas(modifier = Modifier.size(120.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 2

        // Lingkaran Radar
        drawCircle(color = NeonCyan.copy(alpha = 0.3f), radius = radius, center = center, style = Stroke(width = 3f))
        drawCircle(color = NeonCyan.copy(alpha = 0.1f), radius = radius * 0.6f, center = center, style = Stroke(width = 2f))

        // Sapuan Radar (Sweep)
        val sweepBrush = Brush.sweepGradient(
            colors = listOf(Color.Transparent, NeonCyan.copy(alpha = 0.8f))
        )
        drawCircle(
            brush = sweepBrush,
            radius = radius,
            center = center
        )

        // Garis Jarum
        val needleX = center.x + radius * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat()
        val needleY = center.y + radius * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()
        drawLine(
            color = NeonCyan,
            start = center,
            end = Offset(needleX, needleY),
            strokeWidth = 4f
        )
    }
}
