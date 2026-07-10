package com.example.smartrecipebox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartrecipebox.fragments.IntroFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply saved theme BEFORE rendering layout
        ThemeHelper.applyTheme(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, IntroFragment())
                .commit()
        }
    }
}