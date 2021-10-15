package com.verygoodsecurity.vgscheckout.config.ui.view.address.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions

/**
 * Base class of address section UI options.
 */
abstract class CheckoutBillingAddressOptions : Parcelable {

    /**
     * Holds country input field UI options.
     */
    abstract val countryOptions: VGSCheckoutCountryOptions

    /**
     * Holds city input field UI options.
     */
    abstract val cityOptions: VGSCheckoutCityOptions

    /**
     * Holds address input field UI options.
     */
    abstract val addressOptions: VGSCheckoutAddressOptions

    /**
     * Holds optional address input field UI options.
     */
    abstract val optionalAddressOptions: VGSCheckoutOptionalAddressOptions

    /**
     * Holds postal address input field UI options.
     */
    abstract val postalAddressOptions: VGSCheckoutPostalAddressOptions

    /**
     * Address block visibility.
     */
    abstract val visibility: VGSCheckoutBillingAddressVisibility
}