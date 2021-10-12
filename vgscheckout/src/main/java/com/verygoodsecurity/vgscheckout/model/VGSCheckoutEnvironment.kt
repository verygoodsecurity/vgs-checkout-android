package com.verygoodsecurity.vgscheckout.model

import android.os.Parcelable
import com.verygoodsecurity.vgscheckout.collect.util.extension.concatWithDash
import kotlinx.parcelize.Parcelize

/**
 * Type of vault to communicate with.
 *
 * @param defaultValue default vault environment without data region.
 * @param suffix organization vault region, for example: "-eu-2", value will be "live-eu-2" respectively.
 *
 * @property value final type of vault with suffix.
 */
sealed class VGSCheckoutEnvironment constructor(defaultValue: String, suffix: String) : Parcelable {

    val value: String = defaultValue concatWithDash suffix

    /**
     *  Live environment using live vault.
     *
     *  @param suffix organization vault region, for example: "-eu-2", value will be "live-eu-2" respectively.
     */
    @Parcelize
    data class Live(val suffix: String = "") : VGSCheckoutEnvironment(DEFAULT_VALUE, suffix) {

        companion object {

            private const val DEFAULT_VALUE = "live"
        }
    }

    /**
     *  Sandbox environment using sandbox vault.
     *
     *  @param suffix organization vault region, for example: "-eu-2", value will be "sandbox-eu-2" respectively.
     */
    @Parcelize
    data class Sandbox(val suffix: String = "") : VGSCheckoutEnvironment(DEFAULT_VALUE, suffix) {

        companion object {

            private const val DEFAULT_VALUE = "sandbox"
        }
    }
}