package com.verygoodsecurity.vgscheckout.config.ui.view

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewConfig
import kotlinx.parcelize.Parcelize

@Parcelize
class VGSCheckoutPostalCodeOptions private constructor(
    override val contentPath: String
) : ViewConfig() {

    class Builder {

        private var contentPath: String = ""

        fun setContentPath(contentPath: String) = this.apply {
            this.contentPath = contentPath
        }

        fun build() = VGSCheckoutPostalCodeOptions(contentPath)
    }
}