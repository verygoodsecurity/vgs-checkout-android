package com.verygoodsecurity.vgscheckout.view.checkout

import android.content.Context
import android.util.AttributeSet
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.widget.VGSEditText
import com.verygoodsecurity.vgscheckout.view.checkout.core.VGSCheckoutInputView

internal class VGSCheckoutPersonNameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : VGSCheckoutInputView<VGSEditText>(context, attrs, defStyleAttr) {

    override fun getInputLayout(): Int = R.layout.vgs_checkout_person_name_input_layout

    override fun getInputViewId(): Int = R.id.vgsEtPersonName
}