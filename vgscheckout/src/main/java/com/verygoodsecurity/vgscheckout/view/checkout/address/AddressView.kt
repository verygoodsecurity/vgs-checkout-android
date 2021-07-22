package com.verygoodsecurity.vgscheckout.view.checkout.address

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
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
import com.verygoodsecurity.vgscheckout.view.checkout.core.InputViewStateHolder
import com.verygoodsecurity.vgscheckout.view.checkout.core.OnInputViewStateChangedListener
import com.verygoodsecurity.vgscheckout.view.checkout.core.OnStateChangeListener
import com.verygoodsecurity.vgscheckout.view.checkout.core.ViewState
import com.verygoodsecurity.vgscheckout.view.checkout.grid.DividerGridLayout

internal class AddressView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), OnInputViewStateChangedListener,
    AdapterView.OnItemSelectedListener, VGSDropdownEventSpinner.OnDropdownStateChangeListener {

    var onStateChangeListener: OnStateChangeListener? = null

    private val errorMessages: ObservableLinkedHashMap<Int, String?> = initErrorMessages()

    private var dividerGridLayout: DividerGridLayout

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

    private val addressInputStateHolder: InputViewStateHolder
    private val cityInputStateHolder: InputViewStateHolder
    private val regionInputStateHolder: InputViewStateHolder
    private val postalAddressInputStateHolder: InputViewStateHolder
    private val inputViewsHolders: Array<InputViewStateHolder>

    private val defaultBorderColor by lazy { getColor(R.color.vgs_checkout_border_default) }
    private val focusedBorderColor by lazy { getColor(R.color.vgs_checkout_border_highlighted) }
    private val errorBorderColor by lazy { getColor(R.color.vgs_checkout_border_error) }
    private val errorDrawable by lazy { getDrawable(R.drawable.vgs_checkout_ic_error_white_10dp) }

    init {

        LayoutInflater.from(context).inflate(R.layout.vgs_checkout_address_view, this)

        dividerGridLayout = findViewById(R.id.dglRoot)

        countriesRoot = findViewById(R.id.clCountriesRoot)
        countriesSpinner = findViewById(R.id.spinnerCountries)

        addressRoot = findViewById(R.id.llcAddressRoot)
        addressSubtitle = findViewById(R.id.mtvAddressSubtitle)
        addressInput = findViewById(R.id.vgsEtAddressInput)

        cityRoot = findViewById(R.id.llcCityRoot)
        citySubtitle = findViewById(R.id.mtvCitySubtitle)
        cityInput = findViewById(R.id.vgsEtCityInput)

        regionInputRoot = findViewById(R.id.llcRegionInputRoot)
        regionSubtitle = findViewById(R.id.mtvRegionInputSubtitle)
        regionInput = findViewById(R.id.vgsEtRegionInput)

        postalAddressRoot = findViewById(R.id.llcPostalAddressRoot)
        postalAddressSubtitle = findViewById(R.id.mtvPostalAddressSubtitle)
        postalAddressInput = findViewById(R.id.vgsEtPostalAddressInput)

        addressInputStateHolder = InputViewStateHolder(addressInput, this)
        cityInputStateHolder = InputViewStateHolder(cityInput, this)
        regionInputStateHolder = InputViewStateHolder(regionInput, this)
        postalAddressInputStateHolder = InputViewStateHolder(postalAddressInput, this)
        inputViewsHolders = arrayOf(
            addressInputStateHolder,
            cityInputStateHolder,
            regionInputStateHolder,
            postalAddressInputStateHolder
        )

        setupCountries()
        setupRegions()
        setupPostalAddressCode()

        initInputValidation()
        initListeners()
    }

    override fun onStateChange(id: Int, state: ViewState) {
        updateGridColor()
        onStateChangeListener?.onStateChanged(this, isInputValid())
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        (parent?.getItemAtPosition(position) as? String)?.let {
            setupRegions(it)
            setupPostalAddressCode(it)
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

    private fun initErrorMessages(): ObservableLinkedHashMap<Int, String?> {
        val defaultMessages = linkedMapOf<Int, String?>(
            R.id.vgsEtAddressInput to null,
            R.id.vgsEtCityInput to null,
            R.id.vgsEtRegionInput to null,
            R.id.vgsEtPostalAddressInput to null
        )
        return object : ObservableLinkedHashMap<Int, String?>(defaultMessages) {

            override fun onChanged(map: ObservableLinkedHashMap<Int, String?>) {
                // TODO: Show error text
                map.forEach {
                    val drawable = if (it.value == null) null else null
                    when (it.key) {
                        R.id.vgsEtAddressInput -> addressSubtitle.setDrawableEnd(drawable)
                        R.id.vgsEtCityInput -> citySubtitle.setDrawableEnd(drawable)
                        R.id.vgsEtRegionInput -> regionSubtitle.setDrawableEnd(drawable)
                        R.id.mtvPostalAddressSubtitle ->
                            postalAddressSubtitle.setDrawableEnd(drawable)
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
        val countries = AddressHelper.handledCountries
        countriesSpinner.adapter = createSpinnerAdapter(countries)
        countriesSpinner.onItemSelectedListener = this
        countriesSpinner.setSelection(countries.indexOf(USA))
    }

    private fun setupRegions(country: String = USA) {
        val regionType = AddressHelper.getRegionType(country)
        regionSubtitle.text = getRegionSubtitle(regionType)
        regionInput.setHint(getRegionHint(regionType))
    }

    private fun setupPostalAddressCode(country: String = USA) {
        when (AddressHelper.getPostalAddressType(country)) {
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

    private fun updateGridColor() {
        dividerGridLayout.setGridColor(
            when {
                countriesSpinner.isDropdownOpened || inputViewsHolders.any { it.state.hasFocus } -> focusedBorderColor
                inputViewsHolders.any { !it.state.isValid && it.state.isDirty } -> errorBorderColor
                else -> defaultBorderColor
            }
        )
    }

    private fun isInputValid(): Boolean = inputViewsHolders.all { it.state.isValid }

    private fun isAnyInputFocused() =
        listOf(addressInput, cityInput, regionInput, postalAddressInput).any { it.isFocused }

    companion object {

        private const val MIN_INPUT_LENGTH = 1
    }
}