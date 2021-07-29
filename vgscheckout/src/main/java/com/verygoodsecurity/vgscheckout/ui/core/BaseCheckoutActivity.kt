package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.CHECKOUT_RESULT_EXTRA_KEY
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.CheckoutMultiplexingActivity
import com.verygoodsecurity.vgscheckout.util.extension.disableScreenshots
import com.verygoodsecurity.vgscheckout.util.extension.getDrawableCompat
import com.verygoodsecurity.vgscheckout.util.extension.gone
import com.verygoodsecurity.vgscheckout.util.extension.showWithText
import com.verygoodsecurity.vgscheckout.view.checkout.address.AddressView
import com.verygoodsecurity.vgscheckout.view.checkout.card.CreditCardView
import com.verygoodsecurity.vgscheckout.view.checkout.core.BaseCheckoutFormView
import com.verygoodsecurity.vgscheckout.view.checkout.holder.CardHolderView

internal abstract class BaseCheckoutActivity<C : CheckoutConfiguration> :
    AppCompatActivity(), View.OnClickListener, VgsCollectResponseListener,
    BaseCheckoutFormView.OnStateChangeListener,
    BaseCheckoutFormView.OnValidationErrorListener {

    protected val config: C by lazy { resolveConfig(EXTRA_KEY_CONFIG) }

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

    abstract fun resolveConfig(key: String): C

    abstract fun resolveCollect(): VGSCollect

    abstract fun onPayClicked()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableScreenshots()
        setContentView(R.layout.vgs_checkout_activity)
        initView(savedInstanceState)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mbPay -> handlePayClicked()
            R.id.ivBack -> super.onBackPressed()
        }
    }

    override fun onResponse(response: VGSResponse?) {
        with(Intent()) {
            putExtra(CHECKOUT_RESULT_EXTRA_KEY, VGSCheckoutResult(response?.code, response?.body))
            setResult(Activity.RESULT_OK, this)
        }
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
        cardHolderView = findViewById(R.id.cardHolderView)
        creditCardView = findViewById(R.id.creditCardView)
        cardDetailsError = findViewById(R.id.mtvCardDetailsError)
        addressView = findViewById(R.id.addressView)
        addressError = findViewById(R.id.mtvAddressError)
        payButton = findViewById(R.id.mbPay)
        initCheckoutView(cardHolderView, config.formConfig.cardOptions.cardHolderOptions.visibility)
        initCheckoutView(creditCardView, VGSCheckoutFieldVisibility.VISIBLE)
        initCheckoutView(addressView, config.formConfig.addressOptions.visibility)
        config.formConfig.payButtonTitle?.let { payButton.text = it }
        payButton.setOnClickListener(this)
        findViewById<ImageView>(R.id.ivBack).setOnClickListener(this)
    }

    private fun initCheckoutView(
        view: BaseCheckoutFormView,
        visibility: VGSCheckoutFieldVisibility
    ) {
        when (visibility) {
            VGSCheckoutFieldVisibility.GONE -> view.gone()
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
        creditCardView.isEnabled = false
        payButton.isClickable = false
        payButton.text = getString(R.string.vgs_checkout_pay_button_processing_title)
        payButton.icon = getDrawableCompat(R.drawable.vgs_checkout_animated_ic_progress_white_16dp)
        (payButton.icon as? Animatable)?.start()
        onPayClicked()
    }

    companion object {

        private const val EXTRA_KEY_CONFIG = "extra_checkout_config"

        internal fun startForResult(
            context: Context,
            activityLauncher: ActivityResultLauncher<Intent>,
            config: CheckoutConfiguration
        ) {
            val target = when (config) {
                is VGSCheckoutConfiguration -> CheckoutActivity::class.java
                is VGSCheckoutMultiplexingConfiguration -> CheckoutMultiplexingActivity::class.java
                else -> throw IllegalArgumentException("Not implemented!")
            }
            activityLauncher.launch(Intent(context, target).apply {
                putExtra(EXTRA_KEY_CONFIG, config)
            })
        }
    }
}