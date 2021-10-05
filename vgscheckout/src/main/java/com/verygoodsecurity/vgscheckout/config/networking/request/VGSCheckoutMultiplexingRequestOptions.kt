package com.verygoodsecurity.vgscheckout.config.networking.request

import com.verygoodsecurity.vgscheckout.config.networking.request.core.CheckoutRequestOptions
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHTTPMethod
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class VGSCheckoutMultiplexingRequestOptions private constructor(
    override val httpMethod: VGSCheckoutHTTPMethod,
    override val extraHeaders: Map<String, String>,
    override val extraData: Map<String, @RawValue Any>,
    override val mergePolicy: VGSCheckoutDataMergePolicy
) : CheckoutRequestOptions() {

    internal constructor(token: String) : this(
        VGSCheckoutHTTPMethod.POST,
        mapOf(
            CONTENT_TYPE_HEADER_NAME to CONTENT_TYPE,
            AUTHORIZATION_HEADER_NAME to "$BEARER_TOKEN_TYPE $token"
        ),
        emptyMap(),
        VGSCheckoutDataMergePolicy.NESTED_JSON
    )

    private companion object {

        private const val CONTENT_TYPE_HEADER_NAME = "Content-Type"
        private const val CONTENT_TYPE = "application/json"

        private const val AUTHORIZATION_HEADER_NAME = "Authorization"
        private const val BEARER_TOKEN_TYPE = "Bearer"
    }
}