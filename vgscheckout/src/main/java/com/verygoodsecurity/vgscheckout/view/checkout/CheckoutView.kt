package com.verygoodsecurity.vgscheckout.view.checkout

import android.content.Context
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldState.*
import com.verygoodsecurity.vgscheckout.collect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscheckout.collect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscheckout.collect.widget.PersonNameEditText
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.util.ObservableLinkedHashMap
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.view.checkout.adapter.CardIconAdapter
import com.verygoodsecurity.vgscheckout.view.checkout.adapter.CardMaskAdapter

internal class CheckoutView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), StateChangeListener {

    internal var onPayListener: OnPayClickListener? = null

    private val errorMessages: ObservableLinkedHashMap<Int, String?> = initErrorMessages()

    private val clCardDetails: ConstraintLayout
    private val etCardHolder: PersonNameEditText
    private val tvCardHolderHint: MaterialTextView
    private val llCardHolder: LinearLayout
    private val etCardNumber: VGSCardNumberEditText
    private val tvCardNumberHint: MaterialTextView
    private val etExpirationDate: ExpirationDateEditText
    private val tvExpirationDateHint: MaterialTextView
    private val etSecurityCode: CardVerificationCodeEditText
    private val tvSecurityCodeHint: MaterialTextView
    private val bPay: MaterialButton
    private val tvError: MaterialTextView
    private val vDividerHorizontal: View
    private val vDividerVertical: View

    private val cardHolderStateHolder: InputViewStateHolder
    private val cardNumberStateHolder: InputViewStateHolder
    private val dateStateHolder: InputViewStateHolder
    private val cvcStateHolder: InputViewStateHolder

    private val defaultBorderColor by lazy { getColor(R.color.vgs_checkout_border_default) }
    private val focusedBorderColor by lazy { getColor(R.color.vgs_checkout_border_highlighted) }
    private val errorBorderColor by lazy { getColor(R.color.vgs_checkout_border_error) }
    private val defaultBorderWidth by lazy { resources.getDimensionPixelSize(R.dimen.vgs_checkout_default_stoke_width) }
    private val errorDrawable by lazy { getDrawable(R.drawable.vgs_checkout_ic_error_white_10dp) }
    private val cvcHint by lazy { getString(R.string.vgs_checkout_card_verification_code_hint) }
    private val cvvHint by lazy { getString(R.string.vgs_checkout_card_verification_value_hint) }

    init {
        LayoutInflater.from(context).inflate(R.layout.vgs_checkout_view_layout, this)

        clCardDetails = findViewById(R.id.clCardDetails)
        etCardHolder = findViewById(R.id.vgsEtCardHolder)
        tvCardHolderHint = findViewById(R.id.mtvCardHolderHint)
        llCardHolder = findViewById(R.id.llCardHolder)
        etCardNumber = findViewById(R.id.vgsEtCardNumber)
        tvCardNumberHint = findViewById(R.id.mtvCardNumberHint)
        etExpirationDate = findViewById(R.id.vgsEtDate)
        tvExpirationDateHint = findViewById(R.id.mtvDateHint)
        etSecurityCode = findViewById(R.id.vgsEtCVC)
        tvSecurityCodeHint = findViewById(R.id.mtvCVCHint)
        bPay = findViewById(R.id.mbPay)
        tvError = findViewById(R.id.tvCardDetailsError)
        vDividerHorizontal = findViewById(R.id.viewDividerHorizontal)
        vDividerVertical = findViewById(R.id.viewDividerVertical)

        cardHolderStateHolder = InputViewStateHolder(etCardHolder, this)
        cardNumberStateHolder = InputViewStateHolder(etCardNumber, this)
        dateStateHolder = InputViewStateHolder(etExpirationDate, this)
        cvcStateHolder = InputViewStateHolder(etSecurityCode, this)

        initListeners()
    }

    override fun onStateChange(id: Int, state: ViewState) {
        updatePayButton()
        when (id) {
            R.id.vgsEtCardHolder -> handleCardHolderStateChanged(state)
            R.id.vgsEtCardNumber -> handleCardNumberStateChanged(state)
            R.id.vgsEtDate -> handleDateStateChanged(state)
            R.id.vgsEtCVC -> handleCVCStateChanged(state)
        }
    }

    fun applyConfig(config: CheckoutFormConfiguration) {
        etCardHolder.setFieldName(config.cardHolderOptions.fieldName)
        llCardHolder.setVisibility(config.cardHolderOptions.visibility)
        etCardNumber.setFieldName(config.cardNumberOptions.fieldName)
        etCardNumber.setValidCardBrands(
            *config.cardNumberOptions.cardBrands.map { it.toCollectCardBrand() }.toTypedArray()
        )
        etCardNumber.setCardMaskAdapter(CardMaskAdapter(config.cardNumberOptions.cardBrands))
        etCardNumber.setCardIconAdapter(
            CardIconAdapter(this@CheckoutView.context, config.cardNumberOptions.cardBrands)
        )
        etExpirationDate.setFieldName(config.expirationDateOptions.fieldName)
        etSecurityCode.setFieldName(config.cvcOptions.fieldName)
        config.payButtonTitle?.let { bPay.text = it }
    }

    fun getCollectViews() = arrayOf(
        etCardHolder,
        etCardNumber,
        etExpirationDate,
        etSecurityCode
    )

    private fun initErrorMessages(): ObservableLinkedHashMap<Int, String?> {
        val defaultMessages = linkedMapOf<Int, String?>(
            R.id.vgsEtCardHolder to null,
            R.id.vgsEtCardNumber to null,
            R.id.vgsEtDate to null,
            R.id.vgsEtCVC to null
        )
        return object : ObservableLinkedHashMap<Int, String?>(defaultMessages) {

            override fun onChanged(map: ObservableLinkedHashMap<Int, String?>) {
                tvError.showWithText(map.firstNotNullOfOrNull { it.value })
                map.forEach {
                    val drawable = if (it.value == null) null else errorDrawable
                    when (it.key) {
                        R.id.vgsEtCardHolder -> tvCardHolderHint.setDrawableEnd(drawable)
                        R.id.vgsEtCardNumber -> tvCardNumberHint.setDrawableEnd(drawable)
                        R.id.vgsEtDate -> tvExpirationDateHint.setDrawableEnd(drawable)
                        R.id.vgsEtCVC -> tvSecurityCodeHint.setDrawableEnd(drawable)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        bPay.setOnClickListener { handlePayClicked() }
    }

    private fun handlePayClicked() {
        llCardHolder.disable()
        clCardDetails.disable()
        bPay.isClickable = false
        bPay.text = getString(R.string.vgs_checkout_pay_button_processing_title)
        bPay.icon = getDrawable(R.drawable.vgs_checkout_animated_ic_progress_circle_white_16dp)
        (bPay.icon as Animatable).start()
        onPayListener?.onPayClicked()
    }

    private fun handleCardHolderStateChanged(state: ViewState) {
        llCardHolder.applyStokeColor(defaultBorderWidth, getBorderColor(state))
        if (state.shouldValidate()) {
            errorMessages[R.id.vgsEtCardHolder] = when {
                !state.isValid || state.isEmpty -> getString(R.string.vgs_checkout_card_holder_empty_error)
                else -> null
            }
        }
    }

    private fun handleCardNumberStateChanged(state: ViewState) {
        updateSecurityCodeHint()
        updateCardDetailsBorderColor()
        if (state.shouldValidate()) {
            errorMessages[R.id.vgsEtCardNumber] = when {
                state.isEmpty -> getString(R.string.vgs_checkout_card_number_empty_error)
                !state.isValid -> getString(R.string.vgs_checkout_card_number_invalid_error)
                else -> null
            }
        }
    }

    private fun handleDateStateChanged(state: ViewState) {
        updateCardDetailsBorderColor()
        if (state.shouldValidate()) {
            errorMessages[R.id.vgsEtDate] = when {
                state.isEmpty -> getString(R.string.vgs_checkout_card_expiration_date_empty_error)
                !state.isValid -> getString(R.string.vgs_checkout_card_expiration_date_invalid_error)
                else -> null
            }
        }
    }

    private fun handleCVCStateChanged(state: ViewState) {
        updateCardDetailsBorderColor()
        if (state.shouldValidate()) {
            errorMessages[R.id.vgsEtCVC] = when {
                state.isEmpty -> getString(R.string.vgs_checkout_card_verification_code_empty_error)
                !state.isValid -> getString(R.string.vgs_checkout_card_verification_code_invalid_error)
                else -> null
            }
        }
    }

    private fun updatePayButton() {
        bPay.isEnabled = isInputValid()
    }

    private fun updateSecurityCodeHint() {
        etSecurityCode.setHint(getCVCHint())
    }

    private fun updateCardDetailsBorderColor() {
        with(
            getBorderColor(
                cardNumberStateHolder.getState(),
                dateStateHolder.getState(),
                cvcStateHolder.getState()
            )
        ) {
            clCardDetails.applyStokeColor(defaultBorderWidth, this)
            vDividerHorizontal.setBackgroundColor(this)
            vDividerVertical.setBackgroundColor(this)
        }
    }

    private fun getCVCHint() =
        if (etCardNumber.getState()?.cardBrand.isAmericanExpress()) cvvHint else cvcHint

    private fun getBorderColor(vararg state: ViewState?): Int = when {
        state.any { it?.hasFocus == true } -> focusedBorderColor
        state.any { it?.isValid == false && it.isDirty } -> errorBorderColor
        else -> defaultBorderColor
    }

    private fun isInputValid(
        vararg state: InputViewStateHolder? = arrayOf(
            cardHolderStateHolder,
            cardNumberStateHolder,
            dateStateHolder,
            cvcStateHolder
        )
    ): Boolean = state.all { it?.getState()?.isValid == true }

    private fun ViewState.shouldValidate() = hasFocusedBefore && isDirty
}

internal interface OnPayClickListener {

    fun onPayClicked()
}