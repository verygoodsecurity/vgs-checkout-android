package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Card holder input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 */
@Parcelize
internal class VGSCheckoutPaymentCardHolderOptions private constructor(
    override val fieldName: String
) : CardHolderOptions() {

    /**
     *  Defines if input field should be visible to user.
     */
    @IgnoredOnParcel
    override val visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE

    /**
     * Public constructor.
     */
    constructor() : this(FIELD_NAME)

    private companion object {

        private const val FIELD_NAME = "card.name"
    }
}