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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

// Data bintang yang disimpan di memori (Tidak di-render ulang terus menerus)
data class StarData(val x: Float, val y: Float, val radius: Float, val isRed: Boolean)

// 1. Teks Glitch yang lebih ringan
@Composable
fun GlitchText(text: String, color: Color = NeonCyan, fontSize: Int = 16) {
    val infiniteTransition = rememberInfiniteTransition(label = "glitch")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = -1.5f, targetValue = 1.5f,
        animationSpec = infiniteRepeatable(tween(100, easing = LinearEasing), RepeatMode.Reverse), label = "x"
    )

    Box(contentAlignment = Alignment.Center) {
        Text(
            text = text, color = BloodRed,
            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = fontSize.sp,
            modifier = Modifier.graphicsLayer { translationX = offsetX }
        )
        Text(
            text = text, color = Color.Cyan,
            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = fontSize.sp,
            modifier = Modifier.graphicsLayer { translationX = -offsetX }
        )
        Text(text = text, color = color, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = fontSize.sp)
    }
}

// 2. Latar Belakang Galaksi yang sudah dioptimalkan
@Composable
fun HorrorGalaxyBackground(content: @Composable () -> Unit) {
    // Simpan data bintang di remember agar tidak dibuat ulang setiap frame
    val stars = remember {
        List(60) {
            StarData(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 2f + 1f,
                isRed = it % 4 == 0
            )
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "galaxy")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Reverse), label = "pulse"
    )

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(VoidBlack, ComicPurpleDark.copy(alpha = 0.3f), VoidBlack)))) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            stars.forEach { star ->
                drawCircle(
                    color = if (star.isRed) BloodCrimson.copy(alpha = pulse) else Color.White.copy(alpha = pulse),
                    radius = star.radius,
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
