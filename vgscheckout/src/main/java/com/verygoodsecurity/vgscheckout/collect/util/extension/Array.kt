package com.verygoodsecurity.vgscheckout.collect.util.extension

internal inline fun <reified T>Array<T>.except(item: T): Array<T> {
    return this.filter { it != item }.toTypedArray()
}