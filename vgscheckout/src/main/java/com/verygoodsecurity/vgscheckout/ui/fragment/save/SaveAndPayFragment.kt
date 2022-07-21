package com.verygoodsecurity.vgscheckout.ui.fragment.save

import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.exception.internal.NoInternetConnectionException
import com.verygoodsecurity.vgscheckout.networking.command.AddCardCommand
import com.verygoodsecurity.vgscheckout.networking.command.TransferCommand
import com.verygoodsecurity.vgscheckout.util.extension.getSourceId
import com.verygoodsecurity.vgscheckout.util.extension.toTransferResponse

internal class SaveAndPayFragment : OrchestrationFragment<VGSCheckoutPaymentConfig>() {

    override fun handleSaveCardResult(result: AddCardCommand.Result) {
        val source = result.getSourceId()
        makeTransfer(source)
    }

    private fun makeTransfer(source: String) {
        TransferCommand(
            requireContext(),
            TransferCommand.Params(
                config.baseUrl,
                config.order?.id ?: "",
                source,
                config.accessToken
            )
        ).execute(::handleTransferResult)
    }

    private fun handleTransferResult(result: TransferCommand.Result) {
        //todo add analytic
        setLoading(false)

        if (result.code == NoInternetConnectionException.CODE) { // TODO: Refactor error handling
            showRetrySnackBar(getString(R.string.vgs_checkout_no_network_error)) { handleSaveClicked() }
            return
        }

        with(resultHandler) {
            getResultBundle().putTransferResponse(result.toTransferResponse())
            setResult(result.isSuccessful)
        }
    }
}
