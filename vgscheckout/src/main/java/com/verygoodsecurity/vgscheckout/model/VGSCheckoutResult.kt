package com.verygoodsecurity.vgscheckout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class VGSCheckoutResult : Parcelable {

    @Parcelize
    data class Success constructor(val code: Int?, val body: String?) : VGSCheckoutResult() {
        override fun toString(): String {
            return "${this.javaClass.simpleName}\ncode: $code \nbody:$body"
        }
    }

    @Parcelize
    data class Failed constructor(val code: Int?, val body: String?) : VGSCheckoutResult() {
        override fun toString(): String {
            return "${this.javaClass.simpleName}\ncode: $code \nbody:$body"
        }
    }

    @Parcelize
    object Canceled : VGSCheckoutResult() {
        override fun toString(): String {
            return this.javaClass.simpleName
        }
    }
}