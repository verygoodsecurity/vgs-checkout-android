package com.verygoodsecurity.vgscheckout.ui

import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.VGSHttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.api.client.ApiClient
import com.verygoodsecurity.vgscheckout.collect.core.api.client.OkHttpClient
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkRequest
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.exception.internal.VGSCheckoutFinIdNotFoundException
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutCard
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutAddCardResponse
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import com.verygoodsecurity.vgscheckout.ui.core.OnPaymentMethodSelectedListener
import com.verygoodsecurity.vgscheckout.util.extension.toTransactionResponse
import org.json.JSONObject

internal class PaymentActivity : BaseCheckoutActivity<VGSCheckoutPaymentConfig>(),
    OnPaymentMethodSelectedListener {

    private val client: ApiClient = OkHttpClient()

    override fun initFragment() {
        if (config.savedCards.isEmpty()) {
            super.initFragment()
            return
        }
        navigateToPaymentMethods()
    }

    override fun onDestroy() {
        super.onDestroy()
        client.cancelAll()
    }

    override fun onCardSelected(card: VGSCheckoutCard) {
        pay(card.finId)
    }

    override fun handleSuccessfulAddCardResponse(response: VGSCheckoutAddCardResponse) {
        try {
            pay(readFinancialInstrumentId(response))
        } catch (e: VGSCheckoutException) {
            sendResult(VGSCheckoutResult.Failed(resultBundle, e))
        }
    }

    @Throws(VGSCheckoutException::class)
    private fun readFinancialInstrumentId(response: VGSCheckoutAddCardResponse): String {
        try {
            return JSONObject(response.body!!).getJSONObject(JSON_KEY_DATA).getString(JSON_KEY_ID)
        } catch (e: Exception) {
            throw VGSCheckoutFinIdNotFoundException(e)
        }
    }

    private fun pay(financialInstrumentId: String) {
        loadingHandler.setIsLoading(true)
        client.enqueue(createPayRequest(financialInstrumentId)) {
            runOnUiThread {
                val transactionResponse = it.toTransactionResponse()
                resultBundle.putTransactionResponse(transactionResponse)
                sendResult(
                    if (transactionResponse.isSuccessful) {
                        VGSCheckoutResult.Success(resultBundle)
                    } else {
                        VGSCheckoutResult.Failed(resultBundle)
                    }
                )
            }
        }
    }

    private fun createPayRequest(financialInstrumentId: String) = NetworkRequest(
        method = HTTPMethod.POST,
        url = BuildConfig.PAYMENT_ORCHESTRATION_URL + PAYMENT_PATH,
        customHeader = emptyMap(),
        customData = createPayload(financialInstrumentId),
        fieldsIgnore = false,
        fileIgnore = false,
        format = VGSHttpBodyFormat.JSON,
        requestTimeoutInterval = 60_000
    )

    private fun createPayload(findId: String): String {
        return "{\"fi_id\": \"$findId\"," +
                "\"tnt\": \"${config.tenantId}\"," +
                "\"amount\": ${config.paymentInfo.amount}," +
                "\"currency\": \"${config.paymentInfo.currency}\"" +
                "}"
    }

    companion object {

        private const val PAYMENT_PATH = "/transfers"
        private const val JSON_KEY_DATA = "data"
        private const val JSON_KEY_ID = "id"
    }
}