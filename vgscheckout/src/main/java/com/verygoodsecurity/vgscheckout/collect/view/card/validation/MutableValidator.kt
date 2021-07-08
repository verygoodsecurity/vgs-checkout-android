package com.verygoodsecurity.vgscheckout.collect.view.card.validation

/** @suppress */
interface MutableValidator:VGSValidator {
    fun clearRules()
    fun addRule(validator: VGSValidator)
}