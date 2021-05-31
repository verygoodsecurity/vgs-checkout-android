package com.verygoodsecurity.vgscheckout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

internal class CheckoutActivity : AppCompatActivity(R.layout.checkout_activity) {

    private val formConfig: VGSCheckoutForm by lazy { requireExtra(EXTRA_KEY) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(
            CheckoutActivity::class.java.simpleName,
            "CheckoutActivity::onCreate, tenantID = ${formConfig.tenantID}"
        )
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