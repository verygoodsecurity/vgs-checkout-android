package com.verygoodsecurity.vgscheckout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class Card constructor(
    val finId: String,
    val holderName: String,
    val lastFour: String,
    val expiry: String,
    val brand: String
): Parcelable