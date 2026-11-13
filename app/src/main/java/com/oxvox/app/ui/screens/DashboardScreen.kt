package com.oxvox.app.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.oxvox.app.data.RemoteSessionViewModel
import com.oxvox.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToRemote: () -> Unit,
    onNavigateToParental: () -> Unit,
    onNavigateToMatrix: () -> Unit,
    onNavigateToDevAdmin: () -> Unit,
    onNavigateToCore: () -> Unit,
    onNavigateToVillainMode: () -> Unit,
    viewModel: DeviceViewModel = viewModel(),
    remoteViewModel: RemoteSessionViewModel = viewModel()
) {
    val devices by viewModel.devices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMsg by viewModel.errorMsg.collectAsState()
    val context = LocalContext.current
    var selectedDeviceId by remember { mutableStateOf<String?>(null) }

    val scanLauncher = rememberLauncherForActivityResult(contract = ScanContract()) { result ->
        if (result.contents != null) {
            viewModel.linkDevice(result.contents)
            Toast.makeText(context, "QR Terbaca: ${result.contents}", Toast.LENGTH_SHORT).show()
        }
    }

    HorrorGalaxyBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { GlitchText("OXVOX // SYSTEM COMPROMISED", fontSize = 14) },
                    actions = {
                        TextButton(onClick = { 
                            val options = ScanOptions()
                            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                            options.setPrompt("SCAN OXVOX NODE")
                            options.setBeepEnabled(false)
                            scanLauncher.launch(options)
                        }) { Text("[SCAN]", color = ToxicGreen, fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
                        TextButton(onClick = onNavigateToRemote) { Text("[ASSIST]", color = BloodRed, fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
                        TextButton(onClick = onNavigateToParental) { Text("[FAMILY]", color = ComicPurple, fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
                        TextButton(onClick = onNavigateToMatrix) { Text("[MATRIX]", color = NeonCyan, fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
                        TextButton(onClick = onNavigateToDevAdmin) { Text("[DEV]", color = BoneWhite, fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
                        TextButton(onClick = onNavigateToCore) { Text("[CORE]", color = BloodRed, fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = VoidBlack.copy(alpha = 0.9f))
                )
            },
            floatingActionButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FloatingActionButton(onClick = onNavigateToVillainMode, containerColor = BloodCrimson, contentColor = Color.White) { Text("☠", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                    FloatingActionButton(onClick = { viewModel.fetchDevices() }, containerColor = VillainDarkGray, contentColor = NeonCyan) { Text("↻", fontSize = 20.sp) }
                    FloatingActionButton(onClick = { viewModel.registerCurrentDevice() }, containerColor = ComicPurple, contentColor = Color.White) { Text("+", fontSize = 24.sp) }
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
                
                // HERO ANIME SECTION (Diganti Box Warna Solid Sementara)
                Box(
                    modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(8.dp)).border(2.dp, BloodRed, RoundedCornerShape(8.dp)).background(ComicPurpleDark),
                    contentAlignment = Alignment.Center
                ) {
                    Text("PROTOCOL: ALPHA", color = BloodRed, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = BloodCrimson.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) CircularProgressIndicator(color = BloodRed, modifier = Modifier.align(Alignment.CenterHorizontally))
                if (errorMsg != null) Text("> $errorMsg", color = BloodRed, fontFamily = FontFamily.Monospace, modifier = Modifier.align(Alignment.CenterHorizontally))

                Text("> TARGET NODES", color = BoneWhite, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(devices) { device ->
                        VillainDeviceCard(
                            device = device,
                            onQrClick = { selectedDeviceId = device.id },
                            onDeleteClick = { viewModel.deleteDevice(device.id!!) },
                            onHelpClick = { remoteViewModel.requestRemoteSession(device.id!!) }
                        )
                    }
                }
            }
        }
        
        if (selectedDeviceId != null) {
            QrCodeDialog(deviceId = selectedDeviceId!!, onDismiss = { selectedDeviceId = null })
        }
    }
}

@Composable
fun VillainDeviceCard(device: DeviceData, onQrClick: () -> Unit, onDeleteClick: () -> Unit, onHelpClick: () -> Unit) {
    VillainCard(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(device.device_name, color = BloodRed, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(4.dp))
                Text("ID: ${device.device_model}", color = TextWhite, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                Text("OS: ${device.android_version}", color = ToxicGreen, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
            }
            
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.background(BloodCrimson.copy(alpha = 0.3f), RoundedCornerShape(4.dp)).border(1.dp, BloodCrimson, RoundedCornerShape(4.dp)).clickable { onHelpClick() }.padding(horizontal = 12.dp, vertical = 6.dp)) {
                    Text("HELP", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.background(ComicPurple.copy(alpha = 0.3f), RoundedCornerShape(4.dp)).border(1.dp, ComicPurple, RoundedCornerShape(4.dp)).clickable { onQrClick() }.padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Text("QR", color = Color.White, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    }
                    Box(modifier = Modifier.background(Color.Black, RoundedCornerShape(4.dp)).border(1.dp, BloodRed, RoundedCornerShape(4.dp)).clickable { onDeleteClick() }.padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Text("DEL", color = BloodRed, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}
