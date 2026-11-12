package com.oxvox.app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CoreSystemViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun triggerEmergencyProtocol() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = supabase.auth.currentUserOrNull()?.id ?: return@launch
                
                // 1. Hapus semua sesi remote
                supabase.postgrest["remote_sessions"].delete {
                    filter { eq("requester_id", userId) }
                }
                
                // 2. Catat log darurat
                supabase.postgrest["emergency_logs"].insert(
                    mapOf(
                        "user_id" to userId,
                        "action" to "EMERGENCY_PURGE"
                    )
                )
                _message.value = "SUCCESS: All remote sessions terminated & logs secured."
            } catch (e: Exception) {
                _message.value = "ERROR: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessage() { _message.value = null }
}
