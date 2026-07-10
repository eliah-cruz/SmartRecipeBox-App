package com.example.smartrecipebox.models

import java.io.Serializable

data class Recipe(
    val id: String,
    val title: String,
    val timeMinutes: Int,
    val ingredients: List<String>,
    val tags: Set<String>
) : Serializable

fun Recipe.isQuick(): Boolean = this.timeMinutes <= 20