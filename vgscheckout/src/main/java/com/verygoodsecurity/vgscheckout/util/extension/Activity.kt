package com.verygoodsecurity.vgscheckout.util.extension

import android.app.Activity
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

internal fun Activity.disableScreenshots() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_SECURE,
        WindowManager.LayoutParams.FLAG_SECURE
    )
}

internal fun Activity.getDrawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)