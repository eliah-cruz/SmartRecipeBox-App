package com.example.smartrecipebox.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseManager {
    private const val SUPABASE_URL = "https://lmikxitvbziqtbinffpj.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_Ynqcs8jf8xNlJ_9Ghbo2wA_oiyI-BJc"

    val client = createSupabaseClient(SUPABASE_URL, SUPABASE_KEY) {
        install(Auth)
        install(Postgrest)
    }
}