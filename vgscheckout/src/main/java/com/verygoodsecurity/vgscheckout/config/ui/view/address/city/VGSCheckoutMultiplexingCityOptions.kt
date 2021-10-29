package com.verygoodsecurity.vgscheckout.config.ui.view.address.city

import kotlinx.parcelize.Parcelize

/**
 * Multiplexing city input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 */
@Parcelize
class VGSCheckoutMultiplexingCityOptions private constructor(
    override val fieldName: String,
) : CityOptions() {

    /**
     * Public constructor.
     */
    constructor() : this(FIELD_NAME)

    private companion object {

        private const val FIELD_NAME = "card.billing_address.city"
    }
}