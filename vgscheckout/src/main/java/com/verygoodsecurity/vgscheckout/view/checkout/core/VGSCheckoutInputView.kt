package com.verygoodsecurity.vgscheckout.view.checkout.core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewStub
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.verygoodsecurity.vgscheckout.R

@Suppress("LeakingThis")
abstract class VGSCheckoutInputView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    @LayoutRes
    abstract fun getInputLayout(): Int

    init {

        LayoutInflater.from(context).inflate(R.layout.vgs_checkout_input_view_layout, this)
        initInputView()
    }

    private fun initInputView() {
        with(findViewById<ViewStub>(R.id.inputViewStub)) {
            layoutResource = getInputLayout()
            inflate()
        }
    }
}