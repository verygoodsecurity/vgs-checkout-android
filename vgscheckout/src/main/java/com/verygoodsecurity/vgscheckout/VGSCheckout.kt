package com.verygoodsecurity.vgscheckout

import android.app.Activity
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutConfiguration
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity

const val CHECKOUT_RESULT_EXTRA_KEY = "checkout_result_extra_key"

class VGSCheckout {

    fun present(activity: Activity, requestCode: Int, config: VGSCheckoutConfiguration) {
        BaseCheckoutActivity.startForResult(activity, requestCode, config)
    }
}