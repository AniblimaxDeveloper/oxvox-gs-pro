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
data class FamilyGroup(val id: String? = null, val owner_id: String? = null, val family_name: String)

@Serializable
data class ParentalRule(
    val id: String? = null,
    val family_id: String,
    val target_device_id: String,
    val screen_time_limit_minutes: Int = 120,
    val bedtime_start: String = "21:00",
    val bedtime_end: String = "06:00",
    val is_active: Boolean = true
)

class FamilyViewModel : ViewModel() {
    private val _family = MutableStateFlow<FamilyGroup?>(null)
    val family: StateFlow<FamilyGroup?> = _family

    private val _rules = MutableStateFlow<List<ParentalRule>>(emptyList())
    val rules: StateFlow<List<ParentalRule>> = _rules

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchFamily() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = supabase.auth.currentUserOrNull()?.id ?: return@launch
                val result = supabase.postgrest["family_groups"].select { filter { eq("owner_id", userId) } }
                val list = result.decodeList<FamilyGroup>()
                _family.value = list.firstOrNull()
                if (_family.value != null) fetchRules(_family.value!!.id!!)
            } catch (e: Exception) { e.printStackTrace() } 
            finally { _isLoading.value = false }
        }
    }

    fun createFamily(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = supabase.auth.currentUserOrNull()?.id ?: return@launch
                val newFamily = FamilyGroup(owner_id = userId, family_name = name)
                supabase.postgrest["family_groups"].insert(newFamily)
                fetchFamily()
            } catch (e: Exception) { e.printStackTrace() } 
            finally { _isLoading.value = false }
        }
    }

    fun fetchRules(familyId: String) {
        viewModelScope.launch {
            try {
                val result = supabase.postgrest["parental_rules"].select { filter { eq("family_id", familyId) } }
                _rules.value = result.decodeList<ParentalRule>()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun addRule(deviceId: String, limitMinutes: Int, bedStart: String, bedEnd: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val familyId = _family.value?.id ?: return@launch
                val newRule = ParentalRule(family_id = familyId, target_device_id = deviceId, screen_time_limit_minutes = limitMinutes, bedtime_start = bedStart, bedtime_end = bedEnd)
                supabase.postgrest["parental_rules"].insert(newRule)
                fetchRules(familyId)
            } catch (e: Exception) { e.printStackTrace() } 
            finally { _isLoading.value = false }
        }
    }
}
