package com.verygoodsecurity.vgscheckout.collect.view.card.formatter.date

import com.verygoodsecurity.vgscheckout.collect.view.card.formatter.Formatter
import com.verygoodsecurity.vgscheckout.collect.view.date.DatePickerMode

internal interface DatePickerFormatter:
    Formatter {
    fun setMode(mode: DatePickerMode)
}