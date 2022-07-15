package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.AddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.OptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.CityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.PostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.core.CheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.CountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.core.CheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions

//region Helper val's for quicker access to nested objects
internal val CheckoutConfig.cardOptions: CheckoutCardOptions
    get() = this.formConfig.cardOptions

internal val CheckoutConfig.cardHolderOptions: VGSCheckoutCardHolderOptions
    get() = this.formConfig.cardOptions.cardHolderOptions

internal val CheckoutConfig.cardNumberOptions: VGSCheckoutCardNumberOptions
    get() = this.formConfig.cardOptions.cardNumberOptions

internal val CheckoutConfig.expiryOptions: VGSCheckoutExpirationDateOptions
    get() = this.formConfig.cardOptions.expirationDateOptions

internal val CheckoutConfig.cvcOptions: VGSCheckoutCVCOptions
    get() = this.formConfig.cardOptions.cvcOptions

internal val CheckoutConfig.billingAddressOptions: CheckoutBillingAddressOptions
    get() = this.formConfig.addressOptions

internal val CheckoutConfig.countryOptions: CountryOptions
    get() = this.formConfig.addressOptions.countryOptions

internal val CheckoutConfig.addressOptions: AddressOptions
    get() = this.formConfig.addressOptions.addressOptions

internal val CheckoutConfig.optionalAddressOptions: OptionalAddressOptions
    get() = this.formConfig.addressOptions.optionalAddressOptions

internal val CheckoutConfig.cityOptions: CityOptions
    get() = this.formConfig.addressOptions.cityOptions

internal val CheckoutConfig.postalCodeOptions: PostalCodeOptions
    get() = this.formConfig.addressOptions.postalCodeOptions
//endregion
