package com.verygoodsecurity.vgscheckout.config.ui.view.address

import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutCustomAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutCustomOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCustomCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutCustomPostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.core.CheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCustomCountryOptions
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
    override val countryOptions: VGSCheckoutCustomCountryOptions = VGSCheckoutCustomCountryOptions(),
    override val cityOptions: VGSCheckoutCustomCityOptions = VGSCheckoutCustomCityOptions(),
    override val addressOptions: VGSCheckoutCustomAddressOptions = VGSCheckoutCustomAddressOptions(),
    override val optionalAddressOptions: VGSCheckoutCustomOptionalAddressOptions = VGSCheckoutCustomOptionalAddressOptions(),
    override val postalAddressOptions: VGSCheckoutCustomPostalAddressOptions = VGSCheckoutCustomPostalAddressOptions(),
    override val visibility: VGSCheckoutBillingAddressVisibility = VGSCheckoutBillingAddressVisibility.VISIBLE
) : CheckoutBillingAddressOptions()