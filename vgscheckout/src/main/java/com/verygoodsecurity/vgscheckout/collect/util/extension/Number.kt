package com.verygoodsecurity.vgscheckout.collect.util.extension

/** @suppress */
internal fun String.isNumeric(): Boolean = this.toDoubleOrNull() != null