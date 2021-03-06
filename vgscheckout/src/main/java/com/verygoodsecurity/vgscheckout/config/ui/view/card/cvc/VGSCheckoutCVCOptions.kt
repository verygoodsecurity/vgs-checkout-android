package com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Card security code input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param isIconHidden defines if card brand icon should be hidden.
 */
@Parcelize
class VGSCheckoutCVCOptions internal constructor(
    override val fieldName: String,
    val isIconHidden: Boolean,
) : ViewOptions() {

    /**
     *  Defines if input field should be visible to user.
     */
    @IgnoredOnParcel
    override val visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
}