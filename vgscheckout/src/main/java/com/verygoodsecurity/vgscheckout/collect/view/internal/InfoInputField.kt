package com.verygoodsecurity.vgscheckout.collect.view.internal

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldContent
import com.verygoodsecurity.vgscheckout.collect.view.card.FieldType
import com.verygoodsecurity.vgscheckout.collect.view.card.conection.InputInfoConnection
import com.verygoodsecurity.vgscheckout.collect.view.core.serializers.FieldDataSerializer

/** @suppress */
internal open class InfoInputField(
    context: Context,
    attrs: AttributeSet? = null
) : BaseInputField(context, attrs) {

    var serializer: FieldDataSerializer<*, *>? = null

    override var fieldType: FieldType = FieldType.INFO

    override fun applyFieldType() {
        inputConnection = InputInfoConnection(id, validator)

        val str = text.toString()
        val stateContent = FieldContent.InfoContent().apply {
            this.serializer = this@InfoInputField.serializer
            this.data = str
        }
        val state = collectCurrentState(stateContent)

        inputConnection?.setOutput(state)

        applyNewTextWatcher(null)
        applyInputType()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
    }

    private fun applyInputType() {
        val type = inputType

        when (type) {
            InputType.TYPE_CLASS_NUMBER,
            InputType.TYPE_CLASS_DATETIME,
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD -> {}
            InputType.TYPE_TEXT_VARIATION_PASSWORD,
            InputType.TYPE_NUMBER_VARIATION_PASSWORD,
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD -> {
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            else -> inputType = InputType.TYPE_CLASS_TEXT
        }

        refreshInput()
    }

}