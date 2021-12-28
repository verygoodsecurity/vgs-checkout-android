package com.verygoodsecurity.vgscheckout.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.VisibleForTesting
import androidx.core.os.bundleOf
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingAddCardConfig
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingPaymentConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.ui.CustomCheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.MultiplexingPaymentCheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.MultiplexingSaveCardActivity
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import kotlinx.parcelize.Parcelize

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal const val EXTRA_KEY_ARGS = "com.verygoodsecurity.vgscheckout.model.extra_checkout_args"

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal const val EXTRA_KEY_RESULT = "com.verygoodsecurity.vgscheckout.model.extra_checkout_result"

internal class CheckoutResultContract :
    ActivityResultContract<CheckoutResultContract.Args<CheckoutConfig>, VGSCheckoutResult>() {

    override fun createIntent(context: Context, input: Args<CheckoutConfig>?): Intent {
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

    private fun getIntentTarget(args: Args<CheckoutConfig>?): Class<out BaseCheckoutActivity<*>> {
        return when (args?.config) {
            is VGSCheckoutCustomConfig -> CustomCheckoutActivity::class.java
            is VGSCheckoutMultiplexingAddCardConfig -> MultiplexingSaveCardActivity::class.java
            is VGSCheckoutMultiplexingPaymentConfig -> MultiplexingPaymentCheckoutActivity::class.java
            else -> throw IllegalArgumentException("Invalid checkout config.")
        }
    }

    @Parcelize
    data class Args<C : CheckoutConfig>(val config: C) : Parcelable {

        companion object {

            fun <C : CheckoutConfig> fromIntent(intent: Intent): Args<C> {
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