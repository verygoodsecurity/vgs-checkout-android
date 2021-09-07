package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutSecurityCodeOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.DateSeparateSerializer
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutMultiplexingFormConfiguration private constructor(
    override val cardOptions: VGSCheckoutCardOptions,
    override val addressOptions: VGSCheckoutBillingAddressOptions,
) : CheckoutFormConfiguration() {

    constructor() : this(getVGSCheckoutCardOptions(), VGSCheckoutBillingAddressOptions())
}

private fun getVGSCheckoutCardOptions() = VGSCheckoutCardOptions(
    VGSCheckoutCardNumberOptions("card.number"),
    VGSCheckoutCardHolderOptions("card.name"),
    VGSCheckoutSecurityCodeOptions("card.cvc"),
    VGSCheckoutExpirationDateOptions(
        "card.expDate",
        DateSeparateSerializer(
            "card.exp_month",
            "card.exp_year"
        ),
        outputFormatRegex = "MM/YYYY"
    )
)