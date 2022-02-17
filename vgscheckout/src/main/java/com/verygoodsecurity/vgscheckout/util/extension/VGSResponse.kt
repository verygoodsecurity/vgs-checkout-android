package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse

internal fun VGSResponse.isNetworkConnectionError() =
    (this as? VGSResponse.ErrorResponse)?.code == VGSError.NO_NETWORK_CONNECTIONS.code