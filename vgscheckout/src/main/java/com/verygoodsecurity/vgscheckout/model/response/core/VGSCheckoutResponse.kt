package com.verygoodsecurity.vgscheckout.model.response.core

import android.os.Parcelable

abstract class VGSCheckoutResponse : Parcelable {

    abstract val isSuccessful: Boolean

    abstract val code: Int

    abstract val body: String?

    abstract val message: String?
}