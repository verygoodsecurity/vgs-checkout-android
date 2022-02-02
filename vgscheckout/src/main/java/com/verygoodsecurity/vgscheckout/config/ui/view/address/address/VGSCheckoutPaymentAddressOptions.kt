package com.verygoodsecurity.vgscheckout.config.ui.view.address.address

import kotlinx.parcelize.Parcelize

/**
 * Address input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 */
@Parcelize
internal class VGSCheckoutPaymentAddressOptions private constructor(
    override val fieldName: String,
) : AddressOptions() {

    /**
     * Public constructor.
     */
    constructor() : this(FIELD_NAME)

    private companion object {

        private const val FIELD_NAME = "card.billing_address.address1"
    }
}