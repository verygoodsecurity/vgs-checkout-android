package com.verygoodsecurity.vgscheckout.config.ui.view.address.code

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.Parcelize

/**
 * Postal address input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 */
@Parcelize
class VGSCheckoutPostalAddressOptions @JvmOverloads constructor(
    override val fieldName: String = ""
) : ViewOptions()