package com.verygoodsecurity.vgscheckout

import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult

interface VGSCheckoutCallback {

    fun onCheckoutResult(result: VGSCheckoutResult)
}