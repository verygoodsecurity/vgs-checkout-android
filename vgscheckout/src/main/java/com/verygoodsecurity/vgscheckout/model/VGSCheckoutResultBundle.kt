package com.verygoodsecurity.vgscheckout.model

import android.os.Bundle
import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutCardResponse
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutDeleteCardResponse
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

    inline fun <reified T : Parcelable> getParcelableList(key: String): ArrayList<T>? {
        bundle.classLoader = T::class.java.classLoader
        return bundle.getParcelableArrayList(key)
    }

    // TODO: Implement ability to get list of objects

    fun getBoolean(key: String): Boolean? =
        if (bundle.containsKey(key)) bundle.getBoolean(key) else null

    internal fun putAddCardResponse(response: VGSCheckoutCardResponse) {
        bundle.putParcelable(ADD_CARD_RESPONSE, response)
    }

    internal fun putDeleteCardResponse(response: VGSCheckoutDeleteCardResponse) {
        val responseList =
            getParcelableList<VGSCheckoutDeleteCardResponse>(DELETE_CARD_RESPONSES) ?: ArrayList()
        responseList.add(response)
        bundle.putParcelableArrayList(DELETE_CARD_RESPONSES, responseList)
    }

    internal fun putShouldSaveCard(shouldSaveCard: Boolean) {
        bundle.putBoolean(SHOULD_SAVE_CARD, shouldSaveCard)
    }

    internal fun putIsPreSavedCard(isPreSavedCard: Boolean) {
        bundle.putBoolean(IS_PRE_SAVED_CARD, isPreSavedCard)
    }

    companion object Keys {

        const val ADD_CARD_RESPONSE = "com.verygoodsecurity.vgscheckout.add_card_response"
        const val DELETE_CARD_RESPONSES = "com.verygoodsecurity.vgscheckout.delete_card_responses"
        const val SHOULD_SAVE_CARD = "com.verygoodsecurity.vgscheckout.should_save_card"
        const val IS_PRE_SAVED_CARD = "com.verygoodsecurity.vgscheckout.is_pre_saved_card"
    }
}