package com.verygoodsecurity.vgscheckout.view.checkout.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldState.*
import com.verygoodsecurity.vgscheckout.collect.view.internal.CVCInputField
import com.verygoodsecurity.vgscheckout.collect.view.internal.CardInputField
import com.verygoodsecurity.vgscheckout.collect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscheckout.collect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscheckout.collect.widget.PersonNameEditText
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscheckout.config.ui.view.card.VGSCheckoutCardOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardholder.VGSCheckoutCardHolderOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.VGSCheckoutCardNumberOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cvc.VGSCheckoutCVCOptions
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.VGSCheckoutExpirationDateOptions
import com.verygoodsecurity.vgscheckout.util.ObservableLinkedHashMap
import com.verygoodsecurity.vgscheckout.util.extension.*
import com.verygoodsecurity.vgscheckout.view.checkout.card.adapter.CardIconAdapter
import com.verygoodsecurity.vgscheckout.view.checkout.card.adapter.CardMaskAdapter
import com.verygoodsecurity.vgscheckout.view.checkout.core.InputViewHolder
import com.verygoodsecurity.vgscheckout.view.checkout.core.OnInputViewStateChangedListener
import com.verygoodsecurity.vgscheckout.view.checkout.core.OnStateChangeListener
import com.verygoodsecurity.vgscheckout.view.checkout.core.ViewState

// TODO: Add ability to set color state list for collect input views text color.
// TODO: Add ability to icon tint color for collect input view.
internal class CreditCardView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), OnInputViewStateChangedListener {

    var onStateChangeListener: OnStateChangeListener? = null

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
    private val tvError: MaterialTextView
    private val vDividerHorizontal: View
    private val vDividerVertical: View

    private val cardHolderHolder: InputViewHolder
    private val cardNumberHolder: InputViewHolder
    private val dateHolder: InputViewHolder
    private val cvcHolder: InputViewHolder
    private val inputViewsHolders: Array<InputViewHolder>

    private val defaultBorderColor by lazy { getColor(R.color.vgs_checkout_border_default) }
    private val focusedBorderColor by lazy { getColor(R.color.vgs_checkout_border_highlighted) }
    private val errorBorderColor by lazy { getColor(R.color.vgs_checkout_border_error) }
    private val defaultBorderWidth by lazy { resources.getDimensionPixelSize(R.dimen.vgs_checkout_default_stoke_width) }
    private val errorDrawable by lazy { getDrawable(R.drawable.vgs_checkout_ic_error_white_10dp) }
    private val cvcHint by lazy { getString(R.string.vgs_checkout_card_verification_code_hint) }
    private val cvvHint by lazy { getString(R.string.vgs_checkout_card_verification_value_hint) }

    init {
        LayoutInflater.from(context).inflate(R.layout.vgs_checkout_credit_card_view, this)
        orientation = VERTICAL

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
        tvError = findViewById(R.id.tvCardDetailsError)
        vDividerHorizontal = findViewById(R.id.viewDividerHorizontal)
        vDividerVertical = findViewById(R.id.viewDividerVertical)

        cardHolderHolder = InputViewHolder(etCardHolder, this)
        cardNumberHolder = InputViewHolder(etCardNumber, this)
        dateHolder = InputViewHolder(etExpirationDate, this)
        cvcHolder = InputViewHolder(etSecurityCode, this)
        inputViewsHolders =  arrayOf(
            cardHolderHolder,
            cardNumberHolder,
            dateHolder,
            cvcHolder
        )
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        llCardHolder.setEnabled(enabled, true)
        clCardDetails.setEnabled(enabled, true)
    }

    override fun onStateChange(id: Int, state: ViewState) {
        onStateChangeListener?.onStateChanged(this, isValid())
        when (id) {
            R.id.vgsEtCardHolder -> handleCardHolderStateChanged(state)
            R.id.vgsEtCardNumber -> handleCardNumberStateChanged(state)
            R.id.vgsEtDate -> handleDateStateChanged(state)
            R.id.vgsEtCVC -> handleCVCStateChanged(state)
        }
    }

    fun applyConfig(options: VGSCheckoutCardOptions) {
        applyCardHolderOptions(options.cardHolderOptions)
        applyCardNumberOptions(options.cardNumberOptions)
        applyExpirationDateOptions(options.expirationDateOptions)
        applySecurityCodeOptions(options.cvcOptions)
    }

    fun getVGSViews() = arrayOf(
        etCardHolder,
        etCardNumber,
        etExpirationDate,
        etSecurityCode
    )

    fun isValid() = inputViewsHolders.all { it.state.isValid }

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

    private fun applyCardHolderOptions(options: VGSCheckoutCardHolderOptions) {
        etCardHolder.setFieldName(options.fieldName)
        llCardHolder.setVisibility(options.visibility)
    }

    private fun applyCardNumberOptions(options: VGSCheckoutCardNumberOptions) {
        etCardNumber.setFieldName(options.fieldName)
        etCardNumber.setCardBrandPreviewIconMode(
            if (options.isIconHidden) {
                CardInputField.PreviewIconMode.NEVER
            } else {
                CardInputField.PreviewIconMode.ALWAYS
            }
        )
        etCardNumber.setValidCardBrands(*options.cardBrands.map { it.toCollectCardBrand() }
            .toTypedArray())
        etCardNumber.setCardMaskAdapter(CardMaskAdapter(options.cardBrands))
        etCardNumber.setCardIconAdapter(
            CardIconAdapter(
                this@CreditCardView.context,
                options.cardBrands
            )
        )
    }

    private fun applyExpirationDateOptions(options: VGSCheckoutExpirationDateOptions) {
        etExpirationDate.setFieldName(options.fieldName)
    }

    private fun applySecurityCodeOptions(options: VGSCheckoutCVCOptions) {
        etSecurityCode.setFieldName(options.fieldName)
        etSecurityCode.setPreviewIconMode(
            if (options.isIconHidden) {
                CVCInputField.PreviewIconVisibility.NEVER
            } else {
                CVCInputField.PreviewIconVisibility.ALWAYS
            }
        )
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

    private fun updateSecurityCodeHint() {
        etSecurityCode.setHint(getCVCHint())
    }

    private fun updateCardDetailsBorderColor() {
        with(
            getBorderColor(
                cardNumberHolder.state,
                dateHolder.state,
                cvcHolder.state
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
}