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
import com.verygoodsecurity.vgscheckout.util.CollectProvider
import com.verygoodsecurity.vgscheckout.util.toCollectHTTPMethod
import com.verygoodsecurity.vgscheckout.util.toCollectMergePolicy
import com.verygoodsecurity.vgscheckout.view.CheckoutView
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest

internal class CheckoutActivity : AppCompatActivity(R.layout.checkout_activity) {

    private val formConfig: VGSCheckoutForm by lazy { requireExtra(EXTRA_KEY) }

    private val collect: VGSCollect by lazy { CollectProvider().get(this, formConfig) }

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

        findViewById<CheckoutView>(R.id.cvForm)?.run {
            with(formConfig.uiConfig) {
                applyConfig(cardNumberConfig)
                applyConfig(cardHolderConfig)
                applyConfig(cardVerificationCodeConfig)
                applyConfig(expirationDateConfig)
                applyConfig(postalCodeConfig)
            }
        }
    }

    private fun asyncSubmit() {
        with(formConfig.routeConfig.requestConfig) {
            collect.asyncSubmit(
                VGSRequest.VGSRequestBuilder()
                    .setPath(path)
                    .setCustomData(extraData)
                    .setFieldNameMappingPolicy(mergePolicy.toCollectMergePolicy())
                    .setMethod(httpMethod.toCollectHTTPMethod())
                    .build()
            )
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