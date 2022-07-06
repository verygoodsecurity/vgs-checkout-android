package com.verygoodsecurity.vgscheckout.networking.command

import android.content.Context
import com.verygoodsecurity.vgscheckout.collect.util.extension.concatWithSlash
import com.verygoodsecurity.vgscheckout.config.payment.OrderDetails
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.networking.client.HttpMethod
import com.verygoodsecurity.vgscheckout.networking.client.HttpRequest
import com.verygoodsecurity.vgscheckout.networking.client.HttpResponse
import com.verygoodsecurity.vgscheckout.networking.command.core.Command
import org.json.JSONObject

internal class GetOrderDetails constructor(
    context: Context,
    params: Params
) : Command<GetOrderDetails.Params, GetOrderDetails.Result>(context, params) {

    override fun run(params: Params, onResult: (Result) -> Unit) {
        client.enqueue(createRequest(params)) {
            onResult.invoke(
                Result(
                    parseResponse(it)
                )
            )
        }
    }

    private fun createRequest(params: Params) = HttpRequest(
        "https://tntipgdjdyl-4880868f-d88b-4333-ab70-d9deecdbffc4.sandbox.verygoodproxy.com/orders" concatWithSlash params.orderId,
        null,
        mapOf(
            AUTHORIZATION_HEADER_KEY to String.format(
                AUTHORIZATION_HEADER_VALUE,
                params.accessToken
            )
        ),
        HttpMethod.GET
    )

    private fun parseResponse(response: HttpResponse): OrderDetails? {
        if (response.body.isNullOrEmpty()) {
            return null
        }
        return try {
            JSONObject(response.body).getJSONObject(JSON_KEY_DATA).run {
                OrderDetails(
                    getString(JSON_KEY_ID),
                    getInt(JSON_KEY_AMOUNT),
                    getString(JSON_KEY_CURRENCY),
                    OrderDetails.Raw(
                        response.isSuccessful,
                        response.code,
                        response.body,
                        response.message
                    )
                )
            }
        } catch (e: Exception) {
            null
        }
    }


    override fun map(params: Params, exception: VGSCheckoutException) = Result(null)


    companion object {

        private const val AUTHORIZATION_HEADER_KEY = "Authorization"
        private const val AUTHORIZATION_HEADER_VALUE = "Bearer %s"

        private const val PATH = "orders"

        private const val JSON_KEY_DATA = "data"
        private const val JSON_KEY_ID = "id"
        private const val JSON_KEY_AMOUNT = "amount"
        private const val JSON_KEY_CURRENCY = "currency"
    }

    internal data class Params(
        val url: String,
        val orderId: String,
        val accessToken: String
    ) : Command.Params()

    internal data class Result(
        val orderDetails: OrderDetails?
    ) : Command.Result()
}