package com.verygoodsecurity.vgscheckout.util.extension

internal fun <T> MutableList<T>.addIf(canBeAdded: Boolean, item: T) {
    if (canBeAdded) add(item)
}