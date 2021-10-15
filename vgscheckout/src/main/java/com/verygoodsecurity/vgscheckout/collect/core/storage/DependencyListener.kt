package com.verygoodsecurity.vgscheckout.collect.core.storage

import com.verygoodsecurity.vgscheckout.collect.core.model.state.Dependency

/** @suppress */
internal interface DependencyListener {

    fun dispatchDependencySetting(dependency: Dependency)
}