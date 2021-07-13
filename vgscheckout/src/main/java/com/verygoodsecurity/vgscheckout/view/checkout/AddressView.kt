package com.verygoodsecurity.vgscheckout.view.checkout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatSpinner
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.util.address.AddressHelper

internal class AddressView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), AdapterView.OnItemSelectedListener {

    private val spinnerCountries: AppCompatSpinner

    init {

        LayoutInflater.from(context).inflate(R.layout.vgs_checkout_address_view, this)

        spinnerCountries = findViewById(R.id.spinnerCountries)

        initCountriesSpinner(spinnerCountries)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("Test", "item = ${parent?.getItemAtPosition(position)}")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun initCountriesSpinner(countriesSpinner: AppCompatSpinner) {
        val countries = AddressHelper.getCountries()
        countriesSpinner.adapter = createSpinnerAdapter(countries)
        countriesSpinner.onItemSelectedListener = this
        countriesSpinner.setSelection(countries.indexOf(AddressHelper.getCurrentLocaleCountry()))
    }

    private fun createSpinnerAdapter(data: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(context, R.layout.vgs_checkout_spinner_header_layout, data).apply {
            setDropDownViewResource(R.layout.vgs_checkout_spinner_item)
        }
    }
}