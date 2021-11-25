package com.verygoodsecurity.vgscheckout.util.extension

import android.app.Activity
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

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