package com.verygoodsecurity.vgscheckout.ui.core

internal interface ValidationResultListener {

    fun onSuccess()

    fun onFailed(invalidFieldsAnalyticsNames: List<String>)
}