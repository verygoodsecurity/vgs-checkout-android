package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutPaymentBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import kotlinx.parcelize.Parcelize

/**
 * Payment orchestration flow checkout form configuration.
 *
 * @param cardOptions card details section UI options.
 * @param addressOptions address details section UI options.
 * @param validationBehaviour defines validation behavior.
 * @param saveCardOptionEnabled defines if save card checkbox should be visible.
 */
@Parcelize
class VGSCheckoutPaymentFormConfig private constructor(
    override val cardOptions: VGSCheckoutCardOptions,
    override val addressOptions: VGSCheckoutPaymentBillingAddressOptions,
    override val validationBehaviour: VGSCheckoutFormValidationBehaviour,
    override val saveCardOptionEnabled: Boolean
) : CheckoutFormConfig() {

    /**
     * Public constructor.
     *
     * @param addressOptions address details section UI options.
     * @param validationBehaviour defines validation behavior. Default is [VGSCheckoutFormValidationBehaviour.ON_SUBMIT].
     * @param saveCardOptionEnabled defines if save card checkbox should be visible. Default is true.
     */
    constructor(
        addressOptions: VGSCheckoutPaymentBillingAddressOptions,
        validationBehaviour: VGSCheckoutFormValidationBehaviour,
        saveCardOptionEnabled: Boolean
    ) : this(
        VGSCheckoutCardOptions(
            VGSCheckoutCardNumberOptions(CARD_NUMBER_FIELD_NAME, false, VGSCheckoutCardBrand.BRANDS),
            VGSCheckoutCardHolderOptions(CARD_HOLDER_FIELD_NAME, VGSCheckoutFieldVisibility.VISIBLE),
            VGSCheckoutCVCOptions(CVC_FIELD_NAME, false),
            VGSCheckoutExpirationDateOptions(
                EXPIRY_FIELD_NAME,
                VGSDateSeparateSerializer(
                    EXPIRY_MONTH_FIELD_NAME,
                    EXPIRY_YEAR_FIELD_NAME
                ),
                EXPIRY_DATE_INPUT_FORMAT,
                EXPIRY_DATE_OUTPUT_FORMAT
            )
        ),
        addressOptions,
        validationBehaviour,
        saveCardOptionEnabled
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