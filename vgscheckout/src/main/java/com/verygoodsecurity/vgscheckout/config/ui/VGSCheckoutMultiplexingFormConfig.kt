package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutMultiplexingBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutMultiplexingCardOptions
import kotlinx.parcelize.Parcelize

/**
 * Multiplexing flow checkout form configuration.
 *
 * @param cardOptions card details section UI options.
 * @param addressOptions address details section UI options.
 * @param validationBehaviour defines validation behavior.
 */
@Parcelize
class VGSCheckoutMultiplexingFormConfig private constructor(
    override val cardOptions: VGSCheckoutMultiplexingCardOptions,
    override val addressOptions: VGSCheckoutMultiplexingBillingAddressOptions,
    override val validationBehaviour: VGSCheckoutFormValidationBehaviour,
) : CheckoutFormConfig() {

    /**
     * Public constructor.
     *
     * @param addressOptions address details section UI options.
     * @param validationBehaviour defines validation behavior. Default is [VGSCheckoutFormValidationBehaviour.ON_SUBMIT].
     */
    @JvmOverloads
    constructor(
        addressOptions: VGSCheckoutMultiplexingBillingAddressOptions = VGSCheckoutMultiplexingBillingAddressOptions(),
        validationBehaviour: VGSCheckoutFormValidationBehaviour = VGSCheckoutFormValidationBehaviour.ON_SUBMIT,
    ) : this(VGSCheckoutMultiplexingCardOptions(), addressOptions, validationBehaviour)
}