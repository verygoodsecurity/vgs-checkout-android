package com.verygoodsecurity.vgscheckout.config.ui.view.address.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions

/**
 * Base class of address section UI options.
 */
abstract class CheckoutBillingAddressOptions : Parcelable {

    /**
     * Holds country input field UI options.
     */
    abstract val countryOptions: ViewOptions

    /**
     * Holds city input field UI options.
     */
    abstract val cityOptions: ViewOptions

    /**
     * Holds address input field UI options.
     */
    abstract val addressOptions: ViewOptions

    /**
     * Holds optional address input field UI options.
     */
    abstract val optionalAddressOptions: ViewOptions

    /**
     * Holds postal address input field UI options.
     */
    abstract val postalAddressOptions: ViewOptions

    /**
     * Address block visibility.
     */
    abstract val visibility: VGSCheckoutBillingAddressVisibility
}