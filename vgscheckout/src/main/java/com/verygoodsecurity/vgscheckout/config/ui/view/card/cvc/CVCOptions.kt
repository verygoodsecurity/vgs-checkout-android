package com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions

/**
 * Base class of card cvc input field options.
 */
abstract class CVCOptions: ViewOptions() {

    /**
     * Defines if card brand icon should be hidden.
     */
    abstract val isIconHidden: Boolean
}