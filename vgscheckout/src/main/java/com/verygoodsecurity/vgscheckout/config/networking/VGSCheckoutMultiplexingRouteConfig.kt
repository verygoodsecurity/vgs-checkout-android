package com.verygoodsecurity.vgscheckout.config.networking

import com.verygoodsecurity.vgscheckout.config.networking.core.CheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutMultiplexingRequestOptions
import kotlinx.parcelize.Parcelize

/**
 * Multiplexing checkout route configuration. Only for internal use, as multiplexing request have
 * fixed structure.
 */
@Parcelize
class VGSCheckoutMultiplexingRouteConfig private constructor(
    override val path: String,
    override val hostnamePolicy: VGSCheckoutHostnamePolicy,
    override val requestOptions: VGSCheckoutMultiplexingRequestOptions
) : CheckoutRouteConfig() {

    internal constructor(accessToken: String) : this(
        PATH,
        VGSCheckoutHostnamePolicy.Vault,
        VGSCheckoutMultiplexingRequestOptions(accessToken)
    )

    private companion object {

        private const val PATH = "/financial_instruments"
    }
}