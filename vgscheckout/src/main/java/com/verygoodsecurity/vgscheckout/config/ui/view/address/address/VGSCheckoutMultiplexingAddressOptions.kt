package com.verygoodsecurity.vgscheckout.config.ui.view.address.address

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

/**
 * Address input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 */
@Parcelize
class VGSCheckoutMultiplexingAddressOptions private constructor(
    override val fieldName: String,
) : ViewOptions() {

    /**
     * Public constructor.
     */
    constructor() : this(FIELD_NAME)

    private companion object {

        private const val FIELD_NAME = "card.billing_address.address1"
    }
}