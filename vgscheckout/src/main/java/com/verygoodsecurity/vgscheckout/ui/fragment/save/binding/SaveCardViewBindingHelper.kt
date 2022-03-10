package com.verygoodsecurity.vgscheckout.ui.fragment.save.binding

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.storage.InternalStorage
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.widget.*

// TODO: Remove all unused views
@Suppress("unused")
internal class SaveCardViewBindingHelper(inflater: LayoutInflater, @LayoutRes layoutId: Int) {

    val rootView: View = inflater.inflate(layoutId, null)

    val cardDetailsMtv: MaterialTextView by lazy { findViewById(R.id.mtvCardDetailsTitle) }
    val cardDetailsLL: LinearLayoutCompat by lazy { findViewById(R.id.llCardDetails) }
    val cardHolderTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilCardHolder) }
    val cardHolderEt: PersonNameEditText by lazy { findViewById(R.id.vgsEtCardHolder) }
    val cardNumberTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilCardNumber) }
    val cardNumberEt: VGSCardNumberEditText by lazy { findViewById(R.id.vgsEtCardNumber) }
    val expirationDateTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilExpirationDate) }
    val expirationDateEt: ExpirationDateEditText by lazy { findViewById(R.id.vgsEtExpirationDate) }
    val securityCodeTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilSecurityCode) }
    val securityCodeEt: CardVerificationCodeEditText by lazy { findViewById(R.id.vgsEtSecurityCode) }

    val billingAddressMtv: MaterialTextView by lazy { findViewById(R.id.mtvBillingAddressTitle) }
    val billingAddressLL: LinearLayoutCompat by lazy { findViewById(R.id.llBillingAddress) }
    val countryTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilCountry) }
    val countryEt: VGSCountryEditText by lazy { findViewById(R.id.vgsEtCountry) }
    val addressTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilAddress) }
    val addressEt: VGSEditText by lazy { findViewById(R.id.vgsEtAddress) }
    val optionalAddressTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilAddressOptional) }
    val optionalAddressEt: VGSEditText by lazy { findViewById(R.id.vgsEtAddressOptional) }
    val cityTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilCity) }
    val cityEt: VGSEditText by lazy { findViewById(R.id.vgsEtCity) }
    val postalCodeTil: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilPostalCode) }
    val postalCodeEt: VGSEditText by lazy { findViewById(R.id.vgsEtPostalCode) }

    val saveCardCheckbox: MaterialCheckBox by lazy { findViewById(R.id.mcbSaveCard) }
    val saveCardButton: MaterialButton by lazy { findViewById(R.id.mbSaveCard) }

    private val storage = InternalStorage()

    fun getAssociatedList() = storage.getAssociatedList()

    private fun <T : View> findViewById(id: Int): T {
        return rootView.findViewById<T>(id).also {
            storage.performSubscription(it as? InputFieldView)
        }
    }

}