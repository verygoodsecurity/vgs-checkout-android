package com.verygoodsecurity.vgscheckout.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class VGSCheckoutCreditCard constructor(
    val finId: String,
    internal val holderName: String,
    internal val number: String,
    internal val expiryMonth: Int,
    internal val expiryYear: Int,
    internal val brand: String
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