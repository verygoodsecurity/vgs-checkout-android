package com.verygoodsecurity.vgscheckout.config.payment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class OrderDetails(
    val price: Int,
    val currency: String,
    val raw: Raw
) : Parcelable {

    @kotlinx.parcelize.Parcelize
    data class Raw(
        val isSuccessful: Boolean,
        val code: Int,
        val body: String,
        val message: String?
    ) : Parcelable
}