package com.verygoodsecurity.vgscheckout.config.ui.view.address

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions
import kotlinx.parcelize.Parcelize

/**
 * Custom flow checkout form address section UI options.
 *
 * @param countryOptions country input field UI options.
 * @param cityOptions city input field UI options.
 * @param addressOptions address input field UI options.
 * @param optionalAddressOptions optional address input field UI options.
 * @param postalCodeOptions postal code input field UI options.
 * @param visibility defines if address section UI should be visible to user.
 */
@Parcelize
class VGSCheckoutBillingAddressOptions internal constructor(
    val countryOptions: VGSCheckoutCountryOptions,
    val cityOptions: VGSCheckoutCityOptions,
    val addressOptions: VGSCheckoutAddressOptions,
    val optionalAddressOptions: VGSCheckoutOptionalAddressOptions,
    val postalCodeOptions: VGSCheckoutPostalCodeOptions,
    val visibility: VGSCheckoutBillingAddressVisibility
) : Parcelable