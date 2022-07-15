package com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration

import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Expiration date input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param dateSeparateSerializer split date into separate JSON values before send to proxy.
 * @param inputFormatRegex ISO 8601 date input format.
 * @param outputFormatRegex ISO 8601 format in which date will be sent to proxy.
 */
@Parcelize
class VGSCheckoutExpirationDateOptions constructor(
    override val fieldName: String,
    val dateSeparateSerializer: VGSDateSeparateSerializer?,
    val inputFormatRegex: String,
    val outputFormatRegex: String
) : ViewOptions() {

    /**
     *  Defines if input field should be visible to user.
     */
    @IgnoredOnParcel
    override val visibility: VGSCheckoutFieldVisibility = VGSCheckoutFieldVisibility.VISIBLE
}