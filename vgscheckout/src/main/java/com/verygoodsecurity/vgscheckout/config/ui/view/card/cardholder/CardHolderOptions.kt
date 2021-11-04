package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder

import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions

/**
 * Base class of card holder input field options.
 */
abstract class CardHolderOptions : ViewOptions() {

    /**
     * Defines if card holder name input field should be visible to user.
     */
    abstract val visibility: VGSCheckoutFieldVisibility
}