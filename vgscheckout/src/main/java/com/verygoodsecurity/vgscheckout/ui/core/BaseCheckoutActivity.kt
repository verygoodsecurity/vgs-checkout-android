package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.CancelEvent
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.RequestEvent
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.collect.widget.*
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.util.CollectProvider
import com.verygoodsecurity.vgscheckout.util.country.CountriesHelper
import com.verygoodsecurity.vgscheckout.util.country.model.Country
import com.verygoodsecurity.vgscheckout.util.country.model.PostalAddressType
import com.verygoodsecurity.vgscheckout.util.extension.*

private const val BILLING_ADDRESS_MIN_CHARS_COUNT = 1
private const val ICON_ALPHA_ENABLED = 1f
private const val ICON_ALPHA_DISABLED = 0.5f

internal abstract class BaseCheckoutActivity<C : CheckoutConfiguration> :
    AppCompatActivity(), VgsCollectResponseListener, InputFieldView.OnTextChangedListener {

    protected val config: C by lazy { resolveConfig(intent) }

    protected val collect: VGSCollect by lazy {
        CollectProvider().get(this, config).apply {
            addOnResponseListeners(this@BaseCheckoutActivity)
        }
    }

    private val cardDetailsMtv: MaterialTextView by lazy { findViewById(R.id.mtvCardDetailsTitle) }
    private val cardDetailsLL: LinearLayoutCompat by lazy { findViewById(R.id.llCardDetails) }
    private val cardHolderTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilCardHolder) }
    private val cardHolderEt: PersonNameEditText by lazy { findViewById(R.id.vgsEtCardHolder) }
    private val cardNumberTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilCardNumber) }
    private val cardNumberEt: VGSCardNumberEditText by lazy { findViewById(R.id.vgsEtCardNumber) }
    private val expirationDateTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilExpirationDate) }
    private val expirationDateEt: ExpirationDateEditText by lazy { findViewById(R.id.vgsEtExpirationDate) }
    private val securityCodeTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilSecurityCode) }
    private val securityCodeEt: CardVerificationCodeEditText by lazy { findViewById(R.id.vgsEtSecurityCode) }

    private val billingAddressMtv: MaterialTextView by lazy { findViewById(R.id.mtvBillingAddressTitle) }
    private val billingAddressLL: LinearLayoutCompat by lazy { findViewById(R.id.llBillingAddress) }
    private val countryTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilCountry) }
    private val countryEt: VGSEditText by lazy { findViewById(R.id.vgsEtCountry) }
    private val addressTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilAddress) }
    private val addressEt: VGSEditText by lazy { findViewById(R.id.vgsEtAddress) }
    private val optionalAddressTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilAddressOptional) }
    private val optionalAddressEt: VGSEditText by lazy { findViewById(R.id.vgsEtAddressOptional) }
    private val cityTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilCity) }
    private val cityEt: VGSEditText by lazy { findViewById(R.id.vgsEtCity) }
    private val postalAddressTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilPostalAddress) }
    private val postalAddressEt: VGSEditText by lazy { findViewById(R.id.vgsEtPostalAddress) }

    private val saveCardButton: MaterialButton by lazy { findViewById(R.id.mbSaveCard) }

    private val billingAddressValidationRule: VGSInfoRule by lazy {
        val countryValidationRegex = CountriesHelper.countries
            .map { it.name to it.code }
            .unzip()
            .toList()
            .flatten()
            .joinTo(StringBuilder("\\b("), separator = "|", postfix = ")\\b")
            .toString()

        VGSInfoRule.ValidationBuilder()
            .setAllowableMinLength(BILLING_ADDRESS_MIN_CHARS_COUNT)
            .setRegex(countryValidationRegex)
            .build()
    }

    private var selectedCountry: Country = CountriesHelper.countries.first()

    abstract fun resolveConfig(intent: Intent): C

    abstract fun handleSaveCard()

    abstract fun hasCustomHeaders(): Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableScreenshots()
        setContentView(R.layout.vgs_checkout_activity)
        initView(savedInstanceState)
    }

    override fun onBackPressed() {
        config.analyticTracker.log(CancelEvent)
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResponse(response: VGSResponse?) {
        (response as? VGSResponse.ErrorResponse)?.let {
            if (it.code == VGSError.NO_NETWORK_CONNECTIONS.code) {
                setInputViewsEnabled(true)
                updateSaveButtonState(false)
                showNetworkConnectionErrorSnackBar()
                return
            }
        }
        val resultBundle = CheckoutResultContract.Result(response?.toCheckoutResult()).toBundle()
        setResult(Activity.RESULT_OK, Intent().putExtras(resultBundle))
        finish()
    }

    override fun onTextChange(view: InputFieldView, isEmpty: Boolean) {
        when (view.id) {
            R.id.vgsEtCardHolder -> cardHolderTil.setError(null)
            R.id.vgsEtCardNumber -> cardNumberTil.setError(null)
            R.id.vgsEtExpirationDate -> expirationDateTil.setError(null)
            R.id.vgsEtSecurityCode -> securityCodeTil.setError(null)
            R.id.vgsEtSecurityCode -> countryTil.setError(null)
            R.id.vgsEtAddress -> addressTil.setError(null)
            R.id.vgsEtAddressOptional -> optionalAddressTil.setError(null)
            R.id.vgsEtCity -> cityTil.setError(null)
            R.id.vgsEtPostalAddress -> postalAddressTil.setError(null)
        }
    }

    @CallSuper
    protected open fun initView(savedInstanceState: Bundle?) {
        initToolbar()
        initViews()
    }

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.mtToolbar))
    }

    private fun initViews() {
        initCardDetailsViews()
        initBillingAddressViews()
        initSaveButton()
    }

    private fun initCardDetailsViews() {
        with(config.formConfig.cardOptions) {
            initCardHolderView(cardHolderOptions)
            initCardNumberView(cardNumberOptions)
            initExpirationDateView(expirationDateOptions)
            initSecurityCodeView(cvcOptions)
        }
    }

    private fun initCardHolderView(options: VGSCheckoutCardHolderOptions) {
        if (options.visibility == VGSCheckoutFieldVisibility.HIDDEN) {
            cardHolderTil.gone()
            return
        }
        cardHolderEt.setFieldName(options.fieldName)
        cardHolderEt.addOnTextChangeListener(this)
        collect.bindView(cardHolderEt)
    }

    private fun initCardNumberView(options: VGSCheckoutCardNumberOptions) {
        cardNumberEt.setFieldName(options.fieldName)
        cardNumberEt.setValidCardBrands(options.cardBrands)
        cardNumberEt.setIsCardBrandPreviewHidden(options.isIconHidden)
        cardNumberEt.addOnTextChangeListener(this)
        collect.bindView(cardNumberEt)
    }

    private fun initExpirationDateView(options: VGSCheckoutExpirationDateOptions) {
        expirationDateEt.setDateRegex(options.inputFormatRegex)
        expirationDateEt.setOutputRegex(options.outputFormatRegex)
        expirationDateEt.setSerializer(
            options.dateSeparateSerializer?.toCollectDateSeparateSerializer()
        )
        expirationDateEt.addOnTextChangeListener(this)
        expirationDateEt.setFieldName(options.fieldName)
        collect.bindView(expirationDateEt)
    }

    private fun initSecurityCodeView(options: VGSCheckoutCVCOptions) {
        securityCodeEt.setFieldName(options.fieldName)
        securityCodeEt.setIsPreviewIconHidden(options.isIconHidden)
        securityCodeEt.addOnTextChangeListener(this)
        collect.bindView(securityCodeEt)
    }

    private fun initBillingAddressViews() {
        if (config.formConfig.addressOptions.visibility == VGSCheckoutBillingAddressVisibility.HIDDEN) {
            findViewById<LinearLayoutCompat>(R.id.llBillingAddress).gone()
            return
        }
        with(config.formConfig.addressOptions) {
            initCountryView(countryOptions)
            initAddressView(addressOptions)
            initOptionalAddressView(optionalAddressOptions)
            initCityView(cityOptions)
            initPostalAddressView(postalAddressOptions)
        }
    }

    private fun initCountryView(options: VGSCheckoutCountryOptions) {
        countryEt.setFieldName(options.fieldName)
        countryEt.addRule(billingAddressValidationRule)
        countryEt.isFocusable = false
        countryEt.setOnClickListener { showCountrySelectionDialog() }
        countryEt.addOnTextChangeListener(this)
        collect.bindView(countryEt)
        updateCountryView()
    }

    private fun updateCountryView() {
        countryEt.setText(selectedCountry.name)
    }

    private fun initAddressView(options: VGSCheckoutAddressOptions) {
        addressEt.setFieldName(options.fieldName)
        addressEt.addRule(billingAddressValidationRule)
        addressEt.addOnTextChangeListener(this)
        collect.bindView(addressEt)
    }

    private fun initOptionalAddressView(options: VGSCheckoutOptionalAddressOptions) {
        optionalAddressEt.setFieldName(options.fieldName)
        optionalAddressEt.addRule(billingAddressValidationRule)
        collect.bindView(optionalAddressEt)
    }

    private fun initCityView(options: VGSCheckoutCityOptions) {
        cityEt.setFieldName(options.fieldName)
        cityEt.addRule(billingAddressValidationRule)
        cityEt.addOnTextChangeListener(this)
        collect.bindView(cityEt)
    }

    private fun initPostalAddressView(options: VGSCheckoutPostalAddressOptions) {
        postalAddressEt.setFieldName(options.fieldName)
        postalAddressEt.addOnTextChangeListener(this)
        postalAddressEt.setOnEditorActionListener(object : InputFieldView.OnEditorActionListener {

            override fun onEditorAction(v: View?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveCard()
                    return true
                }
                return false
            }
        })
        collect.bindView(postalAddressEt)
        updatePostalAddressView()
    }

    private fun updatePostalAddressView() {
        postalAddressTil.setHint(getString(getPostalAddressHint()))
        postalAddressTil.setError(null)
        postalAddressEt.addRule(getPostalAddressValidationRule())
    }

    @StringRes
    private fun getPostalAddressHint() = when (selectedCountry.postalAddressType) {
        PostalAddressType.ZIP -> R.string.vgs_checkout_address_info_zip_hint
        PostalAddressType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_hint
    }

    @StringRes
    private fun getPostalAddressEmptyErrorMessage() = when (selectedCountry.postalAddressType) {
        PostalAddressType.ZIP -> R.string.vgs_checkout_address_info_zipcode_empty_error
        PostalAddressType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_empty_error
    }

    @StringRes
    private fun getPostalAddressInvalidErrorMessage() = when (selectedCountry.postalAddressType) {
        PostalAddressType.ZIP -> R.string.vgs_checkout_address_info_zipcode_invalid_error
        PostalAddressType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_invalid_error
    }

    private fun getPostalAddressValidationRule() = VGSInfoRule.ValidationBuilder()
        .setRegex(selectedCountry.postalAddressRegex)
        .build()

    private fun initSaveButton() {
        saveCardButton.setOnClickListener { saveCard() }
    }

    private fun saveCard() {
        hideSoftKeyboard()
        val invalidFields = getInvalidFieldsTypes()
        val isInputValid = invalidFields.isEmpty()
        if (isInputValid) {
            setInputViewsEnabled(false)
            updateSaveButtonState(true)
            handleSaveCard()
        }
        sendRequestEvent(isInputValid, invalidFields)
    }

    private fun getInvalidFieldsTypes(): List<String> {
        val cardDetailsInvalidFields = getCardDetailsInvalidFields()
        val billingAddressInvalidFields = getBillingAddressInvalidFields()
        return cardDetailsInvalidFields + billingAddressInvalidFields
    }

    private fun getCardDetailsInvalidFields(): List<String> {
        val result = mutableListOf<String>()

        val isCardHolderValid = config.isCardHolderHidden() || validate(
            cardHolderEt,
            cardHolderTil,
            R.string.vgs_checkout_card_holder_empty_error
        )
        result.addIf(!isCardHolderValid, cardHolderEt.getAnalyticsName())

        val isCardNumberValid = validate(
            cardNumberEt,
            cardNumberTil,
            R.string.vgs_checkout_card_number_empty_error,
            R.string.vgs_checkout_card_number_invalid_error
        )
        result.addIf(!isCardNumberValid, cardNumberEt.getAnalyticsName())

        val isExpirationDateValid = validate(
            expirationDateEt,
            expirationDateTil,
            R.string.vgs_checkout_card_expiration_date_empty_error,
            R.string.vgs_checkout_card_expiration_date_invalid_error
        )
        result.addIf(!isExpirationDateValid, expirationDateEt.getAnalyticsName())

        val isSecurityCodeValid = validate(
            securityCodeEt,
            securityCodeTil,
            R.string.vgs_checkout_security_code_empty_error,
            R.string.vgs_checkout_security_code_invalid_error
        )
        result.addIf(!isSecurityCodeValid, securityCodeEt.getAnalyticsName())

        return result
    }

    private fun getBillingAddressInvalidFields(): List<String> {
        if (config.formConfig.addressOptions.visibility == VGSCheckoutBillingAddressVisibility.HIDDEN) {
            return emptyList()
        }

        val result = mutableListOf<String>()

        val isAddressValid = validate(
            addressEt,
            addressTil,
            R.string.vgs_checkout_address_info_line1_empty_error
        )
        result.addIf(!isAddressValid, addressEt.getAnalyticsName())

        val isCityValid = validate(
            cityEt,
            cityTil,
            R.string.vgs_checkout_address_info_city_empty_error
        )
        result.addIf(!isCityValid, cityEt.getAnalyticsName())

        val postalAddressValid = validate(
            postalAddressEt,
            postalAddressTil,
            getPostalAddressEmptyErrorMessage(),
            getPostalAddressInvalidErrorMessage()
        )
        result.addIf(!postalAddressValid, postalAddressEt.getAnalyticsName())

        val isCountryValid = validate(
            countryEt,
            countryTil,
            R.string.vgs_checkout_address_info_country_empty_error,
            R.string.vgs_checkout_address_info_country_invalid_error
        )
        result.addIf(!isCountryValid, countryEt.getAnalyticsName())

        return result
    }

    private fun validate(
        target: InputFieldView,
        parent: VGSTextInputLayout,
        @StringRes emptyError: Int,
        @StringRes invalidError: Int? = null
    ): Boolean = when {
        target.getFieldState()?.isEmpty == true -> {
            parent.setError(emptyError)
            false
        }
        target.getFieldState()?.isValid == false -> {
            invalidError?.let { parent.setError(it) }
            false
        }
        else -> {
            parent.setError(null)
            true
        }
    }

    private fun setInputViewsEnabled(isEnabled: Boolean) {
        cardDetailsLL.setEnabled(isEnabled, true, cardDetailsMtv)
        billingAddressLL.setEnabled(isEnabled, true, billingAddressMtv)
        val alpha = if (isEnabled) ICON_ALPHA_ENABLED else ICON_ALPHA_DISABLED
        cardNumberEt.setDrawablesAlphaColorFilter(alpha)
        securityCodeEt.setDrawablesAlphaColorFilter(alpha)
    }

    private fun updateSaveButtonState(isLoading: Boolean) {
        with(saveCardButton) {
            isClickable = !isLoading
            if (isLoading) {
                text = getString(R.string.vgs_checkout_save_button_processing_title)
                icon = getDrawableCompat(R.drawable.vgs_checkout_ic_loading_animated_white_16)
                (icon as? Animatable)?.start()
            } else {
                saveCardButton.text = getString(R.string.vgs_checkout_save_button_save_card_title)
                icon = null
            }
        }
    }

    private fun showCountrySelectionDialog() {
        val countries = CountriesHelper.countries
        val countryNames = countries.map { it.name }.toTypedArray()
        val selectedIndex = if(selectedCountry.name.equals(countryEt.getText())) {
            countries.indexOf(selectedCountry)
        } else {
            -1
        }

        var selected = -1
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.vgs_checkout_select_country_dialog_title)
            .setSingleChoiceItems(countryNames, selectedIndex) { _, which -> selected = which }
            .setNegativeButton(
                R.string.vgs_checkout_select_country_dialog_negative_button_title,
                null
            )
            .setPositiveButton(R.string.vgs_checkout_select_country_dialog_positive_button_title) { _, _ ->
                countries.getOrNull(selected)?.let {
                    selectedCountry = it
                    updateCountryView()
                    updatePostalAddressView()
                }
            }
            .show()
    }

    private fun showNetworkConnectionErrorSnackBar() {
        val message = getString(R.string.vgs_checkout_no_network_error)
        Snackbar.make(findViewById(R.id.llRoot), message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.vgs_checkout_no_network_retry)) { saveCard() }
            .show()
    }

    private fun sendRequestEvent(isSuccessful: Boolean, invalidFields: List<String>) {
        with(config) {
            analyticTracker.log(
                RequestEvent(
                    isSuccessful,
                    routeConfig.hostnamePolicy is VGSCheckoutHostnamePolicy.CustomHostname,
                    routeConfig.requestOptions.extraData.isNotEmpty(),
                    hasCustomHeaders(),
                    routeConfig.requestOptions.mergePolicy,
                    invalidFields
                )
            )
        }
    }
}