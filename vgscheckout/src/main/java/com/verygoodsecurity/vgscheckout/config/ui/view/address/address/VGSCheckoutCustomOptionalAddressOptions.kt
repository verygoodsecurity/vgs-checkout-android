package com.verygoodsecurity.vgscheckout.config.ui.view.address.address

import kotlinx.parcelize.Parcelize

/**
 * Optional address input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 */
@Parcelize
class VGSCheckoutCustomOptionalAddressOptions @JvmOverloads constructor(
    override val fieldName: String = "",
) : OptionalAddressOptions()