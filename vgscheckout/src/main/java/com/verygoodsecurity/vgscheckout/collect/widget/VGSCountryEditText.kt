package com.verygoodsecurity.vgscheckout.collect.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.FieldType
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.collect.view.core.serializers.CountryNameToIsoSerializer
import com.verygoodsecurity.vgscheckout.util.country.CountriesHelper
import com.verygoodsecurity.vgscheckout.util.country.model.Country
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger

internal class VGSCountryEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : InputFieldView(context, attrs, defStyleAttr) {

    private var countries: List<Country> = CountriesHelper.countries

    private var _selectedCountry: Country = CountriesHelper.getCountry(CountriesHelper.ISO.USA)
    val selectedCountry: Country get() = _selectedCountry

    internal var onCountrySelectedListener: OnCountrySelectedListener? = null

    private val billingAddressValidationRule: VGSInfoRule by lazy {
        VGSInfoRule.ValidationBuilder()
            .setAllowableMinLength(BILLING_ADDRESS_MIN_CHARS_COUNT)
            .build()
    }

    interface OnCountrySelectedListener {
        fun onCountrySelected(country: Country)
    }

    init {
        setupViewType(FieldType.COUNTRY)

        applyValidationRule(billingAddressValidationRule)
        isFocusable = false
        setOnClickListener { showCountrySelectionDialog() }
        setFieldDataSerializer(CountryNameToIsoSerializer())
        setText(selectedCountry.name)
    }

    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
            putParcelable(COUNTRY, _selectedCountry)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            _selectedCountry = state.getParcelable(COUNTRY)!!
            super.onRestoreInstanceState(state.getParcelable(INSTANCE_STATE))
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    fun setSupportedCountries(iso: List<String>) {
        val countries = CountriesHelper.getCountries(iso)
        val invalidCountries = countries.filter { !it.isValid() }
        val validCountries = countries - invalidCountries
        invalidCountries.takeIf { it.isNotEmpty() }?.let { logInvalidCountryCodes(it) }
        validCountries.takeIf { it.isNotEmpty() }?.let {
            this.countries = it
            this._selectedCountry = it.first()
        }
    }

    fun setSelectedCountry(country: Country) {
        if (country.isValid() && countries.contains(country)) {
            _selectedCountry = country
            onCountrySelectedListener?.onCountrySelected(country)
            setText(country.name)
        }
    }

    private fun logInvalidCountryCodes(countries: List<Country>) {
        VGSCheckoutLogger.debug(message = "TODO() ${countries.map { it.code }}")
    }

    private var countryDialog: Dialog? = null
    private fun showCountrySelectionDialog() {
        if (countryDialog == null || !countryDialog!!.isShowing) {
            val countryNames = countries.map { it.name }.toTypedArray()
            val selectedIndex = countries.indexOf(selectedCountry)
            var selected = -1
            countryDialog = MaterialAlertDialogBuilder(context)
                .setTitle(R.string.vgs_checkout_select_country_dialog_title)
                .setSingleChoiceItems(countryNames, selectedIndex) { _, which -> selected = which }
                .setNegativeButton(
                    R.string.vgs_checkout_select_country_dialog_negative_button_title,
                    null
                )
                .setPositiveButton(R.string.vgs_checkout_select_country_dialog_positive_button_title) { _, _ ->
                    countries.getOrNull(selected)?.let {
                        setSelectedCountry(it)
                    }
                }.create()
                .also { it.show() }
        }
    }

    companion object {
        private const val BILLING_ADDRESS_MIN_CHARS_COUNT = 1
        private const val INSTANCE_STATE = "on_save_instance_state"
        private const val COUNTRY = "billing_country"
    }
}