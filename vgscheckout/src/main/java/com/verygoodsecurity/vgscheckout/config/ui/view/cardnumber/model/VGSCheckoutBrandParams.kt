package com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

private const val DEFAULT_MASK = "#### #### #### #### ###"

/**
 * Creates a new set of card brand parameters.
 *
 * @constructor Primary constructor.
 * @param mask - Represents format of the current card's number.
 * @param algorithm - The algorithm for validation checkSum.
 * @param rangeNumber - The length of the card's number which a brand supported.
 * @param rangeCVV - The length of the card's CVC number which a brand supported.
 */
@Parcelize
class VGSCheckoutBrandParams(
    val mask: String = DEFAULT_MASK,
    val algorithm: VGSCheckoutChecksumAlgorithm = VGSCheckoutChecksumAlgorithm.NONE,
    val rangeNumber: Array<Int> = VGSCheckoutCardType.UNKNOWN.rangeNumber,
    val rangeCVV: Array<Int> = VGSCheckoutCardType.UNKNOWN.rangeCVV
): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VGSCheckoutBrandParams

        if (mask != other.mask) return false
        if (algorithm != other.algorithm) return false
        if (!rangeNumber.contentEquals(other.rangeNumber)) return false
        if (!rangeCVV.contentEquals(other.rangeCVV)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mask.hashCode()
        result = 31 * result + algorithm.hashCode()
        result = 31 * result + rangeNumber.contentHashCode()
        result = 31 * result + rangeCVV.contentHashCode()
        return result
    }
}