package com.verygoodsecurity.vgscheckout.ui.core

import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutAddCardResponse

internal interface OnAddCardResponseListener {

    fun onAddCardResponse(response: VGSCheckoutAddCardResponse, shouldSaveCard: Boolean?)
}