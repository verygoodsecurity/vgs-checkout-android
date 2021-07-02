package com.verygoodsecurity.vgscheckout.view.checkout

import android.content.Context
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.databinding.CheckoutLayoutBinding
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

    private val binding = CheckoutLayoutBinding.inflate(LayoutInflater.from(context), this)
    private val cardHolderStateHolder = InputViewStateHolder(binding.vgsEtCardHolder, this)
    private val cardNumberStateHolder = InputViewStateHolder(binding.vgsEtCardNumber, this)
    private val expirationStateHolder = InputViewStateHolder(binding.vgsEtExpirationDate, this)
    private val cvcStateHolder = InputViewStateHolder(binding.vgsEtCVC, this)

    private val errorMessages: ObservableLinkedHashMap<Int, String?> = initErrorMessages()

    //region Resources
    private val defaultBorderColor by lazy { getColor(R.color.vgs_checkout_stroke_default) }
    private val focusedBorderColor by lazy { getColor(R.color.vgs_checkout_stroke_highlighted) }
    private val errorSBorderColor by lazy { getColor(R.color.vgs_checkout_stroke_error) }
    private val defaultBorderWidth by lazy { resources.getDimensionPixelSize(R.dimen.stoke_width) }
    private val cvcHint by lazy { getString(R.string.vgs_checkout_card_cvc_hint) }
    private val cvvHint by lazy { getString(R.string.vgs_checkout_card_cvv_hint) }
    //endregion

    init {
        initListeners()
    }

    override fun onStateChange(id: Int, state: ViewState) {
        when (id) {
            R.id.vgsEtCardHolder -> handleCardHolderStateChanged(state)
            R.id.vgsEtCardNumber -> handleCardNumberStateChanged(state)
            R.id.vgsEtExpirationDate -> handleExpirationStateChanged(state)
            R.id.vgsEtCVC -> handleCVCStateChanged(state)
        }
    }

    //region Public API TODO: Refactor
    fun applyConfig(config: CheckoutFormConfiguration) {
        // Apply pay button title
        config.payButtonTitle?.let { binding.mbPay.text = it }

        if (config is VGSCheckoutFormConfiguration) {
            // Apply card holder config
            binding.vgsEtCardNumber.setFieldName(config.cardHolderOptions.fieldName)
            binding.llCardHolder.setVisibility(config.cardHolderOptions.visibility)

            // Apply card number config
            with(config.cardNumberOptions) {
                binding.vgsEtCardNumber.setFieldName(fieldName)
                binding.vgsEtCardNumber.setValidCardBrands(*cardBrands.map { it.toCollectCardBrand() }
                    .toTypedArray())
                binding.vgsEtCardNumber.setCardMaskAdapter(CardMaskAdapter(cardBrands))
                binding.vgsEtCardNumber.setCardIconAdapter(
                    CardIconAdapter(
                        this@CheckoutView.context,
                        cardBrands
                    )
                )
            }

            // Apply expiration date config
            binding.vgsEtExpirationDate.setFieldName(config.expirationDateOptions.fieldName)

            // Apply cvc config
            binding.vgsEtCVC.setFieldName(config.cvcOptions.fieldName)
        }
    }

    fun getCollectViews() = arrayOf(
        binding.vgsEtCardHolder,
        binding.vgsEtCardNumber,
        binding.vgsEtExpirationDate,
        binding.vgsEtCVC
    )
    //endregion

    private fun initErrorMessages(): ObservableLinkedHashMap<Int, String?> {
        val defaultMessages = linkedMapOf<Int, String?>(
            R.id.vgsEtCardHolder to null,
            R.id.vgsEtCardNumber to null,
            R.id.vgsEtExpirationDate to null,
            R.id.vgsEtCVC to null
        )
        return object : ObservableLinkedHashMap<Int, String?>(defaultMessages) {

            override fun onChanged(map: ObservableLinkedHashMap<Int, String?>) {
                binding.tvCardDetailsError.showWithText(map.firstNotNullOfOrNull { it.value })
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
        binding.mbPay.icon = getDrawable(R.drawable.animated_ic_progress_circle_white_16dp)
        (binding.mbPay.icon as Animatable).start()
        onPayListener?.onPayClicked()
    }

    private fun handleCardHolderStateChanged(state: ViewState) {
        binding.llCardHolder.applyStokeColor(defaultBorderWidth, getBorderColor(state))
        if (state.shouldValidate()) {
            errorMessages[R.id.vgsEtCardHolder] = when {
                state.isEmpty -> getString(R.string.vgs_checkout_card_holder_error)
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

    private fun handleExpirationStateChanged(state: ViewState) {
        updateCardDetailsBorderColor()
        if (state.shouldValidate()) {
            errorMessages[R.id.vgsEtExpirationDate] = when {
                state.isEmpty -> getString(R.string.vgs_checkout_card_date_empty_error)
                !state.isValid -> getString(R.string.vgs_checkout_card_date_invalid_error)
                else -> null
            }
        }
    }

    private fun handleCVCStateChanged(state: ViewState) {
        updateCardDetailsBorderColor()
        if (state.shouldValidate()) {
            errorMessages[R.id.vgsEtCVC] = when {
                state.isEmpty -> getString(R.string.vgs_checkout_card_cvc_empty_error)
                !state.isValid -> getString(R.string.vgs_checkout_card_cvc_invalid_error)
                else -> null
            }
        }
    }

    private fun updateSecurityCodeHint() {
        (if (binding.vgsEtCardNumber.getState()?.cardBrand.isAmericanExpress()) cvvHint else cvcHint).run {
            binding.vgsEtCVC.setHint(this)
        }
    }

    private fun updateCardDetailsBorderColor() {
        with(
            getBorderColor(
                cardNumberStateHolder.getState(),
                expirationStateHolder.getState(),
                cvcStateHolder.getState()
            )
        ) {
            binding.clCardDetails.applyStokeColor(defaultBorderWidth, this)
            binding.viewDividerHorizontal.setBackgroundColor(this)
            binding.viewDividerVertical.setBackgroundColor(this)
        }
    }

    private fun getBorderColor(vararg state: ViewState?): Int = when {
        state.any { it?.hasFocus == true } -> focusedBorderColor
        state.any { it?.isValid == false && it.isDirty } -> errorSBorderColor
        else -> defaultBorderColor
    }

    private fun ViewState.shouldValidate() = hasFocusedBefore && isDirty
}

internal interface OnPayClickListener {

    fun onPayClicked()
}