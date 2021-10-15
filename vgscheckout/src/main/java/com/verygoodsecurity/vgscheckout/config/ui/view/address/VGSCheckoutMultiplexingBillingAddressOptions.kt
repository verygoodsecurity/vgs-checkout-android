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
class VGSCheckoutMultiplexingBillingAddressOptions private constructor(
    override val countryOptions: VGSCheckoutCountryOptions,
    override val cityOptions: VGSCheckoutCityOptions,
    override val addressOptions: VGSCheckoutAddressOptions,
    override val optionalAddressOptions: VGSCheckoutOptionalAddressOptions,
    override val postalAddressOptions: VGSCheckoutPostalAddressOptions,
    override val visibility: VGSCheckoutBillingAddressVisibility
) : CheckoutBillingAddressOptions() {

    /**
     * Public constructor.
     *
     * @param visibility defines if address section UI should be visible to user.
     */
    @JvmOverloads
    constructor(visibility: VGSCheckoutBillingAddressVisibility = VGSCheckoutBillingAddressVisibility.VISIBLE) : this(
        VGSCheckoutCountryOptions(COUNTRY_FIELD_NAME),
        VGSCheckoutCityOptions(CITY_FIELD_NAME),
        VGSCheckoutAddressOptions(ADDRESS_FIELD_NAME),
        VGSCheckoutOptionalAddressOptions(OPTIONAL_ADDRESS_FIELD_NAME),
        VGSCheckoutPostalAddressOptions(POSTAL_ADDRESS_FIELD_NAME),
        visibility
    )

    private companion object {

        private const val COUNTRY_FIELD_NAME = "card.billing_address.country"
        private const val CITY_FIELD_NAME = "card.billing_address.city"
        private const val ADDRESS_FIELD_NAME = "card.billing_address.address1"
        private const val OPTIONAL_ADDRESS_FIELD_NAME = "card.billing_address.address2"
        private const val POSTAL_ADDRESS_FIELD_NAME = "card.billing_address.postal_code"
    }
}