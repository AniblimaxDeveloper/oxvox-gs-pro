package com.oxvox.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oxvox.app.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VillainModeScreen(onBack: () -> Unit) {
    var hackLines by remember { mutableStateOf(listOf<String>()) }
    val lines = listOf(
        "> INITIALIZING OVERRIDE...",
        "> BYPASSING FIREWALL... [OK]",
        "> INJECTING PAYLOAD... [OK]",
        "> ACCESSING KERNEL... [OK]",
        "> EXTRACTING DATA... [100%]",
        "> SYSTEM FULLY COMPROMISED.",
        "> WELCOME TO THE DARK SIDE."
    )

    LaunchedEffect(Unit) {
        for (line in lines) {
            delay(800)
            hackLines = hackLines + line
        }
    }

    HorrorGalaxyBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { GlitchText("VILLAIN PROTOCOL", color = BloodRed) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                hackLines.forEach { line ->
                    val color = if (line.contains("COMPROMISED") || line.contains("DARK SIDE")) BloodRed else ToxicGreen
                    Text(
                        text = line,
                        color = color,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("RETURN TO COMMAND CENTER", color = Color.White, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}
