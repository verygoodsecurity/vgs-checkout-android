package com.verygoodsecurity.vgscheckout.config.ui.view.address.address

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

/**
 * Multiplexing optional address input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 */
@Parcelize
class VGSCheckoutMultiplexingOptionalAddressOptions private constructor(
    override val fieldName: String,
) : ViewOptions() {

    /**
     * Public constructor.
     */
    constructor() : this(FIELD_NAME)

    private companion object {

        private const val FIELD_NAME = "card.billing_address.address2"
    }
}