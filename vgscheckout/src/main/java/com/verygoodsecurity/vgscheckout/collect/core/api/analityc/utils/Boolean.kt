package com.verygoodsecurity.vgscheckout.collect.core.api.analityc.utils

fun Boolean.toAnalyticStatus(): String {
    return if(this) {
        "Ok"
    } else {
        "Failed"
    }
}