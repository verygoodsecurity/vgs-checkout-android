package com.verygoodsecurity.vgscheckout.ui.fragment.save

import com.verygoodsecurity.vgscheckout.networking.command.AddCardCommand
import com.verygoodsecurity.vgscheckout.util.extension.getBaseUrl

internal class SaveAndPayFragment : OrchestrationFragment() {

    private var addCardCommand: AddCardCommand? = null

    override fun onDestroy() {
        super.onDestroy()
        addCardCommand?.cancel()
    }

    override fun onActionButtonClick() {
        saveCard()
    }

    override fun onRetryButtonClick() {
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

}