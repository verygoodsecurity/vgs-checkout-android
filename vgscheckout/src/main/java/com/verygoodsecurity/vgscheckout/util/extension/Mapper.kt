package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHTTPMethod
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.model.VGSCollectFieldNameMappingPolicy

internal fun VGSCheckoutHTTPMethod.toCollectHTTPMethod() = when (this) {
    VGSCheckoutHTTPMethod.POST -> HTTPMethod.POST
    VGSCheckoutHTTPMethod.DELETE -> HTTPMethod.DELETE
    VGSCheckoutHTTPMethod.GET -> HTTPMethod.GET
    VGSCheckoutHTTPMethod.PATCH -> HTTPMethod.PATCH
    VGSCheckoutHTTPMethod.PUT -> HTTPMethod.PUT
}

internal fun VGSCheckoutDataMergePolicy.toCollectMergePolicy() = when (this) {
    VGSCheckoutDataMergePolicy.FLAT_JSON -> VGSCollectFieldNameMappingPolicy.FLAT_JSON
    VGSCheckoutDataMergePolicy.NESTED_JSON -> VGSCollectFieldNameMappingPolicy.NESTED_JSON
    VGSCheckoutDataMergePolicy.NESTED_JSON_WITH_ARRAY_OVERWRITE -> VGSCollectFieldNameMappingPolicy.NESTED_JSON_WITH_ARRAYS_OVERWRITE
    VGSCheckoutDataMergePolicy.NESTED_JSON_WITH_ARRAY_MERGE -> VGSCollectFieldNameMappingPolicy.NESTED_JSON_WITH_ARRAYS_MERGE
}