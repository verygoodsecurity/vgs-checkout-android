package com.verygoodsecurity.vgscheckout.view.checkout.address.util

import androidx.annotation.StringRes
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.country.model.PostalAddressType
import com.verygoodsecurity.vgscheckout.view.checkout.address.util.country.model.RegionType

@StringRes
internal fun RegionType.subtitle() = when (this) {
    RegionType.STATE -> R.string.vgs_checkout_address_info_region_type_state_subtitle
    RegionType.COUNTY -> R.string.vgs_checkout_address_info_region_type_county_subtitle
    RegionType.PROVINCE -> R.string.vgs_checkout_address_info_region_type_province_subtitle
    RegionType.SUBURB -> R.string.vgs_checkout_address_info_region_type_suburb_subtitle
    RegionType.UNKNOWN -> R.string.vgs_checkout_address_info_region_type_unknown_subtitle
}

@StringRes
internal fun RegionType.hint() = when (this) {
    RegionType.STATE -> R.string.vgs_checkout_address_info_region_type_state_hint
    RegionType.COUNTY -> R.string.vgs_checkout_address_info_region_type_county_hint
    RegionType.PROVINCE -> R.string.vgs_checkout_address_info_region_type_province_hint
    RegionType.SUBURB -> R.string.vgs_checkout_address_info_region_type_suburb_hint
    RegionType.UNKNOWN -> R.string.vgs_checkout_address_info_region_type_unknown_hint
}

@StringRes
internal fun RegionType.emptyError() = when (this) {
    RegionType.STATE -> R.string.vgs_checkout_address_info_state_empty_error
    RegionType.SUBURB -> R.string.vgs_checkout_address_info_suburb_empty_error
    RegionType.PROVINCE -> R.string.vgs_checkout_address_info_province_empty_error
    RegionType.COUNTY -> R.string.vgs_checkout_address_info_county_empty_error
    RegionType.UNKNOWN -> R.string.vgs_checkout_address_info_region_unknown_empty_error
}

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