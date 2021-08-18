package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.view.checkout.address.AddressView
import com.verygoodsecurity.vgscheckout.view.checkout.card.CreditCardView
import com.verygoodsecurity.vgscheckout.view.checkout.core.BaseCheckoutFormView
import com.verygoodsecurity.vgscheckout.view.checkout.holder.CardHolderView

internal abstract class BaseCheckoutActivity<C : CheckoutConfiguration> :
    AppCompatActivity(), View.OnClickListener, VgsCollectResponseListener,
    BaseCheckoutFormView.OnStateChangeListener,
    BaseCheckoutFormView.OnValidationErrorListener {

    protected val config: C by lazy { resolveConfig(intent) }

    protected val collect by lazy {
        resolveCollect().apply {
            addOnResponseListeners(this@BaseCheckoutActivity)
        }
    }

    private lateinit var cardHolderView: CardHolderView
    private lateinit var creditCardView: CreditCardView
    private lateinit var cardDetailsError: MaterialTextView
    private lateinit var addressView: AddressView
    private lateinit var addressError: MaterialTextView
    private lateinit var payButton: MaterialButton

    abstract fun resolveConfig(intent: Intent): C

    abstract fun resolveCollect(): VGSCollect

    abstract fun onPayClicked()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableScreenshots()
        setContentView(R.layout.vgs_checkout_activity)
        initView(savedInstanceState)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mbPay -> handlePayClicked()
            R.id.ivBack -> super.onBackPressed()
        }
    }

    override fun onResponse(response: VGSResponse?) {
        val resultBundle = CheckoutResultContract.Result(response?.toCheckoutResult()).toBundle()
        setResult(Activity.RESULT_OK, Intent().putExtras(resultBundle))
        finish()
    }

    override fun onStateChanged(view: View, isInputValid: Boolean) {
        payButton.isEnabled = isInputValid()
    }

    override fun onValidationError(view: View, message: String?) {
        when (view.id) {
            R.id.cardHolderView, R.id.creditCardView -> {
                val errorMessage = cardHolderView.getError() ?: creditCardView.getError()
                cardDetailsError.showWithText(errorMessage)
            }
            R.id.addressView -> addressError.showWithText(message)
        }
    }

    @CallSuper
    protected open fun initView(savedInstanceState: Bundle?) {
        initBackButton()
        initCardHolderView()
        initCreditCardView()
        initAddressView()
        initPayButton()
    }

    private fun initBackButton() {
        findViewById<ImageView>(R.id.ivBack).setOnClickListener(this)
    }

    private fun initCardHolderView() {
        cardHolderView = findViewById(R.id.cardHolderView)
        when (config.formConfig.cardOptions.cardHolderOptions.visibility) {
            VGSCheckoutFieldVisibility.HIDDEN -> cardHolderView.gone()
            else -> {
                cardHolderView.onErrorListener = this
                cardHolderView.onStateChangeListener = this
                cardHolderView.applyConfig(config.formConfig)
                collect.bindView(*cardHolderView.getInputViews())
            }
        }
    }

    private fun initCreditCardView() {
        cardDetailsError = findViewById(R.id.mtvCardDetailsError)
        creditCardView = findViewById(R.id.creditCardView)
        creditCardView.onErrorListener = this
        creditCardView.onStateChangeListener = this
        creditCardView.applyConfig(config.formConfig)
        collect.bindView(*creditCardView.getInputViews())
    }

    private fun initAddressView() {
        addressError = findViewById(R.id.mtvAddressError)
        addressView = findViewById(R.id.addressView)
        when (config.formConfig.addressOptions.visibility) {
            VGSCheckoutBillingAddressVisibility.HIDDEN -> {
                findViewById<MaterialTextView>(R.id.mtvAddressTitle).gone()
                addressView.gone()
            }
            else -> {
                addressView.onErrorListener = this
                addressView.onStateChangeListener = this
                addressView.applyConfig(config.formConfig)
                collect.bindView(*addressView.getInputViews())
            }
        }
    }

    private fun initPayButton() {
        payButton = findViewById(R.id.mbPay)
        payButton.setOnClickListener(this)
        config.formConfig.payButtonTitle?.let { payButton.text = it }
    }

    private fun initCheckoutView(
        view: BaseCheckoutFormView,
        visibility: VGSCheckoutFieldVisibility
    ) {
        when (visibility) {
            VGSCheckoutFieldVisibility.HIDDEN -> view.gone()
            else -> {
                view.onErrorListener = this
                view.onStateChangeListener = this
                view.applyConfig(config.formConfig)
                collect.bindView(*view.getInputViews())
            }
        }
    }

    private fun isInputValid(): Boolean {
        return cardHolderView.isInputValid() && creditCardView.isInputValid() && addressView.isInputValid()
    }

    private fun handlePayClicked() {
        cardHolderView.disable()
        creditCardView.disable()
        addressView.disable()
        payButton.isClickable = false
        payButton.text = getString(R.string.vgs_checkout_pay_button_processing_title)
        payButton.icon =
            getDrawableCompat(R.drawable.vgs_checkout_animated_ic_progress_white_16dp)
        (payButton.icon as? Animatable)?.start()
        onPayClicked()
    }
}