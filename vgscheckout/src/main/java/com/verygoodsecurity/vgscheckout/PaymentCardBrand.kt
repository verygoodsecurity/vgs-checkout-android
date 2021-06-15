package com.verygoodsecurity.vgscheckout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class PaymentCardBrand(
    open val regex: String,
    open val mask: String,
    //etc...
) : Parcelable {
    @Parcelize
    data class AmericanExpress(
        override val regex: String = "regex_amex",
        override val mask: String = "mask_amex"
    ) :
        PaymentCardBrand(regex, mask)

    @Parcelize
    data class Visa(
        override val regex: String = "regex_visa",
        override val mask: String = "mask_visa"
    ) :
        PaymentCardBrand(regex, mask)

    @Parcelize
    data class Mastercard(
        override val regex: String = "regex_master",
        override val mask: String = "mask_mastercard"
    ) :
        PaymentCardBrand(regex, mask)
}

typealias VISA = PaymentCardBrand.Visa
typealias AMEX = PaymentCardBrand.AmericanExpress
typealias MasterCard = PaymentCardBrand.Mastercard


