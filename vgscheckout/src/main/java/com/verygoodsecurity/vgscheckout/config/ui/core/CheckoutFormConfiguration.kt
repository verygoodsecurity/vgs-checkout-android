package com.verygoodsecurity.vgscheckout.config.ui.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions

abstract class CheckoutFormConfiguration internal constructor() : Parcelable {

    abstract val cardOptions: VGSCheckoutCardOptions
    abstract val addressOptions: VGSCheckoutAddressOptions
    abstract val payButtonTitle: String?
}