package com.verygoodsecurity.vgscheckout.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class Card constructor(
    val finId: String,
    val holderName: String,
    val number: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val brand: String,
    val raw: String
) : Parcelable {

    @IgnoredOnParcel
    internal val lastFour: String = number.takeLast(LAST_FOUR_LENGTH)

    @IgnoredOnParcel
    internal val twoDigitExpiryYear: String = expiryYear.toString().takeLast(TWO_DIGIT_YEAR_LENGTH)

    companion object {

        private const val LAST_FOUR_LENGTH = 4
        private const val TWO_DIGIT_YEAR_LENGTH = 2
    }
}