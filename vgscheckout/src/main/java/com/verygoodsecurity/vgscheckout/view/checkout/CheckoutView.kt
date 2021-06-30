package com.verygoodsecurity.vgscheckout.view.checkout

import android.content.Context
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ScrollView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.databinding.CheckoutLayoutBinding
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.view.checkout.adapter.CardIconAdapter
import com.verygoodsecurity.vgscheckout.view.checkout.adapter.CardMaskAdapter
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.model.state.FieldState.CardHolderNameState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener

class CheckoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr), OnFieldStateChangeListener {

    private val binding = CheckoutLayoutBinding.inflate(LayoutInflater.from(context), this)
    private val cardDetailsBinding = binding.includeCardDetails

    private val defaultStrokeWidth by lazy { resources.getDimensionPixelSize(R.dimen.stoke_width) }

    internal var onPayListener: OnPayClickListener? = null

    // Stroke colors
    private val defaultStrokeColor by lazy { getColor(R.color.vgs_checkout_stroke_default) }
    private val highlightedStrokeColor by lazy { getColor(R.color.vgs_checkout_stroke_highlighted) }
    private val errorStrokeColor by lazy { getColor(R.color.vgs_checkout_stroke_error) }

    init {
        initListeners()
    }

    private fun initListeners() {
        cardDetailsBinding.vgsEtCardHolder.setOnFieldStateChangeListener(this)
        cardDetailsBinding.vgsEtCardNumber.setOnFieldStateChangeListener(this)
        cardDetailsBinding.vgsEtExpirationDate.setOnFieldStateChangeListener(this)
        cardDetailsBinding.vgsEtCVC.setOnFieldStateChangeListener(this)

        // Init pay button click listener
        binding.mbPay.setOnClickListener { handlePayClicked() }
    }

    override fun onStateChange(state: FieldState) {
        updatePayButtonState()
        when (state) {
            is CardHolderNameState -> handleCardHolderStateChange(state)
            else -> handleCardDetailsStateChanged()
        }
    }

    fun applyConfig(config: CheckoutFormConfiguration) {
        // Apply pay button title
        config.payButtonTitle?.let { binding.mbPay.text = it }

        if (config is VGSCheckoutFormConfiguration) {
            // Apply card holder config
            binding.includeCardDetails.vgsEtCardNumber.setFieldName(config.cardHolderOptions.fieldName)
            binding.includeCardDetails.llCardHolder.setVisibility(config.cardHolderOptions.visibility)

            // Apply card number config
            with(config.cardNumberOptions) {
                cardDetailsBinding.vgsEtCardNumber.setFieldName(fieldName)
                cardDetailsBinding.vgsEtCardNumber.setValidCardBrands(*cardBrands.map { it.toCollectCardBrand() }
                    .toTypedArray())
                cardDetailsBinding.vgsEtCardNumber.setCardMaskAdapter(CardMaskAdapter(cardBrands))
                cardDetailsBinding.vgsEtCardNumber.setCardIconAdapter(
                    CardIconAdapter(
                        this@CheckoutView.context,
                        cardBrands
                    )
                )
            }

            // Apply expiration date config
            cardDetailsBinding.vgsEtExpirationDate.setFieldName(config.expirationDateOptions.fieldName)

            // Apply cvc config
            cardDetailsBinding.vgsEtCVC.setFieldName(config.cvcOptions.fieldName)
        }
    }

    fun bindViews(collect: VGSCollect) {
        collect.bindView(cardDetailsBinding.vgsEtCardHolder)
        collect.bindView(cardDetailsBinding.vgsEtCardNumber)
        collect.bindView(cardDetailsBinding.vgsEtExpirationDate)
        collect.bindView(cardDetailsBinding.vgsEtCVC)
    }

    private fun handlePayClicked() {
        cardDetailsBinding.llCardHolder.disable()
        cardDetailsBinding.clCardDetails.disable()
        binding.mbPay.text = getString(R.string.vgs_checkout_pay_button_processing_title)
        binding.mbPay.icon = getDrawable(R.drawable.animated_ic_progress_circle_white_16dp)
        (binding.mbPay.icon as Animatable).start()
        onPayListener?.onPayClicked()
    }

    private fun handleCardHolderStateChange(state: CardHolderNameState) {
        cardDetailsBinding.llCardHolder.applyStokeColor(defaultStrokeWidth, getStrokeColor(state))
    }

    private fun handleCardDetailsStateChanged() {
        updateCardDetailsBorderColor()
        updateSecurityCodeHint()
        // TODO: Handle error message
    }

    private fun updateCardDetailsBorderColor() {
        with(
            getStrokeColor(
                cardDetailsBinding.vgsEtCardNumber.getState(),
                cardDetailsBinding.vgsEtExpirationDate.getState(),
                cardDetailsBinding.vgsEtCVC.getState()
            )
        ) {
            cardDetailsBinding.clCardDetails.applyStokeColor(defaultStrokeWidth, this)
            cardDetailsBinding.viewDividerHorizontal.setBackgroundColor(this)
            cardDetailsBinding.viewDividerVertical.setBackgroundColor(this)
        }
    }

    private fun updateSecurityCodeHint() {
        cardDetailsBinding.vgsEtCVC.setHint(
            getString(
                if (cardDetailsBinding.vgsEtCardNumber.isAmericanExpress()) {
                    R.string.vgs_checkout_card_cvv_hint
                } else {
                    R.string.vgs_checkout_card_cvc_hint
                }
            )
        )
    }

    private fun updatePayButtonState() {
        binding.mbPay.isEnabled = isAllInputValid()
    }

    private fun getStrokeColor(vararg state: FieldState?): Int = when {
        state.any { it?.isValid == false && !it.isEmpty && !it.hasFocus } -> errorStrokeColor
        state.any { it?.hasFocus == true } -> highlightedStrokeColor
        else -> defaultStrokeColor
    }

    private fun isAllInputValid(): Boolean {
        return isInputValid(
            cardDetailsBinding.vgsEtCardHolder.getState(),
            cardDetailsBinding.vgsEtCardNumber.getState(),
            cardDetailsBinding.vgsEtExpirationDate.getState(),
            cardDetailsBinding.vgsEtCVC.getState()
        )
    }

    private fun isInputValid(vararg state: FieldState?): Boolean = state.all { it?.isValid == true }
}

internal interface OnPayClickListener {

    fun onPayClicked()
}