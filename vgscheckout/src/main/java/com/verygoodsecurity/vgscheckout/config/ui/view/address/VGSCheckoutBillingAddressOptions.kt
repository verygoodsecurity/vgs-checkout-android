package com.verygoodsecurity.vgscheckout.config.ui.view.address

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutBillingAddressOptions @JvmOverloads constructor(
    val countryOptions: VGSCheckoutCountryOptions = VGSCheckoutCountryOptions(),
    val cityOptions: VGSCheckoutCityOptions = VGSCheckoutCityOptions(),
    val addressOptions: VGSCheckoutAddressOptions = VGSCheckoutAddressOptions(),
    val optionalAddressOptions: VGSCheckoutOptionalAddressOptions = VGSCheckoutOptionalAddressOptions(),
    val postalAddressOptions: VGSCheckoutPostalAddressOptions = VGSCheckoutPostalAddressOptions(),
    val visibility: VGSCheckoutBillingAddressVisibility = VGSCheckoutBillingAddressVisibility.VISIBLE
) : Parcelable