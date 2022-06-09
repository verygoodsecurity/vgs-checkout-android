package com.verygoodsecurity.vgscheckout.ui.fragment.method

import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.util.extension.toCardResponse

internal class SelectPaymentMethodFragment : PaymentMethodFragment<VGSCheckoutAddCardConfig>() {

    override fun processSelectedCard(card: Card) {
        with(resultHandler) {
            getResultBundle().putAddCardResponse(card.toCardResponse())
            getResultBundle().putIsPreSavedCard(true)
            setResult(true)
        }
    }
}