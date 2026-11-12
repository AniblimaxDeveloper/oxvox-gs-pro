package com.oxvox.app.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.oxvox.app.data.DeviceData
import com.oxvox.app.data.DeviceViewModel
import com.oxvox.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DeviceViewModel = viewModel()) {
    val devices by viewModel.devices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMsg by viewModel.errorMsg.collectAsState()
    val context = LocalContext.current
    
    var selectedDeviceId by remember { mutableStateOf<String?>(null) }

    // Launcher untuk Scanner QR
    val scanLauncher = rememberLauncherForActivityResult(contract = ScanContract()) { result ->
        if (result.contents != null) {
            viewModel.linkDevice(result.contents)
            Toast.makeText(context, "QR Terbaca: ${result.contents}", Toast.LENGTH_SHORT).show()
        }
    }

    CyberBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("OXVOX // COMMAND", color = NeonCyan, fontFamily = FontFamily.Monospace, fontSize = 16.sp) },
                    actions = {
                        // Tombol Scan QR di Top Bar
                        TextButton(onClick = {
                            val options = ScanOptions()
                            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                            options.setPrompt("SCAN OXVOX NODE")
                            options.setBeepEnabled(false)
                            options.setOrientationLocked(true)
                            scanLauncher.launch(options)
                        }) {
                            Text("[SCAN QR]", color = ComicPurple, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = VillainBlack.copy(alpha = 0.8f))
                )
            },
            floatingActionButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Tombol Refresh
                    FloatingActionButton(
                        onClick = { viewModel.fetchDevices() },
                        containerColor = VillainDarkGray,
                        contentColor = NeonCyan
                    ) {
                        Text("↻", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    // Tombol Tambah
                    FloatingActionButton(
                        onClick = { viewModel.registerCurrentDevice() },
                        containerColor = ComicPurple,
                        contentColor = TextWhite
                    ) {
                        Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("SYSTEM ONLINE", color = NeonCyan, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        Text("ENCRYPTION: ACTIVE", color = ComicPurple, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        Text("THREAT LEVEL: ZERO", color = BloodRed, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    }
                    RadarWidget()
                }

                HorizontalDivider(color = NeonCyan.copy(alpha = 0.3f), modifier = Modifier.padding(bottom = 16.dp))

                if (isLoading) {
                    CircularProgressIndicator(color = NeonCyan, modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                if (errorMsg != null) {
                    Text(
                        text = "> $errorMsg",
                        color = BloodRed,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
                    )
                }

                Text("ACTIVE NODES", color = TextWhite, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                if (devices.isEmpty() && !isLoading && errorMsg == null) {
                    Text(
                        text = "> No devices connected.\n> Tap '+' to initialize node.",
                        color = TextGray,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                }

                LazyColumn {
                    items(devices) { device ->
                        CyberDeviceCard(
                            device = device,
                            onQrClick = { selectedDeviceId = device.id },
                            onDeleteClick = { viewModel.deleteDevice(device.id!!) }
                        )
                    }
                }
            }
        }
        
        if (selectedDeviceId != null) {
            QrCodeDialog(
                deviceId = selectedDeviceId!!,
                onDismiss = { selectedDeviceId = null }
            )
        }
    }
}

@Composable
fun CyberDeviceCard(device: DeviceData, onQrClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = VillainDarkGray.copy(alpha = 0.9f)),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(device.device_name, color = NeonCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Model: ${device.device_model}", color = TextWhite, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                Text("OS: ${device.android_version}", color = TextGray, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                Text("ID: ${device.id?.take(8)}...", color = TextGray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }
            
            // Tombol Aksi (QR & Hapus)
            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .background(ComicPurple.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .border(1.dp, ComicPurple, RoundedCornerShape(4.dp))
                        .clickable { onQrClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("QR", color = NeonCyan, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(
                    modifier = Modifier
                        .background(BloodRed.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .border(1.dp, BloodRed, RoundedCornerShape(4.dp))
                        .clickable { onDeleteClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("DEL", color = BloodRed, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}
