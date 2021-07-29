package com.verygoodsecurity.vgscheckout.view.checkout.core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.widget.VGSDropdownEventSpinner
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.util.ObservableLinkedHashMap
import com.verygoodsecurity.vgscheckout.util.extension.getColor
import com.verygoodsecurity.vgscheckout.util.extension.getDrawable
import com.verygoodsecurity.vgscheckout.view.checkout.grid.DividerGridLayout
import java.util.*

@Suppress("LeakingThis")
internal abstract class BaseCheckoutFormView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DividerGridLayout(context, attrs, defStyleAttr), InputFieldViewHolder.OnInputFieldStateChanged {

    var onStateChangeListener: OnStateChangeListener? = null

    var onErrorListener: OnValidationErrorListener? = null

    protected val errorDrawable = getDrawable(R.drawable.vgs_checkout_ic_error_white_10dp)

    // TODO: Handle order of error messages
    private val errors = object : ObservableLinkedHashMap<Int, String?>() {

        override fun onChanged(map: ObservableLinkedHashMap<Int, String?>) {
            onErrorListener?.onValidationError(this@BaseCheckoutFormView, getError())
        }
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun getColumnsCount(): Int

    protected abstract fun getRowsCount(): Int

    abstract fun applyConfig(config: CheckoutFormConfiguration)

    abstract fun getInputViews(): Array<InputFieldView>

    abstract fun isInputValid(): Boolean

    init {
        LayoutInflater.from(context).inflate(getLayoutId(), this)
        initGrid()
    }

    @CallSuper
    override fun onStateChange(inputId: Int, state: InputFieldViewHolder.ViewState) {
        onStateChangeListener?.onStateChanged(this, isInputValid())
    }

    fun getError(): String? = errors.values.firstOrNull { it != null }

    protected fun updateGridColor(
        vararg viewHolders: InputFieldViewHolder.ViewState,
        spinner: VGSDropdownEventSpinner? = null
    ) {
        setGridColor(
            when {
                spinner?.isDropdownOpened == true || isInputFocused(*viewHolders) -> getColor(R.color.vgs_checkout_border_highlighted)
                viewHolders.any { isInputInvalid(it) } -> getColor(R.color.vgs_checkout_border_error)
                else -> getColor(R.color.vgs_checkout_border_default)
            }
        )
    }

    protected fun updateError(key: Int, message: String?) {
        errors[key] = message
    }

    protected fun isInputFocused(vararg state: InputFieldViewHolder.ViewState): Boolean {
        return state.any { it.hasFocus }
    }

    protected fun isInputEmpty(state: InputFieldViewHolder.ViewState): Boolean {
        return state.hasBeenFocusedBefore && state.isDirty && state.isEmpty
    }

    protected fun isInputInvalid(state: InputFieldViewHolder.ViewState): Boolean {
        return state.hasBeenFocusedBefore && state.isDirty && !state.isValid
    }

    protected fun isInputValid(vararg states: InputFieldViewHolder.ViewState): Boolean {
        return states.all { it.isValid }
    }

    private fun initGrid() {
        columnCount = getColumnsCount()
        rowCount = getRowsCount()
        setRoundedCorners(resources.getDimension(R.dimen.vgs_checkout_margin_padding_size_small))
        setGridWidth(resources.getDimensionPixelSize(R.dimen.vgs_checkout_default_divider_size))
        setGridColor(getColor(R.color.vgs_checkout_border_default))
    }

    protected companion object {

        const val NOT_EMPTY_REGEX = ".+"
    }

    internal interface OnStateChangeListener {

        fun onStateChanged(view: View, isInputValid: Boolean)
    }

    internal interface OnValidationErrorListener {

        fun onValidationError(view: View, message: String?)
    }
}