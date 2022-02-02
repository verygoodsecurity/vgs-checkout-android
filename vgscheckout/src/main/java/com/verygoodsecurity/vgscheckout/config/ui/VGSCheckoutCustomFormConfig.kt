package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutCustomBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCustomCardOptions
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
class VGSCheckoutCustomFormConfig internal constructor(
    override val cardOptions: VGSCheckoutCustomCardOptions,
    override val addressOptions: VGSCheckoutCustomBillingAddressOptions,
    override val validationBehaviour: VGSCheckoutFormValidationBehaviour,
    override val saveCardOptionEnabled: Boolean,
) : CheckoutFormConfig() {

    /**
     * Public constructor.
     *
     * @param cardOptions card details section UI options.
     * @param addressOptions address details section UI options.
     * @param validationBehaviour defines validation behavior. Default is [VGSCheckoutFormValidationBehaviour.ON_SUBMIT].
     */
    @JvmOverloads
    constructor(
        cardOptions: VGSCheckoutCustomCardOptions = VGSCheckoutCustomCardOptions(),
        addressOptions: VGSCheckoutCustomBillingAddressOptions = VGSCheckoutCustomBillingAddressOptions(),
        validationBehaviour: VGSCheckoutFormValidationBehaviour = VGSCheckoutFormValidationBehaviour.ON_SUBMIT
    ) : this(cardOptions, addressOptions, validationBehaviour, false)
}