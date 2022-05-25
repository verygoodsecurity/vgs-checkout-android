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
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.PersonNameRule
import com.verygoodsecurity.vgscheckout.util.extension.getColor

internal class PersonNameEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InputFieldView(context, attrs, defStyleAttr) {

    init {
        setupViewType(FieldType.CARD_HOLDER_NAME)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PersonNameEditText,
            0, 0
        ).apply {

            try {
                val inputType =
                    getInt(R.styleable.PersonNameEditText_inputType, EditorInfo.TYPE_NULL)
                val fieldName = getString(R.styleable.PersonNameEditText_fieldName)
                val hint = getString(R.styleable.PersonNameEditText_hint)
                val textSize = getDimension(R.styleable.PersonNameEditText_textSize, -1f)
                val textColor = getColor(R.styleable.PersonNameEditText_textColor)
                val text = getString(R.styleable.PersonNameEditText_text)
                val textStyle = getInt(R.styleable.PersonNameEditText_textStyle, -1)
                val cursorVisible = getBoolean(R.styleable.PersonNameEditText_cursorVisible, true)
                val enabled = getBoolean(R.styleable.PersonNameEditText_enabled, true)
                val isRequired = getBoolean(R.styleable.PersonNameEditText_isRequired, true)
                val singleLine = getBoolean(R.styleable.PersonNameEditText_singleLine, true)
                val scrollHorizontally =
                    getBoolean(R.styleable.PersonNameEditText_scrollHorizontally, true)
                val gravity = getInt(
                    R.styleable.PersonNameEditText_gravity,
                    Gravity.START or Gravity.CENTER_VERTICAL
                )
                val ellipsize = getInt(R.styleable.PersonNameEditText_ellipsize, 0)

                val minLines = getInt(R.styleable.PersonNameEditText_minLines, 0)
                val maxLines = getInt(R.styleable.PersonNameEditText_maxLines, 0)

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
                setTypeface(getTypeface(), textStyle)

                setText(text)
                setEnabled(enabled)

                setInputType(inputType)
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
     * Adds a validation rule for the field.
     */
    fun addRule(rule: PersonNameRule) {
        applyValidationRule(rule)
    }
}