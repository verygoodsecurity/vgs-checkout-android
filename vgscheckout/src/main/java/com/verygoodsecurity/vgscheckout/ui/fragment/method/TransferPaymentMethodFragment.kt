package com.verygoodsecurity.vgscheckout.ui.fragment.method

import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.exception.internal.NoInternetConnectionException
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.networking.command.TransferCommand
import com.verygoodsecurity.vgscheckout.util.extension.getBaseUrl

internal class TransferPaymentMethodFragment : PaymentMethodFragment<VGSCheckoutPaymentConfig>() {

    override fun processSelectedCard(card: Card) {
        TransferCommand(
            requireContext(),
            TransferCommand.Params(
                config.getBaseUrl(requireContext()),
                config.orderDetails?.id ?: "",
                card.finId,
                config.accessToken
            )
        ).execute(::handleTransferResult)
    }

    private fun handleTransferResult(result: TransferCommand.Result) {
        //todo add analytic
        if (result.code == NoInternetConnectionException.CODE) { // TODO: Refactor error handling
            setLoading(false)
            showRetrySnackBar(getString(R.string.vgs_checkout_no_network_error)) { processUserChoice() }
            return
        }

        //todo publish results
    }
}