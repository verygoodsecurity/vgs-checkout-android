package com.verygoodsecurity.vgscheckout.model

import android.os.Bundle
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutAddCardResponse
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutTransactionResponse
import kotlinx.parcelize.Parcelize

/**
 * Wrapper object that helps to retrieve checkout result from bundle.
 */
@Parcelize
class VGSCheckoutResultBundle private constructor(@PublishedApi internal val bundle: Bundle) :
    Parcelable {

    internal constructor() : this(Bundle())

    inline fun <reified T : Parcelable> getParcelable(key: String): T? {
        bundle.classLoader = T::class.java.classLoader
        return bundle.getParcelable(key)
    }

    internal fun putAddCardResponse(response: VGSCheckoutAddCardResponse) {
        bundle.putParcelable(ADD_CARD_RESPONSE_KEY, response)
    }

    internal fun putTransactionResponse(response: VGSCheckoutTransactionResponse) {
        bundle.putParcelable(TRANSACTION_RESPONSE_KEY, response)
    }

    companion object Keys {

        const val ADD_CARD_RESPONSE_KEY = "com.verygoodsecurity.vgscheckout.add_card_response"
        const val TRANSACTION_RESPONSE_KEY = "com.verygoodsecurity.vgscheckout.transaction_response"
    }
}