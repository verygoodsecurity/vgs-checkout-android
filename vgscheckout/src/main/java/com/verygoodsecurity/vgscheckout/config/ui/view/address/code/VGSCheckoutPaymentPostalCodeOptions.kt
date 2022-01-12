package com.verygoodsecurity.vgscheckout.config.ui.view.address.code

import kotlinx.parcelize.Parcelize

/**
 * Postal code input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 */
@Parcelize
class VGSCheckoutPaymentPostalCodeOptions private constructor(
    override val fieldName: String,
) : PostalCodeOptions() {

    /**
     * Public constructor.
     */
    constructor() : this(FIELD_NAME)

    private companion object {

        private const val FIELD_NAME = "card.billing_address.postal_code"
    }
}