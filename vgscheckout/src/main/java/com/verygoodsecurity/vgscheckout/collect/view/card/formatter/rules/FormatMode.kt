package com.verygoodsecurity.vgscheckout.collect.view.card.formatter.rules

/**
 * Specifies rules on how to format the user input. Currently, it has an impact only on datePickerModes as input.
 * By default, field apply strict mode.
 */
internal enum class FormatMode {

    /**
     * Ignore any incorrect user input.
     */
    STRICT,

    /**
     * Apply any digits in user input.
     */
    FLEXIBLE
}