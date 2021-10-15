package com.verygoodsecurity.vgscheckout.collect.core.storage.external

import com.verygoodsecurity.vgscheckout.collect.core.storage.DependencyListener
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import java.util.HashMap

internal interface ExternalDependencyDispatcher {
    fun addDependencyListener(fieldName: String?, notifier: DependencyListener)
    fun dispatch(map: HashMap<String, Any?>)
    fun dispatch(fieldName:String, value:Any)
}