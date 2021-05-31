package com.verygoodsecurity.vgscheckout.config.ui

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.config.ui.view.*
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSUserInterfaceConfig private constructor(
    val cardNumberConfig: VGSCardNumberConfig,
    val cardHolderConfig: VGSCardHolderConfig,
    val cardVerificationCodeConfig: VGSCardVerificationCodeConfig,
    val expirationDateConfig: VGSExpirationDateConfig,
    val postalCodeConfig: VGSPostalCodeConfig,
) : Parcelable {

    class Builder {

        private var cardNumberConfig = VGSCardNumberConfig.Builder().build()
        private var cardHolderConfig = VGSCardHolderConfig.Builder().build()
        private var cardVerificationCodeConfig = VGSCardVerificationCodeConfig.Builder().build()
        private var expirationDateConfig = VGSExpirationDateConfig.Builder().build()
        private var postalCodeConfig = VGSPostalCodeConfig.Builder().build()

        fun setCardNumberConfig(config: VGSCardNumberConfig) = this.apply {
            this.cardNumberConfig = config
        }

        fun setCardHolderConfig(config: VGSCardHolderConfig) = this.apply {
            this.cardHolderConfig = config
        }

        fun setCardVerificationCodeConfig(config: VGSCardVerificationCodeConfig) = this.apply {
            this.cardVerificationCodeConfig = config
        }

        fun setExpirationDateConfig(config: VGSExpirationDateConfig) = this.apply {
            this.expirationDateConfig = config
        }

        fun setPostalCodeConfig(config: VGSPostalCodeConfig) = this.apply {
            this.postalCodeConfig = config
        }

        fun build(): VGSUserInterfaceConfig = VGSUserInterfaceConfig(
            cardNumberConfig,
            cardHolderConfig,
            cardVerificationCodeConfig,
            expirationDateConfig,
            postalCodeConfig,
        )
    }
}