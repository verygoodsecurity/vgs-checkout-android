package com.verygoodsecurity.vgscheckout.util.country

import com.verygoodsecurity.vgscheckout.util.country.model.Country
import com.verygoodsecurity.vgscheckout.util.country.model.PostalAddressType
import com.verygoodsecurity.vgscheckout.util.country.model.RegionType
import java.util.*

internal object CountriesHelper {

    val countries: Array<Country> by lazy { generateCountriesList() }

    private const val EMPTY = ""

    private fun generateCountriesList(): Array<Country> {
        return arrayOf(
            getCountry(ISO.USA),
            getCountry(ISO.CANADA),
            getCountry(ISO.AUSTRALIA),
            getCountry(ISO.NEW_ZEALAND),
            getCountry(ISO.UNITED_KINGDOM)
        )
    }

    private fun getCountry(iso: String) = Country(
        Locale(EMPTY, iso).getDisplayCountry(Locale.US),
        iso,
        getRegionType(iso),
        getPostalAddressType(iso),
        getPostalAddressRegex(iso)
    )

    private fun getPostalAddressType(iso: String) = when (iso) {
        ISO.USA -> PostalAddressType.ZIP
        else -> PostalAddressType.POSTAL
    }

    private fun getRegionType(iso: String) = when (iso) {
        ISO.USA, ISO.AUSTRALIA -> RegionType.STATE
        ISO.CANADA -> RegionType.PROVINCE
        ISO.NEW_ZEALAND -> RegionType.SUBURB
        ISO.UNITED_KINGDOM -> RegionType.COUNTY
        else -> RegionType.UNKNOWN
    }

    private fun getPostalAddressRegex(iso: String) = when (iso) {
        ISO.USA -> "^([0-9]{5})(?:-([0-9]{4}))?\$"
        ISO.CANADA -> "^([ABCEGHJKLMNPRSTVXY][0-9][ABCEGHJKLMNPRSTVWXYZ])\\s*([0-9][ABCEGHJKLMNPRSTVWXYZ][0-9])\$"
        ISO.AUSTRALIA -> "^\\d{4}\$"
        ISO.NEW_ZEALAND -> "^\\d{4}\$"
        ISO.UNITED_KINGDOM -> ".+"
        else -> ".+"
    }

    object ISO {

        internal const val USA = "US"
        internal const val CANADA = "CA"
        internal const val AUSTRALIA = "AU"
        internal const val NEW_ZEALAND = "NZ"
        internal const val UNITED_KINGDOM = "GB"
    }
}