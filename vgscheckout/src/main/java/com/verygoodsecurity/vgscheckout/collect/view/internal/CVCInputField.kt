package com.verygoodsecurity.vgscheckout.collect.view.internal

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldContent
import com.verygoodsecurity.vgscheckout.collect.view.Dependency
import com.verygoodsecurity.vgscheckout.collect.view.card.CardType
import com.verygoodsecurity.vgscheckout.collect.view.card.FieldType
import com.verygoodsecurity.vgscheckout.collect.view.card.conection.InputCardCVCConnection
import com.verygoodsecurity.vgscheckout.collect.view.card.text.CVCValidateFilter
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.CardCVCCodeValidator
import com.verygoodsecurity.vgscheckout.collect.view.cvc.CVCIconAdapter
import com.verygoodsecurity.vgscheckout.collect.view.internal.CVCInputField.PreviewIconGravity.END
import com.verygoodsecurity.vgscheckout.collect.view.internal.CVCInputField.PreviewIconGravity.START
import com.verygoodsecurity.vgscheckout.collect.view.internal.CVCInputField.PreviewIconVisibility.*

/** @suppress */
internal class CVCInputField(
    context: Context,
    attributeSet: AttributeSet? = null
) : BaseInputField(context, attributeSet) {

    override var fieldType: FieldType = FieldType.CVC
    private var cardContent: FieldContent.CardNumberContent = FieldContent.CardNumberContent()

    private var iconAdapter = CVCIconAdapter(context)

    private var previewIconVisibility = NEVER
    private var previewIconGravity = END

    init {
        validator.addRule(CardCVCCodeValidator(cardContent.rangeCVV))
    }

    override fun applyFieldType() {
        inputConnection = InputCardCVCConnection(id, validator)

        val str = text.toString()
        val stateContent = FieldContent.InfoContent().apply {
            this.data = str
        }
        val state = collectCurrentState(stateContent)

        inputConnection?.setOutput(state)

        applyLengthFilter(cardContent.rangeCVV.last())
        applyInputType()
    }

    private fun applyInputType() {
        if (!isValidInputType(inputType)) {
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        refreshInput()
    }

    private fun isValidInputType(type: Int): Boolean {
        return type == InputType.TYPE_CLASS_NUMBER ||
                type == InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
    }

    override fun setInputType(type: Int) {
        val validType = validateInputType(type)
        super.setInputType(validType)
        refreshInput()
    }

    private fun validateInputType(type: Int): Int {
        return when (type) {
            InputType.TYPE_CLASS_NUMBER -> type
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_NUMBER_VARIATION_PASSWORD -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            InputType.TYPE_NUMBER_VARIATION_PASSWORD -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD -> type
            else -> InputType.TYPE_CLASS_NUMBER
        }
    }

    override fun dispatchDependencySetting(dependency: Dependency) {
        when (dependency.dependencyType) {
            Dependency.DependencyType.CARD -> handleCardDependency(dependency.value as FieldContent.CardNumberContent)
            else -> super.dispatchDependencySetting(dependency)
        }
        inputConnection?.run()
    }

    override fun updateTextChanged(str: String) {
        super.updateTextChanged(str)
        refreshIcon()
    }

    private fun applyLengthFilter(length: Int) {
        val filterLength = InputFilter.LengthFilter(length)
        filters = arrayOf(CVCValidateFilter(), filterLength)
    }

    override fun setupAutofill() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setAutofillHints(View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE)
        }
    }

    internal fun setPreviewIconVisibility(visibility: PreviewIconVisibility) {
        this.previewIconVisibility = visibility
    }

    internal fun setPreviewIconVisibility(mode: Int) {
        this.previewIconVisibility = PreviewIconVisibility.values()[mode]
    }

    internal fun setPreviewIconGravity(gravity: PreviewIconGravity) {
        this.previewIconGravity = gravity
    }

    internal fun setPreviewIconGravity(gravity: Int) {
        this.previewIconGravity = PreviewIconGravity.values()[gravity]
    }

    internal fun setPreviewIconAdapter(adapter: CVCIconAdapter?) {
        this.iconAdapter = adapter ?: CVCIconAdapter(context)
    }

    private fun handleCardDependency(cardContent: FieldContent.CardNumberContent) {
        if (this.cardContent != cardContent) {
            this.cardContent = cardContent
            applyLengthFilter(cardContent.rangeCVV.last())
            (inputConnection as? InputCardCVCConnection)?.defaultValidator =
                CardCVCCodeValidator(cardContent.rangeCVV)
            text = text
            refreshIcon()
        }
    }

    private fun refreshIcon() {
        when (previewIconVisibility) {
            ALWAYS -> setIcon()
            HAS_CONTENT -> if (text.isNullOrEmpty()) removeIcon() else setIcon()
            IF_BRAND_DETECTED -> if (cardContent.cardBrandName == CardType.UNKNOWN.name) removeIcon() else setIcon()
            NEVER -> removeIcon()
        }
    }

    private fun setIcon() {
        val icon = iconAdapter.getItem(
            cardContent.cardtype,
            cardContent.cardBrandName,
            cardContent.rangeCVV.last(),
            localVisibleRect
        )
        when (previewIconGravity) {
            START -> setCompoundDrawablesRelativeOrNull(start = icon)
            END -> setCompoundDrawablesRelativeOrNull(end = icon)
        }
    }

    private fun removeIcon() {
        setCompoundDrawablesRelativeOrNull()
    }

    private fun TextInputEditText.setCompoundDrawablesRelativeOrNull(
        start: Drawable? = null,
        top: Drawable? = null,
        end: Drawable? = null,
        bottom: Drawable? = null
    ) {
        this.setCompoundDrawablesRelative(start, top, end, bottom)
    }

    enum class PreviewIconVisibility {

        ALWAYS,
        HAS_CONTENT,
        IF_BRAND_DETECTED,
        NEVER
    }

    enum class PreviewIconGravity {

        START,
        END
    }
}