package com.verygoodsecurity.vgscheckout.collect.core.storage

import com.verygoodsecurity.vgscheckout.collect.core.OnVgsViewStateChangeListener

/** @suppress */
internal interface IStateEmitter {
    fun performSubscription(): OnVgsViewStateChangeListener
    fun attachStateChangeListener(listener:OnFieldStateChangeListener?)
    fun attachFieldDependencyObserver(listener:FieldDependencyObserver?)
}