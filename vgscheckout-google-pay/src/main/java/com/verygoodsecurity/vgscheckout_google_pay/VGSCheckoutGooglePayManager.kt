package com.verygoodsecurity.vgscheckout_google_pay

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.verygoodsecurity.vgscheckout_google_pay.extensions.toStringList
import org.json.JSONArray
import org.json.JSONObject

class VGSCheckoutGooglePayManager constructor(
    context: Context,
    private val merchantId: String
) {

    private val client = Wallet.getPaymentsClient(
        context,
        Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
            .build()
    )

    private val baseRequest: JSONObject = JSONObject().apply {
        put("apiVersion", 2)
        put("apiVersionMinor", 0)
    }

    private val allowedCardNetworks: JSONArray = JSONArray(arrayOf("MASTERCARD", "VISA"))

    private val allowedAuthMethods: JSONArray = JSONArray(arrayOf("PAN_ONLY", "CRYPTOGRAM_3DS"))

    private val isReadyToPayRequest: JSONObject = baseRequest.apply {
        put("allowedPaymentMethods", JSONArray().put(generateBaseCardPaymentMethod()))
    }

    private val cardPaymentMethod: JSONObject = generateBaseCardPaymentMethod().apply {
        put("tokenizationSpecification", JSONObject().apply {
            put("type", "PAYMENT_GATEWAY")
            put("parameters", JSONObject().apply {
                put("gateway", "verygoodsecurity")
                put("gatewayMerchantId", merchantId)
            })
        })
    }

    fun isReadyToPay(onResult: (Boolean, Exception?) -> Unit) {
        client.isReadyToPay(IsReadyToPayRequest.fromJson(isReadyToPayRequest.toString()))
            .addOnCompleteListener { task ->
                try {
                    task.getResult(ApiException::class.java)?.let {
                        onResult.invoke(it, null)
                    }
                } catch (e: ApiException) {
                    onResult.invoke(false, e)
                }
            }
    }

    fun loadPaymentData(
        price: Int,
        currency: String,
        activity: Activity
    ) {
        val request = baseRequest.apply {
            put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod))
            put("transactionInfo", JSONObject().apply {
                put("totalPrice", price.toString())
                put("totalPriceStatus", "FINAL")
                put("countryCode", "US")
                put("currencyCode", currency)
            })
            put("merchantInfo", JSONObject().put("merchantName", "Example Merchant"))
        }
        AutoResolveHelper.resolveTask(
            client.loadPaymentData(PaymentDataRequest.fromJson(request.toString())),
            activity,
            LOAD_PAYMENT_DATA_REQUEST_CODE
        )
    }

    /**
     * Should be called from [Activity.onActivityResult] function. User
     */
    fun onLoadPaymentDataResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        listener: VGSCheckoutGooglePayListener
    ) {
        if (requestCode != LOAD_PAYMENT_DATA_REQUEST_CODE) {
            return
        }
        when (resultCode) {
            Activity.RESULT_OK ->
                // TODO: Refactor this code
                if (data == null) {
                    listener.onError(VGSCheckoutGooglePayException.create(null))
                    return
                } else {
                    val paymentData = PaymentData.getFromIntent(data)
                    if (paymentData == null) {
                        listener.onError(VGSCheckoutGooglePayException.create(null))
                        return
                    } else {
                        try {
                            listener.onSuccess(parseToken(paymentData))
                        } catch (e: Exception) {
                            listener.onError(VGSCheckoutGooglePayException.create(null))
                        }
                    }
                }
            AutoResolveHelper.RESULT_ERROR -> {
                listener.onError(
                    VGSCheckoutGooglePayException.create(
                        AutoResolveHelper.getStatusFromIntent(
                            data
                        )
                    )
                )
            }
            Activity.RESULT_CANCELED -> listener.onCancel()
        }
    }

    private fun generateBaseCardPaymentMethod() = JSONObject().apply {
        put("type", "CARD")
        put("parameters", JSONObject().apply {
            put("allowedAuthMethods", allowedAuthMethods)
            put("allowedCardNetworks", allowedCardNetworks)
        })
    }

    // TODO: Refactor
    @Throws(Exception::class)
    private fun parseToken(data: PaymentData): VGSCheckoutGooglePayToken {
        val paymentData = JSONObject(data.toJson())
        val paymentMethodDataJson = paymentData.getJSONObject("paymentMethodData")
        val tokenizationDataJson = paymentMethodDataJson.getJSONObject("tokenizationData")

        val tokenJson = JSONObject(tokenizationDataJson.getString("token"))

        val signature = tokenJson.getString("signature")

        val intermediateSigningKeyJson = tokenJson.getJSONObject("intermediateSigningKey")

        val signedKeyJson = JSONObject(intermediateSigningKeyJson.getString("signedKey"))
        val keyValue = signedKeyJson.getString("keyValue")
        val keyExpiration = signedKeyJson.getString("keyExpiration")

        val signatures = intermediateSigningKeyJson.getJSONArray("signatures").toStringList()

        val protocolVersion = tokenJson.getString("protocolVersion")

        val signedMessageJson = JSONObject(tokenJson.getString("signedMessage"))
        val encryptedMessage = signedMessageJson.getString("encryptedMessage")
        val ephemeralPublicKey = signedMessageJson.getString("ephemeralPublicKey")
        val tag = signedMessageJson.getString("tag")

        return VGSCheckoutGooglePayToken(
            signature,
            VGSCheckoutGooglePayToken.IntermediateSigningKey(
                VGSCheckoutGooglePayToken.IntermediateSigningKey.SignedKey(
                    keyValue,
                    keyExpiration
                ),
                signatures
            ),
            protocolVersion,
            VGSCheckoutGooglePayToken.SignedMessage(
                encryptedMessage,
                ephemeralPublicKey,
                tag
            )
        )
    }

    companion object {

        const val LOAD_PAYMENT_DATA_REQUEST_CODE = 1
    }
}