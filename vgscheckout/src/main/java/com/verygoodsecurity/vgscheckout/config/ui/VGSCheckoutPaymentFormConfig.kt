package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutPaymentBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
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
class VGSCheckoutPaymentFormConfig internal constructor(
    override val cardOptions: VGSCheckoutCardOptions,
    override val addressOptions: VGSCheckoutPaymentBillingAddressOptions,
    override val validationBehaviour: VGSCheckoutFormValidationBehaviour,
    override val saveCardOptionEnabled: Boolean
) : CheckoutFormConfig()