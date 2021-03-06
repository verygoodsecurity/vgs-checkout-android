package com.verygoodsecurity.vgscheckout.collect.view.card.validation

import java.util.regex.Pattern

/** @suppress */
internal class InfoValidator:VGSValidator {
    private val m = Pattern.compile(".*?")

    override fun isValid(content: String?): Boolean {
        val str = content?.trim()
        return !str.isNullOrEmpty() &&
                m.matcher(str).matches()
    }
}