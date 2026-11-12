package com.oxvox.app.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oxvox.app.data.DeviceData
import com.oxvox.app.data.DeviceViewModel
import com.oxvox.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DeviceViewModel = viewModel()) {
    val devices by viewModel.devices.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DEVICE CENTER", color = NeonCyan, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VillainBlack)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.registerCurrentDevice() },
                containerColor = ComicPurple
            ) {
                Text("+", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = VillainBlack
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = NeonCyan, modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            if (devices.isEmpty() && !isLoading) {
                Text(
                    text = "No devices registered.\nTap '+' to register this device.",
                    color = TextGray,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp)
                )
            }

            LazyColumn {
                items(devices) { device ->
                    DeviceCard(device)
                }
            }
        }
    }
}

@Composable
fun DeviceCard(device: DeviceData) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = VillainDarkGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(device.device_name, color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(device.device_model, color = TextWhite, fontSize = 14.sp)
            Text(device.android_version, color = TextGray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Status: ${device.connection_status}", color = ComicPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
