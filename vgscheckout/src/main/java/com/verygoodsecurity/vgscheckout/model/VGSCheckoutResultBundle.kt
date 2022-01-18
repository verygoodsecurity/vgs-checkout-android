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
class VGSCheckoutResultBundle private constructor(private val bundle: Bundle) : Parcelable {

    internal constructor() : this(Bundle())

    /**
     * If add card request was made that response object will be returned, null otherwise.
     */
    fun getAddCardResponse(): VGSCheckoutAddCardResponse? {
        bundle.classLoader = VGSCheckoutAddCardResponse::class.java.classLoader
        return bundle.getParcelable(ADD_CARD_RESPONSE_KEY)
    }

    /**
     * If transaction request was made that response object will be returned, null otherwise.
     */
    fun getTransactionResponse(): VGSCheckoutTransactionResponse? {
        bundle.classLoader = VGSCheckoutTransactionResponse::class.java.classLoader
        return bundle.getParcelable(TRANSACTION_RESPONSE_KEY)
    }

    internal fun putAddCardResponse(response: VGSCheckoutAddCardResponse) {
        bundle.putParcelable(ADD_CARD_RESPONSE_KEY, response)
    }

    internal fun putTransactionResponse(response: VGSCheckoutTransactionResponse) {
        bundle.putParcelable(TRANSACTION_RESPONSE_KEY, response)
    }

    companion object Keys {

        const val ADD_CARD_RESPONSE_KEY = "com.verygoodsecurity.vgscheckout.add_card_response_key"
        const val TRANSACTION_RESPONSE_KEY =
            "com.verygoodsecurity.vgscheckout.transaction_response_key"
    }
}