package com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Expiration date input field date split serializer.
 *
 * User input date will be send as two different JSON values.
 *
 * Expiration date input field name fill be ignored.
 *
 * @param monthFieldName - this field name will be used for month in request json.
 * @param yearFieldName - this field name will be used for year in request json.
 */
@Parcelize
data class VGSDateSeparateSerializer(
    internal val monthFieldName: String,
    internal val yearFieldName: String
) : Parcelable
