package com.verygoodsecurity.vgscheckout.view.checkout.core

import android.view.View

internal interface OnStateChangeListener {

    fun onStateChanged(view: View, isInputValid: Boolean)
}