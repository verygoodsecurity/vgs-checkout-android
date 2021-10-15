package com.verygoodsecurity.vgscheckout.util.extension

import android.content.Intent
import android.os.Parcelable

inline fun <reified T : Parcelable> Intent.readExtraParcelable(key: String): T? {
    this.setExtrasClassLoader(T::class.java.classLoader)
    return getParcelableExtra(key)
}