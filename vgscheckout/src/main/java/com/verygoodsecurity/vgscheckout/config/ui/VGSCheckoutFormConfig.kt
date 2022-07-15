package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import kotlinx.parcelize.Parcelize

/**
 * Custom flow checkout form configuration.
 *
 * @param cardOptions card details section UI options.
 * @param addressOptions address details section UI options.
 * @param validationBehaviour defines validation behavior.
 * @param saveCardOptionEnabled defines if save card checkbox should be visible.
 */
@Parcelize
class VGSCheckoutFormConfig internal constructor(
    override val cardOptions: VGSCheckoutCardOptions = VGSCheckoutCardOptions(),
    override val addressOptions: VGSCheckoutBillingAddressOptions = VGSCheckoutBillingAddressOptions(),
    override val validationBehaviour: VGSCheckoutFormValidationBehaviour = VGSCheckoutFormValidationBehaviour.ON_SUBMIT,
    override val saveCardOptionEnabled: Boolean = false,
) : CheckoutFormConfig()