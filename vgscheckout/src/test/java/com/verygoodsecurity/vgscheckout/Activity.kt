package com.verygoodsecurity.vgscheckout

import android.app.Activity
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.FieldType
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscheckout.collect.view.internal.BaseInputField
import com.verygoodsecurity.vgscheckout.collect.widget.*
import org.mockito.Mockito

internal fun Activity.applyEditText(
    collect: VGSCollect,
    typeField: FieldType,
    fieldName: String? = null,
    text: String? = null,
): InputFieldView {
    val view = when (typeField) {
        FieldType.CARD_NUMBER -> createCardNumber(this, fieldName, text)
        FieldType.CVC -> createCardCVC(this, fieldName, text)
        FieldType.CARD_EXPIRATION_DATE -> createCardExpDate(this, fieldName, text)
        FieldType.CARD_HOLDER_NAME -> createCardHolder(this, fieldName, text)
        FieldType.COUNTRY -> createCountryField(this, fieldName, text)
        FieldType.INFO -> createInfoField(this, fieldName, text)
        FieldType.SSN -> createSSNField(this, fieldName, text)
    }

    (view.statePreparer.getView() as? BaseInputField)?.prepareFieldTypeConnection()

    collect.bindView(view)

    return view
}

private fun createSSNField(
    activity: Activity,
    fieldName: String? = "createSSNField",
    text: String? = null,
): InputFieldView {
    return Mockito.spy(SSNEditText(activity)).apply {
        setFieldName(fieldName)
        text?.let { setText(it) }
    }
}

private fun createInfoField(
    activity: Activity,
    fieldName: String? = "createInfoField",
    text: String? = null,
): InputFieldView {
    return Mockito.spy(VGSEditText(activity)).apply {
        setFieldName(fieldName)
        text?.let { setText(it) }
        addRule(
            VGSInfoRule.ValidationBuilder()
                .setAllowableMinLength(1)
                .build()
        )
    }
}

private fun createCardNumber(
    activity: Activity,
    fieldName: String? = "createCardNumber",
    text: String? = null,
): VGSCardNumberEditText {
    return Mockito.spy(VGSCardNumberEditText(activity)).apply {
        setFieldName(fieldName)
        text?.let { setText(it) }
    }
}

private fun createCardCVC(
    activity: Activity,
    fieldName: String? = "createCardCVC",
    text: String? = null,
): CardVerificationCodeEditText {
    return Mockito.spy(CardVerificationCodeEditText(activity)).apply {
        setFieldName(fieldName)
        text?.let { setText(it) }
    }
}

private fun createCountryField(
    activity: Activity,
    fieldName: String? = "createCountryField",
    text: String? = null,
): VGSCountryEditText {
    return Mockito.spy(VGSCountryEditText(activity)).apply {
        setFieldName(fieldName)
        text?.let { setText(it) }
    }
}

private fun createCardHolder(
    activity: Activity,
    fieldName: String? = "createCardHolder",
    text: String? = null,
): PersonNameEditText {
    return Mockito.spy(PersonNameEditText(activity)).apply {
        setFieldName(fieldName)
        text?.let { setText(it) }
    }
}

private fun createCardExpDate(
    activity: Activity,
    fieldName: String? = "createCardExpDate",
    text: String? = null,
): ExpirationDateEditText {
    return Mockito.spy(ExpirationDateEditText(activity)).apply {
        setFieldName(fieldName)
        text?.let { setText(it) }
    }
}