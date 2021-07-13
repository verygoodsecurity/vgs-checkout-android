package com.verygoodsecurity.vgscheckout.view.checkout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.view.custom.DividerGridLayout

internal class AddressView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {

        LayoutInflater.from(context).inflate(R.layout.vgs_checkout_address_view, this)
        findViewById<DividerGridLayout>(R.id.dglRoot).isEnabled = true
    }
}