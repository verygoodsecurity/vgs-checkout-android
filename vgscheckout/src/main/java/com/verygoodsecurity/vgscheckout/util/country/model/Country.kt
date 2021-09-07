package com.verygoodsecurity.vgscheckout.util.country.model

internal data class Country(
    val name: String,
    val code: String,
    val regionType: RegionType,
    val postalAddressType: PostalAddressType,
    val postalAddressRegex: String
)