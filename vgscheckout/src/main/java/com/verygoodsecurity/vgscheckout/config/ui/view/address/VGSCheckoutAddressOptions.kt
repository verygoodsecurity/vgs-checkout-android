package com.verygoodsecurity.vgscheckout.config.ui.view.address

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutCityAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutAddressOptions private constructor(
    val countryOptions: VGSCheckoutCountryOptions,
    val cityOptions: VGSCheckoutCityOptions,
    val addressOptions: VGSCheckoutCityAddressOptions,
    val postalAddressOptions: VGSCheckoutPostalAddressOptions,
    val visibility: VGSCheckoutFieldVisibility
) : Parcelable {

    class Builder {

        private var countryOptions = VGSCheckoutCountryOptions.Builder().build()
        private var cityOptions = VGSCheckoutCityOptions.Builder().build()
        private var addressOptions = VGSCheckoutCityAddressOptions.Builder().build()
        private var postalAddressOptions = VGSCheckoutPostalAddressOptions.Builder().build()
        private var visibility = VGSCheckoutFieldVisibility.VISIBLE

        fun setCountryOptions(options: VGSCheckoutCountryOptions) = this.apply {
            this.countryOptions = options
        }

        fun setCityOptions(options: VGSCheckoutCityOptions) = this.apply {
            this.cityOptions = options
        }

        fun setAddressOptions(options: VGSCheckoutCityAddressOptions) = this.apply {
            this.addressOptions = options
        }

        fun setPostalAddressOptions(options: VGSCheckoutPostalAddressOptions) = this.apply {
            this.postalAddressOptions = options
        }

        fun setAddressFormVisibility(visibility: VGSCheckoutFieldVisibility) = this.apply {
            this.visibility = visibility
        }

        fun build(): VGSCheckoutAddressOptions = VGSCheckoutAddressOptions(
            countryOptions,
            cityOptions,
            addressOptions,
            postalAddressOptions,
            visibility
        )
    }
}