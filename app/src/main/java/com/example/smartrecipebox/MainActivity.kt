package com.example.smartrecipebox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartrecipebox.fragments.IntroFragment
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.smartrecipebox.fragments.LoginFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeHelper.applyTheme(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if user is logged in using SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        // Only load the first fragment if this is a fresh start
        if (savedInstanceState == null) {
            if (isLoggedIn) {
                // User is already logged in, show the normal app
                loadFragment(IntroFragment())
            } else {
                // User is not logged in, show Login Screen
                loadFragment(LoginFragment())
            }
        }
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()

            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}