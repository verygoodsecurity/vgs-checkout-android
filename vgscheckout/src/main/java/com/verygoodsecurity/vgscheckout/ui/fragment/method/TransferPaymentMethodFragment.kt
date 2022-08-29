package com.verygoodsecurity.vgscheckout.ui.fragment.method

import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.exception.internal.NoInternetConnectionException
import com.verygoodsecurity.vgscheckout.model.Card
import com.verygoodsecurity.vgscheckout.networking.command.TransferCommand
import com.verygoodsecurity.vgscheckout.util.extension.toTransferResponse

internal class TransferPaymentMethodFragment : PaymentMethodFragment<VGSCheckoutPaymentConfig>() {

    override fun processSelectedCard(card: Card) {
        setLoading(true)

        TransferCommand(
            requireContext(),
            TransferCommand.Params(
                config.baseUrl,
                config.order?.id ?: "",
                card.finId,
                config.routeConfig
            )
        ).execute(::handleTransferResult)
    }

    private fun handleTransferResult(result: TransferCommand.Result) {
        //todo add analytic
        setLoading(false)

        if (result.code == NoInternetConnectionException.CODE) { // TODO: Refactor error handling
            setLoading(false)
            showRetrySnackBar(getString(R.string.vgs_checkout_no_network_error)) { processUserChoice() }
            return
        }

        with(resultHandler) {
            getResultBundle().putTransferResponse(result.toTransferResponse())
            getResultBundle().putIsPreSavedCard(true)
            setResult(true)
        }
    }
}