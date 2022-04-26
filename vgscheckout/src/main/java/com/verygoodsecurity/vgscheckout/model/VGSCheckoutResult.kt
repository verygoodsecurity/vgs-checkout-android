package com.verygoodsecurity.vgscheckout.model

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import kotlinx.parcelize.Parcelize

/**
 * The result of an attempt to submit checkout form.
 *
 * @property data wrapper object that helps to retrieve checkout result from bundle.
 */
sealed class VGSCheckoutResult : Parcelable {

    abstract val data: VGSCheckoutResultBundle

    /**
     * Checkout was successfully completed.
     *
     * @param data wrapper object that helps to retrieve checkout result from bundle.
     */
    @Parcelize
    data class Success(override val data: VGSCheckoutResultBundle) : VGSCheckoutResult()

    /**
     * Checkout was failed due network errors or invalid setup.
     *
     * @param data wrapper object that helps to retrieve checkout result from bundle.
     * @param exception local or setup error that interrupt checkout.
     */
    @Parcelize
    data class Failed constructor(
        override val data: VGSCheckoutResultBundle,
        val exception: VGSCheckoutException? = null,
    ) : VGSCheckoutResult()

    /**
     * Checkout cancelled by user.
     *
     * @param data wrapper object that helps to retrieve checkout result from bundle.
     */
    @Parcelize
    data class Canceled(override val data: VGSCheckoutResultBundle) : VGSCheckoutResult()
}