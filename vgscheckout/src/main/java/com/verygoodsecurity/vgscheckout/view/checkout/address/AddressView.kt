package com.verygoodsecurity.vgscheckout.view.checkout.address

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.VGSCollectView
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.collect.widget.VGSDropdownEventSpinner
import com.verygoodsecurity.vgscheckout.collect.widget.VGSEditText
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.util.ObservableLinkedHashMap
import com.verygoodsecurity.vgscheckout.util.address.AddressHelper
import com.verygoodsecurity.vgscheckout.util.address.USA
import com.verygoodsecurity.vgscheckout.util.address.model.PostalAddressType
import com.verygoodsecurity.vgscheckout.util.address.model.RegionType
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.view.checkout.core.BaseCheckoutFormView
import com.verygoodsecurity.vgscheckout.view.checkout.core.InputViewHolder
import com.verygoodsecurity.vgscheckout.view.checkout.core.OnInputViewStateChangedListener
import com.verygoodsecurity.vgscheckout.view.checkout.core.ViewState
import com.verygoodsecurity.vgscheckout.view.checkout.grid.DividerGridLayout

internal class AddressView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), OnInputViewStateChangedListener,
    AdapterView.OnItemSelectedListener, VGSDropdownEventSpinner.OnDropdownStateChangeListener {

    var onStateChangeListener: BaseCheckoutFormView.OnStateChangeListener? = null

    private val errorMessages: ObservableLinkedHashMap<Int, String?> = initErrorMessages()

    private var dividerGridLayout: DividerGridLayout
    private val errorText: MaterialTextView
    private val countriesRoot: ConstraintLayout
    private val countriesSpinner: VGSDropdownEventSpinner
    private val addressRoot: LinearLayoutCompat
    private val addressSubtitle: MaterialTextView
    private val addressInput: VGSEditText
    private val cityRoot: LinearLayoutCompat
    private val citySubtitle: MaterialTextView
    private val cityInput: VGSEditText
    private val regionInputRoot: LinearLayoutCompat
    private val regionSubtitle: MaterialTextView
    private val regionInput: VGSEditText
    private val postalAddressRoot: LinearLayoutCompat
    private val postalAddressSubtitle: MaterialTextView
    private val postalAddressInput: VGSEditText

    private val addressInputHolder: InputViewHolder
    private val cityInputHolder: InputViewHolder
    private val regionInputHolder: InputViewHolder
    private val postalAddressInputHolder: InputViewHolder

    private val defaultBorderColor by lazy { getColor(R.color.vgs_checkout_border_default) }
    private val focusedBorderColor by lazy { getColor(R.color.vgs_checkout_border_highlighted) }
    private val errorBorderColor by lazy { getColor(R.color.vgs_checkout_border_error) }
    private val errorDrawable by lazy { getDrawable(R.drawable.vgs_checkout_ic_error_white_10dp) }

    private var currentCountry: String = USA

    init {

        LayoutInflater.from(context).inflate(R.layout.vgs_checkout_address_view, this)

        orientation = VERTICAL

        dividerGridLayout = findViewById(R.id.dglRoot)
        errorText = findViewById(R.id.tvAddressError)
        countriesRoot = findViewById(R.id.clCountriesRoot)
        countriesSpinner = findViewById(R.id.spinnerCountries)
        addressRoot = findViewById(R.id.llcAddressRoot)
        addressSubtitle = findViewById(R.id.mtvAddressSubtitle)
        addressInput = findViewById(R.id.vgsEtAddress)
        cityRoot = findViewById(R.id.llcCityRoot)
        citySubtitle = findViewById(R.id.mtvCitySubtitle)
        cityInput = findViewById(R.id.vgsEtCity)
        regionInputRoot = findViewById(R.id.llcRegionInputRoot)
        regionSubtitle = findViewById(R.id.mtvRegionInputSubtitle)
        regionInput = findViewById(R.id.vgsEtRegion)
        postalAddressRoot = findViewById(R.id.llcPostalAddressRoot)
        postalAddressSubtitle = findViewById(R.id.mtvPostalAddressSubtitle)
        postalAddressInput = findViewById(R.id.vgsEtPostalAddress)

        addressInputHolder = InputViewHolder(addressInput, this)
        cityInputHolder = InputViewHolder(cityInput, this)
        regionInputHolder = InputViewHolder(regionInput, this)
        postalAddressInputHolder = InputViewHolder(postalAddressInput, this)

        setupCountries()
        setupRegions()
        setupPostalAddressCode()

        initInputValidation()
        initListeners()
    }

    override fun onStateChange(id: Int, state: ViewState) {
        updateGridColor()
        onStateChangeListener?.onStateChanged(this, isValid())
        if (state.shouldValidate()) {
            when (id) {
                R.id.vgsEtAddress -> updateAddressError(state)
                R.id.vgsEtCity -> updateCityError(state)
                R.id.vgsEtRegion -> updateRegionError(state)
                R.id.vgsEtPostalAddress -> updatePostalAddressError(state)
            }
        }
    }

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
        cityInput.setFieldName(options.cityOptions.fieldName)
        addressInput.setFieldName(options.addressOptions.fieldName)
        postalAddressInput.setFieldName(options.postalAddressOptions.fieldName)
        regionInput.setFieldName(options.regionOptions.fieldName)
    }

    fun getVGSViews() = arrayOf<VGSCollectView>(
        countriesSpinner,
        addressInput,
        cityInput,
        regionInput,
        postalAddressInput
    )

    fun isValid(): Boolean = isAllInputInvalid()

    private fun initErrorMessages(): ObservableLinkedHashMap<Int, String?> {
        val defaultMessages = linkedMapOf<Int, String?>(
            R.id.vgsEtAddress to null,
            R.id.vgsEtCity to null,
            R.id.vgsEtRegion to null,
            R.id.vgsEtPostalAddress to null
        )
        return object : ObservableLinkedHashMap<Int, String?>(defaultMessages) {

            override fun onChanged(map: ObservableLinkedHashMap<Int, String?>) {
                errorText.showWithText(map.firstNotNullOfOrNull { it.value })
                map.forEach {
                    val drawable = if (it.value == null) null else errorDrawable
                    when (it.key) {
                        R.id.vgsEtAddress -> addressSubtitle.setDrawableEnd(drawable)
                        R.id.vgsEtCity -> citySubtitle.setDrawableEnd(drawable)
                        R.id.vgsEtRegion -> regionSubtitle.setDrawableEnd(drawable)
                        R.id.vgsEtPostalAddress -> postalAddressSubtitle.setDrawableEnd(drawable)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        countriesSpinner.onDropdownStateChangeListener = this
    }

    private fun initInputValidation() {
        countriesSpinner.onDropdownStateChangeListener
        val minLengthValidationRule = VGSInfoRule.ValidationBuilder()
            .setAllowableMinLength(MIN_INPUT_LENGTH)
            .build()
        addressInput.addRule(minLengthValidationRule)
        regionInput.addRule(minLengthValidationRule)
        cityInput.addRule(minLengthValidationRule)
        regionInput.addRule(minLengthValidationRule)
    }

    private fun setupCountries() {
        val countries = AddressHelper.countries
        countriesSpinner.adapter = createSpinnerAdapter(countries)
        countriesSpinner.onItemSelectedListener = this
        countriesSpinner.setSelection(countries.indexOf(USA))
    }

    private fun setupRegions() {
        val regionType = AddressHelper.getRegionType(currentCountry)
        regionSubtitle.text = getRegionSubtitle(regionType)
        regionInput.setHint(getRegionHint(regionType))
    }

    private fun setupPostalAddressCode() {
        when (AddressHelper.getPostalAddressType(currentCountry)) {
            PostalAddressType.ZIP -> setupZipCode()
            PostalAddressType.POSTAL -> setupPostalCode()
        }
    }

    private fun setupZipCode() {
        postalAddressSubtitle.text = getString(R.string.vgs_checkout_address_info_zip_subtitle)
        postalAddressInput.setHint(getString(R.string.vgs_checkout_address_info_zip_hint))
        //TODO: Set validation
        postalAddressRoot.visible()
    }

    private fun setupPostalCode() {
        postalAddressSubtitle.text =
            getString(R.string.vgs_checkout_address_info_postal_code_subtitle)
        postalAddressInput.setHint(getString(R.string.vgs_checkout_address_info_postal_code_hint))
        //TODO: Set validation
        postalAddressRoot.visible()
    }

    private fun updateRegionError(state: ViewState) {
        errorMessages[R.id.vgsEtRegion] = if (state.isEmpty) {
            getRegionEmptyError(AddressHelper.getRegionType(currentCountry))
        } else {
            null
        }
    }

    private fun updateCityError(state: ViewState) {
        errorMessages[R.id.vgsEtCity] = if (state.isEmpty) {
            getString(R.string.vgs_checkout_address_info_city_empty_error)
        } else {
            null
        }
    }

    private fun updateAddressError(state: ViewState) {
        errorMessages[R.id.vgsEtAddress] = if (state.isEmpty) {
            getString(R.string.vgs_checkout_address_info_address_line1_empty_error)
        } else {
            null
        }
    }

    private fun updatePostalAddressError(state: ViewState) {
        val postalAddressType = AddressHelper.getPostalAddressType(currentCountry)
        errorMessages[R.id.vgsEtPostalAddress] = if (state.isEmpty) {
            getPostalAddressEmptyError(postalAddressType)
        } else if (!state.isValid) {
            getPostalAddressInvalidError(postalAddressType)
        } else {
            null
        }
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

    private fun updateGridColor() {
        dividerGridLayout.setGridColor(
            when {
                countriesSpinner.isDropdownOpened || isAnyInputFocused() -> focusedBorderColor
                isAnyInputInvalid() -> errorBorderColor
                else -> defaultBorderColor
            }
        )
    }

    private fun isAnyInputFocused(): Boolean {
        return addressInputHolder.state.hasFocus ||
                cityInputHolder.state.hasFocus ||
                regionInputHolder.state.hasFocus ||
                postalAddressInputHolder.state.hasFocus
    }

    private fun isAnyInputInvalid(): Boolean {
        return (addressInputHolder.state.isDirty && addressInputHolder.state.isValid.not()) ||
                (cityInputHolder.state.isDirty && cityInputHolder.state.isValid.not()) ||
                (regionInputHolder.state.isDirty && regionInputHolder.state.isValid.not()) ||
                (postalAddressInputHolder.state.isDirty && postalAddressInputHolder.state.isValid.not())
    }

    private fun isAllInputInvalid(): Boolean {
        return addressInputHolder.state.isValid ||
                cityInputHolder.state.isValid ||
                regionInputHolder.state.isValid ||
                postalAddressInputHolder.state.isValid
    }

    companion object {

        private const val MIN_INPUT_LENGTH = 1
    }
}