package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.CHECKOUT_RESULT_EXTRA_KEY
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.CheckoutMultiplexingActivity
import com.verygoodsecurity.vgscheckout.util.extension.disableScreenshots
import com.verygoodsecurity.vgscheckout.view.OnPayClickListener
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse

@Suppress("MemberVisibilityCanBePrivate")
internal abstract class BaseCheckoutActivity<C : CheckoutConfiguration> :
    AppCompatActivity(R.layout.checkout_layout), OnPayClickListener, VgsCollectResponseListener {

    protected val config: C by lazy { resolveConfig(EXTRA_KEY_CONFIG) }

    protected val collect by lazy { resolveCollect() }

    abstract fun resolveConfig(key: String): C

    abstract fun resolveCollect(): VGSCollect

    abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableScreenshots()
        initView(savedInstanceState)
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