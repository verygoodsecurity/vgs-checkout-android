package com.verygoodsecurity.vgscheckout.config.networking.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.request.core.CheckoutRequestOptions

/**
 * Base networking configuration.
 */
abstract class CheckoutRouteConfig internal constructor() : Parcelable {

    /**
     * Inbound rout path for your organization vault.
     */
    abstract val path: String

    /**
     * Define base url to send data.
     *
     * @see [VGSCheckoutHostnamePolicy]
     */
    abstract val hostnamePolicy: VGSCheckoutHostnamePolicy

    /**
     * Define request options like http method, headers etc.
     *
     * @see [CheckoutRequestOptions]
     */
    abstract val requestOptions: CheckoutRequestOptions
}