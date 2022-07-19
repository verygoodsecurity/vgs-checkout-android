package com.verygoodsecurity.vgscheckout.config.payment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class Order(
    val id: String,
    val price: Int,
    val currency: String
) : Parcelable