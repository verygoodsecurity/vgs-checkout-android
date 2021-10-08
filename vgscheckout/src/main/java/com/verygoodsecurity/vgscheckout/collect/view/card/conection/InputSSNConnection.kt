package com.verygoodsecurity.vgscheckout.collect.view.card.conection

import com.verygoodsecurity.vgscheckout.collect.core.OnVgsViewStateChangeListener
import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.view.card.filter.VGSCardFilter
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.VGSValidator

internal class InputSSNConnection(id: Int, validator: VGSValidator?) :
    BaseInputConnection(id, validator) {

    private var output = VGSFieldState()

    override fun setOutput(state: VGSFieldState) {
        output = state
    }

    override fun getOutput() = output

    override fun setOutputListener(listener: OnVgsViewStateChangeListener?) {
        listener?.let { addNewListener(it) } ?: clearAllListeners()
    }

    override fun run() {
        validate()
        notifyAllListeners(output)
    }

    private fun validate() {
        val isRequiredRuleValid = isRequiredValid()
        val isContentRuleValid = isContentValid()

        output.isValid = isRequiredRuleValid && isContentRuleValid
    }

    private fun isContentValid(): Boolean {
        val content = output.content?.data
        return when {
            !output.isRequired && content.isNullOrEmpty() -> true
            output.enableValidation -> checkIsContentValid(content)
            else -> true
        }
    }

    private fun checkIsContentValid(content: String?): Boolean = isValid(content?.trim() ?: "")

    private fun isRequiredValid(): Boolean {
        return output.isRequired && !output.content?.data.isNullOrEmpty() || !output.isRequired
    }

    override fun clearFilters() {}
    override fun addFilter(filter: VGSCardFilter?) {}

}