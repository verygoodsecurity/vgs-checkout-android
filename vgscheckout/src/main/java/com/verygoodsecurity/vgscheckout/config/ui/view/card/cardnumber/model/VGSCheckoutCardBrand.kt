package com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.verygoodsecurity.vgscheckout.collect.view.card.CardType
import com.verygoodsecurity.vgscheckout.util.extension.toCheckoutChecksumAlgorithm
import kotlinx.parcelize.Parcelize

@Suppress("SpellCheckingInspection", "unused")
internal sealed class VGSCheckoutCardBrand : Parcelable {

    //region Fields
    abstract val name: String

    abstract val icon: Int

    abstract val regex: String

    abstract val mask: String

    abstract val cardNumberLength: Array<Int>

    abstract val securityCodeLength: Array<Int>

    abstract val algorithm: VGSCheckoutChecksumAlgorithm
    //endregion

    //region Object
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VGSCheckoutCardBrand

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "VGSCheckoutCardBrand(name='$name', icon=$icon, regex='$regex', mask='$mask', cardNumberLength=${cardNumberLength.contentToString()}, securityCodeLength=${securityCodeLength.contentToString()}, algorithm=$algorithm)"
    }
    //endregion

    //region Utility Structures
    companion object {

        val BRANDS = setOf(
            Elo(),
            VisaElectron(),
            Maestro(),
            Forbrugsforeningen(),
            Dankort(),
            Visa(),
            Mastercard(),
            AmericanExpress(),
            Hipercard(),
            Dinclub(),
            Discover(),
            Unionpay(),
            JCB()
        )

        @DrawableRes
        fun getBrandIcon(brand: String): Int =
            BRANDS.find { it.name == brand }?.icon ?: CardType.UNKNOWN.resId
    }

    @Parcelize
    class Elo private constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            @DrawableRes icon: Int = CardType.ELO.resId,
            mask: String = CardType.ELO.mask
        ) : this(
            CardType.ELO.name,
            icon,
            CardType.ELO.regex,
            mask,
            CardType.ELO.rangeNumber,
            CardType.ELO.rangeCVV,
            CardType.ELO.algorithm.toCheckoutChecksumAlgorithm(),
        )
    }

    @Parcelize
    class VisaElectron private constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            @DrawableRes icon: Int = CardType.VISA_ELECTRON.resId,
            mask: String = CardType.VISA_ELECTRON.mask
        ) : this(
            CardType.VISA_ELECTRON.name,
            icon,
            CardType.VISA_ELECTRON.regex,
            mask,
            CardType.VISA_ELECTRON.rangeNumber,
            CardType.VISA_ELECTRON.rangeCVV,
            CardType.VISA_ELECTRON.algorithm.toCheckoutChecksumAlgorithm(),
        )
    }

    @Parcelize
    class Maestro private constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            @DrawableRes icon: Int = CardType.MAESTRO.resId,
            mask: String = CardType.MAESTRO.mask
        ) : this(
            CardType.MAESTRO.name,
            icon,
            CardType.MAESTRO.regex,
            mask,
            CardType.MAESTRO.rangeNumber,
            CardType.MAESTRO.rangeCVV,
            CardType.MAESTRO.algorithm.toCheckoutChecksumAlgorithm(),
        )
    }

    @Parcelize
    class Forbrugsforeningen private constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            @DrawableRes icon: Int = CardType.FORBRUGSFORENINGEN.resId,
            mask: String = CardType.FORBRUGSFORENINGEN.mask
        ) : this(
            CardType.FORBRUGSFORENINGEN.name,
            icon,
            CardType.FORBRUGSFORENINGEN.regex,
            mask,
            CardType.FORBRUGSFORENINGEN.rangeNumber,
            CardType.FORBRUGSFORENINGEN.rangeCVV,
            CardType.FORBRUGSFORENINGEN.algorithm.toCheckoutChecksumAlgorithm(),
        )
    }

    @Parcelize
    class Dankort private constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            @DrawableRes icon: Int = CardType.DANKORT.resId,
            mask: String = CardType.DANKORT.mask
        ) : this(
            CardType.DANKORT.name,
            icon,
            CardType.DANKORT.regex,
            mask,
            CardType.DANKORT.rangeNumber,
            CardType.DANKORT.rangeCVV,
            CardType.DANKORT.algorithm.toCheckoutChecksumAlgorithm(),
        )
    }

    @Parcelize
    class Visa private constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            @DrawableRes icon: Int = CardType.VISA.resId,
            mask: String = CardType.VISA.mask
        ) : this(
            CardType.VISA.name,
            icon,
            CardType.VISA.regex,
            mask,
            CardType.VISA.rangeNumber,
            CardType.VISA.rangeCVV,
            CardType.VISA.algorithm.toCheckoutChecksumAlgorithm(),
        )
    }

    @Parcelize
    class Mastercard private constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            @DrawableRes icon: Int = CardType.MASTERCARD.resId,
            mask: String = CardType.MASTERCARD.mask
        ) : this(
            CardType.MASTERCARD.name,
            icon,
            CardType.MASTERCARD.regex,
            mask,
            CardType.MASTERCARD.rangeNumber,
            CardType.MASTERCARD.rangeCVV,
            CardType.MASTERCARD.algorithm.toCheckoutChecksumAlgorithm(),
        )
    }

    @Parcelize
    class AmericanExpress private constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            @DrawableRes icon: Int = CardType.AMERICAN_EXPRESS.resId,
            mask: String = CardType.AMERICAN_EXPRESS.mask
        ) : this(
            CardType.AMERICAN_EXPRESS.name,
            icon,
            CardType.AMERICAN_EXPRESS.regex,
            mask,
            CardType.AMERICAN_EXPRESS.rangeNumber,
            CardType.AMERICAN_EXPRESS.rangeCVV,
            CardType.AMERICAN_EXPRESS.algorithm.toCheckoutChecksumAlgorithm(),
        )
    }

    @Parcelize
    class Hipercard private constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            @DrawableRes icon: Int = CardType.HIPERCARD.resId,
            mask: String = CardType.HIPERCARD.mask
        ) : this(
            CardType.HIPERCARD.name,
            icon,
            CardType.HIPERCARD.regex,
            mask,
            CardType.HIPERCARD.rangeNumber,
            CardType.HIPERCARD.rangeCVV,
            CardType.HIPERCARD.algorithm.toCheckoutChecksumAlgorithm(),
        )
    }

    @Parcelize
    class Dinclub private constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            @DrawableRes icon: Int = CardType.DINCLUB.resId,
            mask: String = CardType.DINCLUB.mask
        ) : this(
            CardType.DINCLUB.name,
            icon,
            CardType.DINCLUB.regex,
            mask,
            CardType.DINCLUB.rangeNumber,
            CardType.DINCLUB.rangeCVV,
            CardType.DINCLUB.algorithm.toCheckoutChecksumAlgorithm(),
        )
    }

    @Parcelize
    class Discover private constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            @DrawableRes icon: Int = CardType.DISCOVER.resId,
            mask: String = CardType.DISCOVER.mask
        ) : this(
            CardType.DISCOVER.name,
            icon,
            CardType.DISCOVER.regex,
            mask,
            CardType.DISCOVER.rangeNumber,
            CardType.DISCOVER.rangeCVV,
            CardType.DISCOVER.algorithm.toCheckoutChecksumAlgorithm(),
        )
    }

    @Parcelize
    class Unionpay private constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            @DrawableRes icon: Int = CardType.UNIONPAY.resId,
            mask: String = CardType.UNIONPAY.mask
        ) : this(
            CardType.UNIONPAY.name,
            icon,
            CardType.UNIONPAY.regex,
            mask,
            CardType.UNIONPAY.rangeNumber,
            CardType.UNIONPAY.rangeCVV,
            CardType.UNIONPAY.algorithm.toCheckoutChecksumAlgorithm(),
        )
    }

    @Parcelize
    class JCB private constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand() {

        constructor(
            @DrawableRes icon: Int = CardType.JCB.resId,
            mask: String = CardType.JCB.mask
        ) : this(
            CardType.JCB.name,
            icon,
            CardType.JCB.regex,
            mask,
            CardType.JCB.rangeNumber,
            CardType.JCB.rangeCVV,
            CardType.JCB.algorithm.toCheckoutChecksumAlgorithm(),
        )
    }

    @Parcelize
    class Custom constructor(
        override val name: String,
        @DrawableRes override val icon: Int,
        override val regex: String,
        override val mask: String,
        override val cardNumberLength: Array<Int>,
        override val securityCodeLength: Array<Int>,
        override val algorithm: VGSCheckoutChecksumAlgorithm
    ) : VGSCheckoutCardBrand()
    //endregion
}
