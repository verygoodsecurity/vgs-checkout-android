package com.verygoodsecurity.vgscheckout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.textfield.TextInputLayout
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView

class VGSCheckoutTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    override fun addView(child: View?) {
        super.addView(handleAddView(child))
    }

    override fun addView(child: View?, index: Int) {
        super.addView(handleAddView(child), index)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(handleAddView(child), params)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        super.addView(handleAddView(child), width, height)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(handleAddView(child) ?: child, index, params)
    }

    private fun handleAddView(child: View?): View? {
        return when (child) {
            is InputFieldView -> {
                attachViewToParent(
                    child,
                    childCount,
                    LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                )
                child.statePreparer.getView()
            }
            is FrameLayout -> child
            else -> child
        }
    }
}