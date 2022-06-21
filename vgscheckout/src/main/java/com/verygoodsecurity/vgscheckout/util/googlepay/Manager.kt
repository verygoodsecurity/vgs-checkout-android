package com.verygoodsecurity.vgscheckout.util.googlepay

import android.app.Activity
import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.verygoodsecurity.vgscheckout.model.OrderDetails
import org.json.JSONArray
import org.json.JSONObject

internal class Manager constructor(
    context: Context,
    private val merchantId: String
) {

    private val client = Wallet.getPaymentsClient(
        context,
        Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
            .build()
    )

    private val baseRequest = JSONObject().apply {
        put("apiVersion", 2)
        put("apiVersionMinor", 0)
    }

    private val allowedCardNetworks = JSONArray(arrayOf("MASTERCARD", "VISA"))

    private val allowedAuthMethods = JSONArray(arrayOf("PAN_ONLY", "CRYPTOGRAM_3DS"))

    private val isReadyToPayRequest = IsReadyToPayRequest.fromJson(baseRequest.apply {
        put("allowedPaymentMethods", JSONArray().put(generateBaseCardPaymentMethod()))
    }.toString())

    private val cardPaymentMethod = generateBaseCardPaymentMethod().apply {
        put("tokenizationSpecification", JSONObject().apply {
            put("type", "PAYMENT_GATEWAY")
            put("parameters", JSONObject().apply {
                put("gateway", "verygoodsecurity")
                put("gatewayMerchantId", merchantId)
            })
        })
    }

    fun isReadyToPay(onResult: (Boolean, Exception?) -> Unit) {
        client.isReadyToPay(isReadyToPayRequest).addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)?.let {
                    onResult.invoke(it, null)
                }
            } catch (e: ApiException) {
                onResult.invoke(false, e)
            }
        }
    }

    fun loadPaymentData(order: OrderDetails, activity: Activity) {
        val test = baseRequest.apply {
            put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod))
            put("transactionInfo", JSONObject().apply {
                put("totalPrice", order.price.toString())
                put("totalPriceStatus", "FINAL")
                put("countryCode", "US")
                put("currencyCode", order.currency)
            })
            put("merchantInfo", JSONObject().put("merchantName", "Example Merchant"))
        }
        val paymentDataRequest = PaymentDataRequest.fromJson(test.toString())
        AutoResolveHelper.resolveTask(
            client.loadPaymentData(paymentDataRequest),
            activity,
            LOAD_PAYMENT_DATA_REQUEST_CODE
        )
    }

    private fun generateBaseCardPaymentMethod() = JSONObject().apply {
        put("type", "CARD")
        put("parameters", JSONObject().apply {
            put("allowedAuthMethods", allowedAuthMethods)
            put("allowedCardNetworks", allowedCardNetworks)
        })
    }

    companion object {

        const val LOAD_PAYMENT_DATA_REQUEST_CODE = 1
    }
}