package com.verygoodsecurity.vgscheckout.model

import android.os.Bundle
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutAddCardResponse
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

    fun getBoolean(key: String): Boolean? =
        if (bundle.containsKey(key)) bundle.getBoolean(key) else null

    internal fun putAddCardResponse(response: VGSCheckoutAddCardResponse) {
        bundle.putParcelable(ADD_CARD_RESPONSE, response)
    }

    internal fun putShouldSaveCard(shouldSaveCard: Boolean) {
        bundle.putBoolean(SHOULD_SAVE_CARD, shouldSaveCard)
    }

    companion object Keys {

        const val ADD_CARD_RESPONSE = "com.verygoodsecurity.vgscheckout.add_card_response"
        internal const val SHOULD_SAVE_CARD = "com.verygoodsecurity.vgscheckout.should_save_card"
    }
}