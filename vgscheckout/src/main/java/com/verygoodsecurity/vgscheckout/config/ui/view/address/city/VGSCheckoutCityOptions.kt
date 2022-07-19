package com.verygoodsecurity.vgscheckout.config.ui.view.address.city

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

/**
 * City input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param visibility defines if input field should be visible to user.
 */
@Parcelize
class VGSCheckoutCityOptions internal constructor(
    override val fieldName: String,
    override val visibility: VGSCheckoutFieldVisibility
) : ViewOptions()