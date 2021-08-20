package com.verygoodsecurity.vgscheckout.view.checkout

import android.content.Context
import android.util.AttributeSet
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.view.checkout.core.VGSCheckoutInputView

class VGSCheckoutPersonNameView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : VGSCheckoutInputView(context, attrs, defStyleAttr) {

    override fun getInputLayout(): Int = R.layout.vgs_checkout_person_name_input_layout
}