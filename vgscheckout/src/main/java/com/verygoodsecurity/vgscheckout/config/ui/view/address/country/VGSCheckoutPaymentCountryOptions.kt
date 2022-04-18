package com.verygoodsecurity.vgscheckout.config.ui.view.address.country

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import kotlinx.parcelize.Parcelize

/**
 * Country input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param validCountries list of countries in ISO 3166-2 format that will be show in selection dialog.
 * @param visibility defines if input field should be visible to user.
 */
@Parcelize
class VGSCheckoutPaymentCountryOptions private constructor(
    override val fieldName: String,
    override val validCountries: List<String>,
    override val visibility: VGSCheckoutFieldVisibility
) : CountryOptions() {

    /**
     * Public constructor
     *
     * @param validCountries list of countries in ISO 3166-2 format that will be show in selection dialog.
     * @param visibility defines if input field should be visible to user.
     */
    @JvmOverloads
    constructor(
        validCountries: List<String> = emptyList(),
        visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
    ) : this(FIELD_NAME, validCountries, visibility)

    private companion object {

        private const val FIELD_NAME = "card.billing_address.country"
    }
}