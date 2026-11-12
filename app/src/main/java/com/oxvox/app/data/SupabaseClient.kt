package com.oxvox.app.data

import com.oxvox.app.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.serialization.json.Json

val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.SUPABASE_URL,
    supabaseKey = BuildConfig.SUPABASE_ANON_KEY
) {
    defaultSerializer = Json { 
        ignoreUnknownKeys = true 
        isLenient = true
    }
    install(Auth)
    install(Postgrest)
}
