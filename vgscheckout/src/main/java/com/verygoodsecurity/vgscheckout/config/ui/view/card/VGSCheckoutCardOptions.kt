package com.verygoodsecurity.vgscheckout.config.ui.view.card

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCardOptions @JvmOverloads constructor(
    val cardNumberOptions: VGSCheckoutCardNumberOptions = VGSCheckoutCardNumberOptions(),
    val cardHolderOptions: VGSCheckoutCardHolderOptions = VGSCheckoutCardHolderOptions(),
    val cvcOptions: VGSCheckoutCVCOptions = VGSCheckoutCVCOptions(),
    val expirationDateOptions: VGSCheckoutExpirationDateOptions = VGSCheckoutExpirationDateOptions()
) : Parcelable