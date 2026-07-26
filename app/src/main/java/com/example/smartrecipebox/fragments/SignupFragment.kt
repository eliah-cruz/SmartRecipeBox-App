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

class SignUpFragment : Fragment(R.layout.fragment_signup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvSignUpTitle = view.findViewById<TextView>(R.id.tvSignUpTitle)
        val editName = view.findViewById<EditText>(R.id.editSignUpName)
        val editEmail = view.findViewById<EditText>(R.id.editSignUpEmail)
        val editPassword = view.findViewById<EditText>(R.id.editSignUpPassword)
        val btnSignUp = view.findViewById<Button>(R.id.btnSignUp)
        val txtGoToLogin = view.findViewById<TextView>(R.id.txtGoToLogin)

        // Gradient for the title
        val paint = tvSignUpTitle.paint
        val width = paint.measureText(tvSignUpTitle.text.toString())
        tvSignUpTitle.paint.shader = LinearGradient(
            0f, 0f, width, 0f,
            intArrayOf(
                Color.parseColor("#FFD700"),
                Color.parseColor("#FF5722")
            ),
            null,
            Shader.TileMode.CLAMP
        )

        btnSignUp.setOnClickListener {
            val name = editName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            // Check if the form is fully filled out
            var isFormValid = true

            if (name.isEmpty()) {
                editName.error = "Name is required"
                isFormValid = false
            }

            if (email.isEmpty()) {
                editEmail.error = "Email is required"
                isFormValid = false
            }

            if (password.isEmpty()) {
                editPassword.error = "Password is required"
                isFormValid = false
            }

            // If everything is filled out, create the account!
            if (isFormValid) {
                val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

                SupabaseManager.supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }

                Toast.makeText(requireContext(), "Account Created!", Toast.LENGTH_SHORT).show()
                (activity as MainActivity).loadFragment(IntroFragment())
            }
        }

        txtGoToLogin.setOnClickListener {
            (activity as MainActivity).loadFragment(LoginFragment())
        }
    }
}