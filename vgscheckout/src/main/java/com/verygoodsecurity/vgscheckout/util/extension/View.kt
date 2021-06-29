package com.verygoodsecurity.vgscheckout.util.extension

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

internal fun View.applyStokeColor(width: Int, color: Int) {
    if (background is StateListDrawable) {
        (background.current as? GradientDrawable)?.setStroke(width, color)
        return
    }
    (background as? GradientDrawable)?.setStroke(width, color)
}

internal fun ViewGroup.disable() {
    isEnabled = false
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        child.isEnabled = false
        if (child is ViewGroup) {
            child.disable()
        }
    }
}

internal fun View.getString(@StringRes id: Int) = resources.getString(id)

internal fun View.getDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(context, id)

internal fun View.getColor(@ColorRes id: Int) = ContextCompat.getColor(context, id)