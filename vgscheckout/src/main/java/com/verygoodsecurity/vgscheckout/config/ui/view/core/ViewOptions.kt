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
}