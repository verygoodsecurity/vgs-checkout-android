package com.verygoodsecurity.vgscheckout.util.extension

import android.content.Context
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.networking.*
import com.verygoodsecurity.vgscheckout.collect.core.isSandbox
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.getNormalizedHostName
import com.verygoodsecurity.vgscheckout.config.networking.core.getNormalizedPort
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.AddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.address.OptionalAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.city.CityOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.code.PostalCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.core.CheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.address.country.CountryOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.CardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.CardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.core.CheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.CVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.ExpirationDateOptions
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger

//region Helper val's for quicker access to nested objects
internal val CheckoutConfig.cardOptions: CheckoutCardOptions
    get() = this.formConfig.cardOptions

internal val CheckoutConfig.cardHolderOptions: CardHolderOptions
    get() = this.formConfig.cardOptions.cardHolderOptions

internal val CheckoutConfig.cardNumberOptions: CardNumberOptions
    get() = this.formConfig.cardOptions.cardNumberOptions

internal val CheckoutConfig.expiryOptions: ExpirationDateOptions
    get() = this.formConfig.cardOptions.expirationDateOptions

internal val CheckoutConfig.cvcOptions: CVCOptions
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

internal fun CheckoutConfig.getBaseUrl(context: Context): String {
    val port = routeConfig.hostnamePolicy.getNormalizedPort()
    val hostName = routeConfig.hostnamePolicy.getNormalizedHostName()
    val environment = environment.value

    fun printPortDenied() {
        if (port.isValidPort()) {
            VGSCheckoutLogger.warn(message = context.getString(R.string.vgs_checkout_error_custom_port_is_not_allowed))
        }
    }

    if (!hostName.isNullOrBlank() && hostName.isURLValid()) {
        val host = hostName.toHost().also {
            if (it != hostName) {
                VGSCheckoutLogger.debug(message = "Hostname will be normalized to the $it")
            }
        }
        if (host.isValidIp()) {
            if (!host.isIpAllowed()) {
                VGSCheckoutLogger.warn(message = context.getString(R.string.vgs_checkout_error_custom_ip_is_not_allowed))
                return id.setupURL(environment)
            }
            if (!environment.isSandbox()) {
                VGSCheckoutLogger.warn(message = context.getString(R.string.vgs_checkout_error_env_incorrect))
                return id.setupURL(environment)
            }
            return host.setupLocalhostURL(port)
        } else {
            printPortDenied()
//fixme            cname = host
            return id.setupURL(environment)
        }
    } else {
        printPortDenied()
        return id.setupURL(environment)
    }
}
