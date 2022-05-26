package com.verygoodsecurity.vgscheckout.collect.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.FieldType
import com.verygoodsecurity.vgscheckout.collect.view.cvc.CVCIconAdapter
import com.verygoodsecurity.vgscheckout.collect.view.internal.CVCInputField
import com.verygoodsecurity.vgscheckout.util.extension.getColor

internal class CardVerificationCodeEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InputFieldView(context, attrs, defStyleAttr) {

    init {
        setupViewType(FieldType.CVC)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CardVerificationCodeEditText,
            0, 0
        ).apply {

            try {
                val inputType =
                    getInt(R.styleable.CardVerificationCodeEditText_inputType, EditorInfo.TYPE_NULL)
                val fieldName = getString(R.styleable.CardVerificationCodeEditText_fieldName)
                val hint = getString(R.styleable.CardVerificationCodeEditText_hint)
                val textSize = getDimension(R.styleable.CardVerificationCodeEditText_textSize, -1f)
                val textColor = getColor(R.styleable.CardVerificationCodeEditText_textColor)
                val text = getString(R.styleable.CardVerificationCodeEditText_text)
                val textStyle = getInt(R.styleable.CardVerificationCodeEditText_textStyle, -1)
                val cursorVisible =
                    getBoolean(R.styleable.CardVerificationCodeEditText_cursorVisible, true)
                val enabled = getBoolean(R.styleable.CardVerificationCodeEditText_enabled, true)
                val isRequired =
                    getBoolean(R.styleable.CardVerificationCodeEditText_isRequired, true)
                val singleLine =
                    getBoolean(R.styleable.CardVerificationCodeEditText_singleLine, true)
                val scrollHorizontally =
                    getBoolean(R.styleable.CardVerificationCodeEditText_scrollHorizontally, true)
                val gravity = getInt(
                    R.styleable.CardVerificationCodeEditText_gravity,
                    Gravity.START or Gravity.CENTER_VERTICAL
                )
                val ellipsize = getInt(R.styleable.CardVerificationCodeEditText_ellipsize, 0)

                val minLines = getInt(R.styleable.CardVerificationCodeEditText_minLines, 0)
                val maxLines = getInt(R.styleable.CardVerificationCodeEditText_maxLines, 0)
                val previewCardVisibility = getInt(
                    R.styleable.CardVerificationCodeEditText_previewIconVisibility,
                    CVCInputField.PreviewIconVisibility.NEVER.ordinal
                )
                val previewCardGravity = getInt(
                    R.styleable.CardVerificationCodeEditText_previewIconGravity,
                    CVCInputField.PreviewIconGravity.END.ordinal
                )

                setFieldName(fieldName)
                setHint(hint)
                textColor?.let { setTextColor(it) }
                setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                setCursorVisible(cursorVisible)
                setGravity(gravity)
                canScrollHorizontally(scrollHorizontally)
                setEllipsize(ellipsize)
                setMaxLines(maxLines)
                setMinLines(minLines)
                setSingleLine(singleLine)
                setIsRequired(isRequired)
                getTypeface()?.let {
                    setTypeface(it, textStyle)
                }

                setText(text)
                setEnabled(enabled)
                setInputType(inputType)

                applyPreviewIconMode(previewCardVisibility)
                applyPreviewIconGravity(previewCardGravity)
            } finally {
                recycle()
            }
        }
    }

    /**
     * It return current state of the field.
     *
     * @return current state.
     */
    fun getState(): VGSFieldState {
        return getInnerState()
    }

    /**
     * Sets the custom icons for cvc.
     *
     * @param adapter The adapter is responsible for maintaining the icons backing this view and
     * for producing a drawable for preview.
     */
    fun setPreviewIconAdapter(adapter: CVCIconAdapter?) {
        setCVCPreviewIconAdapter(adapter)
    }

    fun setPreviewIconMode(mode: CVCInputField.PreviewIconVisibility) {
        applyPreviewIconMode(mode.ordinal)
    }
}