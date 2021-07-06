package com.verygoodsecurity.vgscheckout.view.checkout

import android.content.Context
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.databinding.VgsCheckoutViewLayoutBinding
import com.verygoodsecurity.vgscheckout.util.ObservableLinkedHashMap
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.view.checkout.adapter.CardIconAdapter
import com.verygoodsecurity.vgscheckout.view.checkout.adapter.CardMaskAdapter
import com.verygoodsecurity.vgscollect.core.model.state.FieldState.*

internal class CheckoutView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), StateChangeListener {

    internal var onPayListener: OnPayClickListener? = null

    private val binding = VgsCheckoutViewLayoutBinding.inflate(LayoutInflater.from(context), this)
    private val cardHolderStateHolder = InputViewStateHolder(binding.vgsEtCardHolder, this)
    private val cardNumberStateHolder = InputViewStateHolder(binding.vgsEtCardNumber, this)
    private val dateStateHolder = InputViewStateHolder(binding.vgsEtDate, this)
    private val cvcStateHolder = InputViewStateHolder(binding.vgsEtCVC, this)

    private val errorMessages: ObservableLinkedHashMap<Int, String?> = initErrorMessages()

    private val defaultBorderColor by lazy { getColor(R.color.vgs_checkout_border_default) }
    private val focusedBorderColor by lazy { getColor(R.color.vgs_checkout_border_highlighted) }
    private val errorBorderColor by lazy { getColor(R.color.vgs_checkout_border_error) }
    private val defaultBorderWidth by lazy { resources.getDimensionPixelSize(R.dimen.vgs_checkout_default_stoke_width) }
    private val errorDrawable by lazy { getDrawable(R.drawable.vgs_checkout_ic_error_white_10dp) }
    private val cvcHint by lazy { getString(R.string.vgs_checkout_card_verification_code_hint) }
    private val cvvHint by lazy { getString(R.string.vgs_checkout_card_verification_value_hint) }

    init {
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
        binding.vgsEtCardNumber.setFieldName(config.cardHolderOptions.fieldName)
        binding.llCardHolder.setVisibility(config.cardHolderOptions.visibility)
        binding.vgsEtCardNumber.setFieldName(config.cardNumberOptions.fieldName)
        binding.vgsEtCardNumber.setValidCardBrands(
            *config.cardNumberOptions.cardBrands.map { it.toCollectCardBrand() }.toTypedArray()
        )
        binding.vgsEtCardNumber.setCardMaskAdapter(CardMaskAdapter(config.cardNumberOptions.cardBrands))
        binding.vgsEtCardNumber.setCardIconAdapter(
            CardIconAdapter(this@CheckoutView.context, config.cardNumberOptions.cardBrands)
        )
        binding.vgsEtDate.setFieldName(config.expirationDateOptions.fieldName)
        binding.vgsEtCVC.setFieldName(config.cvcOptions.fieldName)
        config.payButtonTitle?.let { binding.mbPay.text = it }
    }

    fun getCollectViews() = arrayOf(
        binding.vgsEtCardHolder,
        binding.vgsEtCardNumber,
        binding.vgsEtDate,
        binding.vgsEtCVC
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
                binding.tvCardDetailsError.showWithText(map.firstNotNullOfOrNull { it.value })
                map.forEach {
                    val drawable = if (it.value == null) null else errorDrawable
                    when (it.key) {
                        R.id.vgsEtCardHolder -> binding.mtvCardHolderHint.setDrawableEnd(drawable)
                        R.id.vgsEtCardNumber -> binding.mtvCardNumberHint.setDrawableEnd(drawable)
                        R.id.vgsEtDate -> binding.mtvDateHint.setDrawableEnd(drawable)
                        R.id.vgsEtCVC -> binding.mtvCVCHint.setDrawableEnd(drawable)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.mbPay.setOnClickListener { handlePayClicked() }
    }

    private fun handlePayClicked() {
        binding.llCardHolder.disable()
        binding.clCardDetails.disable()
        binding.mbPay.isClickable = false
        binding.mbPay.text = getString(R.string.vgs_checkout_pay_button_processing_title)
        binding.mbPay.icon = getDrawable(R.drawable.vgs_checkout_animated_ic_progress_circle_white_16dp)
        (binding.mbPay.icon as Animatable).start()
        onPayListener?.onPayClicked()
    }

    private fun handleCardHolderStateChanged(state: ViewState) {
        binding.llCardHolder.applyStokeColor(defaultBorderWidth, getBorderColor(state))
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
        binding.mbPay.isEnabled = isInputValid()
    }

    private fun updateSecurityCodeHint() {
        binding.vgsEtCVC.setHint(getCVCHint())
    }

    private fun updateCardDetailsBorderColor() {
        with(
            getBorderColor(
                cardNumberStateHolder.getState(),
                dateStateHolder.getState(),
                cvcStateHolder.getState()
            )
        ) {
            binding.clCardDetails.applyStokeColor(defaultBorderWidth, this)
            binding.viewDividerHorizontal.setBackgroundColor(this)
            binding.viewDividerVertical.setBackgroundColor(this)
        }
    }

    private fun getCVCHint() =
        if (binding.vgsEtCardNumber.getState()?.cardBrand.isAmericanExpress()) cvvHint else cvcHint

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