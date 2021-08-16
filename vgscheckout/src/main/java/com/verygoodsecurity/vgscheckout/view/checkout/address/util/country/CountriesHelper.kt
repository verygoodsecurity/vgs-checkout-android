package com.verygoodsecurity.vgscheckout.view.checkout.address.util.country

import com.verygoodsecurity.vgscheckout.view.checkout.address.util.country.model.Country
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.country.model.PostalAddressType
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.country.model.RegionType
import java.util.*

internal object CountriesHelper {

    private val countries: List<Country> by lazy { generateCountriesList() }

    private const val EMPTY = ""

    fun getHandledCountries() = countries.filter {
        it.code == ISO.USA ||
                it.code == ISO.AUSTRALIA ||
                it.code == ISO.CANADA ||
                it.code == ISO.NEW_ZEALAND ||
                it.code == ISO.UNITED_KINGDOM
    }

    fun getCountry(iso: String) = Country(
        Locale(EMPTY, iso).getDisplayCountry(Locale.US),
        iso,
        getRegionType(iso),
        getPostalAddressType(iso),
        getPostalAddressRegex(iso)
    )

    private fun generateCountriesList(): List<Country> {
        return Locale.getISOCountries().map {
            Country(
                Locale(EMPTY, it).getDisplayCountry(Locale.US),
                it,
                getRegionType(it),
                getPostalAddressType(it),
                getPostalAddressRegex(it)
            )
        }
    }

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