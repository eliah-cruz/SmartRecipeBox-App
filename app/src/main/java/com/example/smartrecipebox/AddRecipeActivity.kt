package com.example.smartrecipebox

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartrecipebox.data.RecipeRepository
import com.example.smartrecipebox.data.SupabaseManager
import com.example.smartrecipebox.models.Recipe
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

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
        val btnSave = findViewById<Button>(R.id.btn_save_recipe)
        val btnBack = findViewById<Button>(R.id.btn_back)

        val tagCheckboxes = mapOf(
            findViewById<CheckBox>(R.id.cb_quick) to "Quick",
            findViewById<CheckBox>(R.id.cb_breakfast) to "Breakfast",
            findViewById<CheckBox>(R.id.cb_lunch) to "Lunch",
            findViewById<CheckBox>(R.id.cb_dinner) to "Dinner",
            findViewById<CheckBox>(R.id.cb_vegan) to "Vegan",
            findViewById<CheckBox>(R.id.cb_healthy) to "Healthy",
            findViewById<CheckBox>(R.id.cb_soup) to "Soup",
            findViewById<CheckBox>(R.id.cb_sweet) to "Sweet",
            findViewById<CheckBox>(R.id.cb_dessert) to "Dessert"
        )
        val cbOther = findViewById<CheckBox>(R.id.cb_other)
        val etOtherTags = findViewById<EditText>(R.id.et_other_tags)

        cbOther.setOnCheckedChangeListener { _, isChecked ->
            etOtherTags.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        // Standard Back button finish
        btnBack.setOnClickListener {
            finish()
        }

        // Standard Save button action
        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val timeText = etTime.text.toString().trim()
            val ingredientsText = etIngredients.text.toString().trim()

            // 1. Check if every single box is filled out
            var isFormValid = true

            if (name.isEmpty()) {
                etName.error = "Recipe Name is required"
                isFormValid = false
            }

            if (timeText.isEmpty()) {
                etTime.error = "Cook Time is required"
                isFormValid = false
            }

            if (ingredientsText.isEmpty()) {
                etIngredients.error = "Ingredients are required"
                isFormValid = false
            }

            val selectedTags = mutableSetOf<String>()
            tagCheckboxes.forEach { (cb, tag) ->
                if (cb.isChecked) {
                    selectedTags.add(tag)
                }
            }

            if (cbOther.isChecked) {
                val customText = etOtherTags.text.toString().trim()
                if (customText.isNotEmpty()) {
                    val customTags = customText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    selectedTags.addAll(customTags)
                }
            }

            if (selectedTags.isEmpty()) {
                Toast.makeText(this, "Please select at least one tag.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isFormValid) {
                return@setOnClickListener
            }

            // 3. If everything is filled out perfectly, save the recipe!
            val timeMinutes = timeText.toIntOrNull() ?: 15

            // Split ingredients into a List<String>
            val ingredientsList = ingredientsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }

            // disabe double tap of button
            btnSave.isEnabled = false

            lifecycleScope.launch {
                try {
                    val currentUserId = SupabaseManager.client.auth.currentUserOrNull()?.id

                    if (currentUserId == null) {
                        Toast.makeText(this@AddRecipeActivity, "Please log in first!", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val recipe = Recipe(
                        id = null,
                        userId = currentUserId,
                        title = name,
                        timeMinutes = timeMinutes,
                        ingredients = ingredientsList,
                        tags = selectedTags
                    )

                    RecipeRepository.addRecipe(recipe)

                    Toast.makeText(this@AddRecipeActivity, "Recipe Saved Successfully!", Toast.LENGTH_SHORT).show()
                    finish() // Close activity and return to list
                } catch (e: Exception) {
                    btnSave.isEnabled = true
                    Toast.makeText(this@AddRecipeActivity, "Failed to save recipe: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}