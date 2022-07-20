package com.verygoodsecurity.vgscheckout.demo.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.CheckBoxPreference
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.verygoodsecurity.vgscheckout.demo.BuildConfig
import com.verygoodsecurity.vgscheckout.demo.CheckoutType
import com.verygoodsecurity.vgscheckout.demo.R

class CheckoutSettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var etpVaultId: EditTextPreference? = null
    private var cbpBillingAddress: CheckBoxPreference? = null
    private var cbpCountry: CheckBoxPreference? = null
    private var cbpAddress: CheckBoxPreference? = null
    private var cbpOptionalAddress: CheckBoxPreference? = null
    private var cbpCity: CheckBoxPreference? = null
    private var cbpPostalCode: CheckBoxPreference? = null
    private var lpValidationBehaviour: ListPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(getPreferenceXml(arguments?.getSerializable(KEY_BUNDLE_TYPE) as CheckoutType))
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        initPreferences()
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        lpValidationBehaviour?.let { if (p1 == it.key) updateValidationBehaviourTitle() }
        cbpBillingAddress?.let {
            if (p1 == it.key && p0 != null) {
                updateBillingAddressFieldsVisibility(p0.getBoolean(p1, true))
            }
        }
        etpVaultId?.let {
            if (p1 == it.key && p0 != null) {
                etpVaultId?.summary = p0.getString(it.key, BuildConfig.STORAGE_ID)
            }
        }
    }

    private fun getPreferenceXml(type: CheckoutType) = when (type) {
        CheckoutType.CUSTOM -> R.xml.custom_settings
        CheckoutType.PAYMENT -> R.xml.payment_settings
    }

    private fun initPreferences() {
        val vaultIdKey = getString(R.string.setting_key_vault_id)
        val vaultIdValue =
            preferenceScreen.sharedPreferences?.getString(vaultIdKey, BuildConfig.STORAGE_ID)
        etpVaultId = findPreference(vaultIdKey)
        etpVaultId?.text = vaultIdValue
        etpVaultId?.summary = vaultIdValue
        lpValidationBehaviour = findPreference(getString(R.string.setting_key_validation_behaviour))
        updateValidationBehaviourTitle()
        cbpBillingAddress = findPreference(getString(R.string.setting_key_billing_address_visible))
        cbpCountry = findPreference(getString(R.string.setting_key_country_visible))
        cbpAddress = findPreference(getString(R.string.setting_key_address_visible))
        cbpOptionalAddress =
            findPreference(getString(R.string.setting_key_optional_address_visible))
        cbpCity = findPreference(getString(R.string.setting_key_city_visible))
        cbpPostalCode = findPreference(getString(R.string.setting_key_postal_code_visible))
    }

    private fun updateValidationBehaviourTitle() {
        val key = context?.getString(R.string.setting_key_validation_behaviour)
        lpValidationBehaviour?.title = preferenceScreen.sharedPreferences?.getString(key, null)
    }

    private fun updateBillingAddressFieldsVisibility(isVisible: Boolean) {
        cbpCountry?.isChecked = isVisible
        cbpAddress?.isChecked = isVisible
        cbpOptionalAddress?.isChecked = isVisible
        cbpCity?.isChecked = isVisible
        cbpPostalCode?.isChecked = isVisible
    }

    companion object {

        private const val KEY_BUNDLE_TYPE =
            "com.verygoodsecurity.vgscheckout.demo.settings.key_bundle_type"

        fun create(type: CheckoutType): Fragment = CheckoutSettingsFragment().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_BUNDLE_TYPE, type)
            }
        }
    }
}