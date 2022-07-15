package com.verygoodsecurity.vgscheckout.config.ui.view.address

import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalCodeOptions
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
 * @param postalCodeOptions postal code input field UI options.
 * @param visibility defines if address section UI should be visible to user.
 */
@Parcelize
class VGSCheckoutBillingAddressOptions @JvmOverloads constructor(
    override val countryOptions: VGSCheckoutCountryOptions = VGSCheckoutCountryOptions(),
    override val cityOptions: VGSCheckoutCityOptions = VGSCheckoutCityOptions(),
    override val addressOptions: VGSCheckoutAddressOptions = VGSCheckoutAddressOptions(),
    override val optionalAddressOptions: VGSCheckoutOptionalAddressOptions = VGSCheckoutOptionalAddressOptions(),
    override val postalCodeOptions: VGSCheckoutPostalCodeOptions = VGSCheckoutPostalCodeOptions(),
    override val visibility: VGSCheckoutBillingAddressVisibility = VGSCheckoutBillingAddressVisibility.HIDDEN
) : CheckoutBillingAddressOptions()