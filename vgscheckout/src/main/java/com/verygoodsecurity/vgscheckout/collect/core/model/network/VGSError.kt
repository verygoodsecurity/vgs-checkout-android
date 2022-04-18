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
    FIELD_NAME_NOT_SET(
        1004,
        "Field name is not set."
    )
}