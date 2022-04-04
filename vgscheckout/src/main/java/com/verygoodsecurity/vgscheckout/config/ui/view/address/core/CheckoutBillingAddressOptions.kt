package com.verygoodsecurity.vgscheckout.config.ui.view.address.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.AddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.OptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.CityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.PostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.CountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility

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
     * Holds postal code input field UI options.
     */
    abstract val postalCodeOptions: PostalCodeOptions

    /**
     * Address block visibility.
     */
    abstract val visibility: VGSCheckoutBillingAddressVisibility

    fun isVisible(): Boolean {
        return visibility == VGSCheckoutBillingAddressVisibility.VISIBLE &&
                (countryOptions.visibility == VGSCheckoutFieldVisibility.VISIBLE ||
                        cityOptions.visibility == VGSCheckoutFieldVisibility.VISIBLE ||
                        addressOptions.visibility == VGSCheckoutFieldVisibility.VISIBLE ||
                        optionalAddressOptions.visibility == VGSCheckoutFieldVisibility.VISIBLE ||
                        postalCodeOptions.visibility == VGSCheckoutFieldVisibility.VISIBLE)
    }
}