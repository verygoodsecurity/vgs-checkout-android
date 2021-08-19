package com.verygoodsecurity.vgscheckout.view.checkout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewStub
import android.widget.FrameLayout
import com.verygoodsecurity.vgscheckout.R

class VGSCheckoutInputView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {

        LayoutInflater.from(context).inflate(R.layout.vgs_checkout_input_view_layout, this)

        findViewById<ViewStub>(R.id.viewStub).layoutResource = R.layout.vgs_checkout_person_name_input_layout
        findViewById<ViewStub>(R.id.viewStub).inflate()
    }
}