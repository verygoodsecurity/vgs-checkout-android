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
import com.verygoodsecurity.vgscheckout.CHECKOUT_RESULT_EXTRA_KEY
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.CheckoutMultiplexingActivity
import com.verygoodsecurity.vgscheckout.util.extension.disableScreenshots
import com.verygoodsecurity.vgscheckout.util.extension.getDrawableCompat
import com.verygoodsecurity.vgscheckout.view.checkout.card.CreditCardView
import com.verygoodsecurity.vgscheckout.view.checkout.core.model.OnStateChangeListener

internal abstract class BaseCheckoutActivity<C : CheckoutConfiguration> :
    AppCompatActivity(), View.OnClickListener, VgsCollectResponseListener, OnStateChangeListener {

    protected val config: C by lazy { resolveConfig(EXTRA_KEY_CONFIG) }

    protected val collect by lazy {
        resolveCollect().apply {
            addOnResponseListeners(this@BaseCheckoutActivity)
        }
    }

    private lateinit var creditCardView: CreditCardView

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
        payButton.isEnabled = isInputValid
    }

    @CallSuper
    protected open fun initView(savedInstanceState: Bundle?) {
        findViewById<ImageView>(R.id.ivBack).setOnClickListener(this)
        payButton = findViewById(R.id.mbPay)
        payButton.setOnClickListener(this)
        creditCardView = findViewById(R.id.creditCardView)
        creditCardView.onStateChangeListener = this@BaseCheckoutActivity
        creditCardView.applyConfig(config.formConfig)
        collect.bindView(*creditCardView.getCollectViews())
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

        internal fun startForResult(activity: Activity, code: Int, config: CheckoutConfiguration) {
            val target = when (config) {
                is VGSCheckoutConfiguration -> CheckoutActivity::class.java
                is VGSCheckoutMultiplexingConfiguration -> CheckoutMultiplexingActivity::class.java
                else -> throw IllegalArgumentException("Not implemented!")
            }
            activity.startActivityForResult(
                Intent(activity, target).apply { putExtra(EXTRA_KEY_CONFIG, config) },
                code
            )
        }
    }
}