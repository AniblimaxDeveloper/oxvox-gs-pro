package com.oxvox.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oxvox.app.ui.theme.*

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(VillainBlack).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("OXVOX", color = NeonCyan, fontSize = 48.sp, fontWeight = FontWeight.Bold, letterSpacing = 8.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("COMMAND CENTER", color = TextWhite, fontSize = 16.sp, letterSpacing = 4.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Text("ACCESS GRANTED", color = ComicPurple, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
