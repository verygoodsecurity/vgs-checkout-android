package com.verygoodsecurity.vgscheckout.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.ui.VGSCheckoutVaultFormConfiguration
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscollect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscollect.widget.PersonNameEditText
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText

class CheckoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnFocusChangeListener {

    private val cardHolderLL: LinearLayout by lazy { findViewById(R.id.llCardHolder) }
    private val cardHolderEt: PersonNameEditText by lazy { findViewById(R.id.vgsEtCardHolder) }

    private val cardNumberLL: LinearLayout by lazy { findViewById(R.id.llCardNumber) }
    private val cardNumberEt: VGSCardNumberEditText by lazy { findViewById(R.id.vgsEtCardNumber) }

    private val expireDateLL: LinearLayout by lazy { findViewById(R.id.llExpirationDate) }
    private val expireDateEt: ExpirationDateEditText by lazy { findViewById(R.id.vgsEtExpirationDate) }

    private val cvcLL: LinearLayout by lazy { findViewById(R.id.llCVC) }
    private val cvcEt: CardVerificationCodeEditText by lazy { findViewById(R.id.vgsEtCVC) }

    private val defaultStrokeWidth by lazy { resources.getDimensionPixelSize(R.dimen.stoke_width) }

    // Stroke colors
    private val defaultStrokeColor by lazy {
        ContextCompat.getColor(context, R.color.vgs_stroke_default_color)
    }
    private val highlightedStrokeColor by lazy {
        ContextCompat.getColor(context, R.color.vgs_stroke_highlighted_color)
    }
    private val errorStrokeColor by lazy {
        ContextCompat.getColor(context, R.color.vgs_stroke_error_color)
    }

    init {
        inflate(context, R.layout.checkout_layout, this)
        initListeners()
    }

    private fun initListeners() {
        cardHolderEt.onFocusChangeListener = this
        cardNumberEt.onFocusChangeListener = this
        expireDateEt.onFocusChangeListener = this
        cvcEt.onFocusChangeListener = this
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        val color = if (hasFocus) highlightedStrokeColor else Color.TRANSPARENT
        when (v?.id) {
            R.id.vgsEtCardHolder -> applyStokeColor(
                cardHolderLL,
                if (hasFocus) highlightedStrokeColor else defaultStrokeColor
            )
            R.id.vgsEtCardNumber -> applyStokeColor(cardNumberLL, color)
            R.id.vgsEtExpirationDate -> applyStokeColor(expireDateLL, color)
            R.id.vgsEtCVC -> applyStokeColor(cvcLL, color)
        }
    }

    fun applyConfig(config: VGSCheckoutVaultFormConfiguration) {

    }

    fun bindViews(collect: VGSCollect) {
        collect.bindView(cardHolderEt)
        collect.bindView(cardNumberEt)
        collect.bindView(expireDateEt)
        collect.bindView(cvcEt)
    }

    private fun applyStokeColor(target: View, color: Int) {
        (target.background as? GradientDrawable)?.setStroke(defaultStrokeWidth, color)
    }
}