package com.verygoodsecurity.vgscheckout.config.ui.view.card

import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.core.CheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import kotlinx.parcelize.Parcelize

@Parcelize
internal class VGSCheckoutMultiplexingCardOptions private constructor(
    override val cardNumberOptions: VGSCheckoutCardNumberOptions,
    override val cardHolderOptions: VGSCheckoutCardHolderOptions,
    override val cvcOptions: VGSCheckoutCVCOptions,
    override val expirationDateOptions: VGSCheckoutExpirationDateOptions
) : CheckoutCardOptions() {

    constructor() : this(
        VGSCheckoutCardNumberOptions(CARD_NUMBER_FIELD_NAME),
        VGSCheckoutCardHolderOptions(CARD_HOLDER_FIELD_NAME),
        VGSCheckoutCVCOptions(CVC_FIELD_NAME),
        VGSCheckoutExpirationDateOptions(
            EXPIRY_DATE_FIELD_NAME,
            VGSDateSeparateSerializer(
                MONTH_FIELD_NAME,
                YEAR_FIELD_NAME
            ),
            outputFormatRegex = EXPIRY_DATE_OUTPUT_FORMAT
        )
    )

    private companion object {

        private const val CARD_NUMBER_FIELD_NAME = "card.number"
        private const val CARD_HOLDER_FIELD_NAME = "card.name"
        private const val CVC_FIELD_NAME = "card.cvc"
        private const val EXPIRY_DATE_FIELD_NAME = "card.expDate"
        private const val MONTH_FIELD_NAME = "card.exp_month"
        private const val YEAR_FIELD_NAME = "card.exp_year"
        private const val EXPIRY_DATE_OUTPUT_FORMAT = "MM/YYYY"
    }
}