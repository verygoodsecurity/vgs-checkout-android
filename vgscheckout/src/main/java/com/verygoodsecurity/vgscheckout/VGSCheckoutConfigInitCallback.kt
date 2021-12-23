package com.verygoodsecurity.vgscheckout

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

/**
 * TODO: Add comments
 */
interface VGSCheckoutConfigInitCallback<T : CheckoutConfig> {

    fun onSuccess(config: T)

    fun onFailure(exception: VGSCheckoutException)
}