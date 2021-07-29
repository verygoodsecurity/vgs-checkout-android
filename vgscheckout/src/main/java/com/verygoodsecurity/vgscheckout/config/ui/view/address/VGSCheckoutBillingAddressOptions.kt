package com.verygoodsecurity.vgscheckout.config.ui.view.address

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutBillingAddressOptions private constructor(
    val countryOptions: VGSCheckoutCountryOptions,
    val cityOptions: VGSCheckoutCityOptions,
    val addressOptions: VGSCheckoutAddressOptions,
    val optionalAddressOptions: VGSCheckoutOptionalAddressOptions,
    val postalAddressOptions: VGSCheckoutPostalAddressOptions,
    val visibility: VGSCheckoutBillingAddressVisibility
) : Parcelable {

    class Builder {

        private var countryOptions = VGSCheckoutCountryOptions.Builder().build()
        private var cityOptions = VGSCheckoutCityOptions.Builder().build()
        private var addressOptions = VGSCheckoutAddressOptions.Builder().build()
        private var optionalAddressOptions = VGSCheckoutOptionalAddressOptions.Builder().build()
        private var postalAddressOptions = VGSCheckoutPostalAddressOptions.Builder().build()
        private var visibility = VGSCheckoutBillingAddressVisibility.VISIBLE

        fun setCountryOptions(options: VGSCheckoutCountryOptions) = this.apply {
            this.countryOptions = options
        }

        fun setCityOptions(options: VGSCheckoutCityOptions) = this.apply {
            this.cityOptions = options
        }

        fun setAddressOptions(options: VGSCheckoutAddressOptions) = this.apply {
            this.addressOptions = options
        }

        fun setOptionalAddressOptions(options: VGSCheckoutOptionalAddressOptions) = this.apply {
            this.optionalAddressOptions = options
        }

        fun setPostalAddressOptions(options: VGSCheckoutPostalAddressOptions) = this.apply {
            this.postalAddressOptions = options
        }

        fun setAddressFormVisibility(visibility: VGSCheckoutBillingAddressVisibility) = this.apply {
            this.visibility = visibility
        }

        fun build(): VGSCheckoutBillingAddressOptions = VGSCheckoutBillingAddressOptions(
            countryOptions,
            cityOptions,
            addressOptions,
            optionalAddressOptions,
            postalAddressOptions,
            visibility
        )
    }
}