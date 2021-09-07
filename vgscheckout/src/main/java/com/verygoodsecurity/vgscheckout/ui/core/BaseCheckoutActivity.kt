package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.content.Intent
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
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.collect.widget.*
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutSecurityCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.util.country.CountriesHelper
import com.verygoodsecurity.vgscheckout.util.country.model.Country
import com.verygoodsecurity.vgscheckout.util.country.model.PostalAddressType
import com.verygoodsecurity.vgscheckout.util.extension.*

private const val BILLING_ADDRESS_MIN_CHARS_COUNT = 1

internal abstract class BaseCheckoutActivity<C : CheckoutConfiguration> :
    AppCompatActivity(), VgsCollectResponseListener, InputFieldView.OnTextChangedListener {

    protected val config: C by lazy { resolveConfig(intent) }

    protected val collect by lazy {
        resolveCollect().apply {
            addOnResponseListeners(this@BaseCheckoutActivity)
        }
    }

    private lateinit var cardHolderTil: VGSTextInputLayout
    private lateinit var cardHolderEt: PersonNameEditText
    private lateinit var cardNumberTil: VGSTextInputLayout
    private lateinit var cardNumberEt: VGSCardNumberEditText
    private lateinit var expirationDateTil: VGSTextInputLayout
    private lateinit var expirationDateEt: ExpirationDateEditText
    private lateinit var securityCodeTil: VGSTextInputLayout
    private lateinit var securityCodeEt: CardVerificationCodeEditText

    private lateinit var countryTil: VGSTextInputLayout
    private lateinit var countryEt: VGSEditText
    private lateinit var addressTil: VGSTextInputLayout
    private lateinit var addressEt: VGSEditText
    private lateinit var optionalAddressTil: VGSTextInputLayout
    private lateinit var optionalAddressEt: VGSEditText
    private lateinit var cityTil: VGSTextInputLayout
    private lateinit var cityEt: VGSEditText
    private lateinit var postalAddressTil: VGSTextInputLayout
    private lateinit var postalAddressEt: VGSEditText

    private lateinit var saveCardButton: MaterialButton

    private val billingAddressValidationRule: VGSInfoRule by lazy {
        VGSInfoRule.ValidationBuilder()
            .setAllowableMinLength(BILLING_ADDRESS_MIN_CHARS_COUNT)
            .build()
    }

    private var selectedCountry: Country = CountriesHelper.countries.first()

    abstract fun resolveConfig(intent: Intent): C

    abstract fun resolveCollect(): VGSCollect

    abstract fun handleSaveCard()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableScreenshots()
        setContentView(R.layout.vgs_checkout_activity)
        initView(savedInstanceState)
    }

    override fun onBackPressed() {
        handleBackPressWithConfirmation {
            super.onBackPressed()
            setResult(Activity.RESULT_CANCELED)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResponse(response: VGSResponse?) {
        (response as? VGSResponse.ErrorResponse)?.let {
            if (it.errorCode == VGSError.NO_NETWORK_CONNECTIONS.code) {
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
            initSecurityCodeView(securityCodeOptions)
        }
    }

    private fun initCardHolderView(options: VGSCheckoutCardHolderOptions) {
        cardHolderTil = findViewById(R.id.vgsTilCardHolder)
        cardHolderEt = findViewById(R.id.vgsEtCardHolder)
        if (options.visibility == VGSCheckoutFieldVisibility.HIDDEN) {
            cardHolderTil.gone()
            return
        }
        cardHolderEt.setFieldName(options.fieldName)
        cardHolderEt.addOnTextChangeListener(this)
        collect.bindView(cardHolderEt)
    }

    private fun initCardNumberView(options: VGSCheckoutCardNumberOptions) {
        cardNumberTil = findViewById(R.id.vgsTilCardNumber)
        cardNumberEt = findViewById(R.id.vgsEtCardNumber)
        cardNumberEt.setFieldName(options.fieldName)
        cardNumberEt.setValidCardBrands(options.cardBrands)
        cardNumberEt.setIsCardBrandPreviewHidden(options.isIconHidden)
        cardNumberEt.addOnTextChangeListener(this)
        collect.bindView(cardNumberEt)
    }

    private fun initExpirationDateView(options: VGSCheckoutExpirationDateOptions) {
        expirationDateTil = findViewById(R.id.vgsTilExpirationDate)
        expirationDateEt = findViewById(R.id.vgsEtExpirationDate)
        expirationDateEt.setFieldName(options.fieldName)
        expirationDateEt.addOnTextChangeListener(this)
        collect.bindView(expirationDateEt)
    }

    private fun initSecurityCodeView(options: VGSCheckoutSecurityCodeOptions) {
        securityCodeTil = findViewById(R.id.vgsTilSecurityCode)
        securityCodeEt = findViewById(R.id.vgsEtSecurityCode)
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
        countryTil = findViewById(R.id.vgsTilCountry)
        countryEt = findViewById(R.id.vgsEtCountry)
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
        addressTil = findViewById(R.id.vgsTilAddress)
        addressEt = findViewById(R.id.vgsEtAddress)
        addressEt.setFieldName(options.fieldName)
        addressEt.addRule(billingAddressValidationRule)
        addressEt.addOnTextChangeListener(this)
        collect.bindView(addressEt)
    }

    private fun initOptionalAddressView(options: VGSCheckoutOptionalAddressOptions) {
        optionalAddressTil = findViewById(R.id.vgsTilAddressOptional)
        optionalAddressEt = findViewById(R.id.vgsEtAddressOptional)
        optionalAddressEt.setFieldName(options.fieldName)
        optionalAddressEt.addRule(billingAddressValidationRule)
        collect.bindView(optionalAddressEt)
    }

    private fun initCityView(options: VGSCheckoutCityOptions) {
        cityTil = findViewById(R.id.vgsTilCity)
        cityEt = findViewById(R.id.vgsEtCity)
        cityEt.setFieldName(options.fieldName)
        cityEt.addRule(billingAddressValidationRule)
        cityEt.addOnTextChangeListener(this)
        collect.bindView(cityEt)
    }

    private fun initPostalAddressView(options: VGSCheckoutPostalAddressOptions) {
        postalAddressTil = findViewById(R.id.vgsTilPostalAddress)
        postalAddressEt = findViewById(R.id.vgsEtPostalAddress)
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
        postalAddressEt.addRule(getPostalAddressValidationRule())
        postalAddressEt.setText(null)
    }

    @StringRes
    private fun getPostalAddressHint() = when (selectedCountry.postalAddressType) {
        PostalAddressType.ZIP -> R.string.vgs_checkout_address_info_zip_subtitle
        PostalAddressType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_subtitle
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
        saveCardButton = findViewById(R.id.mbSaveCard)
        saveCardButton.setOnClickListener { saveCard() }
    }

    private fun saveCard() {
        hideSoftKeyboard()
        if (isInputValid()) handleSaveCard()
    }

    private fun isInputValid(): Boolean {
        val isCardDetailsValid = isCardDetailsValid()
        val isBillingAddressValid = isBillingAddressValid()
        return isCardDetailsValid && isBillingAddressValid
    }

    private fun isCardDetailsValid(): Boolean {
        val isCardHolderValid = config.isCardHolderHidden() || validate(
            cardHolderEt,
            cardHolderTil,
            R.string.vgs_checkout_card_holder_empty_error
        )

        val isCardNumberValid = validate(
            cardNumberEt,
            cardNumberTil,
            R.string.vgs_checkout_card_number_empty_error,
            R.string.vgs_checkout_card_number_invalid_error
        )

        val isExpirationDateValid = validate(
            expirationDateEt,
            expirationDateTil,
            R.string.vgs_checkout_card_expiration_date_empty_error,
            R.string.vgs_checkout_card_expiration_date_invalid_error
        )

        val isSecurityCodeValid = validate(
            securityCodeEt,
            securityCodeTil,
            R.string.vgs_checkout_card_verification_code_empty_error,
            R.string.vgs_checkout_card_verification_code_invalid_error
        )

        return isCardHolderValid && isCardNumberValid && isExpirationDateValid && isSecurityCodeValid
    }

    private fun isBillingAddressValid(): Boolean {
        if (config.formConfig.addressOptions.visibility == VGSCheckoutBillingAddressVisibility.HIDDEN) {
            return true
        }

        val isAddressValid = validate(
            addressEt,
            addressTil,
            R.string.vgs_checkout_address_info_address_line1_empty_error
        )

        val isCityValid = validate(
            cityEt,
            cityTil,
            R.string.vgs_checkout_address_info_city_empty_error
        )

        val postalAddressValid = validate(
            postalAddressEt,
            postalAddressTil,
            getPostalAddressEmptyErrorMessage(),
            getPostalAddressInvalidErrorMessage()
        )

        return isAddressValid && isCityValid && postalAddressValid
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

    private fun handleBackPressWithConfirmation(onBackPressed: () -> Unit) {
        if (isBackPressRequireConfirmation()) {
            showBackPressConfirmationDialog(onBackPressed)
        } else {
            onBackPressed.invoke()
        }
    }

    private fun isBackPressRequireConfirmation(): Boolean {
        return cardHolderEt.isContentNotEmpty() ||
                cardNumberEt.isContentNotEmpty() ||
                expirationDateEt.isContentNotEmpty() ||
                securityCodeEt.isContentNotEmpty()
    }

    private fun showBackPressConfirmationDialog(onConfirmed: () -> Unit) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.vgs_checkout_close_dialog_title)
            .setMessage(R.string.vgs_checkout_close_dialog_description)
            .setNegativeButton(R.string.vgs_checkout_close_dialog_cancel, null)
            .setPositiveButton(R.string.vgs_checkout_close_dialog_ok) { _, _ -> onConfirmed.invoke() }
            .show()
    }

    private fun showCountrySelectionDialog() {
        val countries = CountriesHelper.countries
        val countryNames = countries.map { it.name }.toTypedArray()
        val selectedIndex = countries.indexOf(selectedCountry)
        var selected = -1
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.vgs_checkout_select_country_dialog_title)
            .setSingleChoiceItems(countryNames, selectedIndex) { _, which -> selected = which }
            .setNegativeButton(R.string.vgs_checkout_close_dialog_cancel, null)
            .setPositiveButton(R.string.vgs_checkout_close_dialog_ok) { _, _ ->
                countries.getOrNull(selected)?.let {
                    selectedCountry = it
                    updateCountryView()
                    updatePostalAddressView()
                }
            }
            .show()
    }

    private fun showNetworkConnectionErrorSnackBar() {
        val message = getString(R.string.vgs_checkout_network_connection_error)
        Snackbar.make(findViewById(R.id.llRoot), message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.vgs_checkout_retry)) { saveCard() }
            .show()
    }
}