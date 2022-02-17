package com.verygoodsecurity.vgscheckout.util.command.order

import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.VGSHttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkRequest
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutNetworkException
import com.verygoodsecurity.vgscheckout.exception.internal.PaymentInfoParseException
import com.verygoodsecurity.vgscheckout.util.command.NetworkingCommand
import com.verygoodsecurity.vgscheckout.util.command.Result
import com.verygoodsecurity.vgscheckout.util.command.VGSCheckoutCancellable
import org.json.JSONObject

internal class GetPaymentInfo : NetworkingCommand<String, Result<PaymentInfo>>(),
    VGSCheckoutCancellable {

    override fun run(
        params: String,
        onResult: (Result<PaymentInfo>) -> Unit
    ): VGSCheckoutCancellable {
        client.enqueue(createOrderDetailsRequest(params)) {
            if (it.isSuccessful) {
                try {
                    onResult.invoke(Result.Success(parseResponse(it.body)))
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
        return this
    }

    @Throws(VGSCheckoutException::class)
    private fun parseResponse(body: String?): PaymentInfo {
        try {
            val data = JSONObject(body!!).getJSONObject(JSON_KEY_DATA)
            return PaymentInfo(data.getLong(JSON_KEY_AMOUNT), data.getString(JSON_KEY_CURRENCY))
        } catch (e: Exception) {
            throw PaymentInfoParseException(e)
        }
    }

    override fun cancel() {
        client.cancelAll()
    }

    private fun createOrderDetailsRequest(orderId: String): NetworkRequest {
        return NetworkRequest(
            method = HTTPMethod.GET,
            //todo replace PAYMENT_ORCHESTRATION_URL with live URL
            url = BuildConfig.PAYMENT_ORCHESTRATION_URL + ORDERS_PATH + orderId,
            customHeader = emptyMap(),
            customData = Unit,
            fieldsIgnore = false,
            fileIgnore = false,
            format = VGSHttpBodyFormat.JSON,
            requestTimeoutInterval = DEFAULT_REQUEST_TIMEOUT
        )
    }

    companion object {

        private const val ORDERS_PATH = "/orders/"

        private const val JSON_KEY_DATA = "data"
        private const val JSON_KEY_AMOUNT = "amount"
        private const val JSON_KEY_CURRENCY = "currency"
    }
}