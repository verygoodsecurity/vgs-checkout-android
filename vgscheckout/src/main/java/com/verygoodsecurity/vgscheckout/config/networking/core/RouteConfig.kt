package com.verygoodsecurity.vgscheckout.config.networking.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.networking.request.core.RequestConfig
import com.verygoodsecurity.vgscollect.core.Environment

abstract class RouteConfig<R : RequestConfig> internal constructor(): Parcelable {

    abstract val environment: Environment

    abstract val requestConfig: R
}