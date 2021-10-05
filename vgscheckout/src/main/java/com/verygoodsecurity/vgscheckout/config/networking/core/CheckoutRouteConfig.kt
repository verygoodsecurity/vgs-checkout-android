package com.verygoodsecurity.vgscheckout.config.networking.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.request.core.CheckoutRequestOptions

abstract class CheckoutRouteConfig : Parcelable {

    abstract val path: String

    abstract val hostnamePolicy: VGSCheckoutHostnamePolicy

    abstract val requestOptions: CheckoutRequestOptions
}