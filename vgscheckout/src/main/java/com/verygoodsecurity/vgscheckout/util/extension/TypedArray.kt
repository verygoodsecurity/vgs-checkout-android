package com.verygoodsecurity.vgscheckout.util.extension

import android.content.res.ColorStateList
import android.content.res.TypedArray
import androidx.annotation.StyleableRes

internal fun TypedArray.getColorStateListOrNull(@StyleableRes index: Int): ColorStateList? = try {
    getColorStateList(index)
} catch (e: Exception) {
    null
}
