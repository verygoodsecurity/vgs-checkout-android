package com.verygoodsecurity.vgscheckout.ui.fragment.method

import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.model.Card

internal class SelectPaymentMethodFragment : PaymentMethodFragment<VGSCheckoutAddCardConfig>() {

    override fun processSelectedCard(card: Card) {
        resultHandler.setResult(true)
    }
}