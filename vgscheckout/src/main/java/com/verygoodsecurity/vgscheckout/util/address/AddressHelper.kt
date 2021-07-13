package com.verygoodsecurity.vgscheckout.util.address

import java.util.*

object AddressHelper {

    private const val EMPTY = ""

    fun getCountries(): List<String> {
        return Locale.getISOCountries().map { Locale(EMPTY, it).getDisplayCountry(Locale.US) }
    }

    fun getCurrentLocaleCountry(): String = Locale.getDefault().getDisplayCountry(Locale.US)
}