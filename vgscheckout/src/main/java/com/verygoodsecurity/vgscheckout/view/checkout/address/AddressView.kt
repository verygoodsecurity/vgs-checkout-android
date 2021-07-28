package com.verygoodsecurity.vgscheckout.view.checkout.address

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.collect.widget.VGSDropdownEventSpinner
import com.verygoodsecurity.vgscheckout.collect.widget.VGSEditText
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.util.address.AddressHelper
import com.verygoodsecurity.vgscheckout.util.address.USA
import com.verygoodsecurity.vgscheckout.util.address.model.PostalAddressType
import com.verygoodsecurity.vgscheckout.util.address.model.RegionType
import com.verygoodsecurity.vgscheckout.util.extension.*
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

    private val cityViewHolder = InputFieldViewHolder<VGSEditText>(
        findViewById(R.id.mtvCitySubtitle),
        findViewById(R.id.vgsEtCity),
        this
    )

    private val regionViewHolder = InputFieldViewHolder<VGSEditText>(
        findViewById(R.id.mtvRegionInputSubtitle),
        findViewById(R.id.vgsEtRegion),
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
        setupRegions()
        setupPostalAddressCode()
        initInputValidation()
        initListeners()
    }

    override fun getLayoutId(): Int = R.layout.vgs_checkout_address_view

    override fun getColumnsCount() = 1

    override fun getRowsCount() = 4

    override fun applyConfig(config: CheckoutFormConfiguration) {
    }

    override fun isInputValid(): Boolean =
        isInputValid(
            addressViewHolder.state,
            cityViewHolder.state,
            regionViewHolder.state,
            postalAddressViewHolder.state
        )

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        (parent?.getItemAtPosition(position) as? String)?.let {
            currentCountry = it
            setupRegions()
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

    fun applyConfig(options: VGSCheckoutAddressOptions) {
        countriesSpinner.setFieldName(options.countryOptions.fieldName)
        addressViewHolder.input.setFieldName(options.addressOptions.fieldName)
        cityViewHolder.input.setFieldName(options.cityOptions.fieldName)
        regionViewHolder.input.setFieldName(options.regionOptions.fieldName)
        postalAddressViewHolder.input.setFieldName(options.postalAddressOptions.fieldName)
    }

    private fun initListeners() {
        countriesSpinner.onDropdownStateChangeListener = this
    }

    private fun initInputValidation() {
        val minLengthValidationRule = VGSInfoRule.ValidationBuilder()
            .setRegex(NOT_EMPTY_REGEX)
            .build()
        addressViewHolder.input.addRule(minLengthValidationRule)
        cityViewHolder.input.addRule(minLengthValidationRule)
        regionViewHolder.input.addRule(minLengthValidationRule)
        postalAddressViewHolder.input.addRule(minLengthValidationRule)
    }

    private fun setupCountries() {
        val countries = AddressHelper.countries
        countriesSpinner.adapter = createSpinnerAdapter(countries)
        countriesSpinner.onItemSelectedListener = this
        countriesSpinner.setSelection(countries.indexOf(USA))
    }

    private fun setupRegions() {
        val regionType = AddressHelper.getRegionType(currentCountry)
        regionViewHolder.subtitle.text = getRegionSubtitle(regionType)
        regionViewHolder.input.setHint(getRegionHint(regionType))
    }

    private fun setupPostalAddressCode() {
        when (AddressHelper.getPostalAddressType(currentCountry)) {
            PostalAddressType.ZIP -> {
                postalAddressViewHolder.subtitle.text = getString(R.string.vgs_checkout_address_info_zip_subtitle)
                postalAddressViewHolder.input.setHint(getString(R.string.vgs_checkout_address_info_zip_hint))
            }
            PostalAddressType.POSTAL -> {
                postalAddressViewHolder.subtitle.text = getString(R.string.vgs_checkout_address_info_postal_code_subtitle)
                postalAddressViewHolder.input.setHint(getString(R.string.vgs_checkout_address_info_postal_code_hint))
            }
        }
        //TODO: Set validation
    }

    private fun createSpinnerAdapter(data: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(context, R.layout.vgs_checkout_spinner_header_layout, data).apply {
            setDropDownViewResource(R.layout.vgs_checkout_spinner_item)
        }
    }

    private fun getRegionSubtitle(type: RegionType) = getString(
        when (type) {
            RegionType.STATE -> R.string.vgs_checkout_address_info_region_type_state_subtitle
            RegionType.PROVINCE -> R.string.vgs_checkout_address_info_region_type_province_subtitle
            RegionType.SUBURB -> R.string.vgs_checkout_address_info_region_type_suburb_subtitle
            RegionType.COUNTY -> R.string.vgs_checkout_address_info_region_type_county_subtitle
            RegionType.UNKNOWN -> R.string.vgs_checkout_address_info_region_type_unknown_subtitle
        }
    )

    private fun getRegionHint(type: RegionType) = getString(
        when (type) {
            RegionType.STATE -> R.string.vgs_checkout_address_info_region_type_state_hint
            RegionType.PROVINCE -> R.string.vgs_checkout_address_info_region_type_province_hint
            RegionType.SUBURB -> R.string.vgs_checkout_address_info_region_type_suburb_subtitle
            RegionType.COUNTY -> R.string.vgs_checkout_address_info_region_type_county_hint
            RegionType.UNKNOWN -> R.string.vgs_checkout_address_info_region_type_unknown_hint
        }
    )

    private fun getRegionEmptyError(type: RegionType) = getString(
        when (type) {
            RegionType.STATE -> R.string.vgs_checkout_address_info_state_empty_error
            RegionType.SUBURB -> R.string.vgs_checkout_address_info_suburb_empty_error
            RegionType.PROVINCE -> R.string.vgs_checkout_address_info_province_empty_error
            RegionType.COUNTY -> R.string.vgs_checkout_address_info_county_empty_error
            RegionType.UNKNOWN -> R.string.vgs_checkout_address_info_region_unknown_empty_error
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
}