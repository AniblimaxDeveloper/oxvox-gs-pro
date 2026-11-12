package com.oxvox.app.data

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class DeviceData(
    val id: String? = null,
    val user_id: String? = null,
    val device_name: String,
    val device_model: String,
    val android_version: String,
    val connection_status: String = "Online"
)

class DeviceViewModel : ViewModel() {
    private val _devices = MutableStateFlow<List<DeviceData>>(emptyList())
    val devices: StateFlow<List<DeviceData>> = _devices

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg

    init {
        fetchDevices()
    }

    fun fetchDevices() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val result = supabase.postgrest["registered_devices"].select()
                _devices.value = result.decodeList<DeviceData>()
            } catch (e: Exception) {
                _errorMsg.value = "Gagal memuat: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun registerCurrentDevice() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val userId = supabase.auth.currentUserOrNull()?.id
                if (userId == null) {
                    _errorMsg.value = "User tidak ditemukan, silakan login ulang."
                    return@launch
                }
                
                val newDevice = DeviceData(
                    user_id = userId,
                    device_name = "OXVOX Device",
                    device_model = "${Build.MANUFACTURER} ${Build.MODEL}",
                    android_version = "Android ${Build.VERSION.RELEASE}"
                )
                
                supabase.postgrest["registered_devices"].insert(newDevice)
                fetchDevices() // Refresh list
            } catch (e: Exception) {
                _errorMsg.value = "Gagal simpan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
