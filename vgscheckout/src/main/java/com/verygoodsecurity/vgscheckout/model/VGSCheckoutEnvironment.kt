package com.verygoodsecurity.vgscheckout.model

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.collect.util.extension.concatWithDash
import kotlinx.parcelize.Parcelize

/**
 * Type of vault to communicate with.
 */
sealed class VGSCheckoutEnvironment : Parcelable {

    abstract val value: String

    /**
     *  Live environment using live vault.
     *
     *  @param suffix for example: "-eu-2", value will be "live-eu-2" respectively.
     *
     *  @property value final type of vault with suffix.
     */
    @Parcelize
    data class Live(val suffix: String = "") : VGSCheckoutEnvironment() {

        override val value: String
            get() = DEFAULT_VALUE concatWithDash suffix

        companion object {

            private const val DEFAULT_VALUE = "live"
        }
    }

    /**
     *  Sandbox environment using sandbox vault.
     *
     *  @param suffix for example: "-eu-2", value will be "sandbox-eu-2" respectively.
     *
     *  @property value final type of vault with suffix.
     */
    @Parcelize
    data class Sandbox(val suffix: String = "") : VGSCheckoutEnvironment() {

        override val value: String
            get() = DEFAULT_VALUE concatWithDash suffix

        companion object {

            private const val DEFAULT_VALUE = "sandbox"
        }
    }
}