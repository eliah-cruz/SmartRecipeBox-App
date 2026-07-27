package com.example.smartrecipebox.data

import com.example.smartrecipebox.data.SupabaseManager
import com.example.smartrecipebox.models.Recipe
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.auth.auth

object RecipeRepository {
    val categoryArray: Array<String> = arrayOf("All", "Quick", "Breakfast", "Lunch", "Dinner", "Vegan", "Healthy", "Soup", "Sweet", "Dessert")

    val tagEmojiMap: Map<String, String> = mapOf(
        "Breakfast" to "🍳",
        "Lunch" to "🍱",
        "Quick" to "⚡",
        "Dinner" to "🍲",
        "Vegan" to "🥗",
        "Healthy" to "🍎",
        "Soup" to "🥣",
        "Sweet" to "🍬",
        "Dessert" to "🍰"
    )

    // fetch recipes
    suspend fun fetchAllRecipes(): List<Recipe> {
        val currentUserId = SupabaseManager.client.auth.currentUserOrNull()?.id ?: return emptyList()
        return SupabaseManager.client.from("recipes").select { filter { eq("user_id", currentUserId) } }.decodeList<Recipe>()
    }

    // insert recipes
    suspend fun addRecipe(recipe: Recipe) {
        SupabaseManager.client.from("recipes").insert(recipe)
    }

    fun filterRecipes(recipes: List<Recipe>, predicate: (Recipe) -> Boolean): List<Recipe> {
        return recipes.filter(predicate)
    }
}