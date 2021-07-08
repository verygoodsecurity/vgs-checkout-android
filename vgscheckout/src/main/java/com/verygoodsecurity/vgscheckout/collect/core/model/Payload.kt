package com.verygoodsecurity.vgscheckout.collect.core.model

import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod

/** @suppress */
internal data class Payload(val path: String,
                   val method: HTTPMethod,
                   val headers: Map<String, String>?,
                   val data: Map<String, Any>?
)