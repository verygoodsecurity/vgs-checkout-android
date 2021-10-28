package com.verygoodsecurity.vgscheckout.util.country

import com.verygoodsecurity.vgscheckout.util.country.model.Country
import com.verygoodsecurity.vgscheckout.util.country.model.PostalAddressType
import java.util.*

private const val EMPTY = ""

internal object CountriesHelper {

    val countries: List<Country> by lazy { Locale.getISOCountries().map { getCountry(it) } }

    fun getCountry(iso: String) = Country(
        Locale(EMPTY, iso).getDisplayCountry(Locale.US),
        iso,
        getPostalAddressType(iso),
        getPostalAddressRegex(iso)
    )

    fun getCountries(iso: List<String>) = iso.map { getCountry(it) }

    private fun getPostalAddressType(iso: String) = when {
        iso == ISO.USA -> PostalAddressType.ZIP
        ISO.COUNTRIES_ISO_WITHOUT_POSTAL_ADDRESS.contains(iso) -> PostalAddressType.NOTHING
        else -> PostalAddressType.POSTAL
    }

    private fun getPostalAddressRegex(iso: String) = when (iso) {
        ISO.USA -> "^([0-9]{5})(?:-([0-9]{4}))?\$"
        ISO.CANADA -> "^([ABCEGHJKLMNPRSTVXY][0-9][ABCEGHJKLMNPRSTVWXYZ])\\s*([0-9][ABCEGHJKLMNPRSTVWXYZ][0-9])\$"
        ISO.AUSTRALIA, ISO.NEW_ZEALAND -> "^\\d{4}\$"
        else -> ".+"
    }

    object ISO {

        internal const val USA = "US"
        internal const val CANADA = "CA"
        internal const val AUSTRALIA = "AU"
        internal const val NEW_ZEALAND = "NZ"

        internal val COUNTRIES_ISO_WITHOUT_POSTAL_ADDRESS = arrayOf(
            "AE",
            "AG",
            "AN",
            "AO",
            "AW",
            "BF",
            "BI",
            "BJ",
            "BO",
            "BS",
            "BW",
            "BZ",
            "CD",
            "CF",
            "CG",
            "CI",
            "CK",
            "CM",
            "DJ",
            "DM",
            "ER",
            "FJ",
            "GD",
            "GH",
            "GM",
            "GN",
            "GQ",
            "GY",
            "HK",
            "IE",
            "JM",
            "KE",
            "KI",
            "KM",
            "KN",
            "KP",
            "LC",
            "ML",
            "MO",
            "MR",
            "MS",
            "MU",
            "MW",
            "NR",
            "NU",
            "PA",
            "QA",
            "RW",
            "SB",
            "SC",
            "SL",
            "SO",
            "SR",
            "ST",
            "SY",
            "TF",
            "TK",
            "TL",
            "TO",
            "TT",
            "TV",
            "TZ",
            "UG",
            "VU",
            "YE",
            "ZA",
            "ZW",
        )
    }
}