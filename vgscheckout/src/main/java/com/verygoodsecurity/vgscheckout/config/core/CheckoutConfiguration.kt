package com.verygoodsecurity.vgscheckout.config.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration

internal const val DEFAULT_ENVIRONMENT = "sandbox"

abstract class CheckoutConfiguration internal constructor() : Parcelable {

    internal abstract val vaultID: String

    internal abstract val environment: String

    internal abstract val routeConfig: VGSCheckoutRouteConfiguration // Can be more generic type in future

    internal abstract val formConfig: VGSCheckoutFormConfiguration // Can be more generic type in future
}