package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutPaymentBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutPaymentCardOptions
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
class VGSCheckoutAddCardFormConfig private constructor(
    override val cardOptions: VGSCheckoutPaymentCardOptions,
    override val addressOptions: VGSCheckoutPaymentBillingAddressOptions,
    override val validationBehaviour: VGSCheckoutFormValidationBehaviour,
    override val saveCardOptionEnabled: Boolean,
) : CheckoutFormConfig() {

    /**
     * Public constructor.
     *
     * @param addressOptions address details section UI options.
     * @param validationBehaviour defines validation behavior. Default is [VGSCheckoutFormValidationBehaviour.ON_SUBMIT].
     * @param saveCardOptionEnabled defines if save card checkbox should be visible.
     */
    @JvmOverloads
    constructor(
        addressOptions: VGSCheckoutPaymentBillingAddressOptions = VGSCheckoutPaymentBillingAddressOptions(),
        validationBehaviour: VGSCheckoutFormValidationBehaviour = VGSCheckoutFormValidationBehaviour.ON_SUBMIT,
        saveCardOptionEnabled: Boolean = false
    ) : this(
        VGSCheckoutPaymentCardOptions(),
        addressOptions,
        validationBehaviour,
        saveCardOptionEnabled
    )
}