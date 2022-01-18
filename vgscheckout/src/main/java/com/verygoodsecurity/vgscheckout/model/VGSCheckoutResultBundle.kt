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
     * Return add card response if request was made.
     *
     * @return add card response or null.
     */
    fun getAddCardResponse(): VGSCheckoutAddCardResponse? {
        bundle.classLoader = VGSCheckoutAddCardResponse::class.java.classLoader
        return bundle.getParcelable(ADD_CARD_RESPONSE_KEY)
    }

    /**
     * Return transaction response if request was made.
     *
     * @return transaction response or null.
     */
    fun getTransactionResponse(): VGSCheckoutTransactionResponse? {
        bundle.classLoader = VGSCheckoutTransactionResponse::class.java.classLoader
        return bundle.getParcelable(TRANSACTION_RESPONSE_KEY)
    }

    /**
     * @return true if user select save card for future use. Return false if checkbox is not supported by
     * checkout flow, or hidden via saveCardOptionEnabled.
     */
    fun shouldSaveCard(): Boolean? {
        if (bundle.containsKey(SHOULD_SAVE_CARD_KEY)) {
            return bundle.getBoolean(SHOULD_SAVE_CARD_KEY)
        }
        return null
    }

    internal fun putAddCardResponse(response: VGSCheckoutAddCardResponse) {
        bundle.putParcelable(ADD_CARD_RESPONSE_KEY, response)
    }

    internal fun putTransactionResponse(response: VGSCheckoutTransactionResponse) {
        bundle.putParcelable(TRANSACTION_RESPONSE_KEY, response)
    }

    internal fun putShouldSaveCard(shouldSaveCard: Boolean) {
        bundle.putBoolean(SHOULD_SAVE_CARD_KEY, shouldSaveCard)
    }

    private companion object Keys {

        const val ADD_CARD_RESPONSE_KEY = "com.verygoodsecurity.vgscheckout.add_card_response"
        const val TRANSACTION_RESPONSE_KEY = "com.verygoodsecurity.vgscheckout.transaction_response"
        const val SHOULD_SAVE_CARD_KEY = "com.verygoodsecurity.vgscheckout.should_save_card"
    }
}