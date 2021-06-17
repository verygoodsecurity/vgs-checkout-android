package com.verygoodsecurity.vgscheckout.config.core

import android.os.Parcelable

internal const val DEFAULT_ENVIRONMENT = "sandbox"

abstract class CheckoutConfiguration internal constructor() : Parcelable {

    internal abstract val vaultID: String

    internal abstract val environment: String
}