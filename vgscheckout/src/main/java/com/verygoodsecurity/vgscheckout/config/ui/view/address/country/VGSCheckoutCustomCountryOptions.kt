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
class VGSCheckoutCustomCountryOptions @JvmOverloads constructor(
    override val fieldName: String = "",
    override val validCountries: List<String> = emptyList(),
    override val visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
) : CountryOptions()