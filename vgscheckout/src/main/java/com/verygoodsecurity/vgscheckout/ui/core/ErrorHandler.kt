package com.verygoodsecurity.vgscheckout.ui.core

internal interface ErrorHandler {

    fun showNetworkError(onRetry: () -> Unit)
}