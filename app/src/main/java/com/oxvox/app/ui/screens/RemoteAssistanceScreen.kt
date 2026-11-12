package com.oxvox.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.oxvox.app.data.RemoteSessionData
import com.oxvox.app.data.RemoteSessionViewModel
import com.oxvox.app.data.SessionMessage
import com.oxvox.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoteAssistanceScreen(
    onBack: () -> Unit,
    viewModel: RemoteSessionViewModel = viewModel()
) {
    val sessions by viewModel.sessions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var activeChatSession by remember { mutableStateOf<RemoteSessionData?>(null) }

    CyberBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (activeChatSession == null) "X-ASSIST // SESSIONS" else "SECURE CHAT", color = NeonCyan, fontFamily = FontFamily.Monospace, fontSize = 16.sp) },
                    navigationIcon = {
                        TextButton(onClick = { if (activeChatSession == null) onBack() else activeChatSession = null }) {
                            Text("[< BACK]", color = BloodRed, fontFamily = FontFamily.Monospace)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = VillainBlack.copy(alpha = 0.8f))
                )
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                if (activeChatSession == null) {
                    // TAMPILAN DAFTAR SESI
                    Column {
                        if (isLoading) CircularProgressIndicator(color = NeonCyan, modifier = Modifier.align(Alignment.CenterHorizontally))
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
                                    onReject = { viewModel.updateSessionStatus(session.id!!, "Terminated") },
                                    onOpenChat = { activeChatSession = session }
                                )
                            }
                        }
                    }
                } else {
                    // TAMPILAN CHAT
                    ChatScreen(
                        sessionId = activeChatSession!!.id!!,
                        viewModel = viewModel,
                        onEndSession = {
                            viewModel.updateSessionStatus(activeChatSession!!.id!!, "Terminated")
                            activeChatSession = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SessionCard(session: RemoteSessionData, onAccept: () -> Unit, onReject: () -> Unit, onOpenChat: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = VillainDarkGray.copy(alpha = 0.9f)),
        border = BorderStroke(1.dp, if (session.status == "Active") NeonCyan else ComicPurple),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Target ID: ${session.target_device_id.take(8)}...", color = TextWhite, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
            Text("Status: ${session.status}", color = if(session.status == "Pending") ComicPurple else NeonCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            
            Spacer(modifier = Modifier.height(12.dp))
            if (session.status == "Pending") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onAccept, colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)) { Text("ACCEPT", color = VillainBlack) }
                    Button(onClick = onReject, colors = ButtonDefaults.buttonColors(containerColor = BloodRed)) { Text("REJECT", color = TextWhite) }
                }
            } else if (session.status == "Active") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onOpenChat, colors = ButtonDefaults.buttonColors(containerColor = ComicPurple), modifier = Modifier.weight(1f)) {
                        Text("OPEN CHAT", color = TextWhite, fontWeight = FontWeight.Bold)
                    }
                    Button(onClick = onReject, colors = ButtonDefaults.buttonColors(containerColor = BloodRed)) {
                        Text("STOP", color = TextWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatScreen(sessionId: String, viewModel: RemoteSessionViewModel, onEndSession: () -> Unit) {
    val messages by viewModel.messages.collectAsState()
    var inputText by remember { mutableStateOf("") }

    LaunchedEffect(sessionId) {
        viewModel.fetchMessages(sessionId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { msg ->
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    contentAlignment = if (msg.sender_id == viewModel.hashCode().toString()) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = VillainDarkGray),
                        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.3f))
                    ) {
                        Text(text = msg.message, color = NeonCyan, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(12.dp))
                    }
                }
            }
        }
        
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("> Type message...", color = TextGray, fontFamily = FontFamily.Monospace) },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonCyan, unfocusedBorderColor = TextGray, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (inputText.isNotBlank()) {
                    viewModel.sendMessage(sessionId, inputText)
                    inputText = ""
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)) {
                Text("SEND", color = VillainBlack, fontWeight = FontWeight.Bold)
            }
        }
    }
}
