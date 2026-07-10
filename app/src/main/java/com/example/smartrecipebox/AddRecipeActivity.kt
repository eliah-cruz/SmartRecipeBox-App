package com.example.smartrecipebox

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartrecipebox.data.RecipeRepository
import com.example.smartrecipebox.models.Recipe

class AddRecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply saved theme on startup
        ThemeHelper.applyTheme(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        val tvFormTitle = findViewById<TextView>(R.id.tv_form_header)
        tvFormTitle?.applyTextGradient(R.color.primary, R.color.accent)

        val etName = findViewById<EditText>(R.id.et_recipe_name)
        val etTime = findViewById<EditText>(R.id.et_cook_time)
        val etIngredients = findViewById<EditText>(R.id.et_ingredients)
        val etTags = findViewById<EditText>(R.id.et_tags)
        val btnSave = findViewById<Button>(R.id.btn_save_recipe)
        val btnBack = findViewById<Button>(R.id.btn_back)

        // Standard Back button finish
        btnBack.setOnClickListener {
            finish()
        }

        // Standard Save button action
        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val timeText = etTime.text.toString().trim()
            val ingredientsText = etIngredients.text.toString().trim()
            val tagsText = etTags.text.toString().trim()

            if (name.isEmpty() || timeText.isEmpty() || ingredientsText.isEmpty()) {
                Toast.makeText(this, "Please fill in the required fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val timeMinutes = timeText.toIntOrNull() ?: 15

            // Split ingredients into a List<String>
            val ingredientsList = ingredientsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }

            // Split tags into a Set<String> (matching your Recipe model)
            val tagsSet = tagsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()

            // Create and save new recipe
            val newRecipe = Recipe(
                id = System.currentTimeMillis().toString(),
                title = name,
                timeMinutes = timeMinutes,
                ingredients = ingredientsList,
                tags = tagsSet
            )

            RecipeRepository.addRecipe(newRecipe)

            Toast.makeText(this, "Recipe Saved Successfully!", Toast.LENGTH_SHORT).show()
            finish() // Close activity and return to list
        }
    }
}