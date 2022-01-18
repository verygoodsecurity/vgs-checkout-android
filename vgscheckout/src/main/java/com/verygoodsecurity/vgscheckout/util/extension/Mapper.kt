package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.core.HTTPMethod
import com.verygoodsecurity.vgscheckout.collect.core.model.VGSCollectFieldNameMappingPolicy
import com.verygoodsecurity.vgscheckout.collect.core.model.network.NetworkResponse
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.collect.util.extension.toCardBrand
import com.verygoodsecurity.vgscheckout.collect.view.card.BrandParams
import com.verygoodsecurity.vgscheckout.collect.view.card.CardBrand
import com.verygoodsecurity.vgscheckout.collect.view.card.CardType
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.payment.ChecksumAlgorithm
import com.verygoodsecurity.vgscheckout.collect.view.core.serializers.VGSExpDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHTTPMethod
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutChecksumAlgorithm
import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.exception.VGSCheckoutException
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutAddCardResponse
import com.verygoodsecurity.vgscheckout.model.response.VGSCheckoutTransactionResponse

//region Networking
internal fun VGSResponse.toAddCardResponse() = VGSCheckoutAddCardResponse(
    this is VGSResponse.SuccessResponse,
    code,
    body,
    (this as? VGSResponse.ErrorResponse)?.message
)

internal fun VGSCheckoutAddCardResponse.toCheckoutResult(e: VGSCheckoutException? = null): VGSCheckoutResult {
    val bundle = VGSCheckoutResultBundle()
    bundle.putAddCardResponse(this)
    return if (isSuccessful) {
        VGSCheckoutResult.Success(bundle)
    } else {
        VGSCheckoutResult.Failed(bundle, e)
    }
}

internal fun NetworkResponse.toTransactionResponse() = VGSCheckoutTransactionResponse(
    isSuccessful,
    code,
    body,
    message
)

internal fun VGSCheckoutTransactionResponse.toCheckoutResult(
    addCardResponse: VGSCheckoutAddCardResponse,
    e: VGSCheckoutException? = null
): VGSCheckoutResult {
    val bundle = VGSCheckoutResultBundle()
    bundle.putAddCardResponse(addCardResponse)
    bundle.putTransactionResponse(this)
    return if (addCardResponse.isSuccessful && isSuccessful) {
        VGSCheckoutResult.Success(bundle)
    } else {
        VGSCheckoutResult.Failed(bundle, e)
    }
}

internal fun VGSCheckoutHTTPMethod.toCollectHTTPMethod() = when (this) {
    VGSCheckoutHTTPMethod.POST -> HTTPMethod.POST
    VGSCheckoutHTTPMethod.DELETE -> HTTPMethod.DELETE
    VGSCheckoutHTTPMethod.GET -> HTTPMethod.GET
    VGSCheckoutHTTPMethod.PATCH -> HTTPMethod.PATCH
    VGSCheckoutHTTPMethod.PUT -> HTTPMethod.PUT
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