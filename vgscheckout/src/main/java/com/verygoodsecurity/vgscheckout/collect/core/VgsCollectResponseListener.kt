package com.verygoodsecurity.vgscheckout.collect.core

import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse

/**
 * Interface definition for a callback to be invoked when VGSCollect receive response from Server.
 *
 * @since 1.0.0
 */
internal interface VgsCollectResponseListener {

    /**
     * Called when some error is detected or received a response from Server.
     *
     * @param response The common [Response] class.
     */
    fun onResponse(response: VGSResponse?)
}