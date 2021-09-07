package com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.collect.view.core.serializers.VGSExpDateSeparateSerializer
import kotlinx.parcelize.Parcelize

@Parcelize
data class DateSeparateSerializer(
    internal val monthFieldName: String,
    internal val yearFieldName: String
) : Parcelable

internal fun DateSeparateSerializer.mapToCollectDateSeparateSerializer() =
    VGSExpDateSeparateSerializer(this.monthFieldName, this.yearFieldName)