package com.verygoodsecurity.vgscheckout.config.networking

import com.verygoodsecurity.vgscheckout.config.networking.core.CheckoutRouteConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.VGSCheckoutPaymentRequestOptions
import kotlinx.parcelize.Parcelize

/**
 * Payment orchestration checkout route configuration. Only for internal use,
 * as Payment orchestration request have fixed structure.
 */
@Parcelize
class VGSCheckoutPaymentRouteConfig private constructor(
    override val path: String,
    override val hostnamePolicy: VGSCheckoutHostnamePolicy,
    override val requestOptions: VGSCheckoutPaymentRequestOptions
) : CheckoutRouteConfig() {

    internal constructor(accessToken: String) : this(
        PATH,
        VGSCheckoutHostnamePolicy.Vault,
        VGSCheckoutPaymentRequestOptions(accessToken)
    )

    internal companion object {

        internal const val PATH = "/financial_instruments"
    }
}