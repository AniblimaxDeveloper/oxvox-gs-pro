package com.oxvox.app.data

import com.oxvox.app.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import kotlinx.serialization.json.Json

val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.SUPABASE_URL,
    supabaseKey = BuildConfig.SUPABASE_ANON_KEY
) {
    install(Auth)
    install(Postgrest)
    defaultSerializer = Json { ignoreUnknownKeys = true } // Mengabaikan kolom DB yang tidak ada di Kotlin
    
    // Tambahkan Timeout agar tidak loading selamanya
    httpEngine = Android.create {
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
            socketTimeoutMillis = 15000
        }
    }
}
