package com.verygoodsecurity.vgscheckout.config.ui.view.address

/**
 * Defines billing address section visibility mode.
 */
enum class VGSCheckoutBillingAddressVisibility {

    /**
     * Display billing address section.
     */
    VISIBLE,

    /**
     * Hide billing address section. All billing address options (`.fieldname`) will be ignored.
     */
    HIDDEN
}