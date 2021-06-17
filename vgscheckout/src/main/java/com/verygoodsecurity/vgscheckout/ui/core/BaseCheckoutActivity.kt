package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.CHECKOUT_RESULT_EXTRA_KEY
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.util.CollectProvider
import com.verygoodsecurity.vgscheckout.view.OnPayClickListener
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseCheckoutActivity<C : CheckoutConfiguration> :
    AppCompatActivity(R.layout.checkout_layout), OnPayClickListener, VgsCollectResponseListener {

    abstract fun getConfig(key: String): C

    abstract fun initView(savedInstanceState: Bundle?)

    protected val config: C by lazy { getConfig(EXTRA_KEY_CONFIG) }

    protected val vaultID: String by lazy { requireExtra(EXTRA_KEY_VAULT_ID) }

    protected val environment: String by lazy { requireExtra(EXTRA_KEY_ENVIRONMENT) }

    protected val collect by lazy {
        CollectProvider().get(this, vaultID, environment, config).apply {
            addOnResponseListeners(this@BaseCheckoutActivity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableScreenshots()
        initListeners()
        initView(savedInstanceState)
    }

    private fun initListeners() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
    }

    override fun onResponse(response: VGSResponse?) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(CHECKOUT_RESULT_EXTRA_KEY, VGSCheckoutResult(response?.code, response?.body))
        })
        finish()
    }

    private fun disableScreenshots() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
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
            config: CheckoutConfiguration
        ) {
            val target = when (config) {
                is VGSCheckoutConfiguration -> CheckoutActivity::class.java
                is VGSCheckoutMultiplexingConfiguration -> CheckoutActivity::class.java
                else -> throw IllegalArgumentException("Not implemented!")
            }
            activity.startActivityForResult(Intent(activity, target).apply {
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