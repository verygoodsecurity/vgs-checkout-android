package com.verygoodsecurity.vgscheckout.util.command.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class OrderDetails(val price: Int, val currency: String): Parcelable