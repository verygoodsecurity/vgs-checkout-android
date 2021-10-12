package com.verygoodsecurity.vgscheckout.config.networking.request.core

import android.os.Parcelable

/**
 * Base checkout request options.
 */
abstract class CheckoutRequestOptions internal constructor() : Parcelable {

    /**
     *  Define http method.
     */
    abstract val httpMethod: VGSCheckoutHTTPMethod

    /**
     * Request headers.
     */
    abstract val extraHeaders: Map<String, String>

    /**
     *  Extra request payload data.
     */
    abstract val extraData: Map<String, Any>

    /**
     * Define field name mapping policy and how fields data and extra data should be merged before send.
     *
     * @see [VGSCheckoutDataMergePolicy]
     */
    abstract val mergePolicy: VGSCheckoutDataMergePolicy
}