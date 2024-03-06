package com.jwlilly.focusvisible

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class FVSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}