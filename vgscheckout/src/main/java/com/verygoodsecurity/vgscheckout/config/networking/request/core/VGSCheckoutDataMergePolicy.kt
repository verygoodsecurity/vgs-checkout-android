package com.verygoodsecurity.vgscheckout.config.networking.request.core

/**
 * Defines fieldName mapping policy.
 */
enum class VGSCheckoutDataMergePolicy {

    /**
     * Map fieldName to JSON. A value uses in JSON without any changes before submitting.
     * Completely overwrite extra data.
     */
    FLAT_JSON,

    /**
     * Map fieldName to JSON. Deep nested key format is supported. Completely overwrite extra data.
     * Arrays are not supported.
     * VGSCollect supports this format by default.
     *
     * When you need to send data in a specific JSON structure you may do it by adding . notation to fieldName-string.
     * Each . in a fieldName represents a new level of nesting.
     * New field name string could be set into app:fieldName or setFieldName method.
     */
    NESTED_JSON,

    /**
     * Map fieldName to JSON with arrays if index is specified. Also merge extra data array with
     * collect array data at the same nested level if possible.
     */
    NESTED_JSON_WITH_ARRAY_OVERWRITE,

    /**
     * Map fieldName to JSON with arrays if index is specified. Completely overwrite extra data
     * array with collect array data.
     */
    NESTED_JSON_WITH_ARRAY_MERGE,
}