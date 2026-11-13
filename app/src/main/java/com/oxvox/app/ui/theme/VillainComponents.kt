package com.oxvox.app.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// 1. Teks Glitch ala Horror/Cyberpunk
@Composable
fun GlitchText(text: String, color: Color = NeonCyan, fontSize: Int = 16) {
    val infiniteTransition = rememberInfiniteTransition(label = "glitch")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = -2f, targetValue = 2f,
        animationSpec = infiniteRepeatable(tween(50, easing = LinearEasing), RepeatMode.Reverse), label = "x"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(100), RepeatMode.Reverse), label = "alpha"
    )

    Box(contentAlignment = Alignment.Center) {
        // Lapisan bayangan merah
        Text(
            text = text, color = BloodRed.copy(alpha = alpha),
            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = fontSize.sp,
            modifier = Modifier.graphicsLayer { translationX = offsetX; translationY = offsetX }
        )
        // Lapisan bayangan cyan
        Text(
            text = text, color = Color.Cyan.copy(alpha = alpha),
            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = fontSize.sp,
            modifier = Modifier.graphicsLayer { translationX = -offsetX; translationY = -offsetX }
        )
        // Teks Utama
        Text(text = text, color = color, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = fontSize.sp)
    }
}

// 2. Latar Belakang Galaksi Gelap Berdarah
@Composable
fun HorrorGalaxyBackground(content: @Composable () -> Unit) {
    val stars = remember { List(80) { Offset(Random.nextFloat(), Random.nextFloat()) } }
    val infiniteTransition = rememberInfiniteTransition(label = "galaxy")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.1f, targetValue = 0.6f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse), label = "pulse"
    )

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(VoidBlack, ComicPurpleDark.copy(alpha = 0.3f), VoidBlack)))) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            stars.forEachIndexed { index, star ->
                val starColor = if (index % 5 == 0) BloodCrimson else Color.White
                drawCircle(
                    color = starColor.copy(alpha = pulse),
                    radius = Random.nextFloat() * 2.5f + 0.5f,
                    center = Offset(star.x * size.width, star.y * size.height)
                )
            }
        }
        content()
    }
}

// 3. Kartu Bergaya Komik Gelap
@Composable
fun VillainCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(VillainDarkGray)
            .border(1.5.dp, Brush.linearGradient(listOf(BloodRed, ComicPurple, NeonCyan)), RoundedCornerShape(4.dp))
            .padding(12.dp)
    ) {
        content()
    }
}
