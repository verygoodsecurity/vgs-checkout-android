package com.verygoodsecurity.vgscheckout.collect.core.storage

import com.verygoodsecurity.vgscheckout.collect.core.model.state.Dependency
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.view.card.FieldType

internal interface DependencyDispatcher {
    fun onDependencyDetected(type: FieldType, dependency: Dependency)
    fun addDependencyListener(fieldType: FieldType, notifier: DependencyListener)
}