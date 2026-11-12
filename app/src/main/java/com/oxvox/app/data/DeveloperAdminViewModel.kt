package com.oxvox.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class AdminLog(val id: String? = null, val user_id: String? = null, val action_type: String, val description: String, val created_at: String? = null)

class DeveloperAdminViewModel : ViewModel() {
    private val _logs = MutableStateFlow<List<AdminLog>>(emptyList())
    val logs: StateFlow<List<AdminLog>> = _logs

    private val _deviceCount = MutableStateFlow(0)
    val deviceCount: StateFlow<Int> = _deviceCount

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchAdminData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val logsResult = supabase.postgrest["admin_logs"].select()
                _logs.value = logsResult.decodeList<AdminLog>()

                val devicesResult = supabase.postgrest["registered_devices"].select()
                _deviceCount.value = devicesResult.decodeList<DeviceData>().size
            } catch (e: Exception) { e.printStackTrace() } 
            finally { _isLoading.value = false }
        }
    }

    fun createAdminLog(action: String, desc: String) {
        viewModelScope.launch {
            try {
                val userId = supabase.auth.currentUserOrNull()?.id ?: return@launch
                val log = AdminLog(user_id = userId, action_type = action, description = desc)
                supabase.postgrest["admin_logs"].insert(log)
                fetchAdminData()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}
