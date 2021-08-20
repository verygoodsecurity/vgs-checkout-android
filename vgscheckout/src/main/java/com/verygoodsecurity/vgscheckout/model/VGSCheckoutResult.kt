package com.verygoodsecurity.vgscheckout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class VGSCheckoutResult : Parcelable {

    @Parcelize
    data class Success constructor(val body: String?) : VGSCheckoutResult()

    @Parcelize
    data class Failed constructor(val code: Int?, val body: String?) : VGSCheckoutResult()

    @Parcelize
    object Canceled : VGSCheckoutResult()
}