package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import kotlinx.parcelize.Parcelize

/**
 * Card holder input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param visibility defines if card holder name input field should be visible to user.
 */
@Parcelize
class VGSCheckoutPaymentCardHolderOptions private constructor(
    override val fieldName: String,
    override val visibility: VGSCheckoutFieldVisibility,
) : CardHolderOptions() {

    /**
     * Public constructor.
     */
    constructor() : this(FIELD_NAME, VGSCheckoutFieldVisibility.VISIBLE)

    private companion object {

        private const val FIELD_NAME = "card.name"
    }
}