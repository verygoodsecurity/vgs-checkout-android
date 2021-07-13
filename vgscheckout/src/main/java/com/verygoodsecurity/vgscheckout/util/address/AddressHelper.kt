package com.verygoodsecurity.vgscheckout.util.address

import android.content.Context
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.util.address.model.PostalAddressType
import com.verygoodsecurity.vgscheckout.util.address.model.Region
import com.verygoodsecurity.vgscheckout.util.extension.readRawJson
import java.util.*

internal const val USA = "United States"
internal const val CANADA = "Canada"
internal const val AUSTRALIA = "Australia"
internal const val NEW_ZEALAND = "New Zealand"
internal const val UNITED_KINGDOM = "United Kingdom"

internal object AddressHelper {

    private const val EMPTY = ""

    val handledCountries = listOf(USA, CANADA, AUSTRALIA, NEW_ZEALAND, UNITED_KINGDOM)

    fun getCurrentLocaleCountry(): String = Locale.getDefault().getDisplayCountry(Locale.US)

    fun getCountries(): List<String> {
        return Locale.getISOCountries().map { Locale(EMPTY, it).getDisplayCountry(Locale.US) }
    }

    fun getCountryRegions(context: Context, country: String?): List<String>? {
        return when (country) {
            USA -> context.readRawJson<Array<Region>>(R.raw.us_states).map { it.name }
            CANADA -> context.readRawJson<Array<Region>>(R.raw.canada_provinces).map { it.name }
            AUSTRALIA -> context.readRawJson<Array<Region>>(R.raw.au_sates).map { it.name }
            else -> null
        }
    }

    fun getPostalAddressType(country: String?) = when (country) {
        USA -> PostalAddressType.ZIP
        CANADA, AUSTRALIA, NEW_ZEALAND, UNITED_KINGDOM -> PostalAddressType.POSTAL
        else -> PostalAddressType.UNKNOWN
    }
}