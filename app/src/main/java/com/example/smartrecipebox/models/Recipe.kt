package com.example.smartrecipebox.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: String? = null,

    @SerialName("user_id")
    val userId: String? = null,

    val title: String,

    @SerialName("time_minutes")
    val timeMinutes: Int,
    
    val ingredients: List<String>,
    val tags: Set<String>
) : java.io.Serializable

fun Recipe.isQuick(): Boolean = this.timeMinutes <= 20