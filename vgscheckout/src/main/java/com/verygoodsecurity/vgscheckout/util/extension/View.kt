package com.verygoodsecurity.vgscheckout.util.extension

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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

internal fun MaterialTextView.setDrawableStart(drawable: Drawable?) {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
}

internal fun View.visible() {
    this.visibility = View.VISIBLE
}

internal fun View.invisible() {
    this.visibility = View.INVISIBLE
}

internal fun View.gone() {
    this.visibility = View.GONE
}