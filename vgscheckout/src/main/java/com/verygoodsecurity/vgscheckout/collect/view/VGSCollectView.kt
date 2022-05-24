package com.verygoodsecurity.vgscheckout.collect.view

import com.verygoodsecurity.vgscheckout.collect.view.card.FieldType

internal interface VGSCollectView {

    val statePreparer: AccessibilityStatePreparer

    /**
     * Gets the current field type of the InputFieldView.
     *
     * @return FieldType
     *
     * @see FieldType
     */
    fun getFieldType(): FieldType

    /**
     * Sets the text to be used for data transfer to VGS proxy. Usually,
     * it is similar to field-name in JSON path in your inbound route filters.
     *
     * @param fieldName the name of the field
     */
    fun setFieldName(fieldName: String?)

    /**
     * Return the text that field is using for data transfer to VGS proxy.
     *
     * @return The text used by the field.
     */
    fun getFieldName(): String?

    /**
     * Sets the text to be used for data transfer to VGS proxy. Usually,
     * it is similar to field-name in JSON path in your inbound route filters.
     *
     * @param resId the resource identifier of the field name
     */
    fun setFieldName(resId: Int)
}