package com.verygoodsecurity.vgscheckout.view.checkout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.verygoodsecurity.vgscheckout.R

internal class AddressView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    init {

        LayoutInflater.from(context).inflate(R.layout.vgs_checkout_address_view, this)
    }
}