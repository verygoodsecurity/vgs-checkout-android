package com.verygoodsecurity.vgscheckout.config.ui

import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfig
import com.verygoodsecurity.vgscheckout.config.ui.view.address.VGSCheckoutCustomBillingAddressOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCustomCardOptions
import kotlinx.parcelize.Parcelize

/**
 * Custom flow checkout form configuration.
 *
 * @param cardOptions credit card details block UI options.
 * @param addressOptions address details block UI options.
 */
@Parcelize
class VGSCheckoutCustomFormConfig @JvmOverloads constructor(
    override val cardOptions: VGSCheckoutCustomCardOptions = VGSCheckoutCustomCardOptions(),
    override val addressOptions: VGSCheckoutCustomBillingAddressOptions = VGSCheckoutCustomBillingAddressOptions(),
) : CheckoutFormConfig()