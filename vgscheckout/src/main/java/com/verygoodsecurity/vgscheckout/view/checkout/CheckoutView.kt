package com.verygoodsecurity.vgscheckout.view.checkout

import android.content.Context
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.config.ui.core.CheckoutFormConfiguration
import com.verygoodsecurity.vgscheckout.databinding.CheckoutLayoutBinding
import com.verygoodsecurity.vgscheckout.util.ObservableLinkedHashMap
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
) : FrameLayout(context, attrs, defStyleAttr), View.OnFocusChangeListener,
    OnFieldStateChangeListener {

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

    // TODO: Check & handle order
    private val errorMessages = object : ObservableLinkedHashMap<Int, String?>() {

        override fun onChanged(map: HashMap<Int, String?>) {
            binding.tvCardDetailsError.showWithText(map.firstNotNullOfOrNull { it.value })
        }
    }

    // TODO: Remove this crutch as fast as you can
    private var cardHolderValidationEnabled: Boolean = false
    private var cardNumberValidationEnabled: Boolean = false
    private var expirationDateValidationEnabled: Boolean = false
    private var cvcValidationEnabled: Boolean = false

    init {
        initListeners()
    }

    // TODO: Remove this crutch as fast as you can
    // TODO: Add check isDirty
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v?.id) {
            R.id.vgsEtCardHolder -> if (!hasFocus) cardHolderValidationEnabled = true
            R.id.vgsEtCardNumber -> if (!hasFocus) cardNumberValidationEnabled = true
            R.id.vgsEtExpirationDate -> if (!hasFocus) expirationDateValidationEnabled = true
            R.id.vgsEtCVC -> if (!hasFocus) cvcValidationEnabled = true
        }
    }

    override fun onStateChange(state: FieldState) {
        updatePayButtonState()
        when (state) {
            is CardHolderNameState -> validateCardHolderName(state)
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
        binding.vgsEtCardHolder.onFocusChangeListener = this
        binding.vgsEtCardNumber.onFocusChangeListener = this
        binding.vgsEtExpirationDate.onFocusChangeListener = this
        binding.vgsEtCVC.onFocusChangeListener = this

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

    private fun validateCardHolderName(state: CardHolderNameState) {
        binding.llCardHolder.applyStokeColor(defaultStrokeWidth, getStrokeColor(state))
        if (cardHolderValidationEnabled && state.isDirty) {
            errorMessages[binding.vgsEtCardHolder.id] = when {
                !state.isValid -> getString(R.string.vgs_checkout_card_holder_error)
                else -> null
            }
        }
    }

    private fun handleCardNumberStateChange(state: CardNumberState) {
        // TODO: Implement border color
        updateSecurityCodeHint(state.cardBrand)
        if (cardNumberValidationEnabled && state.isDirty) {
            errorMessages[binding.vgsEtCardNumber.id] = when {
                !state.isEmpty -> getString(R.string.vgs_checkout_card_number_empty_error)
                !state.isValid -> getString(R.string.vgs_checkout_card_number_invalid_error)
                else -> null
            }
        }
    }

    private fun handleExpirationStateChange(state: CardExpirationDateState) {
        // TODO: Implement border color
        if (expirationDateValidationEnabled && state.isDirty) {
            errorMessages[binding.vgsEtExpirationDate.id] = when {
                !state.isEmpty -> getString(R.string.vgs_checkout_card_date_empty_error)
                !state.isValid -> getString(R.string.vgs_checkout_card_date_invalid_error)
                else -> null
            }
        }
    }

    private fun handleCvcStateChange(state: CVCState) {
        // TODO: Implement border color
        if (cvcValidationEnabled && state.isDirty) {
            errorMessages[binding.vgsEtCVC.id] = when {
                !state.isEmpty -> getString(R.string.vgs_checkout_card_cvc_empty_error)
                !state.isValid -> getString(R.string.vgs_checkout_card_cvc_invalid_error)
                else -> null
            }
        }
    }

    private fun updatePayButtonState() {
        binding.mbPay.isEnabled = isFieldsValid()
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