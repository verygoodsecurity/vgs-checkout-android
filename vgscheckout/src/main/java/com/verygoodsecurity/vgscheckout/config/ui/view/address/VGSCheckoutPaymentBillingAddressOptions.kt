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
internal class VGSCheckoutPaymentBillingAddressOptions constructor(
    override val countryOptions: VGSCheckoutPaymentCountryOptions = VGSCheckoutPaymentCountryOptions(),
    override val cityOptions: VGSCheckoutPaymentCityOptions = VGSCheckoutPaymentCityOptions(),
    override val addressOptions: VGSCheckoutPaymentAddressOptions = VGSCheckoutPaymentAddressOptions(),
    override val optionalAddressOptions: VGSCheckoutPaymentOptionalAddressOptions = VGSCheckoutPaymentOptionalAddressOptions(),
    override val postalCodeOptions: VGSCheckoutPaymentPostalCodeOptions = VGSCheckoutPaymentPostalCodeOptions(),
    override val visibility: VGSCheckoutBillingAddressVisibility = VGSCheckoutBillingAddressVisibility.HIDDEN
) : CheckoutBillingAddressOptions()