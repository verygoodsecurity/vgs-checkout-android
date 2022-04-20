package com.verygoodsecurity.vgscheckout.config.payment

import androidx.annotation.Size
import com.verygoodsecurity.vgscheckout.config.payment.VGSCheckoutPaymentMethod.SavedCards.Companion.MAX_CARDS_SIZE
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
     * @param cardIds list of cards(financial instruments) ids. Max length [MAX_CARDS_SIZE].
     */
    data class SavedCards constructor(
        @Size(max = MAX_CARDS_SIZE) private val cardIds: List<String>
    ) : VGSCheckoutPaymentMethod() {

        fun getIds(): List<String> {
            return if (cardIds.size > MAX_CARDS_SIZE) {
                VGSCheckoutLogger.warn(message = "Max saved cards limit to fetch is $MAX_CARDS_SIZE! Current saved cards count is: ${cardIds.size})!")
                cardIds.subList(0, MAX_CARDS_SIZE.toInt())
            } else {
                cardIds
            }
        }

        companion object {

            private const val MAX_CARDS_SIZE = 10L
        }
    }
}