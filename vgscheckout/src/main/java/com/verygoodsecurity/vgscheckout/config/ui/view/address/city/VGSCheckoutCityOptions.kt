package com.verygoodsecurity.vgscheckout.config.ui.view.address.city

import kotlinx.parcelize.Parcelize

/**
 * City input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 */
@Parcelize
class VGSCheckoutCityOptions @JvmOverloads constructor(
    override val fieldName: String = "",
) : CityOptions()