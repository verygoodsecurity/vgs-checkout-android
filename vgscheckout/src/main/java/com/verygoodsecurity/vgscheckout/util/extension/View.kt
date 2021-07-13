package com.verygoodsecurity.vgscheckout.util.extension

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility

internal fun View.applyStokeColor(width: Int, color: Int) {
    if (background is StateListDrawable) {
        (background.current as? GradientDrawable)?.setStroke(width, color)
        return
    }
    (background as? GradientDrawable)?.setStroke(width, color)
}

internal fun ViewGroup.setEnabledRecursively(enabled: Boolean) {
    isEnabled = false
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        child.isEnabled = enabled
        if (child is ViewGroup) {
            child.setEnabledRecursively(enabled)
        }
    }
}

internal fun View.getString(@StringRes id: Int) = resources.getString(id)

internal fun View.getDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(context, id)

@ColorInt
internal fun View.getColor(@ColorRes id: Int) = ContextCompat.getColor(context, id)

internal fun View.setVisibility(visibility: VGSCheckoutFieldVisibility) {
    this.visibility = when (visibility) {
        VGSCheckoutFieldVisibility.VISIBLE -> View.VISIBLE
        VGSCheckoutFieldVisibility.GONE -> View.GONE
    }
}

internal fun MaterialTextView.setDrawableEnd(drawable: Drawable?) {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
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

internal fun View.setVisibility(isVisible: Boolean) {
    if (isVisible) this.visible() else this.gone()
}