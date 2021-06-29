package com.verygoodsecurity.vgscheckout.util.extension

import android.app.Activity
import android.view.WindowManager

internal inline fun <reified R> Activity.requireExtra(key: String): R {
    return (intent.extras?.get(key) as? R) ?: throw IllegalArgumentException("Extras required!")
}

internal fun Activity.disableScreenshots() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_SECURE,
        WindowManager.LayoutParams.FLAG_SECURE
    )
}