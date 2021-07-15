package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model

/**
 * The set of actions for checkSum validation.
 */
enum class VGSCheckoutChecksumAlgorithm {

    /** Luhn validation algorithm. */
    LUHN,

    /** Any validation algorithm */
    ANY,

    /** Number will be accepted without any algorithm */
    NONE
}