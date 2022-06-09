package com.verygoodsecurity.vgscheckout.ui.fragment.method

import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.model.Card

internal class TransferPaymentMethodFragment : PaymentMethodFragment() {

    override fun onPayButtonClick(card: Card) {
        transfer()
    }

    private fun transfer() {
        //todo make a transfer
    }

    private fun publishResults() {
        //todo return results with setResult(_)
        with(resultHandler) {
            if (config is VGSCheckoutAddCardConfig) getResultBundle().putIsPreSavedCard(false)
        }
    }
}