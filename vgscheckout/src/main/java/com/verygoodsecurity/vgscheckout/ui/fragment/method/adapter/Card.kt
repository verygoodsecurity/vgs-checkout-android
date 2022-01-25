package com.verygoodsecurity.vgscheckout.ui.fragment.method.adapter

internal data class Card constructor(
    val finId: String,
    val holderName: String,
    val lastFour: String,
    val expiry: String,
    val brand: String
)