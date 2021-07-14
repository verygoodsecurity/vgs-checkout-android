package com.verygoodsecurity.vgscheckout.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

internal class DropdownEventSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatSpinner(context, attrs) {

    var onDropdownStateChangeListener: OnDropdownStateChangeListener? = null

    private var isDropdownOpened: Boolean = false

    override fun performClick(): Boolean {
        notifyDropdownOpened()
        return super.performClick()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus && isDropdownOpened) {
            notifyDropdownClosed()
        }
    }

    private fun notifyDropdownOpened() {
        isDropdownOpened = true
        onDropdownStateChangeListener?.onDropdownOpened()
    }

    private fun notifyDropdownClosed() {
        isDropdownOpened = false
        onDropdownStateChangeListener?.onDropdownClosed()
    }

    interface OnDropdownStateChangeListener {

        fun onDropdownOpened()

        fun onDropdownClosed()
    }
}