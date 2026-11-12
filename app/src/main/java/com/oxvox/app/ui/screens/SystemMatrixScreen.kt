package com.oxvox.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oxvox.app.data.SystemMatrixViewModel
import com.oxvox.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemMatrixScreen(
    onBack: () -> Unit,
    viewModel: SystemMatrixViewModel = viewModel()
) {
    val context = LocalContext.current
    val health by viewModel.health.collectAsState()
    val appCount by viewModel.appCount.collectAsState()
    val riskyApps by viewModel.riskyApps.collectAsState()
    val logs by viewModel.logs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("SECURITY", "PRIVACY", "HEALTH", "LOGS")

    LaunchedEffect(Unit) { viewModel.fetchLogs() }

    CyberBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("SYSTEM MATRIX", color = NeonCyan, fontFamily = FontFamily.Monospace, fontSize = 16.sp) },
                    navigationIcon = { TextButton(onClick = onBack) { Text("[< BACK]", color = BloodRed, fontFamily = FontFamily.Monospace) } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = VillainBlack.copy(alpha = 0.8f))
                )
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = VillainDarkGray,
                    contentColor = NeonCyan
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(selected = selectedTab == index, onClick = { selectedTab = index }) {
                            Text(title, fontFamily = FontFamily.Monospace, fontSize = 12.sp, modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.runFullScan(context) },
                    colors = ButtonDefaults.buttonColors(containerColor = ComicPurple),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(20.dp))
                    else Text("RUN DEEP SCAN", color = TextWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (selectedTab) {
                    0 -> SecurityTab(appCount, riskyApps)
                    1 -> PrivacyTab(riskyApps)
                    2 -> HealthTab(health)
                    3 -> LogsTab(logs)
                }
            }
        }
    }
}

@Composable
fun SecurityTab(appCount: Int, riskyApps: List<String>) {
    Column {
        Text("> ANALYZING APPS...", color = NeonCyan, fontFamily = FontFamily.Monospace)
        Text("Total Installed: $appCount", color = TextWhite, fontFamily = FontFamily.Monospace)
        Text("Threats Found: ${riskyApps.size}", color = if(riskyApps.isEmpty()) NeonCyan else BloodRed, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("> System is ${if(riskyApps.isEmpty()) "SECURE" else "VULNERABLE"}", color = if(riskyApps.isEmpty()) NeonCyan else BloodRed, fontFamily = FontFamily.Monospace, fontSize = 18.sp)
    }
}

@Composable
fun PrivacyTab(riskyApps: List<String>) {
    Column {
        Text("> PRIVACY AUDIT", color = NeonCyan, fontFamily = FontFamily.Monospace)
        Text("Apps requesting sensitive data (Camera/Mic/Location):", color = TextGray, fontSize = 12.sp)
        LazyColumn {
            items(riskyApps.take(10)) { app ->
                Text("- $app", color = BloodRed, fontFamily = FontFamily.Monospace, fontSize = 12.sp, modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}

@Composable
fun HealthTab(health: com.oxvox.app.data.HealthStats?) {
    if (health == null) {
        Text("> Run scan to view health data.", color = TextGray, fontFamily = FontFamily.Monospace)
        return
    }
    val ramPercent = (health.ramUsed.toFloat() / health.ramTotal.toFloat() * 100).toInt()
    val storagePercent = (health.storageUsed.toFloat() / health.storageTotal.toFloat() * 100).toInt()

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("> DEVICE HEALTH", color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        Text("Battery Level: ${health.batteryLevel}%", color = TextWhite, fontFamily = FontFamily.Monospace)
        Text("RAM Usage: $ramPercent% (${health.ramUsed / (1024*1024)}MB / ${health.ramTotal / (1024*1024)}MB)", color = TextWhite, fontFamily = FontFamily.Monospace)
        Text("Storage: $storagePercent% Used", color = TextWhite, fontFamily = FontFamily.Monospace)
    }
}

@Composable
fun LogsTab(logs: List<com.oxvox.app.data.SystemScanLog>) {
    LazyColumn {
        items(logs) { log ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = VillainDarkGray),
                border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("TYPE: ${log.scan_type}", color = NeonCyan, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    Text(log.result_summary, color = TextWhite, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}
