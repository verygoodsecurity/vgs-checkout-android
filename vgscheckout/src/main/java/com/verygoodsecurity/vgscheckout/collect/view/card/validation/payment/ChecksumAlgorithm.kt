package com.verygoodsecurity.vgscheckout.collect.view.card.validation.payment

/**
 * The set of actions for checkSum validation.
 */
internal enum class ChecksumAlgorithm {

    /** Luhn validation algorithm. */
    LUHN,

    /** Any validation algorithm */
    ANY,

    /** Number will be accepted without any algorithm */
    NONE
}