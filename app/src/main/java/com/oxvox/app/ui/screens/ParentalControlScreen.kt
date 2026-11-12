package com.oxvox.app.ui.screens

import android.content.Intent
import android.provider.Settings
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
import com.oxvox.app.data.FamilyViewModel
import com.oxvox.app.data.DeviceViewModel
import com.oxvox.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentalControlScreen(
    onBack: () -> Unit,
    familyViewModel: FamilyViewModel = viewModel(),
    deviceViewModel: DeviceViewModel = viewModel()
) {
    val context = LocalContext.current
    val family by familyViewModel.family.collectAsState()
    val rules by familyViewModel.rules.collectAsState()
    val devices by deviceViewModel.devices.collectAsState()
    val isLoading by familyViewModel.isLoading.collectAsState()

    var familyNameInput by remember { mutableStateOf("") }
    var selectedDeviceId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        familyViewModel.fetchFamily()
        deviceViewModel.fetchDevices()
    }

    CyberBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("PARENTAL CONTROL", color = NeonCyan, fontFamily = FontFamily.Monospace, fontSize = 16.sp) },
                    navigationIcon = {
                        TextButton(onClick = onBack) { Text("[< BACK]", color = BloodRed, fontFamily = FontFamily.Monospace) }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = VillainBlack.copy(alpha = 0.8f))
                )
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                if (isLoading) CircularProgressIndicator(color = NeonCyan, modifier = Modifier.align(Alignment.CenterHorizontally))

                if (family == null) {
                    // UI BUAT KELUARGA
                    Text("> INITIALIZE FAMILY GROUP", color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = familyNameInput, onValueChange = { familyNameInput = it },
                        label = { Text("Family Name") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonCyan, unfocusedBorderColor = TextGray, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { if (familyNameInput.isNotBlank()) familyViewModel.createFamily(familyNameInput) },
                        colors = ButtonDefaults.buttonColors(containerColor = ComicPurple),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("CREATE FAMILY", color = TextWhite, fontWeight = FontWeight.Bold) }
                } else {
                    // UI KELUARGA SUDAH ADA
                    Text("FAMILY: ${family!!.family_name.uppercase()}", color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Izin Usage Access
                    Button(
                        onClick = { 
                            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("GRANT SCREEN TIME PERMISSION", color = TextWhite, fontWeight = FontWeight.Bold) }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("ACTIVE RULES", color = TextWhite, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(rules) { rule ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = VillainDarkGray),
                                border = BorderStroke(1.dp, ComicPurple)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Device: ${rule.target_device_id.take(8)}...", color = NeonCyan, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                                    Text("Limit: ${rule.screen_time_limit_minutes} min/day", color = TextWhite, fontSize = 12.sp)
                                    Text("Bedtime: ${rule.bedtime_start} - ${rule.bedtime_end}", color = TextGray, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // UI Tambah Rule (Sederhana)
                    Text("ADD NEW RULE (Select Device):", color = TextGray, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    LazyColumn(modifier = Modifier.height(100.dp)) {
                        items(devices) { device ->
                            TextButton(onClick = { 
                                familyViewModel.addRule(device.id!!, 120, "21:00", "06:00") 
                            }) {
                                Text("+ ${device.device_model} (${device.id?.take(6)})", color = NeonCyan, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }
    }
}
