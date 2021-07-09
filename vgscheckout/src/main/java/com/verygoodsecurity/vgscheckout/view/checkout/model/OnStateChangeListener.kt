package com.verygoodsecurity.vgscheckout.view.checkout.model

import android.view.View

internal interface OnStateChangeListener {

    fun onStateChanged(view: View, isInputValid: Boolean)
}