package com.example.smartrecipebox.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.smartrecipebox.R
import com.example.smartrecipebox.applyTextGradient
import com.example.smartrecipebox.data.RecipeRepository
import com.example.smartrecipebox.models.Recipe
import kotlinx.coroutines.launch

class RecipeDetailFragment : Fragment() {

    private var targetRecipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        targetRecipe = arguments?.getSerializable(ARG_RECIPE) as? Recipe
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_detail, container, false)

        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_up_fade_in)
        view.startAnimation(animation)

        val btnBack = view.findViewById<Button>(R.id.btn_back)
        val btnDelete = view.findViewById<Button>(R.id.btn_delete) // 1. Find the Delete Button
        val tvTitle = view.findViewById<TextView>(R.id.tv_detail_title)
        tvTitle.applyTextGradient(R.color.primary, R.color.accent)

        val tvTime = view.findViewById<TextView>(R.id.tv_detail_time)
        val tvTags = view.findViewById<TextView>(R.id.tv_detail_tags)
        val ingredientsContainer = view.findViewById<LinearLayout>(R.id.ingredients_list_container)
        val btnTimer = view.findViewById<Button>(R.id.btn_start_timer)
        val btnShare = view.findViewById<Button>(R.id.btn_share_recipe)

        targetRecipe?.let { currentRecipe ->
            tvTitle.text = currentRecipe.title
            tvTime.text = "⏱️ Time: ${currentRecipe.timeMinutes} minutes"

            val resolvedTags = currentRecipe.tags.map { tagName ->
                val emoji = RecipeRepository.tagEmojiMap[tagName] ?: "🏷️"
                "$emoji $tagName"
            }
            tvTags.text = "Tags: " + resolvedTags.joinToString(", ")

            for (ingredient in currentRecipe.ingredients) {
                val itemLayout = inflater.inflate(R.layout.item_ingredient, ingredientsContainer, false)
                val tvItem = itemLayout.findViewById<TextView>(R.id.tv_ingredient_item)
                tvItem.text = "• $ingredient"
                ingredientsContainer.addView(itemLayout)
            }

            btnTimer.setOnClickListener {
                val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
                    putExtra(AlarmClock.EXTRA_LENGTH, currentRecipe.timeMinutes * 60)
                    putExtra(AlarmClock.EXTRA_MESSAGE, "Cook: ${currentRecipe.title}")
                    putExtra(AlarmClock.EXTRA_SKIP_UI, false)
                }
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "No timer application found.", Toast.LENGTH_LONG).show()
                }
            }

            btnShare.setOnClickListener {
                val textBody = """
                    Check out this recipe:
                    ${currentRecipe.title} (${currentRecipe.timeMinutes} minutes)
                    
                    Ingredients:
                    ${currentRecipe.ingredients.joinToString("\n") { "- $it" }}
                """.trimIndent()

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Delicious Recipe: ${currentRecipe.title}")
                    putExtra(Intent.EXTRA_TEXT, textBody)
                }
                startActivity(Intent.createChooser(intent, "Share Recipe via:"))
            }

            // 2. DELETE BUTTON LOGIC
            btnDelete.setOnClickListener {
                // Check if the recipe ID is not null before proceeding
                val recipeId = currentRecipe.id ?: run {
                    Toast.makeText(context, "Cannot delete: Recipe ID is missing", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    try {
                        // Pass the non-null recipeId string
                        RecipeRepository.deleteRecipe(recipeId)

                        // Show success toast
                        Toast.makeText(context, "${currentRecipe.title} deleted!", Toast.LENGTH_SHORT).show()

                        // Navigate back to the previous screen
                        parentFragmentManager.popBackStack()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Failed to delete recipe: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // BACK BUTTON ANIMATION GOING BACK TO LIST
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    companion object {
        private const val ARG_RECIPE = "recipe_arg"

        fun newInstance(recipe: Recipe): RecipeDetailFragment {
            val fragment = RecipeDetailFragment()
            val args = Bundle()
            args.putSerializable(ARG_RECIPE, recipe)
            fragment.arguments = args
            return fragment
        }
    }
}