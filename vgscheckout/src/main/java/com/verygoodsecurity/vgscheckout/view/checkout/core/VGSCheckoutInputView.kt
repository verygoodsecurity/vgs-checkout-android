package com.verygoodsecurity.vgscheckout.view.checkout.core

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewStub
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isInvisible
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldState
import com.verygoodsecurity.vgscheckout.collect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.util.extension.getDrawable
import com.verygoodsecurity.vgscheckout.util.extension.getString
import com.verygoodsecurity.vgscheckout.util.extension.getStyledAttributes
import com.verygoodsecurity.vgscheckout.util.extension.setDrawableStart

@Suppress("LeakingThis")
internal abstract class VGSCheckoutInputView<V : InputFieldView> @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), OnFieldStateChangeListener {

    protected lateinit var inputView: V

    private val subtitleView: MaterialTextView by lazy { findViewById(R.id.mtvSubtitle) }
    private val clearView: AppCompatImageView by lazy { findViewById(R.id.ivClear) }
    private val errorView: MaterialTextView by lazy { findViewById(R.id.mtvError) }

    private var onStateChangeListener: OnStateChangeListener? = null

    @LayoutRes
    abstract fun getInputLayout(): Int

    @IdRes
    abstract fun getInputViewId(): Int

    init {

        LayoutInflater.from(context).inflate(R.layout.vgs_checkout_input_view_layout, this)
        applyAttributes(attrs)
        initInputView()
        initListeners()

        inputView
    }

    override fun onStateChange(state: FieldState) {
        updateSubtitleTextColor()
        updateClearButtonVisibility(state.isEmpty)
        onStateChangeListener?.onStateChange(state.isEmpty, state.hasFocus, state.isValid)
        if (!state.isEmpty) {
            setError("Error")
        } else {
            setError(null)
        }
    }

    fun setFieldName(name: String) {
        inputView.setFieldName(name)
    }

    fun setError(@StringRes id: Int) {
        setError(getString(id))
    }

    fun setError(error: String?) {
        if (error == null) {
            setError(null, null)
        } else {
            setError(error, getDrawable(R.drawable.vgs_checkout_ic_round_error_red_14dp))
        }
    }

    fun setOnStateChangeListener(listener: OnStateChangeListener) {
        onStateChangeListener = listener
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        context.getStyledAttributes(attrs, R.styleable.VGSCheckoutInputView) {
            subtitleView.text = getString(R.styleable.VGSCheckoutInputView_vgs_checkout_subtitle)
        }
    }

    private fun initInputView() {
        with(findViewById<ViewStub>(R.id.inputViewStub)) {
            layoutResource = getInputLayout()
            inflate()
        }
        inputView = findViewById(getInputViewId())
    }

    private fun initListeners() {
        inputView.setOnFieldStateChangeListener(this)
        clearView.setOnClickListener { inputView.setText(null) }
    }

    private fun updateClearButtonVisibility(isInputEmpty: Boolean) {
        clearView.isInvisible = isInputEmpty
    }

    private fun updateSubtitleTextColor() {
        subtitleView.setTextColor(
            getColor(
                when {
                    errorView.text.isNotBlank() -> R.color.vgs_checkout_red_orange
                    inputView.hasFocus() -> R.color.vgs_checkout_navy_blue
                    else -> R.color.vgs_checkout_spun_pearl
                }
            )
        )
    }

    private fun setError(error: String?, drawable: Drawable?) {
        subtitleView.setDrawableStart(drawable)
        errorView.text = error
        updateSubtitleTextColor()
    }

    interface OnStateChangeListener {

        fun onStateChange(isEmpty: Boolean, isFocused: Boolean, isValid: Boolean)
    }
}