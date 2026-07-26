package com.example.smartrecipebox.data

import com.example.smartrecipebox.models.NewRecipeRequest
import com.example.smartrecipebox.models.Recipe
import io.github.jan.supabase.postgrest.postgrest

object RecipeRepository {
    val categoryArray: Array<String> = arrayOf("All", "Quick", "Breakfast", "Dinner", "Dessert")

    val tagEmojiMap: Map<String, String> = mapOf(
        "Breakfast" to "🍳",
        "Lunch" to "🍱",
        "Quick" to "⚡",
        "Dinner" to "🍲",
        "Vegan" to "🥗",
        "Healthy" to "🍎",
        "Soup" to "🥣",
        "Dessert" to "🍰"
    )

    // fetch recipes
    suspend fun getAllRecipes(): List<Recipe> {
        return SupabaseClientProvider.client.postgrest["recipes"]
            .select()
            .decodeList<Recipe>()
    }

    // insert recipes
    suspend fun addRecipe(newRecipe: NewRecipeRequest): Recipe {
        return SupabaseClientProvider.client.postgrest["recipes"]
            .insert(newRecipe) {
                select()
            }
            .decodeSingle<Recipe>()
    }

    fun filterRecipes(recipes: List<Recipe>, predicate: (Recipe) -> Boolean): List<Recipe> {
        return recipes.filter(predicate)
    }
}