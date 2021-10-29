package com.verygoodsecurity.vgscheckout.collect.view.internal

import android.content.Context
import android.os.Build
import android.view.autofill.AutofillValue
import androidx.annotation.RequiresApi
import com.verygoodsecurity.vgscheckout.collect.widget.VGSCountryEditText

internal class CountryInputField(context: Context) : InfoInputField(context) {

    init {
        isFocusable = false
        isFocusableInTouchMode = false
        isCursorVisible = false
    }

    override fun autofill(value: AutofillValue?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            autofillCountry(value)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun autofillCountry(value: AutofillValue?) {
        value?.textValue?.let {
            (vgsParent as? VGSCountryEditText)?.setSelectedCountry(it.toString())
        }
    }
}

