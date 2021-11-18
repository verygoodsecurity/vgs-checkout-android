package com.verygoodsecurity.vgscheckout.util.extension

import android.os.Parcelable
import androidx.fragment.app.Fragment

internal inline fun <reified R : Parcelable> Fragment.requireArgument(key: String): R {
    return requireArguments().getParcelable(key)
        ?: throw IllegalArgumentException("Argument with key[$key] doesn't exist.")
}