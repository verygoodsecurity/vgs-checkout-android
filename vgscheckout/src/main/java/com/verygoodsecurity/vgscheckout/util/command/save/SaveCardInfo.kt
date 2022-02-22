package com.verygoodsecurity.vgscheckout.util.command.save

import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.collect.core.model.network.toVGSResponse
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutNetworkException
import com.verygoodsecurity.vgscheckout.util.command.NetworkingCommand
import com.verygoodsecurity.vgscheckout.util.command.Result
import com.verygoodsecurity.vgscheckout.util.command.VGSCheckoutCancellable
import com.verygoodsecurity.vgscheckout.util.extension.toSaveCardNetworkRequest

internal class SaveCardInfo :
    NetworkingCommand<CardInfo, Result<VGSResponse>>(),
    VGSCheckoutCancellable {

    override fun run(
        params: CardInfo,
        onResult: (Result<VGSResponse>) -> Unit
    ): VGSCheckoutCancellable {

        params.config.toSaveCardNetworkRequest(
            params.baseUrl,
            params.data
        ).run {
            client.enqueue(this) {
                if (it.isSuccessful) {
                    try {
                        onResult.invoke(Result.Success(it.toVGSResponse()))
                    } catch (e: VGSCheckoutException) {
                        onResult.invoke(Result.Error(e))
                    }
                } else {
                    onResult.invoke(
                        Result.Error(
                            VGSCheckoutNetworkException(
                                code = it.code,
                                message = it.message,
                                body = it.body
                            )
                        )
                    )
                }
            }
        }

        return this
    }
}