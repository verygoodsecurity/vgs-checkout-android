package com.verygoodsecurity.vgscheckout.config.ui.view.address.code

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import kotlinx.parcelize.Parcelize

/**
 * Postal code input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param visibility defines if input field should be visible to user.
 */
@Parcelize
internal class VGSCheckoutPaymentPostalCodeOptions private constructor(
    override val fieldName: String,
    override val visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
) : PostalCodeOptions() {

    /**
     * Public constructor.
     */
    constructor() : this(FIELD_NAME)

    private companion object {

        private const val FIELD_NAME = "card.billing_address.postal_code"
    }
}