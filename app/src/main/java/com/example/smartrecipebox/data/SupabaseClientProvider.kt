package com.example.smartrecipebox.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientProvider {
    private const val SUPABASE_URL = "https://lmikxitvbziqtbinffpj.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxtaWt4aXR2YnppcXRiaW5mZnBqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODUwODkxNzUsImV4cCI6MjEwMDY2NTE3NX0.kFvzsQc6HJ9IYq9cV6yew7lurQfJROa3axL-8kYfNLg"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
    }
}