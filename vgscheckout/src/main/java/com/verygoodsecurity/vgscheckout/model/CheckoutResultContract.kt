package com.verygoodsecurity.vgscheckout.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.os.bundleOf
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfiguration
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.ui.CheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.CheckoutMultiplexingActivity
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import kotlinx.parcelize.Parcelize

private const val EXTRA_KEY_ARGS = "com.verygoodsecurity.vgscheckout.model.extra_checkout_args"
private const val EXTRA_KEY_RESULT = "com.verygoodsecurity.vgscheckout.model.extra_checkout_result"

internal class CheckoutResultContract :
    ActivityResultContract<CheckoutResultContract.Args<CheckoutConfiguration>, VGSCheckoutResult>() {

    override fun createIntent(context: Context, input: Args<CheckoutConfiguration>?): Intent {
        return Intent(context, getIntentTarget(input)).apply {
            putExtra(EXTRA_KEY_ARGS, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): VGSCheckoutResult {
        return if (resultCode == Activity.RESULT_OK) {
            intent?.getParcelableExtra<Result>(EXTRA_KEY_RESULT)?.checkoutResult
                ?: VGSCheckoutResult.Failed(-1, "Failed to retrieve checkout result.")
        } else {
            VGSCheckoutResult.Canceled
        }
    }

    private fun getIntentTarget(args: Args<CheckoutConfiguration>?): Class<out BaseCheckoutActivity<*>> {
        return when (args?.config) {
            is VGSCheckoutCustomConfiguration -> CheckoutActivity::class.java
            is VGSCheckoutMultiplexingConfiguration -> CheckoutMultiplexingActivity::class.java
            else -> throw IllegalArgumentException("Invalid checkout config.")
        }
    }

    @Parcelize
    data class Args<C : CheckoutConfiguration>(val config: C) : Parcelable {

        companion object {

            fun <C : CheckoutConfiguration> fromIntent(intent: Intent): Args<C> {
                return intent.getParcelableExtra(EXTRA_KEY_ARGS)
                    ?: throw IllegalArgumentException("Failed to get arguments.")
            }
        }
    }

    @Parcelize
    data class Result(val checkoutResult: VGSCheckoutResult?) : Parcelable {

        fun toBundle(): Bundle = bundleOf(EXTRA_KEY_RESULT to this)
    }
}