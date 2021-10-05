package com.verygoodsecurity.vgscheckout.config.networking

import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutCustomRequestOptions
import com.verygoodsecurity.vgscheckout.config.networking.core.CheckoutRouteConfig
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutCustomRouteConfig @JvmOverloads constructor(
    override val path: String = "",
    override val hostnamePolicy: VGSCheckoutHostnamePolicy = VGSCheckoutHostnamePolicy.Vault,
    override val requestOptions: VGSCheckoutCustomRequestOptions = VGSCheckoutCustomRequestOptions()
) : CheckoutRouteConfig()