package com.verygoodsecurity.vgscheckout.config.ui.view.address.code

import kotlinx.parcelize.Parcelize

/**
 * Postal code input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 */
@Parcelize
class VGSCheckoutCustomPostalCodeOptions @JvmOverloads constructor(
    override val fieldName: String = "",
) : PostalCodeOptions()