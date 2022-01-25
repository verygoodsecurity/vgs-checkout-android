package com.verygoodsecurity.vgscheckout.model

internal data class VGSCheckoutCard constructor(
    val finId: String,
    val holderName: String,
    val lastFour: String,
    val expiry: String,
    val brand: String
)