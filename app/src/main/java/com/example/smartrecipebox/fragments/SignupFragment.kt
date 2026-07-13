package com.example.smartrecipebox.fragments

import android.content.Context
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

class SignUpFragment : Fragment(R.layout.fragment_signup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvSignUpTitle = view.findViewById<TextView>(R.id.tvSignUpTitle)
        tvSignUpTitle.applyTextGradient(R.color.primary, R.color.accent)

        val editName = view.findViewById<EditText>(R.id.editSignUpName)
        val editEmail = view.findViewById<EditText>(R.id.editSignUpEmail)
        val editPassword = view.findViewById<EditText>(R.id.editSignUpPassword)
        val btnSignUp = view.findViewById<Button>(R.id.btnSignUp)
        val txtGoToLogin = view.findViewById<TextView>(R.id.txtGoToLogin)

        btnSignUp.setOnClickListener {
            val name = editName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

                Toast.makeText(requireContext(), "Account Created!", Toast.LENGTH_SHORT).show()

                (activity as MainActivity).loadFragment(IntroFragment())
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        txtGoToLogin.setOnClickListener {
            (activity as MainActivity).loadFragment(LoginFragment())
        }
    }
}