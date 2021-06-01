package com.verygoodsecurity.vgscheckout.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.VGSCheckoutForm
import com.verygoodsecurity.vgscheckout.config.networking.VGSMultiplexingRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.VGSVaultRouteConfig
import com.verygoodsecurity.vgscheckout.view.CheckoutView

internal class CheckoutActivity : AppCompatActivity(R.layout.checkout_activity) {

    private val formConfig: VGSCheckoutForm by lazy { requireExtra(EXTRA_KEY) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(
            CheckoutActivity::class.java.simpleName,
            "CheckoutActivity::onCreate, tenantID = ${formConfig.tenantID}"
        )

        when (val config = formConfig.routeConfig) {
            is VGSVaultRouteConfig -> {
                Log.d("Test", "config is vault, build collect instance")
                Log.d("Test", "extra data = ${config.requestConfig.extraData}")
            }
            is VGSMultiplexingRouteConfig -> {
                Log.d("Test", "config is multiplexing, build collect instance")
            }
        }

        with(findViewById<CheckoutView>(R.id.cvForm)) {
            applyConfig(formConfig.uiConfig.cardNumberConfig)
            applyConfig(formConfig.uiConfig.cardHolderConfig)
            applyConfig(formConfig.uiConfig.cardVerificationCodeConfig)
            applyConfig(formConfig.uiConfig.expirationDateConfig)
            applyConfig(formConfig.uiConfig.postalCodeConfig)
        }
    }

    companion object {

        private const val EXTRA_KEY = "extra_checkout_config"

        internal fun start(context: Context, formConfig: VGSCheckoutForm) {
            context.startActivity(Intent(context, CheckoutActivity::class.java).apply {
                putExtra(EXTRA_KEY, formConfig)
            })
        }

        internal fun startForResult(activity: Activity, code: Int, formConfig: VGSCheckoutForm) {
            activity.startActivityForResult(Intent(activity, CheckoutActivity::class.java).apply {
                putExtra(EXTRA_KEY, formConfig)
            }, code)
        }
    }
}

internal inline fun <reified R> Activity.requireExtra(key: String): R {
    return (intent.extras?.get(key) as? R) ?: throw IllegalArgumentException("Extras required!")
}