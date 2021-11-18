package com.verygoodsecurity.vgscheckout.ui.fragment.core

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.widget.*

// TODO: Remove all unused views
@Suppress("unused")
internal class ManualInputViewBindingHelper(inflater: LayoutInflater, @LayoutRes layoutId: Int) {

    val rootView: View = inflater.inflate(layoutId, null)

    val cardDetailsMtv: MaterialTextView = rootView.findViewById(R.id.mtvCardDetailsTitle)
    val cardDetailsLL: LinearLayoutCompat = rootView.findViewById(R.id.llCardDetails)
    val cardHolderTil: VGSTextInputLayout = rootView.findViewById(R.id.vgsTilCardHolder)
    val cardHolderEt: PersonNameEditText = rootView.findViewById(R.id.vgsEtCardHolder)
    val cardNumberTil: VGSTextInputLayout = rootView.findViewById(R.id.vgsTilCardNumber)
    val cardNumberEt: VGSCardNumberEditText = rootView.findViewById(R.id.vgsEtCardNumber)
    val expirationDateTil: VGSTextInputLayout = rootView.findViewById(R.id.vgsTilExpirationDate)
    val expirationDateEt: ExpirationDateEditText = rootView.findViewById(R.id.vgsEtExpirationDate)
    val securityCodeTil: VGSTextInputLayout = rootView.findViewById(R.id.vgsTilSecurityCode)
    val securityCodeEt: CardVerificationCodeEditText = rootView.findViewById(R.id.vgsEtSecurityCode)

    val billingAddressMtv: MaterialTextView = rootView.findViewById(R.id.mtvBillingAddressTitle)
    val billingAddressLL: LinearLayoutCompat = rootView.findViewById(R.id.llBillingAddress)
    val countryTil: VGSTextInputLayout = rootView.findViewById(R.id.vgsTilCountry)
    val countryEt: VGSCountryEditText = rootView.findViewById(R.id.vgsEtCountry)
    val addressTil: VGSTextInputLayout = rootView.findViewById(R.id.vgsTilAddress)
    val addressEt: VGSEditText = rootView.findViewById(R.id.vgsEtAddress)
    val optionalAddressTil: VGSTextInputLayout = rootView.findViewById(R.id.vgsTilAddressOptional)
    val optionalAddressEt: VGSEditText = rootView.findViewById(R.id.vgsEtAddressOptional)
    val cityTil: VGSTextInputLayout = rootView.findViewById(R.id.vgsTilCity)
    val cityEt: VGSEditText = rootView.findViewById(R.id.vgsEtCity)
    val postalCodeTil: VGSTextInputLayout = rootView.findViewById(R.id.vgsTilPostalCode)
    val postalCodeEt: VGSEditText = rootView.findViewById(R.id.vgsEtPostalCode)

    val saveCardButton: MaterialButton = rootView.findViewById(R.id.mbSaveCard)
}