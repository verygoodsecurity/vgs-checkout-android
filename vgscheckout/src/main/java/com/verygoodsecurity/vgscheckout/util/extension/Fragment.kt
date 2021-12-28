package com.verygoodsecurity.vgscheckout.util.extension

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

internal inline fun <reified R : Parcelable> Fragment.requireParcelable(key: String): R {
    return requireArguments().getParcelable(key)
        ?: throw IllegalArgumentException("Argument with key[$key] doesn't exist.")
}

internal fun Fragment.requireString(key: String): String {
    return requireArguments().getString(key)
        ?: throw IllegalArgumentException("Argument with key[$key] doesn't exist.")
}

internal fun Fragment.getDrawableCompat(@DrawableRes id: Int) =
    ContextCompat.getDrawable(requireContext(), id)