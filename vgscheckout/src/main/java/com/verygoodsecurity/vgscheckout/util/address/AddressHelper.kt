package com.verygoodsecurity.vgscheckout.util.address

import com.verygoodsecurity.vgscheckout.util.address.model.PostalAddressType
import com.verygoodsecurity.vgscheckout.util.address.model.RegionType

internal const val USA = "United States"
internal const val CANADA = "Canada"
internal const val AUSTRALIA = "Australia"
internal const val NEW_ZEALAND = "New Zealand"
internal const val UNITED_KINGDOM = "United Kingdom"

internal object AddressHelper {

    val countries = listOf(USA, CANADA, AUSTRALIA, NEW_ZEALAND, UNITED_KINGDOM)

    fun getPostalAddressType(country: String?) = when (country) {
        USA -> PostalAddressType.ZIP
        else -> PostalAddressType.POSTAL
    }

    fun getRegionType(country: String?) = when (country) {
        USA, AUSTRALIA -> RegionType.STATE
        CANADA -> RegionType.PROVINCE
        NEW_ZEALAND -> RegionType.SUBURB
        UNITED_KINGDOM -> RegionType.COUNTY
        else -> RegionType.UNKNOWN
    }
}