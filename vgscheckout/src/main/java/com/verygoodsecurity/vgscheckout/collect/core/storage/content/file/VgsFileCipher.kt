package com.verygoodsecurity.vgscheckout.collect.core.storage.content.file

import android.net.Uri
import com.verygoodsecurity.vgscheckout.collect.util.extension.NotEnoughMemoryException
import java.util.*

internal interface VgsFileCipher {

    fun save(fieldName: String): Long

    fun retrieve(map: HashMap<String, Any?>): Pair<String, String>?

    @Throws(NotEnoughMemoryException::class)
    fun getBase64(uri: Uri, maxSize: Long): String
}