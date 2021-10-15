package com.verygoodsecurity.vgscheckout.config.networking.request

import com.verygoodsecurity.vgscheckout.config.networking.request.core.CheckoutRequestOptions
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHTTPMethod
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Custom checkout flow request option.
 *
 * @param httpMethod define http method.
 * @param extraHeaders request headers.
 * @param extraData extra request payload data.
 * @param mergePolicy define field name mapping policy and how fields data and extra data should be
 * merged before send.
 */
@Parcelize
class VGSCheckoutCustomRequestOptions @JvmOverloads constructor(
    override val httpMethod: VGSCheckoutHTTPMethod = VGSCheckoutHTTPMethod.POST,
    override val extraHeaders: Map<String, String> = emptyMap(),
    override val extraData: Map<String, @RawValue Any> = emptyMap(),
    override val mergePolicy: VGSCheckoutDataMergePolicy = VGSCheckoutDataMergePolicy.NESTED_JSON
) : CheckoutRequestOptions()