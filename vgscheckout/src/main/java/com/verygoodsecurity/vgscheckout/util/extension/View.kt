package com.verygoodsecurity.vgscheckout.util.extension

import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.google.android.material.textview.MaterialTextView

internal fun ViewGroup.setEnabled(enabled: Boolean, recursively: Boolean) {
    isEnabled = enabled
    if (recursively) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.isEnabled = enabled
            if (child is ViewGroup) {
                child.setEnabled(enabled, recursively)
            }
        }
    }
}

internal fun View.getString(@StringRes id: Int) = resources.getString(id)

internal fun View.getDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(context, id)

@ColorInt
internal fun View.getColor(@ColorRes id: Int) = ContextCompat.getColor(context, id)

@ColorInt
internal fun View.getThemeColor(@AttrRes colorAttr: Int): Int {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(colorAttr, typedValue, true)
    return typedValue.data
}

internal fun MaterialTextView.setDrawableStart(drawable: Drawable?) {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
}