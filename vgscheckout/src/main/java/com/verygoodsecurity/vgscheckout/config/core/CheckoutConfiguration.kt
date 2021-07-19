package com.verygoodsecurity.vgscheckout.config.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.VGSCheckoutRouteConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration

internal const val DEFAULT_ENVIRONMENT = "sandbox"

abstract class CheckoutConfiguration internal constructor() : Parcelable {

    abstract val vaultID: String

    abstract val environment: String

    abstract val routeConfig: VGSCheckoutRouteConfiguration // Can be more generic type in future

    abstract val formConfig: CheckoutFormConfiguration
}