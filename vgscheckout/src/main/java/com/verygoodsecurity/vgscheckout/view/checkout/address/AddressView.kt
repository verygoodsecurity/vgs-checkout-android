package com.verygoodsecurity.vgscheckout.view.checkout.address

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
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
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.view.checkout.address.adapter.CountryAdapter
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.country.CountriesHelper
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.country.model.Country
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.emptyError
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.hint
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.invalidError
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.subtitle
import com.verygoodsecurity.vgscheckout.view.checkout.core.InputViewStateHolder
import com.verygoodsecurity.vgscheckout.view.checkout.core.OnInputViewStateChangedListener
import com.verygoodsecurity.vgscheckout.view.checkout.core.OnStateChangeListener
import com.verygoodsecurity.vgscheckout.view.checkout.core.ViewState
import com.verygoodsecurity.vgscheckout.view.checkout.grid.DividerGridLayout

internal class AddressView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), OnInputViewStateChangedListener,
    AdapterView.OnItemSelectedListener, VGSDropdownEventSpinner.OnDropdownStateChangeListener {

    var onStateChangeListener: OnStateChangeListener? = null

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

    private val addressInputStateHolder: InputViewStateHolder
    private val cityInputStateHolder: InputViewStateHolder
    private val regionInputStateHolder: InputViewStateHolder
    private val postalAddressInputStateHolder: InputViewStateHolder
    private val inputViewsHolders: Array<InputViewStateHolder>

    private val defaultBorderColor by lazy { getColor(R.color.vgs_checkout_border_default) }
    private val focusedBorderColor by lazy { getColor(R.color.vgs_checkout_border_highlighted) }
    private val errorBorderColor by lazy { getColor(R.color.vgs_checkout_border_error) }
    private val errorDrawable by lazy { getDrawable(R.drawable.vgs_checkout_ic_error_white_10dp) }

    private var currentCountry: Country = CountriesHelper.getCountry(CountriesHelper.ISO.USA)

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
        (parent?.getItemAtPosition(position) as? Country)?.let {
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

    fun isValid(): Boolean = inputViewsHolders.all { it.state.isValid }

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
        with(VGSInfoRule.ValidationBuilder().setAllowableMinLength(MIN_INPUT_LENGTH).build()) {
            addressInput.addRule(this)
            regionInput.addRule(this)
            cityInput.addRule(this)
            regionInput.addRule(this)
        }
    }

    private fun setupCountries() {
        val countries = CountriesHelper.getHandledCountries()
        countriesSpinner.adapter = CountryAdapter(context, countries)
        countriesSpinner.onItemSelectedListener = this
        countriesSpinner.setSelection(countries.indexOf(currentCountry))
    }

    private fun setupRegions() {
        regionSubtitle.text = getString(currentCountry.regionType.subtitle())
        regionInput.setHint(getString(currentCountry.regionType.hint()))
    }

    private fun setupPostalAddressCode() {
        postalAddressSubtitle.text = getString(currentCountry.postalAddressType.subtitle())
        postalAddressInput.setHint(getString(currentCountry.postalAddressType.hint()))
        postalAddressInput.addRule(
            VGSInfoRule.ValidationBuilder().setRegex(currentCountry.postalAddressRegex).build()
        )
        postalAddressInput.reInvalidateInput()
    }

    private fun updateGridColor() {
        dividerGridLayout.setGridColor(
            when {
                countriesSpinner.isDropdownOpened || inputViewsHolders.any { it.state.hasFocus } -> focusedBorderColor
                inputViewsHolders.any { !it.state.isValid && it.state.isDirty } -> errorBorderColor
                else -> defaultBorderColor
            }
        )
    }

    private fun updateRegionError(state: ViewState) {
        errorMessages[R.id.vgsEtRegion] = if (state.isEmpty) {
            getString(currentCountry.regionType.emptyError())
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
        errorMessages[R.id.vgsEtPostalAddress] = if (state.isEmpty) {
            getString(currentCountry.postalAddressType.emptyError())
        } else if (!state.isValid) {
            getString(currentCountry.postalAddressType.invalidError())
        } else {
            null
        }
    }

    companion object {

        private const val MIN_INPUT_LENGTH = 1
    }
}