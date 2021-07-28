package com.verygoodsecurity.vgscheckout.view.checkout.card

import android.content.Context
import android.util.AttributeSet
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.internal.CVCInputField
import com.verygoodsecurity.vgscheckout.collect.view.internal.CardInputField
import com.verygoodsecurity.vgscheckout.collect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscheckout.collect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.util.extension.getString
import com.verygoodsecurity.vgscheckout.util.extension.setDrawableEnd
import com.verygoodsecurity.vgscheckout.util.extension.toCollectCardBrand
import com.verygoodsecurity.vgscheckout.view.checkout.card.adapter.CardIconAdapter
import com.verygoodsecurity.vgscheckout.view.checkout.card.adapter.CardMaskAdapter
import com.verygoodsecurity.vgscheckout.view.checkout.core.BaseCheckoutFormView
import com.verygoodsecurity.vgscheckout.view.checkout.core.InputFieldViewHolder

internal class CreditCardView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCheckoutFormView(context, attrs, defStyleAttr),
    InputFieldViewHolder.OnInputFieldStateChanged {

    private val cardNumberViewHolder = InputFieldViewHolder<VGSCardNumberEditText>(
        findViewById(R.id.mtvCardNumber),
        findViewById(R.id.vgsEtCardNumber),
        this
    )

    private val expirationDateViewHolder = InputFieldViewHolder<ExpirationDateEditText>(
        findViewById(R.id.mtvExpirationDate),
        findViewById(R.id.vgsEtExpirationDate),
        this
    )

    private val cvcViewHolder = InputFieldViewHolder<CardVerificationCodeEditText>(
        findViewById(R.id.mtvCVC),
        findViewById(R.id.vgsEtCVC),
        this
    )

    override fun getLayoutId(): Int = R.layout.vgs_checkout_credit_card_view

    override fun getColumnsCount(): Int = DEFAULT_COLUMN_ROW_COUNT

    override fun getRowsCount(): Int = DEFAULT_COLUMN_ROW_COUNT

    override fun applyConfig(config: CheckoutFormConfiguration) {
        applyCardNumberOption(config.cardOptions.cardNumberOptions)
        applyExpirationDateOption(config.cardOptions.expirationDateOptions)
        applyCVCOption(config.cardOptions.cvcOptions)
    }

    override fun onStateChange(inputId: Int, state: InputFieldViewHolder.ViewState) {
        super.onStateChange(inputId, state)
        updateGridColor(
            cardNumberViewHolder.state,
            expirationDateViewHolder.state,
            cvcViewHolder.state
        )
        when (inputId) {
            R.id.vgsEtCardNumber -> updateCardNumberError(state)
            R.id.vgsEtExpirationDate -> updateExpirationDateError(state)
            R.id.vgsEtCVC -> updateCVCError(state)
        }
    }

    override fun isInputValid() = isInputValid(
        cardNumberViewHolder.state,
        expirationDateViewHolder.state,
        cvcViewHolder.state
    )

    private fun applyCardNumberOption(options: VGSCheckoutCardNumberOptions) {
        cardNumberViewHolder.input.setFieldName(options.fieldName)
        cardNumberViewHolder.input.setValidCardBrands(options.cardBrands.map { it.toCollectCardBrand() })
        cardNumberViewHolder.input.setCardMaskAdapter(CardMaskAdapter(options.cardBrands))
        cardNumberViewHolder.input.setCardIconAdapter(CardIconAdapter(context, options.cardBrands))
        cardNumberViewHolder.input.setCardBrandPreviewIconMode(
            when (options.isIconHidden) {
                true -> CardInputField.PreviewIconMode.NEVER
                false -> CardInputField.PreviewIconMode.ALWAYS
            }
        )
    }

    private fun applyExpirationDateOption(options: VGSCheckoutExpirationDateOptions) {
        expirationDateViewHolder.input.setFieldName(options.fieldName)
    }

    private fun applyCVCOption(options: VGSCheckoutCVCOptions) {
        cvcViewHolder.input.setFieldName(options.fieldName)
        cvcViewHolder.input.setPreviewIconMode(
            when (options.isIconHidden) {
                true -> CVCInputField.PreviewIconVisibility.NEVER
                false -> CVCInputField.PreviewIconVisibility.ALWAYS
            }
        )
    }

    private fun updateCardNumberError(state: InputFieldViewHolder.ViewState) {
        val (message, drawable) = when {
            isInputEmpty(state) -> getString(R.string.vgs_checkout_card_number_empty_error) to errorDrawable
            isInputInvalid(state) -> getString(R.string.vgs_checkout_card_number_invalid_error) to errorDrawable
            else -> null to null
        }
        updateError(cardNumberViewHolder.input.id, message)
        cardNumberViewHolder.title.setDrawableEnd(drawable)
    }

    private fun updateExpirationDateError(state: InputFieldViewHolder.ViewState) {
        val (message, drawable) = when {
            isInputEmpty(state) -> getString(R.string.vgs_checkout_card_expiration_date_empty_error) to errorDrawable
            isInputInvalid(state) -> getString(R.string.vgs_checkout_card_expiration_date_invalid_error) to errorDrawable
            else -> null to null
        }
        updateError(expirationDateViewHolder.input.id, message)
        expirationDateViewHolder.title.setDrawableEnd(drawable)
    }

    private fun updateCVCError(state: InputFieldViewHolder.ViewState) {
        val (message, drawable) = when {
            isInputEmpty(state) -> getString(R.string.vgs_checkout_card_verification_code_empty_error) to errorDrawable
            isInputInvalid(state) -> getString(R.string.vgs_checkout_card_verification_code_invalid_error) to errorDrawable
            else -> null to null
        }
        updateError(cvcViewHolder.input.id, message)
        cvcViewHolder.title.setDrawableEnd(drawable)
    }

    companion object {

        private const val DEFAULT_COLUMN_ROW_COUNT = 2
    }
}