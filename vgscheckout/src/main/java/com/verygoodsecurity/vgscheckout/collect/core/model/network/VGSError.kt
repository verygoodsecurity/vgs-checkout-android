package com.verygoodsecurity.vgscheckout.collect.core.model.network

internal enum class VGSError(val code: Int, val message: String) {

    URL_NOT_VALID(
        1480,
        "URL is not valid."
    ),
    NO_INTERNET_PERMISSIONS(
        1481,
        "Permission denied (missing INTERNET permission?)"
    ),
    NO_NETWORK_CONNECTIONS(
        1482,
        "No internet connection."
    ),
    TIME_OUT(
        1483,
        "Network request timeout error."
    ),
    INPUT_DATA_NOT_VALID(
        1001,
        "Field %s is not a valid."
    ),
    FIELD_NAME_NOT_SET(
        1004,
        "Field name is not set."
    ),
    FILE_NOT_FOUND(
        1101,
        "File not found."
    ),
    FILE_SIZE_OVER_LIMIT(
        1103,
        "File size is over limit."
    ),
    NOT_ACTIVITY_CONTEXT(
        1105,
        "Context is not Activity context."
    )
}

internal fun VGSError.toVGSResponse(param: String? = null): VGSResponse.ErrorResponse {
    val message = if (param.isNullOrEmpty()) this.message else String.format(this.message, param)
    return VGSResponse.ErrorResponse(message, this.code)
}