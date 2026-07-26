package com.example.smartrecipebox.data

object SupabaseManager {
    private const val SUPABASE_URL = "https://vguosydclnybswfxyavs.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_V88veZtMLb_gURasEt19Uw_z3fvbDYM"

    val supabase = createSupabaseClient(SUPABASE_URL, SUPABASE_KEY) {
        install(Auth)
        install(Postgrest)
    }
}