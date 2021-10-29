package com.verygoodsecurity.vgscheckout.config.ui.view.address

import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutMultiplexingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutMultiplexingOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutMultiplexingCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutMultiplexingPostalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.core.CheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutMultiplexingCountryOptions
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
class VGSCheckoutMultiplexingBillingAddressOptions private constructor(
    override val countryOptions: VGSCheckoutMultiplexingCountryOptions,
    override val cityOptions: VGSCheckoutMultiplexingCityOptions,
    override val addressOptions: VGSCheckoutMultiplexingAddressOptions,
    override val optionalAddressOptions: VGSCheckoutMultiplexingOptionalAddressOptions,
    override val postalAddressOptions: VGSCheckoutMultiplexingPostalAddressOptions,
    override val visibility: VGSCheckoutBillingAddressVisibility,
) : CheckoutBillingAddressOptions() {

    /**
     * Public constructor.
     *
     * @param countryOptions country input field UI options.
     * @param visibility defines if address section UI should be visible to user.
     */
    @JvmOverloads
    constructor(
        countryOptions: VGSCheckoutMultiplexingCountryOptions = VGSCheckoutMultiplexingCountryOptions(),
        visibility: VGSCheckoutBillingAddressVisibility = VGSCheckoutBillingAddressVisibility.VISIBLE,
    ) : this(
        countryOptions,
        VGSCheckoutMultiplexingCityOptions(),
        VGSCheckoutMultiplexingAddressOptions(),
        VGSCheckoutMultiplexingOptionalAddressOptions(),
        VGSCheckoutMultiplexingPostalAddressOptions(),
        visibility
    )
}