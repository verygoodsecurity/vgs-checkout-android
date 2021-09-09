package com.verygoodsecurity.vgscheckout.collect.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldState
import android.view.inputmethod.EditorInfo
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.FieldType
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule

/**
 * A user interface element that displays text.
 *
 * @since 1.0.0
 */
internal open class VGSEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InputFieldView(context, attrs, defStyleAttr) {

    init {
        setupViewType(FieldType.INFO)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.VGSEditText,
            0, 0
        ).apply {

            try {
                val inputType = getInt(R.styleable.VGSEditText_inputType, EditorInfo.TYPE_NULL)
                val fieldName = getString(R.styleable.VGSEditText_fieldName)
                val hint = getString(R.styleable.VGSEditText_hint)
                val textSize = getDimension(R.styleable.VGSEditText_textSize, -1f)
                val textColor = getColor(R.styleable.VGSEditText_textColor, -1)
                val text = getString(R.styleable.VGSEditText_text)
                val textStyle = getInt(R.styleable.VGSEditText_textStyle, -1)
                val cursorVisible = getBoolean(R.styleable.VGSEditText_cursorVisible, true)
                val enabled = getBoolean(R.styleable.VGSEditText_enabled, true)
                val isRequired = getBoolean(R.styleable.VGSEditText_isRequired, true)
                val singleLine = getBoolean(R.styleable.VGSEditText_singleLine, true)
                val scrollHorizontally = getBoolean(R.styleable.VGSEditText_scrollHorizontally, true)
                val gravity = getInt(R.styleable.VGSEditText_gravity, Gravity.START or Gravity.CENTER_VERTICAL)
                val ellipsize = getInt(R.styleable.VGSEditText_ellipsize, 0)

                val minLines = getInt(R.styleable.VGSEditText_minLines, 0)
                val maxLines = getInt(R.styleable.VGSEditText_maxLines, 0)

                setFieldName(fieldName)
                setHint(hint)
                if (textColor > 0) setTextColor(textColor)
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
    fun getState(): FieldState.InfoState? {
        return getInfoState()
    }

    /** The max text length to display. */
    fun setMaxLength(length: Int) {
        applyMaxLength(length)
    }

    /**
     * Adds a validation rule for the field.
     */
    fun addRule(rule: VGSInfoRule) {
        applyValidationRule(rule)
    }
}