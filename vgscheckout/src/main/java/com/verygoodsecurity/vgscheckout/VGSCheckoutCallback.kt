package com.verygoodsecurity.vgscheckout

import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult

/**
 * Checkout form callback interface definition.
 */
interface VGSCheckoutCallback {

    /**
     * Invoked when a [VGSCheckoutResult] is available.
     *
     * @param result of checkout form.
     */
    fun onCheckoutResult(result: VGSCheckoutResult)
}