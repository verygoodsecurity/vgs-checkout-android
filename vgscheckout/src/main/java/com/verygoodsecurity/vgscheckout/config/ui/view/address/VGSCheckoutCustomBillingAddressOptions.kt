package com.verygoodsecurity.vgscheckout.config.ui.view.address

import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.core.CheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions
import kotlinx.parcelize.Parcelize

/**
 * Custom flow checkout form address section UI options.
 *
 * @param countryOptions country input field UI options.
 * @param cityOptions city input field UI options.
 * @param addressOptions address input field UI options.
 * @param optionalAddressOptions optional address input field UI options.
 * @param postalAddressOptions postal address input field UI options.
 * @param visibility defines if address section UI should be visible to user.
 */
@Parcelize
class VGSCheckoutCustomBillingAddressOptions @JvmOverloads constructor(
    override val countryOptions: VGSCheckoutCountryOptions = VGSCheckoutCountryOptions(),
    override val cityOptions: VGSCheckoutCityOptions = VGSCheckoutCityOptions(),
    override val addressOptions: VGSCheckoutAddressOptions = VGSCheckoutAddressOptions(),
    override val optionalAddressOptions: VGSCheckoutOptionalAddressOptions = VGSCheckoutOptionalAddressOptions(),
    override val postalAddressOptions: VGSCheckoutPostalAddressOptions = VGSCheckoutPostalAddressOptions(),
    override val visibility: VGSCheckoutBillingAddressVisibility = VGSCheckoutBillingAddressVisibility.VISIBLE
) : CheckoutBillingAddressOptions()