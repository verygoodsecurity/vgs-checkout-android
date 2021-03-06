package com.verygoodsecurity.vgscheckout.config.ui.view.address.country

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

/**
 * Country input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param validCountries list of countries in ISO 3166-2 format that will be show in selection dialog.
 * @param visibility defines if input field should be visible to user.
 */
@Parcelize
class VGSCheckoutCountryOptions internal constructor(
    override val fieldName: String,
    val validCountries: List<String>,
    override val visibility: VGSCheckoutFieldVisibility
) : ViewOptions()