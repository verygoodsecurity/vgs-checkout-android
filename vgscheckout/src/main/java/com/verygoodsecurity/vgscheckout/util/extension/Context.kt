package com.verygoodsecurity.vgscheckout.util.extension

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.res.use

internal fun Context.getStyledAttributes(
    attributeSet: AttributeSet?,
    styleArray: IntArray,
    block: TypedArray.() -> Unit
) = this.obtainStyledAttributes(attributeSet, styleArray).use(block)

internal val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)