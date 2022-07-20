package com.verygoodsecurity.vgscheckout.ui.fragment.save

import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.networking.command.AddCardCommand

internal class SaveCardFragment : OrchestrationFragment<VGSCheckoutAddCardConfig>() {

    override fun handleSaveCardResult(result: AddCardCommand.Result) {
        resultHandler.setResult(result.isSuccessful)
    }

}