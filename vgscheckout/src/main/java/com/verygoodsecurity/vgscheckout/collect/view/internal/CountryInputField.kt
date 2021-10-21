package com.verygoodsecurity.vgscheckout.collect.view.internal

import android.content.Context
import android.os.Build
import android.view.autofill.AutofillValue
import androidx.annotation.RequiresApi

internal class CountryInputField(context: Context) : InfoInputField(context) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun autofill(value: AutofillValue?) {
        super.autofill(value)
    }
}

