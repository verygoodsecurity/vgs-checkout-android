package com.verygoodsecurity.vgscheckout.util.extension

import android.view.View
import com.google.android.material.textview.MaterialTextView

internal fun MaterialTextView.showWithText(text: String?) {
    this.text = text
    this.visibility = if (text == null) View.GONE else View.VISIBLE
}