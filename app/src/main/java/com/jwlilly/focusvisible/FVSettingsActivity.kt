package com.jwlilly.focusvisible

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FVSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, FVSettingsFragment())
            .commit()
    }
}