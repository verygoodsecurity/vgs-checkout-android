package com.verygoodsecurity.vgscheckout

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig

/**
 * TODO: Add comments
 */
interface VGSCheckoutConfigInitCallback<T : CheckoutConfig> {

    fun onSuccess(config: T)

    fun onFailure(e: Throwable)
}