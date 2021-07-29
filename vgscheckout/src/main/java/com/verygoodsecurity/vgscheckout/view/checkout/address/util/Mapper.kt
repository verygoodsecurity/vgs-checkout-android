package com.verygoodsecurity.vgscheckout.view.checkout.address.util

import androidx.annotation.StringRes
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.country.model.PostalAddressType

@StringRes
internal fun PostalAddressType.subtitle() = when (this) {
    PostalAddressType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_subtitle
    PostalAddressType.ZIP -> R.string.vgs_checkout_address_info_zip_subtitle
}

@StringRes
internal fun PostalAddressType.hint() = when (this) {
    PostalAddressType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_hint
    PostalAddressType.ZIP -> R.string.vgs_checkout_address_info_zip_hint
}

@StringRes
internal fun PostalAddressType.emptyError() = when (this) {
    PostalAddressType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_empty_error
    PostalAddressType.ZIP -> R.string.vgs_checkout_address_info_zipcode_empty_error
}

@StringRes
internal fun PostalAddressType.invalidError() = when (this) {
    PostalAddressType.POSTAL -> R.string.vgs_checkout_address_info_postal_code_invalid_error
    PostalAddressType.ZIP -> R.string.vgs_checkout_address_info_zipcode_invalid_error
}