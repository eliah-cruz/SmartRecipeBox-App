package com.example.smartrecipebox.data

import com.example.smartrecipebox.models.Recipe

object RecipeRepository {
    val categoryArray: Array<String> = arrayOf("All", "Quick", "Breakfast", "Dinner", "Dessert")

    val tagEmojiMap: Map<String, String> = mapOf(
        "Breakfast" to "🍳",
        "Quick" to "⚡",
        "Dinner" to "🍲",
        "Vegan" to "🥗",
        "Healthy" to "🍎",
        "Soup" to "🥣",
        "Sweet" to "🍬",
        "Dessert" to "🍰"
    )

    private val recipesList = mutableListOf<Recipe>()

    init {
        recipesList.addAll(
            listOf(
                Recipe(
                    id = "1",
                    title = "Classic Pancakes",
                    timeMinutes = 15,
                    ingredients = listOf("1 cup Flour", "1 Egg", "1 cup Milk", "2 tbsp Sugar", "1 pinch Salt"),
                    tags = setOf("Breakfast", "Sweet", "Quick")
                ),
                Recipe(
                    id = "2",
                    title = "Garlic Noodles",
                    timeMinutes = 25,
                    ingredients = listOf("Noodles", "Chicken breast", "Garlic", "Soy Sauce", "Green onion"),
                    tags = setOf("Dinner", "Quick")
                ),
                Recipe(
                    id = "3",
                    title = "Simple Summer Salad",
                    timeMinutes = 10,
                    ingredients = listOf("Lettuce", "Cherry Tomatoes", "Cucumber", "Olive Oil", "Lemon Juice"),
                    tags = setOf("Vegan", "Quick", "Healthy")
                ),
                Recipe(
                    id = "4",
                    title = "Baked Banana Oats",
                    timeMinutes = 35,
                    ingredients = listOf("Oats", "1 Ripe Banana", "Almond Milk", "Honey", "Cinnamon"),
                    tags = setOf("Breakfast", "Healthy")
                ),
                Recipe(
                    id = "5",
                    title = "Tomato Basil Soup",
                    timeMinutes = 20,
                    ingredients = listOf("Tomato Paste", "Heavy Cream", "Garlic", "Vegetable Broth", "Basil"),
                    tags = setOf("Dinner", "Soup", "Quick")
                ),
                Recipe(
                    id = "6",
                    title = "Chocolate Mug Cake",
                    timeMinutes = 5,
                    ingredients = listOf("4 tbsp Flour", "2 tbsp Sugar", "1 tbsp Cocoa Powder", "3 tbsp Milk", "1 tbsp Butter"),
                    tags = setOf("Dessert", "Sweet", "Quick")
                )
            )
        )
    }

    fun getAllRecipes(): List<Recipe> = recipesList

    fun addRecipe(recipe: Recipe) {
        recipesList.add(recipe)
    }

    fun filterRecipes(predicate: (Recipe) -> Boolean): List<Recipe> {
        return recipesList.filter(predicate)
    }
}