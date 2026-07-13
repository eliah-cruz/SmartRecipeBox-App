package com.example.smartrecipebox.fragments

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

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvLoginTitle = view.findViewById<TextView>(R.id.tvLoginTitle)
        tvLoginTitle.applyTextGradient(R.color.primary, R.color.accent)

        val editEmail = view.findViewById<EditText>(R.id.editEmail)
        val editPassword = view.findViewById<EditText>(R.id.editPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val txtGoToSignUp = view.findViewById<TextView>(R.id.txtGoToSignUp)

        btnLogin.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // TEMPORARY: Move to the next screen for frontend testing
                Toast.makeText(requireContext(), "Frontend Test Login", Toast.LENGTH_SHORT).show()
                (activity as MainActivity).loadFragment(IntroFragment())

            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        txtGoToSignUp.setOnClickListener {
            (activity as MainActivity).loadFragment(SignUpFragment())
        }
    }
}