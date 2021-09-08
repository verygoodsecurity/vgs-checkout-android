package com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VGSDateSeparateSerializer(
    internal val monthFieldName: String,
    internal val yearFieldName: String
) : Parcelable
