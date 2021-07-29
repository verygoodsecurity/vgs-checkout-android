package com.verygoodsecurity.vgscheckout.view.checkout.address

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.widget.VGSDropdownEventSpinner
import com.verygoodsecurity.vgscheckout.collect.widget.VGSEditText
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.util.address.AddressHelper
import com.verygoodsecurity.vgscheckout.util.address.USA
import com.verygoodsecurity.vgscheckout.util.address.model.PostalAddressType
import com.verygoodsecurity.vgscheckout.util.extension.getString
import com.verygoodsecurity.vgscheckout.util.extension.setDrawableEnd
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

    private var currentCountry: String = USA

    init {

        setupCountries()
        setupPostalAddressCode()
        initListeners()
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
        (parent?.getItemAtPosition(position) as? String)?.let {
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
        val countries = AddressHelper.countries
        countriesSpinner.adapter = createSpinnerAdapter(countries)
        countriesSpinner.onItemSelectedListener = this
        countriesSpinner.setSelection(countries.indexOf(USA))
    }

    private fun setupPostalAddressCode() {
        val type = AddressHelper.getPostalAddressType(currentCountry)
        postalAddressViewHolder.subtitle.text = getPostalAddressSubtitle(type)
        postalAddressViewHolder.input.setHint(getPostalAddressHint(type))
        //TODO: Set validation
    }

    private fun initListeners() {
        countriesSpinner.onDropdownStateChangeListener = this
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
        val postalAddressType = AddressHelper.getPostalAddressType(currentCountry)
        val (message, drawable) = when {
            isInputEmpty(state) -> getPostalAddressEmptyError(postalAddressType) to errorDrawable
            isInputInvalid(state) -> getPostalAddressInvalidError(postalAddressType) to errorDrawable
            else -> null to null
        }
        updateError(postalAddressViewHolder.input.id, message)
        postalAddressViewHolder.subtitle.setDrawableEnd(drawable)
    }

    private fun createSpinnerAdapter(data: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(context, R.layout.vgs_checkout_spinner_header_layout, data).apply {
            setDropDownViewResource(R.layout.vgs_checkout_spinner_item)
        }
    }

    private fun getPostalAddressSubtitle(type: PostalAddressType) = getString(
        when (type) {
            PostalAddressType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_subtitle
            PostalAddressType.ZIP -> R.string.vgs_checkout_address_info_zip_subtitle
        }
    )

    private fun getPostalAddressHint(type: PostalAddressType) = getString(
        when (type) {
            PostalAddressType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_hint
            PostalAddressType.ZIP -> R.string.vgs_checkout_address_info_zip_hint
        }
    )

    private fun getPostalAddressEmptyError(type: PostalAddressType) = getString(
        when (type) {
            PostalAddressType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_empty_error
            PostalAddressType.ZIP -> R.string.vgs_checkout_address_info_zipcode_empty_error
        }
    )

    private fun getPostalAddressInvalidError(type: PostalAddressType) = getString(
        when (type) {
            PostalAddressType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_invalid_error
            PostalAddressType.ZIP -> R.string.vgs_checkout_address_info_zipcode_invalid_error
        }
    )

    companion object {

        private const val COLUMN_COUNT = 1
        private const val ROW_COUNT = 4
    }
}