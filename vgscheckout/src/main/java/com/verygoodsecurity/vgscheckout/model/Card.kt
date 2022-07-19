package com.verygoodsecurity.vgscheckout.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class Card constructor(
    val finId: String,
    val holderName: String,
    val number: String,
    val last4: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val brand: String,
    val raw: Raw
) : Parcelable {

    @IgnoredOnParcel
    internal val twoDigitExpiryYear: String = expiryYear.toString().takeLast(TWO_DIGIT_YEAR_LENGTH)

    companion object {

        private const val TWO_DIGIT_YEAR_LENGTH = 2
    }

    @Parcelize
    data class Raw(
        val isSuccessful: Boolean,
        val code: Int,
        val body: String,
        val message: String?
    ) : Parcelable
}