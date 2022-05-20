package com.verygoodsecurity.vgscheckout.demo.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.verygoodsecurity.vgscheckout.demo.R

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var lpValidationBehaviour: ListPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        initPreferences()
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        lpValidationBehaviour?.let { if (p1 == it.key) updateValidationBehaviourTitle() }
    }

    private fun initPreferences() {
        lpValidationBehaviour = findPreference(getString(R.string.setting_key_validation_behaviour))
        updateValidationBehaviourTitle()
    }

    private fun updateValidationBehaviourTitle() {
        val key = getString(R.string.setting_key_validation_behaviour)
        lpValidationBehaviour?.title = preferenceScreen.sharedPreferences?.getString(key, null)
    }
}