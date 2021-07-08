package com.verygoodsecurity.vgscheckout.collect.core.storage

import com.verygoodsecurity.vgscheckout.collect.core.model.state.Dependency

/** @suppress */
interface DependencyListener {

    fun dispatchDependencySetting(dependency: Dependency)
}