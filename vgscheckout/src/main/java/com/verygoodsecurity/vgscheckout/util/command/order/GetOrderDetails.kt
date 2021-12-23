package com.verygoodsecurity.vgscheckout.util.command.order

import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.VGSHttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.api.client.ApiClient
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkRequest
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutNetworkException
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutResponseParseException
import com.verygoodsecurity.vgscheckout.util.command.AsynchronousCommand
import com.verygoodsecurity.vgscheckout.util.command.Result
import com.verygoodsecurity.vgscheckout.util.command.VGSCheckoutCancellable
import org.json.JSONObject

internal class GetOrderDetails : AsynchronousCommand<String, Result<OrderDetails>>(),
    VGSCheckoutCancellable {

    private val client = ApiClient.create(false)

    override fun run(
        parameter: String,
        onResult: (Result<OrderDetails>) -> Unit
    ): VGSCheckoutCancellable {
        client.enqueue(createOrderDetailsRequest(parameter)) {
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
                            message = it.message,
                            code = it.code,
                            body = it.body
                        )
                    )
                )
            }
        }
        return this
    }


    @Throws(VGSCheckoutException::class)
    private fun parseResponse(body: String?): OrderDetails {
        if (body.isNullOrEmpty()) {
            throw VGSCheckoutResponseParseException("Response body is null or empty.")
        }
        try {
            val data = JSONObject(body).getJSONObject(JSON_KEY_DATA)
            return OrderDetails(data.getInt(JSON_KEY_AMOUNT), data.getString(JSON_KEY_CURRENCY))
        } catch (e: Exception) {
            throw VGSCheckoutResponseParseException("Can't parse response body.", e)
        }
    }

    override fun cancel() {
        client.cancelAll()
    }

    private fun createOrderDetailsRequest(orderId: String): NetworkRequest {
        return NetworkRequest(
            method = HTTPMethod.GET,
            url = URL + orderId,
            customHeader = emptyMap(),
            customData = Unit,
            fieldsIgnore = false,
            fileIgnore = false,
            format = VGSHttpBodyFormat.JSON,
            requestTimeoutInterval = REQUEST_TIMEOUT
        )
    }

    companion object {

        private const val URL = "https://multiplexing-demo.apps.verygood.systems/orders/"

        private const val REQUEST_TIMEOUT = 60_000L

        private const val JSON_KEY_DATA = "data"
        private const val JSON_KEY_AMOUNT = "amount"
        private const val JSON_KEY_CURRENCY = "currency"
    }
}