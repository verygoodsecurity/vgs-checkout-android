package com.verygoodsecurity.vgscheckout.config.ui.view.address.code

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import kotlinx.parcelize.Parcelize

/**
 * Postal code input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param visibility defines if input field should be visible to user.
 */
@Parcelize
class VGSCheckoutCustomPostalCodeOptions @JvmOverloads constructor(
    override val fieldName: String = "",
    override val visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
) : PostalCodeOptions()