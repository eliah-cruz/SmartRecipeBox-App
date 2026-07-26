package com.example.smartrecipebox.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: String = "",
    val title: String,
    val timeMinutes: Int,
    val ingredients: List<String>,
    val tags: Set<String>
) : java.io.Serializable

fun Recipe.isQuick(): Boolean = this.timeMinutes <= 20

@Serializable
data class NewRecipeRequest(
    val title: String,
    val timeMinutes: Int,
    val ingredients: List<String>,
    val tags: Set<String>
)