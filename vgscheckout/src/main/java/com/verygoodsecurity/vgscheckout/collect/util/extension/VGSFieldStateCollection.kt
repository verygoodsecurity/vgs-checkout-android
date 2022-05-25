package com.verygoodsecurity.vgscheckout.collect.util.extension

import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldContent
import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.view.core.serializers.CountryNameToIsoSerializer
import com.verygoodsecurity.vgscheckout.collect.view.core.serializers.VGSExpDateSeparateSerializer


internal fun MutableCollection<VGSFieldState>.mapToAssociatedList(): MutableCollection<Pair<String, String>> {
    val result = mutableListOf<Pair<String, String>>()
    filter { state -> !state.fieldName.isNullOrEmpty() }.forEach { state ->
        with(state.content!!) {
            when (this) {
                is FieldContent.CardNumberContent -> result.add(
                    state.fieldName!! to (rawData ?: data!!)
                )
                is FieldContent.SSNContent -> result.add(
                    state.fieldName!! to (rawData ?: data!!)
                )
                is FieldContent.CreditCardExpDateContent -> {
                    result.addAll(handleExpirationDateContent(state.fieldName!!, this))
                }
                is FieldContent.InfoContent -> result.add(
                    state.fieldName!! to handleInfoContent(
                        this
                    )
                )
            }
        }
    }
    return result
}

private fun handleExpirationDateContent(
    fieldName: String,
    content: FieldContent.CreditCardExpDateContent
): List<Pair<String, String>> {
    val result = mutableListOf<Pair<String, String>>()
    val data = (content.rawData ?: content.data!!)
    if (content.serializers != null) {
        content.serializers?.forEach {
            if (it is VGSExpDateSeparateSerializer) {
                result.addAll(
                    it.serialize(VGSExpDateSeparateSerializer.Params(data, content.dateFormat))
                )
            }
        }
    } else {
        result.add(fieldName to data)
    }
    return result
}

private fun handleInfoContent(content: FieldContent.InfoContent): String {
    val data = content.rawData ?: (content.data ?: "")
    return (content.serializer as? CountryNameToIsoSerializer)?.serialize(data) ?: data
}