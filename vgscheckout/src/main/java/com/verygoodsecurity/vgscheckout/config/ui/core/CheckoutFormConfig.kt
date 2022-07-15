package com.verygoodsecurity.vgscheckout.config.ui.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.address.core.CheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions

/**
 * Base class of form configuration.
 *
 * Form configuration object is responsible for holding UI config and fields names.
 * @see [com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions]
 */
abstract class CheckoutFormConfig internal constructor() : Parcelable {

    /**
     * Options of card details block.
     */
    abstract val cardOptions: VGSCheckoutCardOptions

    /**
     * Options of address details block.
     */
    abstract val addressOptions: CheckoutBillingAddressOptions

    /**
     * Defines validation behavior.
     */
    abstract val validationBehaviour: VGSCheckoutFormValidationBehaviour

    /**
     * Defines if save card checkbox should be visible.
     */
    internal abstract val saveCardOptionEnabled: Boolean
}