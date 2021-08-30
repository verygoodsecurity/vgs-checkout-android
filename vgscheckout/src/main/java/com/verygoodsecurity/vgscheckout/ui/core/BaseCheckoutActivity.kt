package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.collect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscheckout.collect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscheckout.collect.widget.PersonNameEditText
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.util.extension.*

internal abstract class BaseCheckoutActivity<C : CheckoutConfiguration> :
    AppCompatActivity(), VgsCollectResponseListener {

    protected val config: C by lazy { resolveConfig(intent) }

    protected val collect by lazy {
        resolveCollect().apply {
            addOnResponseListeners(this@BaseCheckoutActivity)
        }
    }

    private lateinit var cardHolderTied: PersonNameEditText
    private lateinit var cardNumberTied: VGSCardNumberEditText
    private lateinit var expirationDateTied: ExpirationDateEditText
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
        with(config.formConfig.cardOptions) {
            initCardHolderView(cardHolderOptions)
            initCardNumberView(cardNumberOptions)
            initExpirationDateView(expirationDateOptions)
            initSecurityCodeView(cvcOptions)
        }
        initSaveButton()
    }

    private fun initCardHolderView(options: VGSCheckoutCardHolderOptions) {
        cardHolderTied = findViewById(R.id.vgsTiedCardHolder)
        cardHolderTied.setFieldName(options.fieldName)
        collect.bindView(cardHolderTied)
    }

    private fun initCardNumberView(options: VGSCheckoutCardNumberOptions) {
        cardNumberTied = findViewById(R.id.vgsTiedCardNumber)
        cardNumberTied.setFieldName(options.fieldName)
        cardNumberTied.setValidCardBrands(options.cardBrands)
        cardNumberTied.setIsCardBrandPreviewHidden(options.isIconHidden)
        collect.bindView(cardNumberTied)
    }

    private fun initExpirationDateView(options: VGSCheckoutExpirationDateOptions) {
        expirationDateTied = findViewById(R.id.vgsTiedExpirationDate)
        expirationDateTied.setFieldName(options.fieldName)
        collect.bindView(expirationDateTied)
    }

    private fun initSecurityCodeView(options: VGSCheckoutCVCOptions) {
        securityCodeTied = findViewById(R.id.vgsTiedSecurityCode)
        securityCodeTied.setFieldName(options.fieldName)
        securityCodeTied.setIsPreviewIconHidden(options.isIconHidden)
        collect.bindView(securityCodeTied)
    }

    private fun initSaveButton() {
        saveCardButton = findViewById(R.id.mbSaveCard)
        saveCardButton.setOnClickListener {
            if (validate()) onPayClicked()
        }
    }

    private fun validate(): Boolean {
        return true
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