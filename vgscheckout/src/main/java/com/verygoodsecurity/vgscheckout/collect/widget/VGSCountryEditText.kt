package com.verygoodsecurity.vgscheckout.collect.widget

import android.content.Context
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
import android.os.Bundle

internal class VGSCountryEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InputFieldView(context, attrs, defStyleAttr) {

    var selectedCountry: Country = CountriesHelper.countries.first()
        set(value) {
            field = value
            setText(value.name)
        }

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
        selectedCountry = CountriesHelper.getCountry(CountriesHelper.ISO.USA)
    }

    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
            putParcelable(COUNTRY, selectedCountry)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            selectedCountry = state.getParcelable(COUNTRY)!!
            super.onRestoreInstanceState(state.getParcelable(INSTANCE_STATE))
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private fun showCountrySelectionDialog() {
        val countries = CountriesHelper.countries
        val countryNames = countries.map { it.name }.toTypedArray()
        val selectedIndex = countries.indexOf(selectedCountry)
        var selected = -1
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.vgs_checkout_select_country_dialog_title)
            .setSingleChoiceItems(countryNames, selectedIndex) { _, which -> selected = which }
            .setNegativeButton(
                R.string.vgs_checkout_select_country_dialog_negative_button_title,
                null
            )
            .setPositiveButton(R.string.vgs_checkout_select_country_dialog_positive_button_title) { _, _ ->
                countries.getOrNull(selected)?.let {
                    selectedCountry = it
                    onCountrySelectedListener?.onCountrySelected(selectedCountry)
                }
            }
            .show()
    }

    companion object {
        private const val BILLING_ADDRESS_MIN_CHARS_COUNT = 1
        private const val INSTANCE_STATE = "on_save_instance_state"
        private const val COUNTRY = "billing_country"
    }
}