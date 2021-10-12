package com.verygoodsecurity.vgscheckout.config.networking

import com.verygoodsecurity.vgscheckout.config.networking.core.CheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutMultiplexingRequestOptions
import kotlinx.parcelize.Parcelize

/**
 * Multiplexing checkout request options. Only for internal use, as multiplexing request have
 * fixed structure.
 */
@Parcelize
class VGSCheckoutMultiplexingRouteConfig private constructor(
    override val path: String,
    override val hostnamePolicy: VGSCheckoutHostnamePolicy,
    override val requestOptions: VGSCheckoutMultiplexingRequestOptions
) : CheckoutRouteConfig() {

    internal constructor(token: String) : this(
        PATH,
        VGSCheckoutHostnamePolicy.Vault,
        VGSCheckoutMultiplexingRequestOptions(token)
    )

    private companion object {

        private const val PATH = "/financial_instruments"
    }
}