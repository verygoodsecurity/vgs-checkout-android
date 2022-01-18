package com.verygoodsecurity.vgscheckout.model

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import kotlinx.parcelize.Parcelize

/**
 * The result of an attempt to submit checkout form.
 */
sealed class VGSCheckoutResult : Parcelable {

    /**
     * Checkout was successfully completed.
     *
     * @param data wrapper object that helps to retrieve checkout result from bundle.
     */
    @Parcelize
    data class Success constructor(val data: VGSCheckoutResultBundle) : VGSCheckoutResult()

    /**
     * Checkout was failed due network errors or invalid setup.
     *
     * @param data wrapper object that helps to retrieve checkout result from bundle.
     */
    @Parcelize
    data class Failed constructor(
        val data: VGSCheckoutResultBundle,
        val exception: VGSCheckoutException? = null,
    ) : VGSCheckoutResult()

    /**
     * Checkout cancelled by user.
     */
    @Parcelize
    object Canceled : VGSCheckoutResult()
}