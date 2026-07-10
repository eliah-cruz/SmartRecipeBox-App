package com.example.smartrecipebox.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.smartrecipebox.R
import com.example.smartrecipebox.applyTextGradient

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_intro, container, false)

        // Dynamic text gradient using white-to-cream colors
        val tvIntroTitle = view.findViewById<TextView>(R.id.tv_intro_title)
        tvIntroTitle.applyTextGradient(R.color.intro_text_start, R.color.intro_text_end)

        val btnGetStarted = view.findViewById<Button>(R.id.btn_get_started)
        btnGetStarted.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragment_container, RecipeListFragment())
                .commit()
        }
        return view
    }
}