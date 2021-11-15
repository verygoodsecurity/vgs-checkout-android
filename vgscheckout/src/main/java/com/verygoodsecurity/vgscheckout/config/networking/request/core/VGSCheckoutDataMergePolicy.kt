package com.verygoodsecurity.vgscheckout.config.networking.request.core

/**
 * Defines fieldName mapping policy.
 */
enum class VGSCheckoutDataMergePolicy {

    /**
     * Map fieldName to JSON. A value uses in JSON without any changes before submitting.
     * Completely overwrite extra data.
     * VGS Checkout supports this format by default.
     */
    FLAT_JSON,

    /**
     * Map fieldName to JSON. Deep nested key format is supported. Completely overwrite extra data.
     * Arrays are not supported.
     *
     * When you need to send data in a specific JSON structure you may do it by adding . notation to fieldName-string.
     * Each . in a fieldName represents a new level of nesting.
     */
    NESTED_JSON,

    /**
     * Map fieldName to JSON with arrays if index is specified. Also merge extra data array with
     * checkout array data at the same nested level if possible.
     */
    NESTED_JSON_WITH_ARRAYS_OVERWRITE,

    /**
     * Map fieldName to JSON with arrays if index is specified. Completely overwrite extra data
     * array with checkout array data.
     */
    NESTED_JSON_WITH_ARRAYS_MERGE,
}