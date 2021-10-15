package com.verygoodsecurity.vgscheckout.collect.core.storage.content.file

import com.verygoodsecurity.vgscheckout.collect.core.storage.VgsStore
import java.util.HashMap

internal interface FileStorage: VgsStore<String, String> {
    fun getAssociatedList():MutableCollection<Pair<String, String>>
    fun dispatch(map: HashMap<String, Any?>)
}