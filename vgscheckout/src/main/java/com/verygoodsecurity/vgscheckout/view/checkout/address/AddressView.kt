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
import com.verygoodsecurity.vgscheckout.collect.widget.VGSDropdownEventSpinner
import com.verygoodsecurity.vgscheckout.collect.widget.VGSEditText
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.util.address.AddressHelper
import com.verygoodsecurity.vgscheckout.util.address.USA
import com.verygoodsecurity.vgscheckout.util.address.model.PostalAddressType
import com.verygoodsecurity.vgscheckout.util.address.model.RegionType
import com.verygoodsecurity.vgscheckout.util.extension.getColor
import com.verygoodsecurity.vgscheckout.util.extension.getString
import com.verygoodsecurity.vgscheckout.util.extension.visible
import com.verygoodsecurity.vgscheckout.view.checkout.address.model.State
import com.verygoodsecurity.vgscheckout.view.checkout.grid.DividerGridLayout
import kotlin.properties.Delegates

internal class AddressView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), AdapterView.OnItemSelectedListener,
    View.OnFocusChangeListener, VGSDropdownEventSpinner.OnDropdownStateChangeListener {

    private lateinit var dividerGridLayout: DividerGridLayout

    private val countriesRoot: ConstraintLayout
    private val countriesSpinner: VGSDropdownEventSpinner

    private val addressRoot: LinearLayoutCompat
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

    private var state: State by Delegates.observable(State.DEFAULT) { _, _, new ->
        if (this::dividerGridLayout.isInitialized) {
            when (new) {
                State.FOCUSED -> dividerGridLayout.setGridColor(getColor(R.color.vgs_checkout_border_highlighted))
                State.ERROR -> dividerGridLayout.setGridColor(getColor(R.color.vgs_checkout_border_error))
                State.DEFAULT -> dividerGridLayout.setGridColor(getColor(R.color.vgs_checkout_border_default))
            }
        }
    }

    init {

        LayoutInflater.from(context).inflate(R.layout.vgs_checkout_address_view, this)

        dividerGridLayout = findViewById(R.id.dglRoot)

        countriesRoot = findViewById(R.id.clCountriesRoot)
        countriesSpinner = findViewById(R.id.spinnerCountries)

        addressRoot = findViewById(R.id.llcAddressRoot)
        addressInput = findViewById(R.id.vgsEtAddressInput)

        cityRoot = findViewById(R.id.llcCityRoot)
        citySubtitle = findViewById(R.id.mtvCitySubtitle)
        cityInput = findViewById(R.id.vgsEtCityInput)

        regionInputRoot = findViewById(R.id.llcRegionInputRoot)
        regionSubtitle = findViewById(R.id.mtvRegionInputSubtitle)
        regionInput = findViewById(R.id.vgsEtRegionInput)

        postalAddressRoot = findViewById(R.id.llcPostalAddressRoot)
        postalAddressSubtitle = findViewById(R.id.mtvPostalAddressSubtitle)
        postalAddressInput = findViewById(R.id.vgsEtPostalAddress)

        setupCountries()
        setupRegions()
        setupPostalAddressCode()
        initListeners()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        (parent?.getItemAtPosition(position) as? String)?.let {
            setupRegions(it)
            setupPostalAddressCode(it)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        state = if (isAnyInputFocused()) {
            State.FOCUSED
        } else {
            State.DEFAULT
        }
    }

    override fun onDropdownOpened() {
        state = State.FOCUSED
    }

    override fun onDropdownClosed() {
        if (isAnyInputFocused().not()) state = State.DEFAULT
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

    private fun initListeners() {
        addressInput.onFocusChangeListener = this
        cityInput.onFocusChangeListener = this
        regionInput.onFocusChangeListener = this
        postalAddressInput.onFocusChangeListener = this

        countriesSpinner.onDropdownStateChangeListener = this
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

    private fun isAnyInputFocused(): Boolean {
        return listOf(addressInput, cityInput, regionInput, postalAddressInput).any { it.isFocused }
    }
}