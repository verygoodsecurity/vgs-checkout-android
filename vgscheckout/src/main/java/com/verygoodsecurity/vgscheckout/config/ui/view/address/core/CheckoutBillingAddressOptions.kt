package com.verygoodsecurity.vgscheckout.config.ui.view.address.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.AddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.OptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.CityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.PostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.CountryOptions

/**
 * Base class of address section UI options.
 */
abstract class CheckoutBillingAddressOptions : Parcelable {

    /**
     * Holds country input field UI options.
     */
    abstract val countryOptions: CountryOptions

    /**
     * Holds city input field UI options.
     */
    abstract val cityOptions: CityOptions

    /**
     * Holds address input field UI options.
     */
    abstract val addressOptions: AddressOptions

    /**
     * Holds optional address input field UI options.
     */
    abstract val optionalAddressOptions: OptionalAddressOptions

    /**
     * Holds postal address input field UI options.
     */
    abstract val postalAddressOptions: PostalAddressOptions

    /**
     * Address block visibility.
     */
    abstract val visibility: VGSCheckoutBillingAddressVisibility
}