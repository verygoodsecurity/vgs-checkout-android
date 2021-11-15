package com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration

import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions

/**
 * Base class of expiration date input field options.
 */
abstract class ExpirationDateOptions: ViewOptions() {

    /**
     * Split date into separate JSON values before send to proxy.
     */
    abstract val dateSeparateSerializer: VGSDateSeparateSerializer?

    /**
     * ISO 8601 date input format.
     */
    abstract val inputFormatRegex: String

    /**
     * ISO 8601 format in which date will be sent to proxy.
     */
    abstract val outputFormatRegex: String
}