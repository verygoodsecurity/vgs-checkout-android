package com.verygoodsecurity.vgscheckout.ui.core

import com.verygoodsecurity.vgscheckout.model.VGSCheckoutCard

internal interface OnPaymentMethodSelectedListener {

    fun onCardSelected(card: VGSCheckoutCard)

    fun onNewCardSelected()
}