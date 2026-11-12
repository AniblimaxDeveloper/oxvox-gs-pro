package com.oxvox.app.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

@Composable
fun CyberBackground(content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "galaxy")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "alpha"
    )

    val stars = remember {
        List(50) {
            Offset(Random.nextFloat(), Random.nextFloat())
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(VillainBlack)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            stars.forEach { star ->
                drawCircle(
                    color = NeonCyan.copy(alpha = alpha),
                    radius = Random.nextFloat() * 3f + 1f,
                    center = Offset(star.x * size.width, star.y * size.height)
                )
            }
        }
        content()
    }
}
