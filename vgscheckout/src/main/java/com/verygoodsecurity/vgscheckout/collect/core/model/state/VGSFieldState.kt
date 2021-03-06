package com.verygoodsecurity.vgscheckout.collect.core.model.state

import com.verygoodsecurity.vgscheckout.collect.view.card.FieldType

/** @suppress */
internal data class VGSFieldState(
    var isFocusable: Boolean = false,
    var isRequired: Boolean = true,
    var enableValidation: Boolean = true,
    var isValid: Boolean = true,
    var type: FieldType = FieldType.INFO,
    var content: FieldContent? = null,
    var fieldName: String? = null,
    var hasUserInteraction: Boolean = false
) {

    fun isNotNullOrEmpty() = !fieldName.isNullOrBlank() && !content?.data.isNullOrEmpty()

    fun isNullOrEmpty() = content?.data.isNullOrEmpty()

    override fun toString(): String {
        return "isFocusable: $isFocusable\n" +
                "isRequired: $isRequired\n" +
                "isValid: $isValid\n" +
                "type: $type\n" +
                "content: ${content.toString()}\n" +
                "fieldName: $fieldName\n"
    }
}

/** @suppress */
internal fun VGSFieldState.mapToFieldState(): FieldState {
    val f = when (type) {
        FieldType.INFO -> FieldState.InfoState()
        FieldType.COUNTRY -> FieldState.InfoState()
        FieldType.CVC -> FieldState.CVCState()
        FieldType.CARD_HOLDER_NAME -> FieldState.CardHolderNameState()
        FieldType.CARD_EXPIRATION_DATE -> FieldState.CardExpirationDateState()
        FieldType.CARD_NUMBER -> prepareCardNumberState(isValid, content as? FieldContent.CardNumberContent)
    }

    f.fieldType = type
    f.isValid = isValid

    f.contentLength = content?.data?.length ?: 0
    f.isEmpty = f.contentLength == 0

    f.isRequired = isRequired
    f.fieldName = fieldName ?: ""
    f.hasFocus = isFocusable
    return f
}

private fun prepareCardNumberState(
    isValid: Boolean,
    content: FieldContent.CardNumberContent?
): FieldState.CardNumberState {
    return FieldState.CardNumberState().apply {
        if (isValid) {
            bin = content?.parseCardBin()
            last = content?.parseCardLast4Digits()
        }
        contentLengthRaw = content?.rawData?.length ?: 0
        number = content?.parseCardNumber()
        cardBrand = content?.cardBrandName ?: ""
        drawableBrandResId = content?.iconResId ?: 0
    }
}
