package com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Card security code input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param isIconHidden defines if card brand icon should be hidden.
 */
@Parcelize
class VGSCheckoutCVCOptions constructor(
    override val fieldName: String = "",
    override val isIconHidden: Boolean = false,
) : CVCOptions() {

    /**
     *  Defines if input field should be visible to user.
     */
    @IgnoredOnParcel
    override val visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
}