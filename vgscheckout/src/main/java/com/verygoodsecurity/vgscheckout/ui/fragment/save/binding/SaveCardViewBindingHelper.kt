package com.verygoodsecurity.vgscheckout.ui.fragment.save.binding

import android.view.LayoutInflater
import android.view.View
import android.widget.Space
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.view.internal.CVCInputField
import com.verygoodsecurity.vgscheckout.collect.view.internal.CardInputField
import com.verygoodsecurity.vgscheckout.collect.view.internal.CountryInputField
import com.verygoodsecurity.vgscheckout.collect.view.internal.DateInputField
import com.verygoodsecurity.vgscheckout.collect.view.internal.InfoInputField
import com.verygoodsecurity.vgscheckout.collect.view.internal.PersonNameInputField

// TODO: Remove all unused views
@Suppress("unused")
internal class SaveCardViewBindingHelper(inflater: LayoutInflater, @LayoutRes layoutId: Int) {

    val rootView: View = inflater.inflate(layoutId, null)

    val cardDetailsMtv: MaterialTextView by lazy { rootView.findViewById(R.id.mtvCardDetailsTitle) }
    val cardDetailsLL: LinearLayoutCompat by lazy { rootView.findViewById(R.id.llCardDetails) }
    val cardHolderTil: TextInputLayout by lazy { rootView.findViewById(R.id.vgsTilCardHolder) }
    val cardHolderEt: PersonNameInputField by lazy { rootView.findViewById(R.id.vgsEtCardHolder) }
    val cardNumberEt: CardInputField by lazy { rootView.findViewById(R.id.vgsEtCardNumber) }
    val expirationDateEt: DateInputField by lazy { rootView.findViewById(R.id.vgsEtExpirationDate) }
    val securityCodeTil: TextInputLayout by lazy { rootView.findViewById(R.id.vgsTilSecurityCode) }
    val securityCodeEt: CVCInputField by lazy { rootView.findViewById(R.id.vgsEtSecurityCode) }

    val billingAddressMtv: MaterialTextView by lazy { rootView.findViewById(R.id.mtvBillingAddressTitle) }
    val billingAddressLL: LinearLayoutCompat by lazy { rootView.findViewById(R.id.llBillingAddress) }
    val countryTil: TextInputLayout by lazy { rootView.findViewById(R.id.vgsTilCountry) }
    val countryEt: CountryInputField by lazy { rootView.findViewById(R.id.vgsEtCountry) }
    val addressTil: TextInputLayout by lazy { rootView.findViewById(R.id.vgsTilAddress) }
    val addressEt: InfoInputField by lazy { rootView.findViewById(R.id.vgsEtAddress) }
    val optionalAddressTil: TextInputLayout by lazy { rootView.findViewById(R.id.vgsTilAddressOptional) }
    val optionalAddressEt: InfoInputField by lazy { rootView.findViewById(R.id.vgsEtAddressOptional) }
    val cityTil: TextInputLayout by lazy { rootView.findViewById(R.id.vgsTilCity) }
    val cityEt: InfoInputField by lazy { rootView.findViewById(R.id.vgsEtCity) }
    val postalCodeTil: TextInputLayout by lazy { rootView.findViewById(R.id.vgsTilPostalCode) }
    val postalCodeEt: InfoInputField by lazy { rootView.findViewById(R.id.vgsEtPostalCode) }

    val cityPostalAddressSpace: Space by lazy { rootView.findViewById(R.id.cityPostalAddressSpace) }

    val saveCardCheckbox: MaterialCheckBox by lazy { rootView.findViewById(R.id.mcbSaveCard) }
    val saveCardButton: MaterialButton by lazy { rootView.findViewById(R.id.mbSaveCard) }

    fun getStates(
        includeInvisibleFields: Boolean = false
    ): MutableCollection<VGSFieldState> {
        return with(mutableListOf<VGSFieldState>()) {
            if (includeInvisibleFields || securityCodeEt.isVisible) add(securityCodeEt.getFieldState())
            if (includeInvisibleFields || expirationDateEt.isVisible) add(expirationDateEt.getFieldState())
            if (includeInvisibleFields || cardNumberEt.isVisible) add(cardNumberEt.getFieldState())
            if (includeInvisibleFields || cardHolderEt.isVisible) add(cardHolderEt.getFieldState())

            if (includeInvisibleFields || addressEt.isVisible) add(addressEt.getFieldState())
            if (includeInvisibleFields || countryEt.isVisible) add(countryEt.getFieldState())
            if (includeInvisibleFields || optionalAddressEt.isVisible) add(optionalAddressEt.getFieldState())
            if (includeInvisibleFields || cityEt.isVisible) add(cityEt.getFieldState())
            if (includeInvisibleFields || postalCodeEt.isVisible) add(postalCodeEt.getFieldState())
            this
        }
    }
}