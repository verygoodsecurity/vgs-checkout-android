package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.VGSCheckoutOptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.VGSCheckoutCityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.VGSCheckoutPostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.VGSCheckoutCountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions

//region Helper val's for quicker access to nested objects
internal val CheckoutConfig.cardOptions: VGSCheckoutCardOptions
    get() = this.formConfig.cardOptions

internal val CheckoutConfig.cardHolderOptions: VGSCheckoutCardHolderOptions
    get() = this.formConfig.cardOptions.cardHolderOptions

internal val CheckoutConfig.cardNumberOptions: VGSCheckoutCardNumberOptions
    get() = this.formConfig.cardOptions.cardNumberOptions

internal val CheckoutConfig.expiryOptions: VGSCheckoutExpirationDateOptions
    get() = this.formConfig.cardOptions.expirationDateOptions

internal val CheckoutConfig.cvcOptions: VGSCheckoutCVCOptions
    get() = this.formConfig.cardOptions.cvcOptions

internal val CheckoutConfig.billingAddressOptions: VGSCheckoutBillingAddressOptions
    get() = this.formConfig.addressOptions

internal val CheckoutConfig.countryOptions: VGSCheckoutCountryOptions
    get() = this.formConfig.addressOptions.countryOptions

internal val CheckoutConfig.addressOptions: VGSCheckoutAddressOptions
    get() = this.formConfig.addressOptions.addressOptions

internal val CheckoutConfig.optionalAddressOptions: VGSCheckoutOptionalAddressOptions
    get() = this.formConfig.addressOptions.optionalAddressOptions

internal val CheckoutConfig.cityOptions: VGSCheckoutCityOptions
    get() = this.formConfig.addressOptions.cityOptions

internal val CheckoutConfig.postalCodeOptions: VGSCheckoutPostalCodeOptions
    get() = this.formConfig.addressOptions.postalCodeOptions
//endregion
