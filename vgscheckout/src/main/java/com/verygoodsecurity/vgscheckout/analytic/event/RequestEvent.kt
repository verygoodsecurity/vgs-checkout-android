package com.verygoodsecurity.vgscheckout.analytic.event

import com.verygoodsecurity.vgscheckout.analytic.event.core.Event
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour

internal data class RequestEvent(
    val isSuccessFull: Boolean,
    val hasCustomHostname: Boolean,
    val hasCustomData: Boolean,
    val hasCustomHeaders: Boolean,
    val hasValidCountries: Boolean,
    val mergingPolicy: VGSCheckoutDataMergePolicy,
    val validationBehaviour: VGSCheckoutFormValidationBehaviour,
    val invalidFieldTypes: List<String>,
) : Event(TYPE) {

    override val attributes: Map<String, Any> = LinkedHashMap<String, Any>().apply {
        put(KEY_STATUS, if (isSuccessFull) STATUS_OK else STATUS_FAILED)
        put(KEY_CONTENT, ArrayList<String>().apply {
            if (hasCustomHostname) add(CUSTOM_HOSTNAME)
            if (hasCustomData) add(CUSTOM_DATA)
            if (hasCustomHeaders) add(CUSTOM_HEADER)
            if (hasValidCountries) add(VALID_COUNTRIES)
            add(mergingPolicy.name.lowercase())
            add(mapValidationBehaviour(validationBehaviour))
        })
        if (invalidFieldTypes.isNotEmpty()) put(KEY_INVALID_FIELDS, invalidFieldTypes)
    }

    private fun mapValidationBehaviour(behaviour: VGSCheckoutFormValidationBehaviour) =
        when (behaviour) {
            VGSCheckoutFormValidationBehaviour.ON_FOCUS -> ON_FOCUS_VALIDATION
            VGSCheckoutFormValidationBehaviour.ON_SUBMIT -> ON_SUBMIT_VALIDATION
        }

    companion object {

        private const val TYPE = "BeforeSubmit"

        private const val KEY_CONTENT = "content"
        private const val KEY_INVALID_FIELDS = "fieldTypes"

        private const val CUSTOM_HOSTNAME = "custom_hostname"
        private const val CUSTOM_DATA = "custom_data"
        private const val CUSTOM_HEADER = "custom_header"
        private const val VALID_COUNTRIES = "valid_countries"
        private const val ON_SUBMIT_VALIDATION = "on_submit_validation"
        private const val ON_FOCUS_VALIDATION = "on_focus_validation"
    }
}