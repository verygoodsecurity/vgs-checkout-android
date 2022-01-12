package com.verygoodsecurity.vgscheckout.config.ui.view.address

import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutPaymentAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutPaymentOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutPaymentCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPaymentPostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.core.CheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutPaymentCountryOptions
import kotlinx.parcelize.Parcelize

/**
 * Payment orchestration flow checkout form address section UI options.
 *
 * @param countryOptions country input field UI options.
 * @param cityOptions city input field UI options.
 * @param addressOptions address input field UI options.
 * @param optionalAddressOptions optional address input field UI options.
 * @param postalCodeOptions postal code input field UI options.
 * @param visibility defines if address section UI should be visible to user.
 */
@Parcelize
class VGSCheckoutPaymentBillingAddressOptions private constructor(
    override val countryOptions: VGSCheckoutPaymentCountryOptions,
    override val cityOptions: VGSCheckoutPaymentCityOptions,
    override val addressOptions: VGSCheckoutPaymentAddressOptions,
    override val optionalAddressOptions: VGSCheckoutPaymentOptionalAddressOptions,
    override val postalCodeOptions: VGSCheckoutPaymentPostalCodeOptions,
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
        visibility: VGSCheckoutBillingAddressVisibility = VGSCheckoutBillingAddressVisibility.HIDDEN,
        countryOptions: VGSCheckoutPaymentCountryOptions = VGSCheckoutPaymentCountryOptions(),
    ) : this(
        countryOptions,
        VGSCheckoutPaymentCityOptions(),
        VGSCheckoutPaymentAddressOptions(),
        VGSCheckoutPaymentOptionalAddressOptions(),
        VGSCheckoutPaymentPostalCodeOptions(),
        visibility
    )
}