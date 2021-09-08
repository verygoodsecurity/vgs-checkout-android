package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutMultiplexingFormConfiguration private constructor(
    override val cardOptions: VGSCheckoutCardOptions,
    override val addressOptions: VGSCheckoutBillingAddressOptions,
) : CheckoutFormConfiguration() {

    constructor() : this(getVGSCheckoutCardOptions(), VGSCheckoutBillingAddressOptions())

    companion object {
        private fun getVGSCheckoutCardOptions() = VGSCheckoutCardOptions(
            VGSCheckoutCardNumberOptions("card.number"),
            VGSCheckoutCardHolderOptions("card.name"),
            VGSCheckoutCVCOptions("card.cvc"),
            VGSCheckoutExpirationDateOptions(
                "card.expDate",
                VGSDateSeparateSerializer(
                    "card.exp_month",
                    "card.exp_year"
                ),
                outputFormatRegex = "MM/YYYY"
            )
        )
    }
}

