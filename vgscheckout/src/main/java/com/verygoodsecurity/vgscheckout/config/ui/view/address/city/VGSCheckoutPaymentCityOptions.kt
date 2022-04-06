package com.verygoodsecurity.vgscheckout.config.ui.view.address.city

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import kotlinx.parcelize.Parcelize

/**
 * City input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param visibility defines if input field should be visible to user.
 */
@Parcelize
internal class VGSCheckoutPaymentCityOptions private constructor(
    override val fieldName: String,
    override val visibility: VGSCheckoutFieldVisibility
) : CityOptions() {

    /**
     * Public constructor.
     *
     * @param visibility defines if input field should be visible to user.
     */
    constructor(
        visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
    ) : this(FIELD_NAME, visibility)

    private companion object {

        private const val FIELD_NAME = "card.billing_address.city"
    }
}