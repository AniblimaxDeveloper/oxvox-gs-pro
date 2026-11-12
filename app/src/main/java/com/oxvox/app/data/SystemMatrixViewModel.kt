package com.oxvox.app.data

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Environment
import android.os.StatFs
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
data class SystemScanLog(val id: String? = null, val user_id: String? = null, val scan_type: String, val result_summary: String)

data class HealthStats(val ramUsed: Long, val ramTotal: Long, val storageUsed: Long, val storageTotal: Long, val batteryLevel: Int)

class SystemMatrixViewModel : ViewModel() {
    private val _health = MutableStateFlow<HealthStats?>(null)
    val health: StateFlow<HealthStats?> = _health

    private val _appCount = MutableStateFlow(0)
    val appCount: StateFlow<Int> = _appCount

    private val _riskyApps = MutableStateFlow<List<String>>(emptyList())
    val riskyApps: StateFlow<List<String>> = _riskyApps

    private val _logs = MutableStateFlow<List<SystemScanLog>>(emptyList())
    val logs: StateFlow<List<SystemScanLog>> = _logs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun runFullScan(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            
            // 1. Scan Health (RAM, Storage, Battery) di Background Thread
            withContext(Dispatchers.IO) {
                val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val memInfo = ActivityManager.MemoryInfo()
                activityManager.getMemoryInfo(memInfo)
                
                val stat = StatFs(Environment.getDataDirectory().path)
                val blockSize = stat.blockSizeLong
                val totalBlocks = stat.blockCountLong
                val availableBlocks = stat.availableBlocksLong
                
                val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                
                _health.value = HealthStats(
                    ramUsed = memInfo.totalMem - memInfo.availMem,
                    ramTotal = memInfo.totalMem,
                    storageUsed = (totalBlocks - availableBlocks) * blockSize,
                    storageTotal = totalBlocks * blockSize,
                    batteryLevel = level
                )

                // 2. Scan Privacy & Security (Aplikasi)
                val packages = context.packageManager.getInstalledPackages(0)
                _appCount.value = packages.size
                
                val risky = mutableListOf<String>()
                for (pkg in packages) {
                    // Deteksi sederhana: aplikasi yang meminta izin berbahaya
                    if (pkg.requestedPermissions != null) {
                        for (perm in pkg.requestedPermissions) {
                            if (perm.contains("CAMERA") || perm.contains("RECORD_AUDIO") || perm.contains("ACCESS_FINE_LOCATION")) {
                                risky.add(pkg.packageName)
                                break
                            }
                        }
                    }
                }
                _riskyApps.value = risky
            }

            // 3. Kirim Log ke Supabase
            try {
                val userId = supabase.auth.currentUserOrNull()?.id ?: return@launch
                val log = SystemScanLog(
                    user_id = userId,
                    scan_type = "FULL_MATRIX_SCAN",
                    result_summary = "Apps: ${_appCount.value}, Risky: ${_riskyApps.value.size}, RAM Used: ${_health.value?.ramUsed}"
                )
                supabase.postgrest["system_scans"].insert(log)
                fetchLogs()
            } catch (e: Exception) { e.printStackTrace() }

            _isLoading.value = false
        }
    }

    fun fetchLogs() {
        viewModelScope.launch {
            try {
                val result = supabase.postgrest["system_scans"].select()
                _logs.value = result.decodeList<SystemScanLog>()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}
