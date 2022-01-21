package com.verygoodsecurity.vgscheckout.ui.core

internal interface ValidationResultListener {

    fun onSuccess(shouldSaveCard: Boolean?)

    fun onFailed(invalidFieldsAnalyticsNames: List<String>)
}