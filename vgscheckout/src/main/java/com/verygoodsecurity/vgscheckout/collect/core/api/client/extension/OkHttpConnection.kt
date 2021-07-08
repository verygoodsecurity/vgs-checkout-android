package com.verygoodsecurity.vgscheckout.collect.core.api.client.extension

import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import okhttp3.MediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST

internal fun String?.toRequestBodyOrNull(mediaType: MediaType?, method: HTTPMethod) =
    when (method) {
        HTTPMethod.GET -> null
        else -> this?.toRequestBody(mediaType) ?: EMPTY_REQUEST
    }
