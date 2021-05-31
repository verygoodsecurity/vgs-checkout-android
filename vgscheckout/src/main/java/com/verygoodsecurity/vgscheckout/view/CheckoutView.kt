package com.verygoodsecurity.vgscheckout.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.ui.view.*
import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewConfig

class CheckoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.checkout_layout, this)
    }

    fun applyConfig(config: ViewConfig) {
        when (config) {
            is VGSCardNumberConfig -> TODO("Implement")
            is VGSCardHolderConfig -> TODO("Implement")
            is VGSCardVerificationCodeConfig -> TODO("Implement")
            is VGSExpirationDateConfig -> TODO("Implement")
            is VGSPostalCodeConfig -> TODO("Implement")
        }
    }
}