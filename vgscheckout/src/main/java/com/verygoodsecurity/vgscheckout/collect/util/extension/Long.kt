package com.verygoodsecurity.vgscheckout.collect.util.extension

import java.text.SimpleDateFormat
import java.util.*

private const val ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'"

internal fun Long.toIso8601() = SimpleDateFormat(ISO_8601, Locale.US).format(this)