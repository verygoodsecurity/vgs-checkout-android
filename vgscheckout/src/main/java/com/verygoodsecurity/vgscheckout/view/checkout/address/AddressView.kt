package com.verygoodsecurity.vgscheckout.view.checkout.address

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.collect.widget.VGSDropdownEventSpinner
import com.verygoodsecurity.vgscheckout.collect.widget.VGSEditText
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.util.extension.getString
import com.verygoodsecurity.vgscheckout.util.extension.setDrawableEnd
import com.verygoodsecurity.vgscheckout.view.checkout.address.adapter.CountryAdapter
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.country.CountriesHelper
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.country.model.Country
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.emptyError
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.hint
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.invalidError
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.subtitle
import com.verygoodsecurity.vgscheckout.view.checkout.core.BaseCheckoutFormView
import com.verygoodsecurity.vgscheckout.view.checkout.core.InputFieldViewHolder

internal class AddressView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCheckoutFormView(context, attrs, defStyleAttr), AdapterView.OnItemSelectedListener,
    VGSDropdownEventSpinner.OnDropdownStateChangeListener {

    private val countriesSpinner: VGSDropdownEventSpinner = findViewById(R.id.spinnerCountries)

    private val addressViewHolder = InputFieldViewHolder<VGSEditText>(
        findViewById(R.id.mtvAddressSubtitle),
        findViewById(R.id.vgsEtAddress),
        this
    )

    private val addressOptionViewHolder = InputFieldViewHolder<VGSEditText>(
        findViewById(R.id.mtvAddressOptionalSubtitle),
        findViewById(R.id.vgsEtAddressOptional),
        this
    )

    private val cityViewHolder = InputFieldViewHolder<VGSEditText>(
        findViewById(R.id.mtvCitySubtitle),
        findViewById(R.id.vgsEtCity),
        this
    )

    private val postalAddressViewHolder = InputFieldViewHolder<VGSEditText>(
        findViewById(R.id.mtvPostalAddressSubtitle),
        findViewById(R.id.vgsEtPostalAddress),
        this
    )

    private var currentCountry: Country = CountriesHelper.getCountry(CountriesHelper.ISO.USA)

    init {

        setupCountries()
        setupPostalAddressCode()
        initListeners()
        initInputValidation()
    }

    override fun getLayoutId(): Int = R.layout.vgs_checkout_address_view

    override fun getColumnsCount() = COLUMN_COUNT

    override fun getRowsCount() = ROW_COUNT

    override fun applyConfig(config: CheckoutFormConfiguration) {
        countriesSpinner.setFieldName(config.addressOptions.countryOptions.fieldName)
        addressViewHolder.input.setFieldName(config.addressOptions.addressOptions.fieldName)
        addressOptionViewHolder.input.setFieldName(config.addressOptions.optionalAddressOptions.fieldName)
        cityViewHolder.input.setFieldName(config.addressOptions.cityOptions.fieldName)
        postalAddressViewHolder.input.setFieldName(config.addressOptions.postalAddressOptions.fieldName)
    }

    override fun getInputViews(): Array<InputFieldView> = arrayOf(
        addressViewHolder.input,
        addressOptionViewHolder.input,
        cityViewHolder.input,
        postalAddressViewHolder.input,
    )

    override fun isInputValid(): Boolean =
        isInputValid(
            addressViewHolder.state,
            cityViewHolder.state,
            postalAddressViewHolder.state
        )

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        (parent?.getItemAtPosition(position) as? Country)?.let {
            currentCountry = it
            setupPostalAddressCode()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onDropdownOpened() {
        updateGridColor()
    }

    override fun onDropdownClosed() {
        updateGridColor()
    }

    override fun onStateChange(inputId: Int, state: InputFieldViewHolder.ViewState) {
        super.onStateChange(inputId, state)
        updateGridColor()
        when (inputId) {
            R.id.vgsEtAddress -> updateAddressError(state)
            R.id.vgsEtCity -> updateCityError(state)
            R.id.vgsEtPostalAddress -> updatePostalAddressError(state)
        }
    }

    private fun setupCountries() {
        val countries = CountriesHelper.getHandledCountries()
        countriesSpinner.adapter = CountryAdapter(context, countries)
        countriesSpinner.onItemSelectedListener = this
        countriesSpinner.setSelection(countries.indexOf(currentCountry))
    }

    private fun setupPostalAddressCode() {
        with(postalAddressViewHolder) {
            subtitle.text = getString(currentCountry.postalAddressType.subtitle())
            input.setHint(getString(currentCountry.postalAddressType.hint()))
            input.addRule(
                VGSInfoRule.ValidationBuilder().setRegex(currentCountry.postalAddressRegex).build()
            )
            input.reInvalidateInput()
        }
    }

    private fun initListeners() {
        countriesSpinner.onDropdownStateChangeListener = this
    }

    private fun initInputValidation() {
        val rule = VGSInfoRule.ValidationBuilder()
            .setRegex(NOT_EMPTY_REGEX)
            .build()
        addressViewHolder.input.addRule(rule)
        cityViewHolder.input.addRule(rule)
        postalAddressViewHolder.input.addRule(rule)
    }

    private fun updateGridColor() {
        updateGridColor(
            addressViewHolder.state,
            addressOptionViewHolder.state,
            cityViewHolder.state,
            postalAddressViewHolder.state,
            spinner = countriesSpinner
        )
    }

    private fun updateAddressError(state: InputFieldViewHolder.ViewState) {
        val (message, drawable) = when {
            isInputEmpty(state) -> getString(R.string.vgs_checkout_address_info_address_line1_empty_error) to errorDrawable
            else -> null to null
        }
        updateError(addressViewHolder.input.id, message)
        addressViewHolder.subtitle.setDrawableEnd(drawable)
    }

    private fun updateCityError(state: InputFieldViewHolder.ViewState) {
        val (message, drawable) = when {
            isInputEmpty(state) -> getString(R.string.vgs_checkout_address_info_city_empty_error) to errorDrawable
            else -> null to null
        }
        updateError(cityViewHolder.input.id, message)
        cityViewHolder.subtitle.setDrawableEnd(drawable)
    }

    private fun updatePostalAddressError(state: InputFieldViewHolder.ViewState) {
        val (message, drawable) = when {
            isInputEmpty(state) -> getString(currentCountry.postalAddressType.emptyError()) to errorDrawable
            isInputInvalid(state) -> getString(currentCountry.postalAddressType.invalidError()) to errorDrawable
            else -> null to null
        }
        updateError(postalAddressViewHolder.input.id, message)
        postalAddressViewHolder.subtitle.setDrawableEnd(drawable)
    }

    companion object {

        private const val COLUMN_COUNT = 1
        private const val ROW_COUNT = 4
    }
}