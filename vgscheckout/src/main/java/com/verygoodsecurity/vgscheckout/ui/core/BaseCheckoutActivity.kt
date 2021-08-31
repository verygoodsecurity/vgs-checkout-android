package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.core.VgsCollectResponseListener
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
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
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

    abstract fun resolveConfig(intent: Intent): C

    abstract fun resolveCollect(): VGSCollect

    abstract fun onPayClicked()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableScreenshots()
        setContentView(R.layout.vgs_checkout_activity)
        initView(savedInstanceState)
    }

    override fun onBackPressed() {
        showConfirmDialog {
            super.onBackPressed()
            setResult(Activity.RESULT_CANCELED)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResponse(response: VGSResponse?) {
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
        }
    }

    @CallSuper
    protected open fun initView(savedInstanceState: Bundle?) {
        initToolbar()
        initViews()
    }

    private fun initToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.vgs_checkout_ic_baseline_close_white_24)
        supportActionBar?.setTitle(R.string.vgs_checkout_add_card_title)
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

    private fun initSecurityCodeView(options: VGSCheckoutCVCOptions) {
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
        collect.bindView(countryEt)
    }

    private fun initAddressView(options: VGSCheckoutAddressOptions) {
        addressTil = findViewById(R.id.vgsTilAddress)
        addressEt = findViewById(R.id.vgsEtAddress)
        addressEt.setFieldName(options.fieldName)
        addressEt.addRule(billingAddressValidationRule)
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
        collect.bindView(cityEt)
    }

    private fun initPostalAddressView(options: VGSCheckoutPostalAddressOptions) {
        postalAddressTil = findViewById(R.id.vgsTilPostalAddress)
        postalAddressEt = findViewById(R.id.vgsEtPostalAddress)
        postalAddressEt.setFieldName(options.fieldName)
        postalAddressEt.addRule(billingAddressValidationRule)
        collect.bindView(postalAddressEt)
    }

    private fun initSaveButton() {
        saveCardButton = findViewById(R.id.mbSaveCard)
        saveCardButton.setOnClickListener {
            if (isCardDetailsValid() && isBillingAddressValid()) onPayClicked()
        }
    }

    private fun isCardDetailsValid(): Boolean {
        val isCardHolderValid = validate(
            cardHolderEt,
            cardHolderTil,
            R.string.vgs_checkout_card_holder_empty_error,
            0
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
        // TODO: Add implementation
        return true
    }

    private fun validate(
        target: InputFieldView,
        parent: VGSTextInputLayout,
        @StringRes emptyError: Int,
        @StringRes invalidError: Int
    ): Boolean = when {
        target.getFieldState()?.isEmpty == true -> {
            parent.setError(emptyError)
            false
        }
        target.getFieldState()?.isValid == false -> {
            parent.setError(invalidError)
            false
        }
        else -> true
    }

    private fun showConfirmDialog(onConfirmed: () -> Unit) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.vgs_checkout_close_dialog_title)
            .setMessage(R.string.vgs_checkout_close_dialog_description)
            .setNegativeButton(R.string.vgs_checkout_close_dialog_cancel) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.vgs_checkout_close_dialog_ok) { dialog, _ ->
                dialog.cancel()
                onConfirmed.invoke()
            }
            .show()
    }
}