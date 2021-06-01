package com.verygoodsecurity.vgscheckout.config.networking.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.request.core.RequestConfig

abstract class RouteConfig<R : RequestConfig> internal constructor(): Parcelable {

    abstract val environment: String

    abstract val requestConfig: R
}