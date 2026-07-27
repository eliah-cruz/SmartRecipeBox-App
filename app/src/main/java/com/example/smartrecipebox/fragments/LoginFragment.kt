package com.example.smartrecipebox.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartrecipebox.R
import com.example.smartrecipebox.MainActivity
import com.example.smartrecipebox.applyTextGradient

import androidx.lifecycle.lifecycleScope
import com.example.smartrecipebox.data.SupabaseManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvLoginTitle = view.findViewById<TextView>(R.id.tvLoginTitle)
        val editEmail = view.findViewById<EditText>(R.id.editEmail)
        val editPassword = view.findViewById<EditText>(R.id.editPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val txtGoToSignUp = view.findViewById<TextView>(R.id.txtGoToSignUp)

        tvLoginTitle.applyTextGradient(R.color.primary, R.color.accent)

        // Gradient for the title
        // val paint = tvLoginTitle.paint
        // val width = paint.measureText(tvLoginTitle.text.toString())
        // tvLoginTitle.paint.shader = LinearGradient(
        //     0f, 0f, width, 0f,
        //     intArrayOf(
        //         Color.parseColor("#FFD700"),
        //         Color.parseColor("#FF5722")
        //     ),
        //     null,
        //     Shader.TileMode.CLAMP
        // )

        btnLogin.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            // Check if the form is fully filled out
            var isFormValid = true

            if (email.isEmpty()) {
                editEmail.error = "Email is required"
                isFormValid = false
            }

            if (password.isEmpty()) {
                editPassword.error = "Password is required"
                isFormValid = false
            }

            // If everything is filled out, log them in!
            if (isFormValid) {
                btnLogin.isEnabled = false
                lifecycleScope.launch {
                    try {
                        SupabaseManager.client.auth.signInWith(Email) {
                            this.email = email
                            this.password = password   
                        }
                        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

                        Toast.makeText(requireContext(), "Login Successfully!", Toast.LENGTH_SHORT).show()
                        (activity as MainActivity).loadFragment(IntroFragment())
                    } catch (e: Exception) {
                        btnLogin.isEnabled = true
                        Toast.makeText(requireContext(), "Login failed: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        txtGoToSignUp.setOnClickListener {
            (activity as MainActivity).loadFragment(SignUpFragment())
        }
    }
}