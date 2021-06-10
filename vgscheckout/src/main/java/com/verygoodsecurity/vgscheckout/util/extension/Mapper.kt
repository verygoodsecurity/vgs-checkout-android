package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHTTPMethod
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutBrandParams
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardType
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutChecksumAlgorithm
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.model.VGSCollectFieldNameMappingPolicy
import com.verygoodsecurity.vgscollect.view.card.BrandParams
import com.verygoodsecurity.vgscollect.view.card.CardBrand
import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.view.card.validation.payment.ChecksumAlgorithm


//region Networking
internal fun VGSCheckoutHTTPMethod.toCollectHTTPMethod() = when (this) {
    VGSCheckoutHTTPMethod.POST -> HTTPMethod.POST
    VGSCheckoutHTTPMethod.DELETE -> HTTPMethod.DELETE
    VGSCheckoutHTTPMethod.GET -> HTTPMethod.GET
    VGSCheckoutHTTPMethod.PATCH -> HTTPMethod.PATCH
    VGSCheckoutHTTPMethod.PUT -> HTTPMethod.PUT
}

internal fun VGSCheckoutDataMergePolicy.toCollectMergePolicy() = when (this) {
    VGSCheckoutDataMergePolicy.NESTED_JSON -> VGSCollectFieldNameMappingPolicy.NESTED_JSON
    VGSCheckoutDataMergePolicy.NESTED_JSON_WITH_ARRAY_OVERWRITE -> VGSCollectFieldNameMappingPolicy.NESTED_JSON_WITH_ARRAYS_OVERWRITE
    VGSCheckoutDataMergePolicy.NESTED_JSON_WITH_ARRAY_MERGE -> VGSCollectFieldNameMappingPolicy.NESTED_JSON_WITH_ARRAYS_MERGE
}
//endregion

//region Card brands
internal fun VGSCheckoutChecksumAlgorithm.toCollectChecksumAlgorithm() = when (this) {
    VGSCheckoutChecksumAlgorithm.ANY -> ChecksumAlgorithm.ANY
    VGSCheckoutChecksumAlgorithm.LUHN -> ChecksumAlgorithm.LUHN
    VGSCheckoutChecksumAlgorithm.NONE -> ChecksumAlgorithm.NONE
}

internal fun VGSCheckoutCardType.toCollectCardType() = when (this) {
    VGSCheckoutCardType.ELO -> CardType.ELO
    VGSCheckoutCardType.VISA_ELECTRON -> CardType.VISA_ELECTRON
    VGSCheckoutCardType.MAESTRO -> CardType.MAESTRO
    VGSCheckoutCardType.FORBRUGSFORENINGEN -> CardType.FORBRUGSFORENINGEN
    VGSCheckoutCardType.DANKORT -> CardType.DANKORT
    VGSCheckoutCardType.VISA -> CardType.VISA
    VGSCheckoutCardType.MASTERCARD -> CardType.MASTERCARD
    VGSCheckoutCardType.AMERICAN_EXPRESS -> CardType.AMERICAN_EXPRESS
    VGSCheckoutCardType.HIPERCARD -> CardType.HIPERCARD
    VGSCheckoutCardType.DINCLUB -> CardType.DINCLUB
    VGSCheckoutCardType.DISCOVER -> CardType.DISCOVER
    VGSCheckoutCardType.UNIONPAY -> CardType.UNIONPAY
    VGSCheckoutCardType.JCB -> CardType.JCB
    VGSCheckoutCardType.UNKNOWN -> CardType.UNKNOWN
}

internal fun VGSCheckoutBrandParams.toCollectBrandParams() = BrandParams(
    mask,
    algorithm.toCollectChecksumAlgorithm(),
    rangeNumber,
    rangeCVV
)

internal fun VGSCheckoutCardBrand.toCollectCardBrand() = CardBrand(
    regex,
    cardBrandName,
    drawableResId,
    params.toCollectBrandParams()
)
//endregion