package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.analytic.event.ResponseEvent
import com.verygoodsecurity.vgscheckout.collect.core.model.VGSCollectFieldNameMappingPolicy
import com.verygoodsecurity.vgscheckout.collect.util.extension.toCardBrand
import com.verygoodsecurity.vgscheckout.collect.view.card.BrandParams
import com.verygoodsecurity.vgscheckout.collect.view.card.CardBrand
import com.verygoodsecurity.vgscheckout.collect.view.card.CardType
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.payment.ChecksumAlgorithm
import com.verygoodsecurity.vgscheckout.collect.view.core.serializers.VGSExpDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHttpMethod
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutChecksumAlgorithm
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutAddCardResponse
import com.verygoodsecurity.vgscheckout.networking.client.HttpMethod
import com.verygoodsecurity.vgscheckout.networking.client.HttpResponse
import com.verygoodsecurity.vgscheckout.networking.command.AddCardCommand

//region Networking
internal fun HttpResponse.toAddCardResult() =
    AddCardCommand.Result(isSuccessful, code, message, body, latency)

internal fun AddCardCommand.Result.toResponseEvent() = ResponseEvent(code, message, latency)

internal fun AddCardCommand.Result.toAddCardResponse() = VGSCheckoutAddCardResponse(isSuccessful, code, body, message)

internal fun VGSCheckoutResultBundle.toCheckoutResult(isSuccessful: Boolean) = if (isSuccessful) {
    VGSCheckoutResult.Success(this)
} else {
    VGSCheckoutResult.Failed(this)
}

internal fun VGSCheckoutHttpMethod.toInternal() = when (this) {
    VGSCheckoutHttpMethod.POST -> HttpMethod.POST
    VGSCheckoutHttpMethod.DELETE -> HttpMethod.DELETE
    VGSCheckoutHttpMethod.GET -> HttpMethod.GET
    VGSCheckoutHttpMethod.PATCH -> HttpMethod.PATCH
    VGSCheckoutHttpMethod.PUT -> HttpMethod.PUT
}

internal fun VGSCheckoutDataMergePolicy.toCollectMergePolicy() = when (this) {
    VGSCheckoutDataMergePolicy.FLAT_JSON -> VGSCollectFieldNameMappingPolicy.FLAT_JSON
    VGSCheckoutDataMergePolicy.NESTED_JSON -> VGSCollectFieldNameMappingPolicy.NESTED_JSON
    VGSCheckoutDataMergePolicy.NESTED_JSON_WITH_ARRAYS_OVERWRITE -> VGSCollectFieldNameMappingPolicy.NESTED_JSON_WITH_ARRAYS_OVERWRITE
    VGSCheckoutDataMergePolicy.NESTED_JSON_WITH_ARRAYS_MERGE -> VGSCollectFieldNameMappingPolicy.NESTED_JSON_WITH_ARRAYS_MERGE
}
//endregion

//region Card brands
internal fun VGSCheckoutChecksumAlgorithm.toCollectChecksumAlgorithm() = when (this) {
    VGSCheckoutChecksumAlgorithm.ANY -> ChecksumAlgorithm.ANY
    VGSCheckoutChecksumAlgorithm.LUHN -> ChecksumAlgorithm.LUHN
    VGSCheckoutChecksumAlgorithm.NONE -> ChecksumAlgorithm.NONE
}

internal fun ChecksumAlgorithm.toCheckoutChecksumAlgorithm() = when (this) {
    ChecksumAlgorithm.ANY -> VGSCheckoutChecksumAlgorithm.ANY
    ChecksumAlgorithm.LUHN -> VGSCheckoutChecksumAlgorithm.LUHN
    ChecksumAlgorithm.NONE -> VGSCheckoutChecksumAlgorithm.NONE
}

internal fun VGSCheckoutCardBrand.toCollectCardType() = when (this) {
    is VGSCheckoutCardBrand.Elo -> CardType.ELO
    is VGSCheckoutCardBrand.VisaElectron -> CardType.VISA_ELECTRON
    is VGSCheckoutCardBrand.Maestro -> CardType.MAESTRO
    is VGSCheckoutCardBrand.Forbrugsforeningen -> CardType.FORBRUGSFORENINGEN
    is VGSCheckoutCardBrand.Dankort -> CardType.DANKORT
    is VGSCheckoutCardBrand.Visa -> CardType.VISA
    is VGSCheckoutCardBrand.Mastercard -> CardType.MASTERCARD
    is VGSCheckoutCardBrand.AmericanExpress -> CardType.AMERICAN_EXPRESS
    is VGSCheckoutCardBrand.Hipercard -> CardType.HIPERCARD
    is VGSCheckoutCardBrand.Dinclub -> CardType.DINCLUB
    is VGSCheckoutCardBrand.Discover -> CardType.DISCOVER
    is VGSCheckoutCardBrand.Unionpay -> CardType.UNIONPAY
    is VGSCheckoutCardBrand.JCB -> CardType.JCB
    else -> CardType.UNKNOWN
}

internal fun VGSCheckoutCardBrand.toCollectCardBrand() = when (this) {
    is VGSCheckoutCardBrand.Custom -> CardBrand(regex, name, icon, toCollectBrandParams())
    else -> toCollectCardType().toCardBrand()
}

internal fun VGSCheckoutCardBrand.toCollectBrandParams(): BrandParams = BrandParams(
    mask,
    algorithm.toCollectChecksumAlgorithm(),
    cardNumberLength,
    securityCodeLength
)
//endregion

internal fun VGSDateSeparateSerializer.toCollectDateSeparateSerializer() =
    VGSExpDateSeparateSerializer(this.monthFieldName, this.yearFieldName)