package com.verygoodsecurity.vgscheckout.view.checkout.holder

import android.content.Context
import android.util.AttributeSet
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.collect.widget.VGSEditText
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.util.extension.getDrawable
import com.verygoodsecurity.vgscheckout.util.extension.getString
import com.verygoodsecurity.vgscheckout.util.extension.setDrawableEnd
import com.verygoodsecurity.vgscheckout.view.checkout.core.BaseCheckoutFormView
import com.verygoodsecurity.vgscheckout.view.checkout.core.InputFieldViewHolder

internal class CardHolderView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCheckoutFormView(context, attrs, defStyleAttr),
    InputFieldViewHolder.OnInputFieldStateChanged {

    private val cardHolderViewHolder = InputFieldViewHolder(
        findViewById(R.id.mtvCardHolder),
        findViewById(R.id.vgsEtCardHolder),
        this
    )

    override fun getLayoutId(): Int = R.layout.vgs_checkout_card_holder_view

    override fun getColumnsCount(): Int = DEFAULT_COLUMN_ROW_COUNT

    override fun getRowsCount(): Int = DEFAULT_COLUMN_ROW_COUNT

    override fun applyConfig(config: VGSCheckoutFormConfiguration) {
        with(findViewById<VGSEditText>(R.id.vgsEtCardHolder)) {
            this.addRule(
                VGSInfoRule.ValidationBuilder()
                    .setRegex(".+")
                    .build()
            )
            this.setFieldName(config.cardOptions.cardHolderOptions.fieldName)
        }
    }

    override fun onStateChange(state: InputFieldViewHolder.ViewState) {
        updateGridColor(state)
        updateErrorMessage(state)
    }

    private fun updateErrorMessage(state: InputFieldViewHolder.ViewState) {
        if (!isChildInvalid(state)) {
            updateError(null)
            cardHolderViewHolder.title.setDrawableEnd(null)
            return
        }
        updateError(getString(R.string.vgs_checkout_card_holder_empty_error))
        cardHolderViewHolder.title.setDrawableEnd(getDrawable(R.drawable.vgs_checkout_ic_error_white_10dp))
    }

    companion object {

        private const val DEFAULT_COLUMN_ROW_COUNT = 1
    }
}