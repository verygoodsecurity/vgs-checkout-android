package com.verygoodsecurity.vgscheckout.config.networking

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutRequestOptions
import kotlinx.parcelize.Parcelize

/**
 * Custom checkout route configuration.
 *
 * @param path inbound rout path for your organization vault.
 * @param hostnamePolicy type of base url to send data.
 * @param requestOptions like http method, headers etc.
 */
@Parcelize
class VGSCheckoutRouteConfig internal constructor(
    val path: String,
    val hostnamePolicy: VGSCheckoutHostnamePolicy,
    val requestOptions: VGSCheckoutRequestOptions
) : Parcelable