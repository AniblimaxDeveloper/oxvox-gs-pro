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
data class RemoteSessionData(
    val id: String? = null,
    val requester_id: String? = null,
    val target_device_id: String,
    val status: String = "Pending",
    val created_at: String? = null
)

class RemoteSessionViewModel : ViewModel() {
    private val _sessions = MutableStateFlow<List<RemoteSessionData>>(emptyList())
    val sessions: StateFlow<List<RemoteSessionData>> = _sessions

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchSessions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = supabase.postgrest["remote_sessions"].select()
                _sessions.value = result.decodeList<RemoteSessionData>()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun requestRemoteSession(deviceId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = supabase.auth.currentUserOrNull()?.id ?: return@launch
                val newSession = RemoteSessionData(
                    requester_id = userId,
                    target_device_id = deviceId,
                    status = "Pending"
                )
                supabase.postgrest["remote_sessions"].insert(newSession)
                fetchSessions()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSessionStatus(sessionId: String, newStatus: String) {
        viewModelScope.launch {
            try {
                supabase.postgrest["remote_sessions"].update({
                    set("status", newStatus)
                }) {
                    filter { eq("id", sessionId) }
                }
                fetchSessions()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
