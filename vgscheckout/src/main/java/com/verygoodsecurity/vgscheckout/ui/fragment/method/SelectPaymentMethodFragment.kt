package com.verygoodsecurity.vgscheckout.ui.fragment.method

import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.util.extension.toCardResponse

internal class SelectPaymentMethodFragment : PaymentMethodFragment() {

    override fun onPayButtonClick(card: Card) {
        with(resultHandler) {
            getResultBundle().putAddCardResponse(card.toCardResponse())
            getResultBundle().putIsPreSavedCard(true)
            setResult(true)
        }
    }
}