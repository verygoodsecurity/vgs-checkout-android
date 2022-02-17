package com.verygoodsecurity.vgscheckout.model.response

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.exception.internal.FinIdNotFoundException
import com.verygoodsecurity.vgscheckout.model.response.core.VGSCheckoutResponse
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class VGSCheckoutAddCardResponse(
    override val isSuccessful: Boolean,
    override val code: Int,
    override val body: String?,
    override val message: String?
) : VGSCheckoutResponse() {

    @Throws(VGSCheckoutException::class)
    internal fun getFinancialInstrumentId(): String {
        try {
            return JSONObject(body!!).getJSONObject(JSON_KEY_DATA).getString(JSON_KEY_ID)
        } catch (e: Exception) {
            throw FinIdNotFoundException(e)
        }
    }

    private companion object {

        const val JSON_KEY_DATA = "data"
        const val JSON_KEY_ID = "id"
    }
}