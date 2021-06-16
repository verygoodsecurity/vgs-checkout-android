package com.verygoodsecurity.vgscheckout.util.extension

internal fun <T> Set<T>.addAllWithReplace(vararg elements: T): Set<T> {
    val result = mutableSetOf<T>()
    result.addAll(this)
    elements.forEach {
        result.remove(it)
        result.add(it)
    }
    return result
}