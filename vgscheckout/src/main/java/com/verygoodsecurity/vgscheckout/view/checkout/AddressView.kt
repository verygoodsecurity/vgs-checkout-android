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
import com.verygoodsecurity.vgscheckout.util.address.model.PostalAddressType
import com.verygoodsecurity.vgscheckout.util.extension.getString
import com.verygoodsecurity.vgscheckout.util.extension.gone
import com.verygoodsecurity.vgscheckout.util.extension.setVisibility
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

    private val addressOptionRoot: LinearLayoutCompat

    private val cityRoot: LinearLayoutCompat

    private val regionsRoot: ConstraintLayout
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

        addressOptionRoot = findViewById(R.id.llcAddressOptionRoot)

        cityRoot = findViewById(R.id.llcCityRoot)

        regionsRoot = findViewById(R.id.clRegionsRoot)
        regionsSpinner = findViewById(R.id.spinnerRegions)

        postalAddressRoot = findViewById(R.id.llcPostalAddressRoot)
        postalAddressSubtitle = findViewById(R.id.mtvPostalAddressSubtitle)
        postalAddressInput = findViewById(R.id.vgsEtPostalAddress)

        initCountries()
        initAddress()
        initAddressOptional()
        initCity()
        initRegions()
        initPostalAddressCode()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        with(parent?.getItemAtPosition(position) as? String) {
            initAddress(this)
            initAddressOptional(this)
            initCity(this)
            initRegions(this)
            initPostalAddressCode(this)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    fun applyConfig() {
        // TODO: Implement apply config (field names etc.)
    }

    private fun initCountries() {
        val countries = AddressHelper.getCountries()
        countriesSpinner.adapter = createSpinnerAdapter(countries)
        countriesSpinner.onItemSelectedListener = this
        countriesSpinner.setSelection(countries.indexOf(AddressHelper.getCurrentLocaleCountry()))
    }

    private fun initAddress(country: String? = AddressHelper.getCurrentLocaleCountry()) {
        addressRoot.setVisibility(AddressHelper.handledCountries.contains(country))
    }

    private fun initAddressOptional(country: String? = AddressHelper.getCurrentLocaleCountry()) {
        addressOptionRoot.setVisibility(AddressHelper.handledCountries.contains(country))

    }

    // TODO: Handle city/town for United Kingdom
    private fun initCity(country: String? = AddressHelper.getCurrentLocaleCountry()) {
        cityRoot.setVisibility(AddressHelper.handledCountries.contains(country))
    }

    // TODO: Handle province for canada
    private fun initRegions(country: String? = AddressHelper.getCurrentLocaleCountry()) {
        val countriesRegions = AddressHelper.getCountryRegions(context, country)
        regionsSpinner.adapter = createSpinnerAdapter(countriesRegions ?: emptyList())
        if (countriesRegions.isNullOrEmpty()) regionsRoot.gone() else regionsRoot.visible()
    }

    private fun initPostalAddressCode(country: String? = AddressHelper.getCurrentLocaleCountry()) {
        when (AddressHelper.getPostalAddressType(country)) {
            PostalAddressType.ZIP -> setupZipCode()
            PostalAddressType.POSTAL -> setupPostalCode()
            PostalAddressType.UNKNOWN -> postalAddressRoot.gone()
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
}