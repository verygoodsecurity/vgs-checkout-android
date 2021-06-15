package com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model

import com.verygoodsecurity.vgscollect.R

/**
 * Standard constants of credit card brands that are supported by SDK.
 *
 * @param regex Regular expression rules for detection card's brand.
 * @param resId The drawable resource represents credit card logo.
 * @param mask Represents format of the current card's number.
 * @param algorithm The algorithm for validation checkSum.
 * @param rangeNumber The array of the card's number which a brand supported.
 * @param rangeCVV The array of the card's CVC number which a brand supported.
 */
//we remove this class from checkout
@Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection", "unused")
enum class VGSCheckoutCardType(val regex:String,
                               val resId:Int,
                               val mask:String,
                               val algorithm: VGSCheckoutChecksumAlgorithm,
                               val rangeNumber:Array<Int>,
                               val rangeCVV:Array<Int>) {

    ELO(
        "^(4011(78|79)|43(1274|8935)|45(1416|7393|763(1|2))|50(4175|6699|67[0-7][0-9]|9000)|627780|63(6297|6368)|650(03([^4])|04([0-9])|05(0|1)|4(0[5-9]|3[0-9]|8[5-9]|9[0-9])|5([0-2][0-9]|3[0-8])|9([2-6][0-9]|7[0-8])|541|700|720|901)|651652|655000|655021)",
        R.drawable.ic_elo_dark,
        "#### #### #### ####",
        VGSCheckoutChecksumAlgorithm.LUHN,
        arrayOf(16),
        arrayOf(3)
    ),

    VISA_ELECTRON(
        "^4(026|17500|405|508|844|91[37])",
        R.drawable.ic_visa_electron_dark,
        "#### #### #### ####",
        VGSCheckoutChecksumAlgorithm.LUHN,
        arrayOf(16),
        arrayOf(3)
    ),

    MAESTRO(
        "^(5018|5020|5038|56|57|58|6304|6390[0-9]{2}|67[0-9]{4})",
        R.drawable.ic_maestro_dark,
        "#### #### #### ####",
        VGSCheckoutChecksumAlgorithm.LUHN,
        (13..19).toList().toTypedArray(),
        arrayOf(3)
    ),

    FORBRUGSFORENINGEN(
        "^600",
        R.drawable.ic_forbrugsforeningen_dark,
        "#### #### #### ####",
        VGSCheckoutChecksumAlgorithm.LUHN,
        arrayOf(16),
        arrayOf(3)
    ),

    DANKORT(
        "^5019",
        R.drawable.ic_dankort_dark,
        "#### #### #### ####",
        VGSCheckoutChecksumAlgorithm.LUHN,
        arrayOf(16),
        arrayOf(3)
    ),

    VISA(
        "^4",
        R.drawable.ic_visa_dark,
        "#### #### #### #### ###",
        VGSCheckoutChecksumAlgorithm.LUHN,
        arrayOf(13,16,19),
        arrayOf(3)
    ),

    MASTERCARD(
        "^(5[1-5]|677189)|^(222[1-9]|2[3-6]\\d{2,}|27[0-1]\\d|2720)([0-9]{2,})",
        R.drawable.ic_mastercard_dark,
        "#### #### #### ####",
        VGSCheckoutChecksumAlgorithm.LUHN,
        arrayOf(16),
        arrayOf(3)
    ),

    AMERICAN_EXPRESS(
        "^3[47]",
        R.drawable.ic_amex_dark,
        "#### ###### #####",
        VGSCheckoutChecksumAlgorithm.LUHN,
        arrayOf(15),
        arrayOf(4)
    ),

    HIPERCARD(
        "^(384100|384140|384160|606282|637095|637568|60(?!11))",
        R.drawable.ic_hipercard_dark,
        "#### #### #### #### ###",
        VGSCheckoutChecksumAlgorithm.LUHN,
        (14..19).toList().toTypedArray(),
        arrayOf(3)
    ),

    DINCLUB(
        "^3(?:[689]|(?:0[059]+))",
        R.drawable.ic_diners_dark,
        "#### ###### ######",
        VGSCheckoutChecksumAlgorithm.LUHN,
        arrayOf(14, 16),
        arrayOf(3)
    ),

    DISCOVER(
        "^(6011|65|64[4-9]|622)",
        R.drawable.ic_discover_dark,
        "#### #### #### ####",
        VGSCheckoutChecksumAlgorithm.LUHN,
        arrayOf(16),
        arrayOf(3)
    ),
    UNIONPAY(
        "^(62)",
        R.drawable.ic_union_pay_dark,
        "#### #### #### #### ###",
        VGSCheckoutChecksumAlgorithm.NONE,
        (16..19).toList().toTypedArray(),
        arrayOf(3)
    ),

    JCB(
        "^35",
        R.drawable.ic_jcb_dark,
        "#### #### #### #### ###",
        VGSCheckoutChecksumAlgorithm.LUHN,
        (16..19).toList().toTypedArray(),
        arrayOf(3)
    ),

    UNKNOWN(
        "^\$a",
        R.drawable.ic_card_front_preview_light,
        "#### #### #### #### ###",
        VGSCheckoutChecksumAlgorithm.NONE,
        (13..19).toList().toTypedArray(),
        arrayOf(3,4)
    );
}