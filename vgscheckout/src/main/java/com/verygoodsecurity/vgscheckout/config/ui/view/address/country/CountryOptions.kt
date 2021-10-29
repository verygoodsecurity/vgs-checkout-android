package com.verygoodsecurity.vgscheckout.config.ui.view.address.country

import com.verygoodsecurity.vgscheckout.config.ui.view.core.ViewOptions

/**
 * Base class of country options.
 */
abstract class CountryOptions : ViewOptions() {

    /**
     * List of countries that will be show in selection dialog.
     */
    abstract val validCountries: List<String>
}