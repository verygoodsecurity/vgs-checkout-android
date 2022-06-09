package com.verygoodsecurity.vgscheckout.ui.fragment.save

import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.exception.internal.NoInternetConnectionException
import com.verygoodsecurity.vgscheckout.networking.command.AddCardCommand
import com.verygoodsecurity.vgscheckout.util.extension.getBaseUrl
import com.verygoodsecurity.vgscheckout.util.extension.toResponseEvent

internal class SaveAndPayFragment : OrchestrationFragment() {

    private var addCardCommand: AddCardCommand? = null

    override fun onDestroy() {
        super.onDestroy()
        addCardCommand?.cancel()
    }

    override fun onActionButtonClick() {
        saveCard()
    }

    private fun saveCard() {
        setIsLoading(true)
        addCardCommand = AddCardCommand(
            requireContext(),
            AddCardCommand.Params(
                config.getBaseUrl(requireContext()),
                config.routeConfig.path,
                config.routeConfig,
                getStates()
            )
        )
        addCardCommand?.execute(::handleSaveCardResult)
    }

    private fun handleSaveCardResult(result: AddCardCommand.Result) {
        if (!shouldHandleAddCard) {
            return
        }
        logSaveCardResponse(result)

        if (result.code == NoInternetConnectionException.CODE) { // TODO: Refactor error handling
            setIsLoading(false)
            showRetrySnackBar(getString(R.string.vgs_checkout_no_network_error)) { saveCard() }
            return
        }
        transfer()
    }


    private fun logSaveCardResponse(result: AddCardCommand.Result) {
        config.analyticTracker.log(result.toResponseEvent())
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