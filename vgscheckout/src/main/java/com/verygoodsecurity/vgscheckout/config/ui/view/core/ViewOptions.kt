package com.verygoodsecurity.vgscheckout.config.ui.view.core

import android.os.Parcelable

/**
 * Base class of view options.
 */
abstract class ViewOptions : Parcelable {

    /**
     * Sets the text to be used for data transfer to VGS proxy.Usually, it is similar to field-name
     * in JSON path in your inbound route filters.
     */
    abstract val fieldName: String

    /**
     * Defines if input field should be visible to user. If input field is invisible then it will not
     * be added to payload.
     */
    abstract val visibility: VGSCheckoutFieldVisibility

    fun isVisible() = visibility == VGSCheckoutFieldVisibility.VISIBLE
}