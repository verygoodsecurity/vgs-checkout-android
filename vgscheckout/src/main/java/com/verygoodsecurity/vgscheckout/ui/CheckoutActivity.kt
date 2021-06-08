package com.verygoodsecurity.vgscheckout.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutVaultConfiguration
import com.verygoodsecurity.vgscheckout.util.CollectProvider
import com.verygoodsecurity.vgscheckout.util.toCollectHTTPMethod
import com.verygoodsecurity.vgscheckout.util.toCollectMergePolicy
import com.verygoodsecurity.vgscheckout.view.CheckoutView
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest

internal class CheckoutActivity : AppCompatActivity(R.layout.checkout_activity) {

    private val vaultID: String by lazy { requireExtra(EXTRA_KEY_VAULT_ID) }
    private val environment: String by lazy { requireExtra(EXTRA_KEY_ENVIRONMENT) }
    private val config: VGSCheckoutVaultConfiguration by lazy { requireExtra(EXTRA_KEY_CONFIG) }
    private val collect by lazy { CollectProvider().get(this, vaultID, environment, config) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(
            CheckoutActivity::class.java.simpleName,
            "CheckoutActivity::onCreate, vaultID = $vaultID"
        )

        findViewById<CheckoutView>(R.id.cvForm)?.let {
            it.applyConfig(config.formConfig)
            it.bindViews(collect)
        }
    }

    private fun asyncSubmit() {
        with(config.routeConfig) {
            collect.asyncSubmit(
                VGSRequest.VGSRequestBuilder()
                    .setPath(path)
                    .setCustomData(requestOptions.extraData)
                    .setFieldNameMappingPolicy(requestOptions.mergePolicy.toCollectMergePolicy())
                    .setMethod(requestOptions.httpMethod.toCollectHTTPMethod())
                    .build()
            )
        }
    }

    companion object {

        private const val EXTRA_KEY_VAULT_ID = "extra_checkout_vault_id"
        private const val EXTRA_KEY_ENVIRONMENT = "extra_checkout_environment"
        private const val EXTRA_KEY_CONFIG = "extra_checkout_config"

        internal fun startForResult(
            activity: Activity,
            code: Int,
            vaultID: String,
            environment: String,
            config: VGSCheckoutVaultConfiguration
        ) {
            activity.startActivityForResult(Intent(activity, CheckoutActivity::class.java).apply {
                putExtra(EXTRA_KEY_VAULT_ID, vaultID)
                putExtra(EXTRA_KEY_ENVIRONMENT, environment)
                putExtra(EXTRA_KEY_CONFIG, config)
            }, code)
        }
    }
}

internal inline fun <reified R> Activity.requireExtra(key: String): R {
    return (intent.extras?.get(key) as? R) ?: throw IllegalArgumentException("Extras required!")
}