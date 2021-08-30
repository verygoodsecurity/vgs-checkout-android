package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.widget.*
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.util.extension.*

internal abstract class BaseCheckoutActivity<C : CheckoutConfiguration> :
    AppCompatActivity(), VgsCollectResponseListener, InputFieldView.OnTextChangedListener {

    protected val config: C by lazy { resolveConfig(intent) }

    protected val collect by lazy {
        resolveCollect().apply {
            addOnResponseListeners(this@BaseCheckoutActivity)
        }
    }

    private lateinit var cardHolderTil: VGSTextInputLayout
    private lateinit var cardHolderTied: PersonNameEditText
    private lateinit var cardNumberTil: VGSTextInputLayout
    private lateinit var cardNumberTied: VGSCardNumberEditText
    private lateinit var expirationDateTil: VGSTextInputLayout
    private lateinit var expirationDateTied: ExpirationDateEditText
    private lateinit var securityCodeTil: VGSTextInputLayout
    private lateinit var securityCodeTied: CardVerificationCodeEditText

    private lateinit var saveCardButton: MaterialButton

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
            R.id.vgsTiedCardHolder -> cardHolderTil.setError(null)
            R.id.vgsTiedCardNumber -> cardNumberTil.setError(null)
            R.id.vgsTiedExpirationDate -> expirationDateTil.setError(null)
            R.id.vgsTiedSecurityCode -> securityCodeTil.setError(null)
        }
    }

    @CallSuper
    protected open fun initView(savedInstanceState: Bundle?) {
        initToolbar()
        initInputViews()
    }


    private fun initToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.vgs_checkout_ic_baseline_close_white_24)
        supportActionBar?.setTitle(R.string.vgs_checkout_add_card_title)
    }

    private fun initInputViews() {
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
        cardHolderTied = findViewById(R.id.vgsTiedCardHolder)
        if (options.visibility == VGSCheckoutFieldVisibility.HIDDEN) {
            cardHolderTil.gone()
            return
        }
        cardHolderTied.setFieldName(options.fieldName)
        cardHolderTied.addOnTextChangeListener(this)
        collect.bindView(cardHolderTied)
    }

    private fun initCardNumberView(options: VGSCheckoutCardNumberOptions) {
        cardNumberTil = findViewById(R.id.vgsTilCardNumber)
        cardNumberTied = findViewById(R.id.vgsTiedCardNumber)
        cardNumberTied.setFieldName(options.fieldName)
        cardNumberTied.setValidCardBrands(options.cardBrands)
        cardNumberTied.setIsCardBrandPreviewHidden(options.isIconHidden)
        cardNumberTied.addOnTextChangeListener(this)
        collect.bindView(cardNumberTied)
    }

    private fun initExpirationDateView(options: VGSCheckoutExpirationDateOptions) {
        expirationDateTil = findViewById(R.id.vgsTilExpirationDate)
        expirationDateTied = findViewById(R.id.vgsTiedExpirationDate)
        expirationDateTied.setFieldName(options.fieldName)
        expirationDateTied.addOnTextChangeListener(this)
        collect.bindView(expirationDateTied)
    }

    private fun initSecurityCodeView(options: VGSCheckoutCVCOptions) {
        securityCodeTil = findViewById(R.id.vgsTilSecurityCode)
        securityCodeTied = findViewById(R.id.vgsTiedSecurityCode)
        securityCodeTied.setFieldName(options.fieldName)
        securityCodeTied.setIsPreviewIconHidden(options.isIconHidden)
        securityCodeTied.addOnTextChangeListener(this)
        collect.bindView(securityCodeTied)
    }

    private fun initBillingAddressViews() {
        if (config.formConfig.addressOptions.visibility == VGSCheckoutBillingAddressVisibility.HIDDEN) {
            findViewById<MaterialCardView>(R.id.mcvBillingAddress).gone()
            return
        }
    }

    private fun initSaveButton() {
        saveCardButton = findViewById(R.id.mbSaveCard)
        saveCardButton.setOnClickListener {
            if (isCardDetailsValid()) onPayClicked()
        }
    }

    private fun isCardDetailsValid(): Boolean {
        val isCardHolderValid = validate(
            cardHolderTied,
            cardHolderTil,
            R.string.vgs_checkout_card_holder_empty_error,
            0
        )

        val isCardNumberValid = validate(
            cardNumberTied,
            cardNumberTil,
            R.string.vgs_checkout_card_number_empty_error,
            R.string.vgs_checkout_card_number_invalid_error
        )

        val isExpirationDateValid = validate(
            expirationDateTied,
            expirationDateTil,
            R.string.vgs_checkout_card_expiration_date_empty_error,
            R.string.vgs_checkout_card_expiration_date_invalid_error
        )

        val isSecurityCodeValid = validate(
            securityCodeTied,
            securityCodeTil,
            R.string.vgs_checkout_card_verification_code_empty_error,
            R.string.vgs_checkout_card_verification_code_invalid_error
        )

        return isCardHolderValid && isCardNumberValid && isExpirationDateValid && isSecurityCodeValid
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