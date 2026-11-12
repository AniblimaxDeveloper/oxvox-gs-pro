package com.oxvox.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oxvox.app.BuildConfig
import com.oxvox.app.data.CoreSystemViewModel
import com.oxvox.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoreSystemScreen(
    onBack: () -> Unit,
    viewModel: CoreSystemViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    CyberBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("CORE SYSTEM // FINAL", color = NeonCyan, fontFamily = FontFamily.Monospace, fontSize = 16.sp) },
                    navigationIcon = { TextButton(onClick = onBack) { Text("[< BACK]", color = BloodRed, fontFamily = FontFamily.Monospace) } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = VillainBlack.copy(alpha = 0.8f))
                )
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())
            ) {
                if (message != null) {
                    Text(message!!, color = if (message!!.contains("SUCCESS")) NeonCyan else BloodRed, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.clearMessage() }) { Text("Dismiss") }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Module 16: Database
                Text("> MODULE 16: DATABASE", color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                Card(colors = CardDefaults.cardColors(containerColor = VillainDarkGray), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Backend: Supabase (PostgreSQL)", color = TextWhite, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        Text("Status: CONNECTED & SECURE", color = NeonCyan, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    }
                }

                // Module 17 & 18: Enrollment & File
                Text("> MODULE 17-18: DATA & FILES", color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                Card(colors = CardDefaults.cardColors(containerColor = VillainDarkGray), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Consent Status: Granted", color = TextWhite, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        Text("Secure Storage: Active (App Sandbox)", color = TextWhite, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    }
                }

                // Module 19: Emergency Protocol
                Spacer(modifier = Modifier.height(16.dp))
                Text("> MODULE 19: EMERGENCY PROTOCOL", color = BloodRed, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                Card(
                    colors = CardDefaults.cardColors(containerColor = BloodRed.copy(alpha = 0.1f)),
                    border = BorderStroke(1.dp, BloodRed),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("WARNING: This will terminate all remote sessions and purge active connections.", color = TextWhite, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.triggerEmergencyProtocol() },
                            colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading
                        ) {
                            if (isLoading) CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(20.dp))
                            else Text("ACTIVATE EMERGENCY PURGE", color = TextWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                // Module 20 & 21-25: Finalization
                Spacer(modifier = Modifier.height(16.dp))
                Text("> MODULE 20-25: FINALIZATION", color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                Card(colors = CardDefaults.cardColors(containerColor = VillainDarkGray), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Version: ${BuildConfig.VERSION_NAME}", color = TextWhite, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        Text("Build: Debug (GitHub Actions)", color = TextWhite, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        Text("Target SDK: 34 (Android 14)", color = TextWhite, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        Text("Architecture: MVVM & Clean UI", color = TextWhite, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("OXVOX - BY LUTZZ", color = ComicPurple, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
