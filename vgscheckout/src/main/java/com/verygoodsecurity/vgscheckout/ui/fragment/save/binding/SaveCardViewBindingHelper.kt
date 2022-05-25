package com.verygoodsecurity.vgscheckout.ui.fragment.save.binding

import android.view.LayoutInflater
import android.view.View
import android.widget.Space
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.view.VGSCheckoutTextInputLayout
import com.verygoodsecurity.vgscheckout.collect.widget.*

// TODO: Remove all unused views
@Suppress("unused")
internal class SaveCardViewBindingHelper(inflater: LayoutInflater, @LayoutRes layoutId: Int) {

    val rootView: View = inflater.inflate(layoutId, null)

    val cardDetailsMtv: MaterialTextView by lazy { rootView.findViewById(R.id.mtvCardDetailsTitle) }
    val cardDetailsLL: LinearLayoutCompat by lazy { rootView.findViewById(R.id.llCardDetails) }
    val cardHolderTil: VGSCheckoutTextInputLayout by lazy { rootView.findViewById(R.id.vgsTilCardHolder) }
    val cardHolderEt: PersonNameEditText by lazy { rootView.findViewById(R.id.vgsEtCardHolder) }
    val cardNumberEt: VGSCardNumberEditText by lazy { rootView.findViewById(R.id.vgsEtCardNumber) }
    val expirationDateEt: ExpirationDateEditText by lazy { rootView.findViewById(R.id.vgsEtExpirationDate) }
    val securityCodeTil: VGSCheckoutTextInputLayout by lazy { rootView.findViewById(R.id.vgsTilSecurityCode) }
    val securityCodeEt: CardVerificationCodeEditText by lazy { rootView.findViewById(R.id.vgsEtSecurityCode) }

    val billingAddressMtv: MaterialTextView by lazy { rootView.findViewById(R.id.mtvBillingAddressTitle) }
    val billingAddressLL: LinearLayoutCompat by lazy { rootView.findViewById(R.id.llBillingAddress) }
    val countryTil: VGSCheckoutTextInputLayout by lazy { rootView.findViewById(R.id.vgsTilCountry) }
    val countryEt: VGSCountryEditText by lazy { rootView.findViewById(R.id.vgsEtCountry) }
    val addressTil: VGSCheckoutTextInputLayout by lazy { rootView.findViewById(R.id.vgsTilAddress) }
    val addressEt: VGSEditText by lazy { rootView.findViewById(R.id.vgsEtAddress) }
    val optionalAddressTil: VGSCheckoutTextInputLayout by lazy { rootView.findViewById(R.id.vgsTilAddressOptional) }
    val optionalAddressEt: VGSEditText by lazy { rootView.findViewById(R.id.vgsEtAddressOptional) }
    val cityTil: VGSCheckoutTextInputLayout by lazy { rootView.findViewById(R.id.vgsTilCity) }
    val cityEt: VGSEditText by lazy { rootView.findViewById(R.id.vgsEtCity) }
    val postalCodeTil: VGSCheckoutTextInputLayout by lazy { rootView.findViewById(R.id.vgsTilPostalCode) }
    val postalCodeEt: VGSEditText by lazy { rootView.findViewById(R.id.vgsEtPostalCode) }

    val cityPostalAddressSpace: Space by lazy { rootView.findViewById(R.id.cityPostalAddressSpace) }

    val saveCardCheckbox: MaterialCheckBox by lazy { rootView.findViewById(R.id.mcbSaveCard) }
    val saveCardButton: MaterialButton by lazy { rootView.findViewById(R.id.mbSaveCard) }
}