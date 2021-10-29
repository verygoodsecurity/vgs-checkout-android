package com.verygoodsecurity.vgscheckout.config.ui.view.address.country

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

/**
 * Multiplexing country input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param validCountries list of countries that will be show in selection dialog.
 */
@Parcelize
class VGSCheckoutMultiplexingCountryOptions private constructor(
    override val fieldName: String,
    val validCountries: List<String>,
) : ViewOptions() {

    /**
     * Public constructor
     *
     * @param validCountries list of countries that will be show in selection dialog.
     */
    @JvmOverloads
    constructor(validCountries: List<String> = emptyList()) : this(FIELD_NAME, validCountries)

    private companion object {

        private const val FIELD_NAME = "card.billing_address.country"
    }
}