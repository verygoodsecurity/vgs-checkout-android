package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber

import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Custom flow card number input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param isIconHidden defines if card brand icon should be hidden.
 * @param cardBrands list of brands that can be detected.
 */
@Parcelize
class VGSCheckoutCardNumberOptions internal constructor(
    override val fieldName: String,
    val isIconHidden: Boolean,
    internal val cardBrands: Set<VGSCheckoutCardBrand>,
) : ViewOptions() {

    /**
     *  Defines if input field should be visible to user.
     */
    @IgnoredOnParcel
    override val visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE

    /**
     * Public constructor. Allow to specify set card brands mode.
     *
     * @param fieldName text to be used for data transfer to VGS proxy.
     * @param isIconHidden defines if card brand icon should be hidden.
     */
    internal constructor(
        fieldName: String,
        isIconHidden: Boolean,
    ) : this(
        fieldName,
        isIconHidden,
        VGSCheckoutCardBrand.BRANDS
    )
}