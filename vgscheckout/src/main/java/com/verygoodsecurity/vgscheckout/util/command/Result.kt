package com.verygoodsecurity.vgscheckout.util.command

import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException

internal sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()

    data class Error(val e: VGSCheckoutException) : Result<Nothing>()
}