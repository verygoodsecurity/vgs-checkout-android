package com.verygoodsecurity.vgscheckout.ui.core

import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle

internal interface ResultHandler {

    fun getResultBundle(): VGSCheckoutResultBundle

    fun setResult(isSuccessful: Boolean)
}