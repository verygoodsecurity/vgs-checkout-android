package com.verygoodsecurity.vgscheckout.config.ui.view.address.core

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions

abstract class CheckoutBillingAddressOptions : Parcelable {

    abstract val countryOptions: VGSCheckoutCountryOptions

    abstract val cityOptions: VGSCheckoutCityOptions

    abstract val addressOptions: VGSCheckoutAddressOptions

    abstract val optionalAddressOptions: VGSCheckoutOptionalAddressOptions

    abstract val postalAddressOptions: VGSCheckoutPostalAddressOptions

    abstract val visibility: VGSCheckoutBillingAddressVisibility
}