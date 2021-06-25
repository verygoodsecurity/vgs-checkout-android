package com.verygoodsecurity.vgscheckout.view.checkout

import android.content.Context
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.view.checkout.adapter.CardIconAdapter
import com.verygoodsecurity.vgscheckout.view.checkout.adapter.CardMaskAdapter
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.model.state.FieldState.CardHolderNameState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscollect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscollect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscollect.widget.PersonNameEditText
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText

class CheckoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), OnFieldStateChangeListener {

    // Root layouts that holds border
    private val cardHolderLL: LinearLayout by lazy { findViewById(R.id.llCardHolder) }
    private val cardDetailsCL: ConstraintLayout by lazy { findViewById(R.id.clCardDetails) }
    private val dividerHorizontal: View by lazy { findViewById(R.id.viewDividerHorizontal) }
    private val dividerVertical: View by lazy { findViewById(R.id.viewDividerVertical) }

    // Input fields
    private val cardHolderEt: PersonNameEditText by lazy { findViewById(R.id.vgsEtCardHolder) }
    private val cardNumberEt: VGSCardNumberEditText by lazy { findViewById(R.id.vgsEtCardNumber) }
    private val expireDateEt: ExpirationDateEditText by lazy { findViewById(R.id.vgsEtExpirationDate) }
    private val cvcEt: CardVerificationCodeEditText by lazy { findViewById(R.id.vgsEtCVC) }

    private val payMB: MaterialButton by lazy { findViewById(R.id.mbPay) }

    private val defaultStrokeWidth by lazy { resources.getDimensionPixelSize(R.dimen.stoke_width) }

    internal var onPayListener: OnPayClickListener? = null

    // Stroke colors
    private val defaultStrokeColor by lazy { getColor(R.color.vgs_checkout_stroke_default) }
    private val highlightedStrokeColor by lazy { getColor(R.color.vgs_checkout_stroke_highlighted) }
    private val errorStrokeColor by lazy { getColor(R.color.vgs_checkout_stroke_error) }

    init {
        inflate(context, R.layout.checkout_layout, this)
        initListeners()
    }

    private fun initListeners() {
        cardHolderEt.setOnFieldStateChangeListener(this)
        cardNumberEt.setOnFieldStateChangeListener(this)
        expireDateEt.setOnFieldStateChangeListener(this)
        cvcEt.setOnFieldStateChangeListener(this)

        // Init pay button click listener
        payMB.setOnClickListener { handlePayClicked() }
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
        config.payButtonTitle?.let { payMB.text = it }

        if (config is VGSCheckoutFormConfiguration) {
            // Apply card holder config
            cardHolderEt.setFieldName(config.cardHolderOptions.fieldName)
            cardHolderLL.setVisibility(config.cardHolderOptions.visibility)

            // Apply card number config
            with(config.cardNumberOptions) {
                cardNumberEt.setFieldName(fieldName)
                cardNumberEt.setValidCardBrands(*cardBrands.map { it.toCollectCardBrand() }
                    .toTypedArray())
                cardNumberEt.setCardMaskAdapter(CardMaskAdapter(cardBrands))
                cardNumberEt.setCardIconAdapter(
                    CardIconAdapter(
                        this@CheckoutView.context,
                        cardBrands
                    )
                )
            }

            // Apply expiration date config
            expireDateEt.setFieldName(config.expirationDateOptions.fieldName)

            // Apply cvc config
            cvcEt.setFieldName(config.cvcOptions.fieldName)
        }
    }

    fun bindViews(collect: VGSCollect) {
        collect.bindView(cardHolderEt)
        collect.bindView(cardNumberEt)
        collect.bindView(expireDateEt)
        collect.bindView(cvcEt)
    }

    private fun handlePayClicked() {
        cardHolderLL.disable()
        cardDetailsCL.disable()
        payMB.text = getString(R.string.vgs_checkout_pay_button_processing_title)
        payMB.icon = getDrawable(R.drawable.animated_ic_progress_circle_white_16dp)
        (payMB.icon as Animatable).start()
        onPayListener?.onPayClicked()
    }

    private fun handleCardHolderStateChange(state: CardHolderNameState) {
        cardHolderLL.applyStokeColor(defaultStrokeWidth, getStrokeColor(state))
    }

    private fun handleCardDetailsStateChanged() {
        updateCardDetailsBorderColor()
        updateSecurityCodeHint()
        // TODO: Handle error message
    }

    private fun updateCardDetailsBorderColor() {
        with(getStrokeColor(cardNumberEt.getState(), expireDateEt.getState(), cvcEt.getState())) {
            cardDetailsCL.applyStokeColor(defaultStrokeWidth, this)
            dividerHorizontal.setBackgroundColor(this)
            dividerVertical.setBackgroundColor(this)
        }
    }

    private fun updateSecurityCodeHint() {
        cvcEt.setHint(
            getString(
                if (cardNumberEt.isAmericanExpress()) {
                    R.string.vgs_checkout_card_cvv_hint
                } else {
                    R.string.vgs_checkout_card_cvc_hint
                }
            )
        )
    }

    private fun updatePayButtonState() {
        payMB.isEnabled = isAllInputValid()
    }

    private fun getStrokeColor(vararg state: FieldState?): Int = when {
        state.any { it?.isValid == false && !it.isEmpty && !it.hasFocus } -> errorStrokeColor
        state.any { it?.hasFocus == true } -> highlightedStrokeColor
        else -> defaultStrokeColor
    }

    private fun isAllInputValid(): Boolean {
        return isInputValid(
            cardHolderEt.getState(),
            cardNumberEt.getState(),
            expireDateEt.getState(),
            cvcEt.getState()
        )
    }

    private fun isInputValid(vararg state: FieldState?): Boolean = state.all { it?.isValid == true }
}

internal interface OnPayClickListener {

    fun onPayClicked()
}