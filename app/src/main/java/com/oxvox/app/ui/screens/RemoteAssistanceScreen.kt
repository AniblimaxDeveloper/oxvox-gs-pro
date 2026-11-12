package com.oxvox.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oxvox.app.data.RemoteSessionData
import com.oxvox.app.data.RemoteSessionViewModel
import com.oxvox.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoteAssistanceScreen(
    onBack: () -> Unit,
    viewModel: RemoteSessionViewModel = viewModel()
) {
    val sessions by viewModel.sessions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    CyberBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("X-ASSIST // SESSIONS", color = NeonCyan, fontFamily = FontFamily.Monospace, fontSize = 16.sp) },
                    navigationIcon = {
                        TextButton(onClick = onBack) {
                            Text("[< BACK]", color = BloodRed, fontFamily = FontFamily.Monospace)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = VillainBlack.copy(alpha = 0.8f))
                )
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                if (isLoading) {
                    CircularProgressIndicator(color = NeonCyan, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                
                Text("ACTIVE SESSIONS", color = TextWhite, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                if (sessions.isEmpty() && !isLoading) {
                    Text("> No active remote sessions.", color = TextGray, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                }

                LazyColumn {
                    items(sessions) { session ->
                        SessionCard(
                            session = session,
                            onAccept = { viewModel.updateSessionStatus(session.id!!, "Active") },
                            onReject = { viewModel.updateSessionStatus(session.id!!, "Terminated") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SessionCard(session: RemoteSessionData, onAccept: () -> Unit, onReject: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = VillainDarkGray.copy(alpha = 0.9f)),
        border = BorderStroke(1.dp, if (session.status == "Active") NeonCyan else ComicPurple),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Target ID: ${session.target_device_id.take(8)}...", color = TextWhite, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
            Text("Status: ${session.status}", color = if(session.status == "Pending") ComicPurple else NeonCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            
            if (session.status == "Pending") {
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onAccept, colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)) {
                        Text("ACCEPT", color = VillainBlack, fontWeight = FontWeight.Bold)
                    }
                    Button(onClick = onReject, colors = ButtonDefaults.buttonColors(containerColor = BloodRed)) {
                        Text("REJECT", color = TextWhite, fontWeight = FontWeight.Bold)
                    }
                }
            } else if (session.status == "Active") {
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onReject, colors = ButtonDefaults.buttonColors(containerColor = BloodRed), modifier = Modifier.fillMaxWidth()) {
                    Text("EMERGENCY STOP", color = TextWhite, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
