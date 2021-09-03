package com.verygoodsecurity.vgscheckout.ui

import android.content.Intent
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscheckout.collect.view.core.serializers.VGSExpDateSeparateSerializer
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutMultiplexingConfiguration
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.core.BaseCheckoutActivity
import com.verygoodsecurity.vgscheckout.util.CollectProvider
import com.verygoodsecurity.vgscheckout.util.extension.toCollectHTTPMethod
import com.verygoodsecurity.vgscheckout.util.extension.toCollectMergePolicy

internal class CheckoutMultiplexingActivity :
    BaseCheckoutActivity<VGSCheckoutMultiplexingConfiguration>() {

    override fun initCardDetailsViews() {
        super.initCardDetailsViews()

        //todo need to think how manage (split) it with default setup flow via FormConfiguration
        cardHolderTied.setFieldName("card.name")
        cardNumberTied.setFieldName("card.number")

        expirationDateTied.setFieldName("card.expDate")
        expirationDateTied.setOutputRegex("MM/YYYY")
        expirationDateTied.setSerializer(
            VGSExpDateSeparateSerializer(
                "card.exp_month",
                "card.exp_year"
            )
        )

        securityCodeTied.setFieldName("card.cvc")
    }

    override fun resolveConfig(intent: Intent) =
        CheckoutResultContract.Args.fromIntent<VGSCheckoutMultiplexingConfiguration>(intent).config

    override fun resolveCollect() = CollectProvider().get(this, config)

    override fun onPayClicked() {
        with(config.routeConfig) {
            collect.asyncSubmit(
                VGSRequest.VGSRequestBuilder()
                    .setPath("/financial_instruments")  //  https://tntshmljla7.sandbox.verygoodproxy.com/financial_instruments
                    .setMethod(requestOptions.httpMethod.toCollectHTTPMethod())
                    .setCustomData(requestOptions.extraData)
                    .setCustomHeader(getExtraHeaders())
                    .setFieldNameMappingPolicy(requestOptions.mergePolicy.toCollectMergePolicy())
                    .build()
            )
        }
    }

    private fun getExtraHeaders(): Map<String, String> {
        return mapOf(
            "Content-Type" to "application/json",
            "Authorization" to "Bearer ${config.token}"
        )
    }
}