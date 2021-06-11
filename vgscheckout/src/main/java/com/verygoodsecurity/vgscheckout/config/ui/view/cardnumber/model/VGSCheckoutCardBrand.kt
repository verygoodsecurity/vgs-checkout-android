package com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * The data class definition for represent custom card brand.
 * It may be useful to add new brands in addition to already defined brands or override existing ones.
 *
 * @constructor  Primary constructor. It's internal and allow to set [VGSCheckoutCardType] only from inside library.
 * @param regex - The regex rules for detection card brand.
 * @param cardBrandName - The name of current card brand. This name may be visible for users.
 * @param drawableResId - The drawable resource represents credit card logo.
 * @param params - The set of parameters for card brand creation.
 */
@Parcelize
data class VGSCheckoutCardBrand internal constructor(
    val regex: String,
    val cardBrandName: String,
    val drawableResId: Int = 0,
    val params: VGSCheckoutBrandParams = VGSCheckoutBrandParams(),
    internal val cardType: VGSCheckoutCardType = VGSCheckoutCardType.UNKNOWN
) : Parcelable {

    constructor(
        regex: String,
        cardBrandName: String,
        drawableResId: Int = 0,
        params: VGSCheckoutBrandParams = VGSCheckoutBrandParams()
    ) : this(regex, cardBrandName, drawableResId, params, VGSCheckoutCardType.UNKNOWN)
}