package com.verygoodsecurity.vgscheckout.config.networking

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutRequestOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutRouteConfiguration @JvmOverloads constructor(
    val path: String = "",
    val hostnamePolicy: VGSCheckoutHostnamePolicy = VGSCheckoutHostnamePolicy.Vault,
    val requestOptions: VGSCheckoutRequestOptions = VGSCheckoutRequestOptions()
) : Parcelable