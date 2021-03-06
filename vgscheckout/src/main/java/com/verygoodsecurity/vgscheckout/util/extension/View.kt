package com.verygoodsecurity.vgscheckout.util.extension

import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

internal fun ViewGroup.setEnabled(
    enabled: Boolean,
    recursively: Boolean,
    vararg excluded: View? = arrayOf()
) {
    isEnabled = enabled
    if (recursively) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (!excluded.contains(child)) child.isEnabled = enabled
            if (child is ViewGroup) child.setEnabled(enabled, recursively, *excluded)
        }
    }
}

internal fun View.gone() {
    visibility = View.GONE
}

internal fun View.visible() {
    visibility = View.VISIBLE
}

internal fun View.setVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

internal fun View.getString(@StringRes id: Int) = resources.getString(id)

@ColorInt
internal fun View.getColor(@ColorRes id: Int) = ContextCompat.getColor(context, id)