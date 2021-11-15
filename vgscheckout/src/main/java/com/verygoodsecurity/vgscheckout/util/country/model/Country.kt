package com.verygoodsecurity.vgscheckout.util.country.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class Country(
    val name: String,
    val code: String,
    val postalCodeType: PostalCodeType,
    val postalCodeRegex: String
) : Parcelable {

    fun isValid() = !name.equals(code, true)
}