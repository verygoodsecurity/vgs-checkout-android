package com.verygoodsecurity.vgscheckout

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity

const val CHECKOUT_RESULT_EXTRA_KEY = "checkout_result_extra_key"

class VGSCheckout {

    fun present(
        context: Context,
        activityLauncher: ActivityResultLauncher<Intent>,
        config: CheckoutConfiguration
    ) {
        BaseCheckoutActivity.startForResult(context, activityLauncher, config)
    }
}