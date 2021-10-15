package com.verygoodsecurity.vgscheckout.util.extension

import android.content.res.TypedArray
import androidx.annotation.StyleableRes
import androidx.core.content.res.getColorOrThrow

internal fun TypedArray.getColor(@StyleableRes index: Int): Int? {
    return try {
        getColorOrThrow(index)
    } catch (e: Exception) {
        null
    }
}