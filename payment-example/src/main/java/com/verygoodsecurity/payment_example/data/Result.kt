package com.verygoodsecurity.payment_example.data

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()

    data class Error(val code: Int = -1, val msg: String? = null) : Result<Nothing>()
}