package com.verygoodsecurity.vgscheckout.config.ui.core

import android.os.Parcelable

abstract class CheckoutFormConfiguration internal constructor() : Parcelable {

    abstract val payButtonTitle: String?
}