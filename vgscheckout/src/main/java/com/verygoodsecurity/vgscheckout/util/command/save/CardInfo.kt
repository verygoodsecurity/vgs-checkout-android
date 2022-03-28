package com.verygoodsecurity.vgscheckout.util.command.save

import com.verygoodsecurity.vgscheckout.config.networking.core.CheckoutRouteConfig

internal data class CardInfo(
    val baseUrl: String,
    val config: CheckoutRouteConfig,
    val data: MutableCollection<Pair<String, String>>,
)