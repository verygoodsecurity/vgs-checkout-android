package com.verygoodsecurity.vgscheckout.ui

import android.content.Intent
import com.verygoodsecurity.vgscheckout.BuildConfig
import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.api.VGSHttpBodyFormat
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.collect.core.model.network.toVGSResponse
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingPaymentConfig
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.exception.internal.VGSCheckoutFinIdNotFoundException
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import com.verygoodsecurity.vgscheckout.util.extension.toCheckoutResult
import org.json.JSONObject

internal class MultiplexingPaymentCheckoutActivity :
    BaseCheckoutActivity<VGSCheckoutMultiplexingPaymentConfig>() {

    override fun resolveConfig(intent: Intent) =
        CheckoutResultContract.Args.fromIntent<VGSCheckoutMultiplexingPaymentConfig>(intent).config

    override fun hasCustomHeaders() = false

    override fun handleResponse(response: VGSResponse) {
        when (response) {
            is VGSResponse.SuccessResponse -> {
                try {
                    pay(readFinancialInstrumentId(response))
                } catch (e: VGSCheckoutException) {
                    sendResult(VGSCheckoutResult.Failed(e.code, e.message, response.body))
                }
            }
            is VGSResponse.ErrorResponse -> sendResult(response.toCheckoutResult())
        }
    }

    @Throws(VGSCheckoutException::class)
    private fun readFinancialInstrumentId(response: VGSResponse.SuccessResponse): String {
        try {
            return JSONObject(response.body!!).getJSONObject(JSON_KEY_DATA).getString(JSON_KEY_ID)
        } catch (e: Exception) {
            throw VGSCheckoutFinIdNotFoundException(e)
        }
    }

    private fun pay(financialInstrumentId: String) {
        client.enqueue(createPayRequest(financialInstrumentId)) {
            runOnUiThread {
                loadingHandler.setIsLoading(false)
                sendResult(it.toVGSResponse().toCheckoutResult())
            }
        }
    }

    private fun createPayRequest(financialInstrumentId: String) = NetworkRequest(
        method = HTTPMethod.POST,
        url = BuildConfig.MULTIPLEXING_URL + PAYMENT_PATH,
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
                "\"amount\": ${config.paymentInfo.price}," +
                "\"currency\": \"${config.paymentInfo.currency}\"" +
                "}"
    }

    companion object {

        private const val PAYMENT_PATH = "/transfers"
        private const val JSON_KEY_DATA = "data"
        private const val JSON_KEY_ID = "id"
    }
}