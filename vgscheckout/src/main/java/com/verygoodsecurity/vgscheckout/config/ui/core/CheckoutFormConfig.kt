package com.verygoodsecurity.vgscheckout.config.ui.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.address.core.CheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.core.CheckoutCardOptions

abstract class CheckoutFormConfig internal constructor() : Parcelable {

    abstract val cardOptions: CheckoutCardOptions

    abstract val addressOptions: CheckoutBillingAddressOptions
}