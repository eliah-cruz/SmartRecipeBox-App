package com.example.smartrecipebox.fragments

import android.content.Intent
import android.os.Bundle
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

import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator

class RecipeDetailFragment : Fragment() {

    private var targetRecipe: Recipe? = null

    private var countDownTimer: CountDownTimer? = null
    private enum class TimerState {
        STOPPED, RUNNING, PAUSED
    }
    private var timerState = TimerState.STOPPED
    private var millisRemaining: Long = 0L

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

        val tvTimerDisplay = view.findViewById<TextView>(R.id.tv_timer_display)

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

            // tap to start -> pause -> resume
            btnTimer.setOnClickListener {
                when (timerState) {
                    TimerState.STOPPED -> {
                        millisRemaining = currentRecipe.timeMinutes * 60 * 1000L
                        startTimer(tvTimerDisplay, btnTimer)
                    }
                    TimerState.RUNNING -> {
                        pauseTimer(btnTimer)
                    }
                    TimerState.PAUSED -> {
                        startTimer(tvTimerDisplay, btnTimer)
                    }
                }
            }

            // long press to reset
            btnTimer.setOnLongClickListener {
                if (timerState != TimerState.STOPPED) {
                    cancelTimer(tvTimerDisplay, btnTimer)
                    true
                } else false
            }

            btnShare.setOnClickListener {
                btnShare.isEnabled = false
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

                btnShare.postDelayed({
                    btnShare.isEnabled = true
                }, 1000)
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

    private fun startTimer(tvTimerDisplay: TextView, btnTimer: Button) {
        timerState = TimerState.RUNNING
        tvTimerDisplay.visibility = View.VISIBLE
        btnTimer.text = "⏸ Pause Timer (Hold to Reset)"
        updateTimerText(tvTimerDisplay, millisRemaining)

        countDownTimer = object : CountDownTimer(millisRemaining, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                millisRemaining = millisUntilFinished
                updateTimerText(tvTimerDisplay, millisUntilFinished)
            }

            override fun onFinish() {
                timerState = TimerState.STOPPED
                millisRemaining = 0L
                tvTimerDisplay.text = "Time's up! 🎉"
                btnTimer.text = context?.getString(R.string.start_timer) ?: "Start Timer"
                playFinishAlert()
            }
        }.start()
    }

    private fun pauseTimer(btnTimer: Button) {
        countDownTimer?.cancel()
        timerState = TimerState.PAUSED
        btnTimer.text = "▶ Resume Timer (Hold to Reset)"
    }

    private fun cancelTimer(tvTimerDisplay: TextView, btnTimer: Button) {
        countDownTimer?.cancel()
        countDownTimer = null
        timerState = TimerState.STOPPED
        millisRemaining = 0L
        tvTimerDisplay.visibility = View.GONE
        btnTimer.text = context?.getString(R.string.start_timer) ?: "Start Timer"
    }

    private fun updateTimerText(tvTimerDisplay: TextView, millisRemaining: Long) {
        val totalSeconds = millisRemaining / 1000
        val minutesPart = totalSeconds / 60
        val secondsPart = totalSeconds % 60
        tvTimerDisplay.text = String.format("%02d:%02d", minutesPart, secondsPart)
    }

    private fun playFinishAlert() {
        val ctx = context ?: return

        try {
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            RingtoneManager.getRingtone(ctx, soundUri)?.play()
        } catch (e: Exception) {
            // ignore audio issues
        }

        val vibrator = ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(500)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        countDownTimer = null
        timerState = TimerState.STOPPED
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