package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy

internal fun VGSCollect.Builder.applyHostnamePolicy(policy: VGSCheckoutHostnamePolicy): VGSCollect.Builder {
    if (policy is VGSCheckoutHostnamePolicy.CustomHostname) {
        setHostname(policy.hostname)
    } else if (policy is VGSCheckoutHostnamePolicy.Local) {
        setHostname(policy.localhost)
        setPort(policy.port)
    }
    return this
}