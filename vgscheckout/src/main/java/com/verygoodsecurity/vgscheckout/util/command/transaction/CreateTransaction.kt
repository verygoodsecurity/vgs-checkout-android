package com.verygoodsecurity.vgscheckout.util.command.transaction

import android.util.Log
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.VGSHttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkRequest
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutTransactionResponse
import com.verygoodsecurity.vgscheckout.util.command.NetworkingCommand
import com.verygoodsecurity.vgscheckout.util.command.Result
import com.verygoodsecurity.vgscheckout.util.command.VGSCheckoutCancellable
import com.verygoodsecurity.vgscheckout.util.extension.toTransactionResponse
import org.json.JSONObject

internal class CreateTransaction :
    NetworkingCommand<TransactionParams, Result<VGSCheckoutTransactionResponse>>(),
    VGSCheckoutCancellable {

    override fun run(
        params: TransactionParams,
        onResult: (Result<VGSCheckoutTransactionResponse>) -> Unit
    ): VGSCheckoutCancellable {
        val payload = createPayload(params)
        val request = createTransactionRequest(payload)
        client.enqueue(request) {
            onResult.invoke(Result.Success(it.toTransactionResponse()))
            Log.d("Test", it.toString())
        }
        return this
    }

    override fun cancel() {
        client.cancelAll()
    }

    private fun createPayload(params: TransactionParams): String {
        val json = JSONObject()
        json.put(JSON_KEY_FIN_ID, params.finId)
        json.put(JSON_KEY_VAULT, params.vaultId)
        json.put(JSON_KEY_AMOUNT, params.amount)
        json.put(JSON_KEY_CURRENCY, params.currency)
        return json.toString()
    }

    private fun createTransactionRequest(payload: String) = NetworkRequest(
        method = HTTPMethod.POST,
        url = BuildConfig.PAYMENT_ORCHESTRATION_URL + PATH,
        customHeader = emptyMap(),
        customData = payload,
        fieldsIgnore = false,
        fileIgnore = false,
        format = VGSHttpBodyFormat.JSON,
        requestTimeoutInterval = DEFAULT_REQUEST_TIMEOUT
    )

    companion object {

        private const val PATH = "/transfers"

        private const val JSON_KEY_FIN_ID = "fi_id"
        private const val JSON_KEY_VAULT = "tnt"
        private const val JSON_KEY_AMOUNT = "amount"
        private const val JSON_KEY_CURRENCY = "currency"
    }
}