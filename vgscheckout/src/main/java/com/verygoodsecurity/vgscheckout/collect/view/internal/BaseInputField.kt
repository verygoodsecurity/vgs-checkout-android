package com.verygoodsecurity.vgscheckout.collect.view.internal

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.autofill.AutofillValue
import android.view.inputmethod.EditorInfo
import androidx.annotation.VisibleForTesting
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.analytic.AnalyticTracker
import com.verygoodsecurity.vgscheckout.analytic.event.AutofillEvent
import com.verygoodsecurity.vgscheckout.collect.core.model.state.*
import com.verygoodsecurity.vgscheckout.collect.view.Dependency
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.FieldType
import com.verygoodsecurity.vgscheckout.collect.view.card.conection.InputRunnable
import com.verygoodsecurity.vgscheckout.collect.view.card.getAnalyticName
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.CompositeValidator
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.LengthValidator
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.MutableValidator
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.RegexValidator
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.ValidationRule
import com.verygoodsecurity.vgscheckout.util.logger.VGSCheckoutLogger

/** @suppress */
internal abstract class BaseInputField @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : TextInputEditText(context, attributeSet), Dependency.DependentView {

    companion object {
        fun getInputField(context: Context, parent: InputFieldView): BaseInputField {
            val field = when (parent.getFieldType()) {
                FieldType.CARD_NUMBER -> CardInputField(context)
                FieldType.CVC -> CVCInputField(context)
                FieldType.CARD_EXPIRATION_DATE -> DateInputField(context)
                FieldType.CARD_HOLDER_NAME -> PersonNameInputField(context)
                FieldType.SSN -> SSNInputField(context)
                FieldType.INFO -> InfoInputField(context)
                FieldType.COUNTRY -> CountryInputField(context)
            }
            field.vgsParent = parent
            return field
        }
    }

    internal var isRequired: Boolean = true
        set(value) {
            field = value
            inputConnection?.getOutput()?.isRequired = value
            inputConnection?.run()
        }

    internal var enableValidation: Boolean = true
        set(value) {
            field = value
            inputConnection?.getOutput()?.enableValidation = value
            inputConnection?.run()
        }

    protected var hasRTL = false

    protected abstract var fieldType: FieldType

    protected var inputConnection: InputRunnable? = null
    protected var validator: MutableValidator = CompositeValidator()

    internal var vgsParent: InputFieldView? = null

    private var onFieldStateChangeListener: OnFieldStateChangeListener? = null

    private var isBackgroundVisible = true

    private var analyticName: String? = null

    var isEdited: Boolean = false
        internal set

    init {
        setupInputConnectionListener()
        setupViewAttributes()
        this.setupAutofill()
    }

    protected open fun setupAutofill() {}

    private fun setupViewAttributes() {
        if (id != -1) return
        id = ViewCompat.generateViewId()
        compoundDrawablePadding =
            resources.getDimension(R.dimen.vgs_checkout_margin_padding_size_small).toInt()
    }

    private fun setupInputConnectionListener() {
        addTextChangedListener {
            if (!isEdited) isEdited = it != null && it.isNotEmpty()
            updateTextChanged(it.toString())
            notifyStateChangeListeners()
        }
    }

    private fun notifyStateChangeListeners() {
        inputConnection?.getOutput()?.mapToFieldState()?.let {
            onFieldStateChangeListener?.onStateChange(this, it)
        }
    }

    protected open fun updateTextChanged(str: String) {
        inputConnection?.also {
            with(it.getOutput()) {
                if (str.isNotEmpty()) {
                    hasUserInteraction = true
                }
                content?.data = str
            }
            it.run()
        }
    }

    override fun onAttachedToWindow() {
        applyFieldType()
        inputConnection?.getOutput()?.enableValidation = enableValidation
        super.onAttachedToWindow()
    }

    protected abstract fun applyFieldType()

    protected fun collectCurrentState(stateContent: FieldContent): VGSFieldState {
        val state = VGSFieldState().apply {
            isRequired = this@BaseInputField.isRequired
            isFocusable = this@BaseInputField.hasFocus()
            type = this@BaseInputField.fieldType
            content = stateContent

            fieldName = this@BaseInputField.tag as? String
        }

        return state
    }

    internal fun setHasBackground(state: Boolean) {
        isBackgroundVisible = state
        if (isBackgroundVisible) {
            setBackgroundResource(android.R.color.transparent)
        }
    }

    @SuppressLint("InlinedApi")
    protected fun isRTL(): Boolean {
        val direction = getResolvedLayoutDirection()
        return direction == View.LAYOUT_DIRECTION_RTL
                || direction == View.TEXT_DIRECTION_ANY_RTL
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && direction == View.TEXT_DIRECTION_FIRST_STRONG_RTL)
                || direction == View.TEXT_DIRECTION_RTL
    }

    private fun getResolvedLayoutDirection(): Int = layoutDirection

    protected fun refreshOutputContent() {
        updateTextChanged(text.toString())
    }

    protected fun refreshInput() {
        val currentSelection = selectionStart
        setText(text)
        val textLength = text?.length ?: 0

        when {
            currentSelection > textLength -> setSelection(textLength)
            selectionStart > currentSelection -> setSelection(selectionStart)
            selectionStart < currentSelection -> setSelection(currentSelection)
        }
    }

    override fun setTag(tag: Any?) {
        tag?.run {
            super.setTag(tag)
            inputConnection?.getOutput()?.fieldName = this as String
        }
    }

    private var minH: Int = 0
    private var minW: Int = 0
    internal fun setMinimumPaddingLimitations(w: Int, h: Int) {
        minH = h
        minW = w
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        val l = left + minW
        val r = right + minW
        val t = top + minH
        val b = bottom + minH
        super.setPadding(l, t, r, b)
    }

    override fun setCompoundDrawables(
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?
    ) {
        if (isRTL()) {
            super.setCompoundDrawables(right, top, left, bottom)
        } else {
            super.setCompoundDrawables(left, top, right, bottom)
        }
    }

    internal var dependentField: Dependency.DependentView? = null

    override fun dispatchDependencySetting(dependency: Dependency) {
        if (dependency.dependencyType == Dependency.DependencyType.TEXT) {
            setText(dependency.value.toString())
        }
    }

    private fun requestFocusOnView(id: Int) {
        when (val nextView = rootView?.findViewById<View>(id)) {
            null -> return
            is InputFieldView -> nextView.statePreparer.getView().requestFocus()
            is BaseInputField -> nextView.requestFocus()
            else -> nextView.requestFocus()
        }
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        return super.requestFocus(direction, previouslyFocusedRect).also {
            setSelection(text?.length ?: 0)
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)

        inputConnection?.getOutput()?.apply {
            if (focused != isFocusable) {
                isFocusable = focused
                hasUserInteraction = true
                inputConnection?.run()
            }
        }
        notifyStateChangeListeners()
    }

    override fun onEditorAction(actionCode: Int) {
        when {
            actionCode == EditorInfo.IME_ACTION_NEXT
                    && nextFocusDownId != View.NO_ID -> requestFocusOnView(nextFocusDownId)
            actionCode == EditorInfo.IME_ACTION_PREVIOUS
                    && nextFocusUpId != View.NO_ID -> requestFocusOnView(nextFocusUpId)
        }
        super.onEditorAction(actionCode)
    }

    fun setOnFieldStateChangeListener(onFieldStateChangeListener: OnFieldStateChangeListener?) {
        this.onFieldStateChangeListener = onFieldStateChangeListener
        inputConnection?.getOutput()?.mapToFieldState()?.let {
            onFieldStateChangeListener?.onStateChange(this, it)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun prepareFieldTypeConnection() {
        applyFieldType()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun refreshInternalState() {
        inputConnection?.run()
    }

    internal open fun getState(): FieldState? {
        return inputConnection?.getOutput()?.mapToFieldState()
    }

    fun getFieldState(): VGSFieldState {
        return inputConnection?.getOutput() ?: VGSFieldState()
    }

    internal var tracker: AnalyticTracker? = null

    override fun autofill(value: AutofillValue?) {
        super.autofill(value)
        tracker?.log(AutofillEvent(getAnalyticsName()))
    }

    protected fun printWarning(tag: String, resId: Int) {
        VGSCheckoutLogger.warn(tag, context.getString(resId))
    }

    open fun applyValidationRule(rule: ValidationRule) {
        validator.clearRules()
        rule.length?.let {
            validator.addRule(LengthValidator(it))
        }

        rule.regex?.let {
            validator.addRule(RegexValidator(it))
        }
    }

    fun isContentEquals(inputField: BaseInputField): Boolean {
        val thisContent = inputConnection?.getOutput()?.content
        val thisData = thisContent?.rawData ?: thisContent?.data
        val otherContent = inputField.inputConnection?.getOutput()?.content
        val otherData = otherContent?.rawData ?: otherContent?.data
        return thisData.equals(otherData)
    }

    fun setAnalyticsName(name: String?) {
        analyticName = name
    }

    fun getAnalyticsName(): String = analyticName ?: fieldType.getAnalyticName()

    /**
     * Interface definition for a callback to be invoked when a view state is changed.
     *
     * @version 1.0.0
     */
    internal interface OnFieldStateChangeListener {

        /**
         * Called when new changes is detected
         *
         * @param state current state of input field
         */
        fun onStateChange(inputField: BaseInputField, state: FieldState)
    }
}

internal val TextInputEditText.localVisibleRect: Rect
    get() {
        val rect = Rect()
        this.getLocalVisibleRect(rect)
        return rect
    }