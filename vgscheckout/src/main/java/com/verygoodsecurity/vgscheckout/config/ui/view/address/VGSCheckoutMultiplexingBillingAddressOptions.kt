package com.verygoodsecurity.vgscheckout.config.ui.view.address

import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutMultiplexingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutMultiplexingOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutMultiplexingCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutMultiplexingPostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.core.CheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutMultiplexingCountryOptions
import kotlinx.parcelize.Parcelize

/**
 * Multiplexing flow checkout form address section UI options.
 *
 * @param countryOptions country input field UI options.
 * @param cityOptions city input field UI options.
 * @param addressOptions address input field UI options.
 * @param optionalAddressOptions optional address input field UI options.
 * @param postalCodeOptions postal code input field UI options.
 * @param visibility defines if address section UI should be visible to user.
 */
@Parcelize
class VGSCheckoutMultiplexingBillingAddressOptions private constructor(
    override val countryOptions: VGSCheckoutMultiplexingCountryOptions,
    override val cityOptions: VGSCheckoutMultiplexingCityOptions,
    override val addressOptions: VGSCheckoutMultiplexingAddressOptions,
    override val optionalAddressOptions: VGSCheckoutMultiplexingOptionalAddressOptions,
    override val postalCodeOptions: VGSCheckoutMultiplexingPostalCodeOptions,
    override val visibility: VGSCheckoutBillingAddressVisibility,
) : CheckoutBillingAddressOptions() {

    /**
     * Public constructor.
     *
     * @param visibility defines if address section UI should be visible to user.
     * @param countryOptions country input field UI options.
     */
    @JvmOverloads
    constructor(
        visibility: VGSCheckoutBillingAddressVisibility = VGSCheckoutBillingAddressVisibility.VISIBLE,
        countryOptions: VGSCheckoutMultiplexingCountryOptions = VGSCheckoutMultiplexingCountryOptions(),
    ) : this(
        countryOptions,
        VGSCheckoutMultiplexingCityOptions(),
        VGSCheckoutMultiplexingAddressOptions(),
        VGSCheckoutMultiplexingOptionalAddressOptions(),
        VGSCheckoutMultiplexingPostalCodeOptions(),
        visibility
    )
}