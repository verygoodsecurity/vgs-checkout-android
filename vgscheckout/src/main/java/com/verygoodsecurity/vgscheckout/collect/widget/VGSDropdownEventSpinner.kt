package com.verygoodsecurity.vgscheckout.collect.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import com.verygoodsecurity.vgscheckout.collect.core.OnVgsViewStateChangeListener
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.AnalyticTracker
import com.verygoodsecurity.vgscheckout.collect.core.model.state.Dependency
import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldContent
import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.core.storage.DependencyListener
import com.verygoodsecurity.vgscheckout.collect.view.AccessibilityStatePreparer
import com.verygoodsecurity.vgscheckout.collect.view.VGSCollectView
import com.verygoodsecurity.vgscheckout.collect.view.card.FieldType
import com.verygoodsecurity.vgscheckout.util.extension.getString

internal class VGSDropdownEventSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatSpinner(context, attrs), VGSCollectView, DependencyListener {

    override val statePreparer: AccessibilityStatePreparer = DropdownEventSpinnerStatePreparer()

    var onDropdownStateChangeListener: OnDropdownStateChangeListener? = null

    private var userOnItemSelectedListener: OnItemSelectedListener? = null
    private var stateListener: OnVgsViewStateChangeListener? = null
    private var isDropdownOpened: Boolean = false
    private var fieldName: String? = null

    init {
        super.setOnItemSelectedListener(object : OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position) as? String
                stateListener?.emit(this@VGSDropdownEventSpinner.id, createState(selectedItem))
                userOnItemSelectedListener?.onItemSelected(parent, view, position, id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                userOnItemSelectedListener?.onNothingSelected(parent)
            }
        })
    }

    override fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
        userOnItemSelectedListener = listener
    }

    override fun getFieldType(): FieldType = FieldType.INFO

    override fun setFieldName(fieldName: String?) {
        this.fieldName = fieldName
        stateListener?.emit(this@VGSDropdownEventSpinner.id, createState())
    }

    override fun setFieldName(resId: Int) {
        this.fieldName = getString(resId)
        stateListener?.emit(this@VGSDropdownEventSpinner.id, createState())
    }

    override fun getFieldName(): String? = fieldName

    override fun addStateListener(stateListener: OnVgsViewStateChangeListener) {
        this.stateListener = stateListener
        stateListener.emit(this@VGSDropdownEventSpinner.id, createState())
    }

    override fun dispatchDependencySetting(dependency: Dependency) {}

    override fun performClick(): Boolean {
        isDropdownOpened = true
        onDropdownStateChangeListener?.onDropdownOpened()
        return super.performClick()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus && isDropdownOpened) {
            isDropdownOpened = false
            onDropdownStateChangeListener?.onDropdownClosed()
        }
    }

    private fun createState(data: String? = selectedItem as? String): VGSFieldState {
        val content = FieldContent.InfoContent().apply {
            this.data = data
            this.rawData = data
        }
        return VGSFieldState(
            fieldName = fieldName,
            isValid = !data.isNullOrEmpty(),
            content = content
        )
    }

    inner class DropdownEventSpinnerStatePreparer : AccessibilityStatePreparer {

        override fun getId(): Int = this@VGSDropdownEventSpinner.id

        override fun getView(): View = this@VGSDropdownEventSpinner

        override fun unsubscribe() {
            stateListener = null
        }

        override fun getDependencyListener(): DependencyListener = this@VGSDropdownEventSpinner

        override fun setAnalyticTracker(tr: AnalyticTracker) {}
    }

    interface OnDropdownStateChangeListener {

        fun onDropdownOpened()

        fun onDropdownClosed()
    }
}

