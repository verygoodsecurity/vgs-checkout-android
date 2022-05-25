package com.verygoodsecurity.vgscheckout.collect.view

import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldContent

internal data class Dependency(val dependencyType: DependencyType, val value: Any) {

    companion object {

        fun text(value: Any) = Dependency(DependencyType.TEXT, value)

        fun length(length: Int) = Dependency(DependencyType.LENGTH, length)

        fun card(card: FieldContent.CardNumberContent) = Dependency(DependencyType.CARD, card)
    }

    enum class DependencyType {
        TEXT,
        LENGTH,
        CARD
    }

    interface DependentView {

        fun dispatchDependencySetting(dependency: Dependency)
    }
}