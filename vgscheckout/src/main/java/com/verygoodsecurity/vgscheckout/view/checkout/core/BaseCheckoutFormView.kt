package com.verygoodsecurity.vgscheckout.view.checkout.core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.util.ObservableLinkedHashMap
import com.verygoodsecurity.vgscheckout.util.extension.getColor
import com.verygoodsecurity.vgscheckout.view.checkout.grid.DividerGridLayout

@Suppress("LeakingThis")
internal abstract class BaseCheckoutFormView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DividerGridLayout(context, attrs, defStyleAttr) {

    var onErrorListener: OnErrorListener? = null

    private val errors = object : ObservableLinkedHashMap<Int, String?>() {

        override fun onChanged(map: ObservableLinkedHashMap<Int, String?>) {
            onErrorListener?.onError(this@BaseCheckoutFormView, map.values.firstOrNull())
        }
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getColumnsCount(): Int

    abstract fun getRowsCount(): Int

    abstract fun applyConfig(config: VGSCheckoutFormConfiguration)

    init {
        LayoutInflater.from(context).inflate(getLayoutId(), this)
        setupGrid()
    }

    fun getError() = errors.values.firstOrNull()

    protected fun updateGridColor(vararg viewHolders: InputFieldViewHolder.ViewState) {
        setGridColor(
            when {
                isChildFocused(*viewHolders) -> getColor(R.color.vgs_checkout_border_highlighted)
                isChildInvalid(*viewHolders) -> getColor(R.color.vgs_checkout_border_error)
                else -> getColor(R.color.vgs_checkout_border_default)
            }
        )
    }

    protected fun updateError(message: String?) {
        errors[0] = message
    }

    protected fun isChildFocused(vararg viewHolders: InputFieldViewHolder.ViewState): Boolean {
        return viewHolders.any { it.hasFocus }
    }

    protected fun isChildEmpty(vararg viewHolders: InputFieldViewHolder.ViewState): Boolean {
        return viewHolders.any { it.hasBeenFocusedBefore && it.isDirty && it.isEmpty }
    }

    protected fun isChildInvalid(vararg viewHolders: InputFieldViewHolder.ViewState): Boolean {
        return viewHolders.any { it.hasBeenFocusedBefore && it.isDirty && !it.isValid }
    }

    private fun setupGrid() {
        columnCount = getColumnsCount()
        rowCount = getRowsCount()
        setRoundedCorners(resources.getDimension(R.dimen.vgs_checkout_margin_padding_size_small))
        setGridWidth(resources.getDimensionPixelSize(R.dimen.vgs_checkout_default_divider_size))
        setGridColor(getColor(R.color.vgs_checkout_border_default))
    }

    internal interface OnErrorListener {

        fun onError(view: View, message: String?)
    }
}