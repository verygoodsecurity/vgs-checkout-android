package com.verygoodsecurity.vgscheckout.demo.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.verygoodsecurity.vgscheckout.demo.R

class SettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }
}