package com.verygoodsecurity.vgscheckout.util.extension

import android.content.res.ColorStateList
import androidx.annotation.ColorInt

@ColorInt
internal fun ColorStateList?.getColor(state: IntArray?, @ColorInt defColor: Int): Int {
    return this?.getColorForState(state, defColor) ?: defColor
}