package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.core.isSandbox
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.getNormalizedHostName
import com.verygoodsecurity.vgscheckout.config.networking.core.getNormalizedPort
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
import com.verygoodsecurity.vgscheckout.networking.*
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger

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


internal fun CheckoutConfig.generateBaseUrl(): String {
    val port = routeConfig.hostnamePolicy.getNormalizedPort()
    val hostName = routeConfig.hostnamePolicy.getNormalizedHostName()
    val environment = environment.value

    fun printPortDenied() {
        if (port.isValidPort()) {
            VGSCheckoutLogger.warn(message = "To protect your device we allow to use PORT only on localhost. PORT will be ignored.")
        }
    }

    if (!hostName.isNullOrBlank() && hostName.isUrlValid()) {
        val host = hostName.toHost()
        if (host != hostName) {
            VGSCheckoutLogger.debug(message = "Hostname will be normalized to the $host")
        }
        if (host.isValidIp()) {
            if (!host.isIpAllowed()) {
                VGSCheckoutLogger.warn(message = "Current IP is not allowed, use localhost or private network IP")
                return id.setupURL(environment, routeId)
            }
            if (!environment.isSandbox()) {
                VGSCheckoutLogger.warn(message = ">Custom local IP and PORT can be used only in a sandbox environment.")
                return id.setupURL(environment, routeId)
            }
            return host.setupLocalhostURL(port)
        } else {
            printPortDenied()
//fixme            cname = host
            return id.setupURL(environment, routeId)
        }
    } else {
        printPortDenied()
        return id.setupURL(environment, routeId)
    }
}
