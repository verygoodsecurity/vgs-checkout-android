package com.verygoodsecurity.vgscheckout.collect.view.card.conection

import com.verygoodsecurity.vgscheckout.collect.view.card.validation.VGSValidator

internal abstract class BaseInputConnection constructor(
    private val id: Int,
    internal var defaultValidator: VGSValidator?
) : InputRunnable {

    protected fun isValid(input: String?): Boolean {
        return defaultValidator?.isValid(input) ?: false
    }
}