package com.verygoodsecurity.vgscheckout.view.checkout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.widget.VGSEditText
import com.verygoodsecurity.vgscheckout.util.address.AddressHelper
import com.verygoodsecurity.vgscheckout.util.address.UNITED_KINGDOM
import com.verygoodsecurity.vgscheckout.util.address.model.PostalAddressType
import com.verygoodsecurity.vgscheckout.util.address.model.RegionType
import com.verygoodsecurity.vgscheckout.util.extension.getString
import com.verygoodsecurity.vgscheckout.util.extension.gone
import com.verygoodsecurity.vgscheckout.util.extension.visible
import com.verygoodsecurity.vgscheckout.view.custom.DividerGridLayout

internal class AddressView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), AdapterView.OnItemSelectedListener {

    private val dividerGridLayout: DividerGridLayout

    private val countriesRoot: ConstraintLayout
    private val countriesSpinner: AppCompatSpinner

    private val addressRoot: LinearLayoutCompat

    private val cityRoot: LinearLayoutCompat
    private val citySubtitle: MaterialTextView

    private val regionInputRoot: LinearLayoutCompat
    private val regionInput: VGSEditText

    private val regionsRoot: ConstraintLayout
    private val regionsSubtitle: MaterialTextView
    private val regionsSpinner: AppCompatSpinner

    private val postalAddressRoot: LinearLayoutCompat
    private val postalAddressSubtitle: MaterialTextView
    private val postalAddressInput: VGSEditText

    init {

        LayoutInflater.from(context).inflate(R.layout.vgs_checkout_address_view, this)

        dividerGridLayout = findViewById(R.id.dglRoot)

        countriesRoot = findViewById(R.id.clCountriesRoot)
        countriesSpinner = findViewById(R.id.spinnerCountries)

        addressRoot = findViewById(R.id.llcAddressRoot)

        cityRoot = findViewById(R.id.llcCityRoot)
        citySubtitle = findViewById(R.id.mtvCitySubtitle)

        regionInputRoot = findViewById(R.id.llcRegionInputRoot)
        regionInput = findViewById(R.id.vgsEtRegionInput)

        regionsRoot = findViewById(R.id.clRegionsRoot)
        regionsSubtitle = findViewById(R.id.mtvRegionsSubtitle)
        regionsSpinner = findViewById(R.id.spinnerRegions)

        postalAddressRoot = findViewById(R.id.llcPostalAddressRoot)
        postalAddressSubtitle = findViewById(R.id.mtvPostalAddressSubtitle)
        postalAddressInput = findViewById(R.id.vgsEtPostalAddress)

        setupCountries()
        setupCity()
        setupRegions()
        setupPostalAddressCode()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        (parent?.getItemAtPosition(position) as? String)?.let {
            setupCity(it)
            setupRegions(it)
            setupPostalAddressCode(it)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    fun applyConfig() {
        // TODO: Implement apply config (field names etc.)
    }

    private fun setupCountries() {
        val countries = AddressHelper.getCountries()
        countriesSpinner.adapter = createSpinnerAdapter(countries)
        countriesSpinner.onItemSelectedListener = this
        countriesSpinner.setSelection(countries.indexOf(AddressHelper.getCurrentLocaleCountry()))
    }

    private fun setupCity(country: String = AddressHelper.getCurrentLocaleCountry()) {
        citySubtitle.text = getString(
            if (country == UNITED_KINGDOM) R.string.vgs_checkout_town_city_hint else R.string.vgs_checkout_city_hint
        )
    }

    private fun setupRegions(country: String = AddressHelper.getCurrentLocaleCountry()) {
        val regions = AddressHelper.getCountryRegions(context, country)
        if (regions.isNullOrEmpty()) setupRegionsInput() else setupRegionsSpinner(country, regions)
    }

    private fun setupRegionsInput() {
        regionInputRoot.visible()
        regionsRoot.gone()
    }

    private fun setupRegionsSpinner(country: String, regions: List<String>) {
        regionsSpinner.adapter = createSpinnerAdapter(regions)
        regionsSubtitle.text = getRegionSubtitle(AddressHelper.getRegionType(country))
        regionsRoot.visible()
        regionInputRoot.gone()
    }

    private fun setupPostalAddressCode(country: String = AddressHelper.getCurrentLocaleCountry()) {
        when (AddressHelper.getPostalAddressType(country)) {
            PostalAddressType.ZIP -> setupZipCode()
            PostalAddressType.POSTAL -> setupPostalCode()
        }
    }

    private fun setupZipCode() {
        postalAddressSubtitle.text = getString(R.string.vgs_checkout_zip_code_subtitle)
        postalAddressInput.setHint(getString(R.string.vgs_checkout_zip_code_hint))
        //TODO: Set validation
        postalAddressRoot.visible()
    }

    private fun setupPostalCode() {
        postalAddressSubtitle.text = getString(R.string.vgs_checkout_postal_code_subtitle)
        postalAddressInput.setHint(getString(R.string.vgs_checkout_postal_code_hint))
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
            RegionType.STATE -> R.string.vgs_checkout_region_state_subtitle
            RegionType.PROVINCE -> R.string.vgs_checkout_region_province_subtitle
            RegionType.UNKNOWN -> R.string.vgs_checkout_region_input_subtitle
        }
    )
}