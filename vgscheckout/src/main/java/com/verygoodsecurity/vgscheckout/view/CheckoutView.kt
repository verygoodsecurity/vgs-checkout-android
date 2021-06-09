package com.verygoodsecurity.vgscheckout.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutVaultFormConfiguration
import com.verygoodsecurity.vgscheckout.util.extension.applyStokeColor
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

    private val defaultStrokeWidth by lazy { resources.getDimensionPixelSize(R.dimen.stoke_width) }

    // Stroke colors
    private val defaultStrokeColor by lazy {
        ContextCompat.getColor(context, R.color.vgs_checkout_stroke_default_color)
    }
    private val highlightedStrokeColor by lazy {
        ContextCompat.getColor(context, R.color.vgs_checkout_stroke_highlighted_color)
    }
    private val errorStrokeColor by lazy {
        ContextCompat.getColor(context, R.color.vgs_checkout_stroke_error_color)
    }

    init {
        inflate(context, R.layout.checkout_layout, this)
        initListeners()
    }

    private fun initListeners() {
        cardHolderEt.setOnFieldStateChangeListener(this)
        cardNumberEt.setOnFieldStateChangeListener(this)
        expireDateEt.setOnFieldStateChangeListener(this)
        cvcEt.setOnFieldStateChangeListener(this)
    }

    override fun onStateChange(state: FieldState) {
        when (state) {
            is CardHolderNameState -> handleCardHolderStateChange(state)
            else -> handleCardDetailsStateChanged()
        }
    }

    fun applyConfig(@Suppress("UNUSED_PARAMETER") config: VGSCheckoutVaultFormConfiguration) {

    }

    fun bindViews(collect: VGSCollect) {
        collect.bindView(cardHolderEt)
        collect.bindView(cardNumberEt)
        collect.bindView(expireDateEt)
        collect.bindView(cvcEt)
    }

    private fun handleCardHolderStateChange(state: CardHolderNameState) {
        cardHolderLL.applyStokeColor(defaultStrokeWidth, getStrokeColor(state))
    }

    private fun handleCardDetailsStateChanged() {
        updateCardDetailsBorderColor()
    }

    private fun updateCardDetailsBorderColor() {
        with(getStrokeColor(cardNumberEt.getState(), expireDateEt.getState(), cvcEt.getState())) {
            cardDetailsCL.applyStokeColor(defaultStrokeWidth, this)
            dividerHorizontal.setBackgroundColor(this)
            dividerVertical.setBackgroundColor(this)
        }
    }

    private fun getStrokeColor(vararg state: FieldState?): Int = when {
        state.any { it?.isValid == false && !it.isEmpty && !it.hasFocus } -> errorStrokeColor
        state.any { it?.hasFocus == true } -> highlightedStrokeColor
        else -> defaultStrokeColor
    }
}