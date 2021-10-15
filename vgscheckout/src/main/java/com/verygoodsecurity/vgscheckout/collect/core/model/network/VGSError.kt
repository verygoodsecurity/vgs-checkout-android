package com.verygoodsecurity.vgscheckout.collect.core.model.network

import android.content.Context
import com.verygoodsecurity.vgscheckout.R

internal enum class VGSError(val code:Int, val messageResId:Int) {
    URL_NOT_VALID(1480,
        R.string.vgs_checkout_error_url_validation
    ),
    NO_INTERNET_PERMISSIONS(1481,
        R.string.vgs_checkout_error_internet_permission
    ),
    NO_NETWORK_CONNECTIONS(1482,
        R.string.vgs_checkout_error_internet_connection
    ),
    TIME_OUT(1483,
        R.string.vgs_checkout_error_time_out
    ),
    INPUT_DATA_NOT_VALID(1001,
        R.string.vgs_checkout_error_field_validation
    ),
    FIELD_NAME_NOT_SET(1004,
        R.string.vgs_checkout_error_field_name_not_set
    ),
    FILE_NOT_FOUND(1101,
        R.string.vgs_checkout_error_file_not_fount
    ),
    FILE_NOT_SUPPORT(1102,
        R.string.vgs_checkout_error_file_not_support
    ),
    FILE_SIZE_OVER_LIMIT(1103,
        R.string.vgs_checkout_error_file_size_validation
    ),
    NOT_ACTIVITY_CONTEXT(1105,
        R.string.vgs_checkout_error_not_activity_context
    )
}

internal fun VGSError.toVGSResponse(
    context: Context,
    vararg params: String?
): VGSResponse.ErrorResponse {
    val message = if (params.isEmpty()) {
        context.getString(this.messageResId)
    } else {
        String.format(context.getString(this.messageResId), *params)
    }

    return VGSResponse.ErrorResponse(message, this.code)
}