package com.verygoodsecurity.vgscheckout.ui.fragment.save.core

internal interface ValidationResultListener {

    fun onSuccess()

    fun onFailed(invalidFieldsAnalyticsNames: List<String>)
}