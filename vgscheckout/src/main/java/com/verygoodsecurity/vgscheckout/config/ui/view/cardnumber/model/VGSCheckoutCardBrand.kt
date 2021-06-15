package com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.verygoodsecurity.vgscheckout.util.extension.toCheckoutChecksumAlgorithm
import com.verygoodsecurity.vgscollect.view.card.CardType
import kotlinx.parcelize.Parcelize

@Suppress("SpellCheckingInspection", "unused")
sealed class VGSCheckoutCardBrand : Parcelable {

    //region Fields
    protected abstract val type: CardType

    open val name: String
        get() = type.name

    open val icon: Int
        get() = type.resId

    open val regex: String
        get() = type.regex

    open val mask: String
        get() = type.mask

    open val cardNumberLength: Array<Int>
        get() = type.rangeNumber

    open val securityCodeLength: Array<Int>
        get() = type.rangeCVV

    open val algorithm: VGSCheckoutChecksumAlgorithm
        get() = type.algorithm.toCheckoutChecksumAlgorithm()
    //endregion

    //region Object
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VGSCheckoutCardBrand

        if (name != other.name) return false
        if (icon != other.icon) return false
        if (regex != other.regex) return false
        if (mask != other.mask) return false
        if (!cardNumberLength.contentEquals(other.cardNumberLength)) return false
        if (!securityCodeLength.contentEquals(other.securityCodeLength)) return false
        if (algorithm != other.algorithm) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + icon
        result = 31 * result + regex.hashCode()
        result = 31 * result + mask.hashCode()
        result = 31 * result + cardNumberLength.contentHashCode()
        result = 31 * result + securityCodeLength.contentHashCode()
        result = 31 * result + algorithm.hashCode()
        return result
    }
    //endregion

    //region Utility Structures
    @Parcelize
    class Elo private constructor(override val type: CardType) : VGSCheckoutCardBrand() {

        constructor() : this(CardType.ELO)
    }

    @Parcelize
    class VisaElectron private constructor(override val type: CardType) : VGSCheckoutCardBrand() {

        constructor() : this(CardType.VISA_ELECTRON)
    }


    @Parcelize
    class Maestro private constructor(override val type: CardType) : VGSCheckoutCardBrand() {

        constructor() : this(CardType.MAESTRO)
    }

    @Parcelize
    class Forbtugsforeningen private constructor(override val type: CardType) :
        VGSCheckoutCardBrand() {

        constructor() : this(CardType.FORBRUGSFORENINGEN)
    }

    @Parcelize
    class Dankort private constructor(override val type: CardType) : VGSCheckoutCardBrand() {

        constructor() : this(CardType.DANKORT)
    }

    @Parcelize
    class Visa private constructor(override val type: CardType) : VGSCheckoutCardBrand() {

        constructor() : this(CardType.VISA)
    }

    @Parcelize
    class Mastercard private constructor(override val type: CardType) : VGSCheckoutCardBrand() {

        constructor() : this(CardType.MASTERCARD)
    }

    @Parcelize
    class AmericanExpress private constructor(override val type: CardType) :
        VGSCheckoutCardBrand() {

        constructor() : this(CardType.AMERICAN_EXPRESS)
    }

    @Parcelize
    class Hypercard private constructor(override val type: CardType) : VGSCheckoutCardBrand() {

        constructor() : this(CardType.HIPERCARD)
    }

    @Parcelize
    class Dinclub private constructor(override val type: CardType) : VGSCheckoutCardBrand() {

        constructor() : this(CardType.DINCLUB)
    }

    @Parcelize
    class Discover private constructor(override val type: CardType) : VGSCheckoutCardBrand() {

        constructor() : this(CardType.DISCOVER)
    }

    @Parcelize
    class Unipay private constructor(override val type: CardType) : VGSCheckoutCardBrand() {

        constructor() : this(CardType.UNIONPAY)
    }

    @Parcelize
    class JCB private constructor(override val type: CardType) : VGSCheckoutCardBrand() {

        constructor() : this(CardType.JCB)
    }

    @Parcelize
    class Unknown private constructor(override val type: CardType) : VGSCheckoutCardBrand() {

        constructor() : this(CardType.UNKNOWN)
    }

    @Parcelize
    class Custom private constructor(
        override val type: CardType,
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            name: String,
            @DrawableRes icon: Int,
            regex: String,
            mask: String,
            cardNumberLength: Array<Int>,
            securityCodeLength: Array<Int>,
            algorithm: VGSCheckoutChecksumAlgorithm
        ) : this(
            CardType.UNKNOWN,
            name,
            icon,
            regex,
            mask,
            cardNumberLength,
            securityCodeLength,
            algorithm
        )
    }
    //endregion
}
