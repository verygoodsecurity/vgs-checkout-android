package com.verygoodsecurity.vgscheckout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * The result of an attempt to submit checkout form.
 */
sealed class VGSCheckoutResult : Parcelable {

    /**
     * Checkout was successfully completed.
     *
     * @param code http response code.
     * @param body http response body.
     */
    @Parcelize
    data class Success constructor(val code: Int?, val body: String?) : VGSCheckoutResult() {

        override fun toString() = "${this.javaClass.simpleName}\ncode: $code \nbody:$body"
    }

    /**
     * Checkout was failed due network errors or invalid setup.
     *
     * @param code http response code or local error code [com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError].
     * @param body http response body or local error message [com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError].
     */
    @Parcelize
    data class Failed constructor(val code: Int?, val body: String?) : VGSCheckoutResult() {

        override fun toString() = "${this.javaClass.simpleName}\ncode: $code \nbody:$body"
    }

    /**
     * Checkout cancelled by user.
     */
    @Parcelize
    object Canceled : VGSCheckoutResult() {

        override fun toString(): String = this.javaClass.simpleName
    }
}