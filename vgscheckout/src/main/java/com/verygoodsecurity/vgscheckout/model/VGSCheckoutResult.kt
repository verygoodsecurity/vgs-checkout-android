package com.verygoodsecurity.vgscheckout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutResult constructor(val code: Int?, val body: String?) : Parcelable