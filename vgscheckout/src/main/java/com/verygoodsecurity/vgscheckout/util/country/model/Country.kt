package com.verygoodsecurity.vgscheckout.util.country.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class Country(
    val name: String,
    val code: String,
    val postalAddressType: PostalAddressType,
    val postalAddressRegex: String
) : Parcelable