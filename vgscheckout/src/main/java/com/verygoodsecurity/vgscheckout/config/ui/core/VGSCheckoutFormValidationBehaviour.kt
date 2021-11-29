package com.verygoodsecurity.vgscheckout.config.ui.core

/**
 * Defines validation behavior.
 */
enum class VGSCheckoutFormValidationBehaviour {


    /**
     * Validate fields and display errors on end editing.
     */
    ON_FOCUS,

    /**
     * Validate fields and display errors on submit action.
     */
    ON_SUBMIT
}