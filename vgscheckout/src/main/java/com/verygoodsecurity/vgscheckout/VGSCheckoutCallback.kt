package com.verygoodsecurity.vgscheckout

import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult

// TODO: Add SAM conversions to VGSCheckoutCallback if it should not has more than one function.

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