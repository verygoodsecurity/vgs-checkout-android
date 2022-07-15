package com.verygoodsecurity.vgscheckout.config.ui.view.card

import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.card.core.CheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import kotlinx.parcelize.Parcelize

/**
 * Payment flow checkout form card details section UI options.
 *
 * @param cardNumberOptions card number input field UI options.
 * @param cardHolderOptions card holder name input field UI options.
 * @param cvcOptions card security code input field UI options.
 * @param expirationDateOptions expiration date input field UI options.
 */
@Parcelize
class VGSCheckoutPaymentCardOptions private constructor(
    override val cardNumberOptions: VGSCheckoutCardNumberOptions,
    override val cardHolderOptions: VGSCheckoutCardHolderOptions,
    override val cvcOptions: VGSCheckoutCVCOptions,
    override val expirationDateOptions: VGSCheckoutExpirationDateOptions,
) : CheckoutCardOptions() {

    /**
     * Public constructor.
     *
     * Payment orchestration flow has fixed requirements of payload etc. so this constructor does not allow to
     * override card details section UI options.
     */
    constructor() : this(
        VGSCheckoutCardNumberOptions(CARD_NUMBER_FIELD_NAME, false, VGSCheckoutCardBrand.BRANDS),
        VGSCheckoutCardHolderOptions(CARD_HOLDER_FIELD_NAME),
        VGSCheckoutCVCOptions(CVC_FIELD_NAME),
        VGSCheckoutExpirationDateOptions(
            EXPIRY_FIELD_NAME,
            VGSDateSeparateSerializer(
                EXPIRY_MONTH_FIELD_NAME,
                EXPIRY_YEAR_FIELD_NAME
            ),
            EXPIRY_DATE_INPUT_FORMAT,
            EXPIRY_DATE_OUTPUT_FORMAT
        )
    )

    companion object {

        private const val CARD_NUMBER_FIELD_NAME = "card.number"
        private const val CARD_HOLDER_FIELD_NAME = "card.name"
        private const val CVC_FIELD_NAME = "card.cvc"
        private const val EXPIRY_FIELD_NAME = "card.expDate"
        private const val EXPIRY_MONTH_FIELD_NAME = "card.exp_month"
        private const val EXPIRY_YEAR_FIELD_NAME = "card.exp_year"
        private const val EXPIRY_DATE_INPUT_FORMAT = "MM/yy"
        private const val EXPIRY_DATE_OUTPUT_FORMAT = "MM/yyyy"
    }
}