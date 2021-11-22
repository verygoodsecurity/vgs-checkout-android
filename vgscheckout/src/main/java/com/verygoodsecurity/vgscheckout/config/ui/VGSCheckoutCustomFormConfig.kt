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
 * @param validationBehaviour defines validation behavior. Default is [VGSCheckoutFormValidationBehaviour.ON_SUBMIT].
 */
@Parcelize
class VGSCheckoutCustomFormConfig @JvmOverloads constructor(
    override val cardOptions: VGSCheckoutCustomCardOptions = VGSCheckoutCustomCardOptions(),
    override val addressOptions: VGSCheckoutCustomBillingAddressOptions = VGSCheckoutCustomBillingAddressOptions(),
    override val validationBehaviour: VGSCheckoutFormValidationBehaviour = VGSCheckoutFormValidationBehaviour.ON_SUBMIT,
) : CheckoutFormConfig()