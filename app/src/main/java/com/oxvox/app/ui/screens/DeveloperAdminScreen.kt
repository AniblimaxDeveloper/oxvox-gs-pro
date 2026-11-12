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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oxvox.app.BuildConfig
import com.oxvox.app.data.DeveloperAdminViewModel
import com.oxvox.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperAdminScreen(
    onBack: () -> Unit,
    viewModel: DeveloperAdminViewModel = viewModel()
) {
    val logs by viewModel.logs.collectAsState()
    val deviceCount by viewModel.deviceCount.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("ADMIN", "AUTH", "AUTOMATION", "REPORTS", "DEV-TOOLS")

    LaunchedEffect(Unit) { viewModel.fetchAdminData() }

    CyberBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("DEV-ADMIN MATRIX", color = NeonCyan, fontFamily = FontFamily.Monospace, fontSize = 16.sp) },
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
                            Text(title, fontFamily = FontFamily.Monospace, fontSize = 10.sp, modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (selectedTab) {
                    0 -> AdminTab(deviceCount, logs, viewModel)
                    1 -> AuthTab()
                    2 -> AutomationTab(viewModel)
                    3 -> ReportsTab(logs)
                    4 -> DevToolsTab()
                }
            }
        }
    }
}

@Composable
fun AdminTab(deviceCount: Int, logs: List<com.oxvox.app.data.AdminLog>, viewModel: DeveloperAdminViewModel) {
    Column {
        Text("> SUPER ADMIN DASHBOARD", color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        Text("Registered Devices: $deviceCount", color = TextWhite, fontFamily = FontFamily.Monospace)
        Text("System Status: ONLINE", color = NeonCyan, fontFamily = FontFamily.Monospace)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.createAdminLog("MANUAL_CHECK", "Admin triggered manual system check") },
            colors = ButtonDefaults.buttonColors(containerColor = ComicPurple),
            modifier = Modifier.fillMaxWidth()
        ) { Text("TRIGGER AUDIT LOG", color = TextWhite, fontFamily = FontFamily.Monospace) }
    }
}

@Composable
fun AuthTab() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("> ACCOUNT SECURITY", color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        Text("- Email Authentication: ACTIVE", color = TextWhite, fontFamily = FontFamily.Monospace)
        Text("- JWT Token Rotation: ACTIVE", color = TextWhite, fontFamily = FontFamily.Monospace)
        Text("- Brute Force Protection: ACTIVE", color = TextWhite, fontFamily = FontFamily.Monospace)
        Text("- Biometric 2FA: NOT CONFIGURED", color = BloodRed, fontFamily = FontFamily.Monospace)
    }
}

@Composable
fun AutomationTab(viewModel: DeveloperAdminViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("> AUTOMATION & EVENTS", color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        Text("Schedule automated system scans.", color = TextGray, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
        Button(
            onClick = { viewModel.createAdminLog("AUTOMATION", "Enabled Daily Deep Scan at 00:00") },
            colors = ButtonDefaults.buttonColors(containerColor = VillainDarkGray),
            border = BorderStroke(1.dp, NeonCyan),
            modifier = Modifier.fillMaxWidth()
        ) { Text("ENABLE DAILY SCAN (00:00)", color = NeonCyan, fontFamily = FontFamily.Monospace) }
    }
}

@Composable
fun ReportsTab(logs: List<com.oxvox.app.data.AdminLog>) {
    Column {
        Text("> SYSTEM AUDIT LOGS", color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            items(logs) { log ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = VillainDarkGray),
                    border = BorderStroke(1.dp, ComicPurple.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("[${log.action_type}]", color = ComicPurple, fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(log.description, color = TextWhite, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DevToolsTab() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("> DEVELOPER TOOLKIT", color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        Text("APK Version: ${BuildConfig.VERSION_NAME}", color = TextWhite, fontFamily = FontFamily.Monospace)
        Text("Build Type: Debug", color = TextWhite, fontFamily = FontFamily.Monospace)
        Text("Package: ${BuildConfig.APPLICATION_ID}", color = TextWhite, fontFamily = FontFamily.Monospace)
        Text("Backend: Supabase (PostgreSQL)", color = TextWhite, fontFamily = FontFamily.Monospace)
        Text("Environment: Termux / GitHub Actions", color = TextGray, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
    }
}
