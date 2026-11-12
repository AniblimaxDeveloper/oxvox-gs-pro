package com.oxvox.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OXVOXTheme {
                DashboardScreen()
            }
        }
    }
}

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VillainBlack)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "OXVOX",
            color = NeonCyan,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 8.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "COMMAND CENTER",
            color = TextWhite,
            fontSize = 16.sp,
            letterSpacing = 4.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "System Online & Secure",
            color = ComicPurple,
            fontSize = 14.sp
        )
    }
}
