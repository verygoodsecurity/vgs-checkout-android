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
import com.verygoodsecurity.vgscheckout.util.ObservableHashMap
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.view.checkout.adapter.CardIconAdapter
import com.verygoodsecurity.vgscheckout.view.checkout.adapter.CardMaskAdapter
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.model.state.FieldState.*
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener

class CheckoutView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), OnFieldStateChangeListener {

    internal var onPayListener: OnPayClickListener? = null

    private val binding = CheckoutLayoutBinding.inflate(LayoutInflater.from(context), this)

    //region Resources
    private val defaultStrokeColor by lazy { getColor(R.color.vgs_checkout_stroke_default) }
    private val highlightedStrokeColor by lazy { getColor(R.color.vgs_checkout_stroke_highlighted) }
    private val errorStrokeColor by lazy { getColor(R.color.vgs_checkout_stroke_error) }
    private val defaultStrokeWidth by lazy { resources.getDimensionPixelSize(R.dimen.stoke_width) }

    private val cvcHint by lazy { getString(R.string.vgs_checkout_card_cvc_hint) }
    private val cvvHint by lazy { getString(R.string.vgs_checkout_card_cvv_hint) }
    //endregion

    private val errorMessages = object : ObservableHashMap<Int, String?>() {

        override fun onChanged(map: HashMap<Int, String?>) {
            binding.tvCardDetailsError.showWithText(map.firstNotNullOfOrNull { it.value })
        }
    }

    init {
        initListeners()
    }

    override fun onStateChange(state: FieldState) {
        updatePayButtonState()
        updateErrorMessage()
        when (state) {
            is CardHolderNameState -> handleCardHolderStateChange(state)
            is CardNumberState -> handleCardNumberStateChange(state)
            is CardExpirationDateState -> handleExpirationStateChange(state)
            is CVCState -> handleCvcStateChange(state)
            else -> throw IllegalArgumentException("Not implemented!")
        }
    }

    //region CheckoutView
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

    private fun initListeners() {
        binding.vgsEtCardHolder.setOnFieldStateChangeListener(this)
        binding.vgsEtCardNumber.setOnFieldStateChangeListener(this)
        binding.vgsEtExpirationDate.setOnFieldStateChangeListener(this)
        binding.vgsEtCVC.setOnFieldStateChangeListener(this)
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

    private fun handleCardHolderStateChange(state: CardHolderNameState) {
        binding.llCardHolder.applyStokeColor(defaultStrokeWidth, getStrokeColor(state))
    }

    private fun handleCardNumberStateChange(state: CardNumberState) {
        updateSecurityCodeHint(state.cardBrand)
        // TODO: Implement
    }

    private fun handleExpirationStateChange(state: CardExpirationDateState) {
        // TODO: Implement
    }

    private fun handleCvcStateChange(state: CVCState) {
        // TODO: Implement
    }

    private fun updatePayButtonState() {
        binding.mbPay.isEnabled = isFieldsValid()
    }

    private fun updateErrorMessage() {
        binding.vgsEtCardHolder.getState()?.let {
            errorMessages[binding.tvCardDetailsError.id] =
                if (!it.isValid && it.isDirty) "Card holder name is empty!" else null
        }
    }

    private fun updateSecurityCodeHint(brand: String?) {
        binding.vgsEtCVC.setHint(if (brand.isAmericanExpress()) cvvHint else cvcHint)
    }

    private fun getStrokeColor(vararg state: FieldState?): Int = when {
        state.any { it?.hasFocus == true } -> highlightedStrokeColor
        state.any { it?.isValid == false && it.isDirty } -> errorStrokeColor
        else -> defaultStrokeColor
    }

    private fun isFieldsValid(
        vararg state: FieldState? = arrayOf(
            binding.vgsEtCardHolder.getState(),
            binding.vgsEtCardNumber.getState(),
            binding.vgsEtExpirationDate.getState(),
            binding.vgsEtCVC.getState()
        )
    ): Boolean = state.all { it?.isValid == true }
}

internal interface OnPayClickListener {

    fun onPayClicked()
}