package com.verygoodsecurity.vgscheckout.ui.fragment.manual.core

internal interface ValidationResultListener {

    fun onSuccess()

    fun onFailed(invalidFields: List<String>)
}