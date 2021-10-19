package com.verygoodsecurity.vgscheckout.util.extension

import android.app.Activity
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

internal fun Activity.setScreenshotsAllowed(isAllowed: Boolean) {
    if (isAllowed) return
    window.setFlags(
        WindowManager.LayoutParams.FLAG_SECURE,
        WindowManager.LayoutParams.FLAG_SECURE
    )
}

internal fun Activity.hideSoftKeyboard() {
    val inputMethodManager: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

internal fun Activity.getDrawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)