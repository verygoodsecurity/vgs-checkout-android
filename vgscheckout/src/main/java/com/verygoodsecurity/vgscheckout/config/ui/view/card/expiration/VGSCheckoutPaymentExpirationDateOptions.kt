package com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration

import com.verygoodsecurity.vgscheckout.config.ui.view.card.expiration.model.VGSDateSeparateSerializer
import kotlinx.parcelize.Parcelize

/**
 * Expiration date input field options.
 *
 * @param fieldName text to be used for data transfer to VGS proxy.
 * @param dateSeparateSerializer split date into separate JSON values before send to proxy.
 * @param inputFormatRegex ISO 8601 date input format.
 * @param outputFormatRegex ISO 8601 format in which date will be sent to proxy.
 */
@Parcelize
class VGSCheckoutPaymentExpirationDateOptions private constructor(
    override val fieldName: String,
    override val dateSeparateSerializer: VGSDateSeparateSerializer?,
    override val inputFormatRegex: String,
    override val outputFormatRegex: String,
) : ExpirationDateOptions() {

    /**
     * Public constructor
     */
    constructor() : this(
        FIELD_NAME,
        VGSDateSeparateSerializer(
            MONTH_FIELD_NAME,
            YEAR_FIELD_NAME
        ),
        EXPIRY_DATE_INPUT_FORMAT,
        EXPIRY_DATE_OUTPUT_FORMAT
    )

    private companion object {

        private const val FIELD_NAME = "card.expDate"
        private const val MONTH_FIELD_NAME = "card.exp_month"
        private const val YEAR_FIELD_NAME = "card.exp_year"
        private const val EXPIRY_DATE_INPUT_FORMAT = "MM/yy"
        private const val EXPIRY_DATE_OUTPUT_FORMAT = "MM/YYYY"
    }
}