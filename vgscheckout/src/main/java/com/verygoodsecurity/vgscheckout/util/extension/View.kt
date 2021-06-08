package com.verygoodsecurity.vgscheckout.util.extension

import android.graphics.drawable.GradientDrawable
import android.view.View

internal fun View.applyStokeColor(width: Int, color: Int) {
    (background as? GradientDrawable)?.setStroke(width, color)
}