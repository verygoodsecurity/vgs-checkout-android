package com.verygoodsecurity.vgscheckout

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutChecksumAlgorithm
import kotlinx.parcelize.Parcelize

// TODO: Add drawableId, range, cvc, etc.
// TODO: (PaymentCardBrand or PaymentCardType?) (VGSCheckoutCardBrand or VGSCheckoutCardType?)
sealed class PaymentCardBrand : Parcelable {

    abstract val regex: String

    abstract val mask: String

    abstract val algorithm: VGSCheckoutChecksumAlgorithm

    /**
     * Primary constructor is private so only AmericanExpress express can change it internal state.
     */
    @Parcelize
    class AmericanExpress private constructor(
        override val regex: String,
        override val mask: String,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : PaymentCardBrand() {

        /**
         * Public constructor does not allow override behavior that cannot change
         */
        constructor() : this(
            "american_express_regex",
            "american_express_mask",
            VGSCheckoutChecksumAlgorithm.LUHN
        )
    }

    /**
     * Constructor require to specify behavior of this card brand
     */
    @Parcelize
    class Custom(
        override val regex: String,
        override val mask: String,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : PaymentCardBrand()
}
