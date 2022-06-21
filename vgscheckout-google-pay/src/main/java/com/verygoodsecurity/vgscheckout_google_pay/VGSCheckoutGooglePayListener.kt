package com.verygoodsecurity.vgscheckout_google_pay

interface VGSCheckoutGooglePayListener {

    fun onSuccess(token: VGSCheckoutGooglePayToken)

    fun onError(e: VGSCheckoutGooglePayException)

    fun onCancel()
}