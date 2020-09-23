package com.learn.focus.pomodoro.app.ui.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.learn.focus.pomodoro.app.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}