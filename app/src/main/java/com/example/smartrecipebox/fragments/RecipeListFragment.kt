package com.example.smartrecipebox.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.smartrecipebox.*
import com.example.smartrecipebox.data.RecipeRepository
import com.example.smartrecipebox.models.Recipe
import com.example.smartrecipebox.models.isQuick

class RecipeListFragment : Fragment() {

    private lateinit var recipeListContainer: LinearLayout
    private lateinit var horizontalScrollView: HorizontalScrollView
    private var activeTagFilter: String? = null
    private var activeSearchText: String = ""
    private var lastClickTime = 0L
    private var scrollXPosition = 0

    // Handler to debounce search typing and prevent glitchy rapid re-draws
    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    companion object {
        private const val KEY_ACTIVE_FILTER = "active_tag_filter"
        private const val KEY_SCROLL_X = "scroll_x_position"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_ACTIVE_FILTER, activeTagFilter)
        if (::horizontalScrollView.isInitialized) {
            outState.putInt(KEY_SCROLL_X, horizontalScrollView.scrollX)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        horizontalScrollView = view.findViewById(R.id.horizontalScrollView)

        if (savedInstanceState != null) {
            activeTagFilter = savedInstanceState.getString(KEY_ACTIVE_FILTER)
            scrollXPosition = savedInstanceState.getInt(KEY_SCROLL_X, 0)
        }

        view.post {
            horizontalScrollView.scrollTo(scrollXPosition, 0)
        }

        val tvHeader = view.findViewById<TextView>(R.id.tv_title_header)
        tvHeader.applyTextGradient(R.color.primary, R.color.accent)

        recipeListContainer = view.findViewById(R.id.recipes_container)
        val etSearch = view.findViewById<EditText>(R.id.et_search)

        // Instant reset on backspace/clear, smooth debounce delay while typing
        etSearch.doAfterTextChanged { text ->
            // 1. Immediately update active search text
            activeSearchText = text.toString().lowercase()

            // 2. Cancel any pending delayed searches
            searchRunnable?.let { searchHandler.removeCallbacks(it) }

            // 3. If empty, update list instantly. If typing, debounce by 200ms.
            if (activeSearchText.isEmpty()) {
                updateList()
            } else {
                searchRunnable = Runnable {
                    updateList()
                }
                searchHandler.postDelayed(searchRunnable!!, 200)
            }
        }
        etSearch.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = etSearch.compoundDrawables[2]
                if (drawableEnd != null) {
                    val clinicalArea = etsearchWidthMinusDrawable(etSearch, drawableEnd)
                    if (event.rawX >= clinicalArea) {
                        etSearch.setText("")
                        v.performClick()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        val button = view.findViewById<Button>(R.id.btn_add_recipe)
        button.setOnClickListener {
            val intent = Intent(requireContext(), AddRecipeActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btn_theme_toggle).setOnClickListener {
            ThemeHelper.toggleTheme(requireContext())
            requireActivity().recreate()
        }

        // ==========================================
        // NEW CODE: Logout Button Logic
        // ==========================================
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            // Open SharedPreferences and change "isLoggedIn" to false
            val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()

            // Show a quick message
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Send the user back to the Login Screen
            (activity as MainActivity).loadFragment(LoginFragment())
        }

        val filters = listOf(
            R.id.chip_all to null,
            R.id.chip_quick to "Quick",
            R.id.chip_breakfast to "Breakfast",
            R.id.chip_dinner to "Dinner",
            R.id.chip_dessert to "Dessert"
        )
        filters.forEach { (id, tag) -> view.findViewById<Button>(id).setOnClickListener { filterByTag(tag) } }

        updateList()
        return view
    }

    private fun etsearchWidthMinusDrawable(editText: EditText, drawable: android.graphics.drawable.Drawable): Float {
        return (editText.right - drawable.bounds.width() - editText.paddingEnd).toFloat()
    }

    private fun filterByTag(tag: String?) {
        activeTagFilter = tag
        updateList()
    }

    private fun updateList() {
        recipeListContainer.removeAllViews()
        val list = RecipeRepository.filterRecipes { r ->
            val mS = r.title.lowercase().contains(activeSearchText)
            val mT = activeTagFilter?.let { if (it == "Quick") r.isQuick() else r.tags.contains(it) } ?: true
            mS && mT
        }

        for (recipe in list) {
            val v = LayoutInflater.from(context).inflate(R.layout.item_recipe, recipeListContainer, false)
            val tV = v.findViewById<TextView>(R.id.tv_recipe_title)
            tV.text = recipe.title

            tV.applyTextGradient(R.color.primary, R.color.accent)

            v.findViewById<TextView>(R.id.tv_recipe_time).text = "${recipe.timeMinutes} mins"
            val tags = v.findViewById<TextView>(R.id.tv_recipe_tags)
            tags.text = recipe.tags.joinToString("   ") { (RecipeRepository.tagEmojiMap[it] ?: "🏷️") + " $it" }

            v.setOnClickListener { navigateToDetails(recipe) }
            recipeListContainer.addView(v)
        }
    }

    private fun navigateToDetails(recipe: Recipe) {
        if (System.currentTimeMillis() - lastClickTime < 500) return
        lastClickTime = System.currentTimeMillis()

        val f = RecipeDetailFragment.newInstance(recipe.id)
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_up_fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_down_fade_out
            )
            .replace(R.id.fragment_container, f)
            .addToBackStack(null)
            .commit()
    }
}