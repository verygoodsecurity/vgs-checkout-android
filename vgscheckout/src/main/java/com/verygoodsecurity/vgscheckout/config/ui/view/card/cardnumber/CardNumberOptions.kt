package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber

import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions

/**
 * Base class of card number input field options.
 */
abstract class CardNumberOptions: ViewOptions() {

    /**
     * Defines if card brand icon should be hidden.
     */
    abstract val isIconHidden: Boolean

    /**
     * List of brands that can be detected.
     */
    internal abstract val cardBrands: Set<VGSCheckoutCardBrand>
}