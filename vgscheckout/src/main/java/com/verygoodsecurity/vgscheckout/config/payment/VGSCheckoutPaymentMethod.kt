package com.verygoodsecurity.vgscheckout.config.payment

import androidx.annotation.Size
import com.verygoodsecurity.vgscheckout.config.payment.VGSCheckoutPaymentMethod.SavedCards.Companion.MAX_IDS_SIZE
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger

/**
 * Payment methods.
 */
sealed class VGSCheckoutPaymentMethod {

    /**
     * Allows user to use only new card(financial instrument).
     */
    internal object NewCard : VGSCheckoutPaymentMethod()

    /**
     * Add ability to use previously saved cards or new card.
     *
     * @param ids list of cards(financial instruments) ids. Max length [MAX_IDS_SIZE].
     */
    data class SavedCards constructor(
        @Size(max = MAX_IDS_SIZE) private val ids: List<String>
    ) : VGSCheckoutPaymentMethod() {

        fun getIds(): List<String> {
            return if (ids.size > MAX_IDS_SIZE) {
                VGSCheckoutLogger.warn(message = "Max saved cards limit to fetch is $MAX_IDS_SIZE! Current saved cards count is: ${ids.size})!")
                ids.subList(0, MAX_IDS_SIZE.toInt())
            } else {
                ids
            }
        }

        companion object {

            private const val MAX_IDS_SIZE = 10L
        }
    }
}