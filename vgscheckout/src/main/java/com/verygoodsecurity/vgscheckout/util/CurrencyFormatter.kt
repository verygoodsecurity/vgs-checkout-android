package com.verygoodsecurity.vgscheckout.util

import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger
import java.text.NumberFormat
import java.util.*
import kotlin.math.pow

internal object CurrencyFormatter {

    private const val MAJOR_UNIT_BASE = 10.0

    fun format(
        amount: Long,
        isoCode: String,
        locale: Locale = Locale.getDefault()
    ): String {
        val currency = Currency.getInstance(isoCode)
        val fractionDigits = currency.defaultFractionDigits
        val majorUnitAmount = amount / MAJOR_UNIT_BASE.pow(fractionDigits.toDouble())

        val format = NumberFormat.getCurrencyInstance(locale)

        try {
            format.currency = currency
            format.minimumFractionDigits = fractionDigits
        } catch (e: Exception) {
            VGSCheckoutLogger.warn(message = "Cannot format amount: $amount currency: $currency")
        }

        return format.format(majorUnitAmount)
    }
}