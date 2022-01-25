package com.verygoodsecurity.vgscheckout.ui.fragment.method

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutCard
import com.verygoodsecurity.vgscheckout.ui.fragment.core.LoadingHandler
import com.verygoodsecurity.vgscheckout.ui.fragment.method.adapter.PaymentMethodsAdapter
import com.verygoodsecurity.vgscheckout.ui.fragment.method.decorator.MarginItemDecoration
import com.verygoodsecurity.vgscheckout.util.extension.requireString
import java.util.*

internal class SelectPaymentMethodFragment : Fragment(R.layout.vgs_checkout_select_method_fragment),
    LoadingHandler, PaymentMethodsAdapter.OnPaymentMethodClickListener {

    private val buttonTitle: String by lazy { requireString(KEY_BUNDLE_BUTTON_TITLE) }

    private val paymentMethodsView: RecyclerView? by lazy { view?.findViewById(R.id.rvPaymentMethods) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun setIsLoading(isLoading: Boolean) {
        // TODO: Handle
    }

    override fun onCardClick(card: VGSCheckoutCard) {
//        Toast.makeText(requireContext(), "onCardClick::$card", Toast.LENGTH_SHORT).show()
    }

    override fun onNewCardClick() {
        Toast.makeText(requireContext(), "onNewCardClick", Toast.LENGTH_SHORT).show()
    }

    private fun initView(view: View) {
        initSavedCardsView()
        initPayButton(view)
    }

    private fun initSavedCardsView() {
        paymentMethodsView?.let {
            it.adapter = PaymentMethodsAdapter(getCards(), this)
            val paddingSmall =
                resources.getDimensionPixelSize(R.dimen.vgs_checkout_margin_padding_size_small)
            val paddingMedium =
                resources.getDimensionPixelSize(R.dimen.vgs_checkout_margin_padding_size_medium)
            it.addItemDecoration(
                MarginItemDecoration(
                    paddingSmall,
                    paddingMedium,
                    paddingMedium,
                    paddingSmall
                )
            )
        }
    }

    private fun initPayButton(view: View) {
        view.findViewById<MaterialButton>(R.id.mbPay)?.let {
            it.text = buttonTitle
            it.setOnClickListener {
                handlePayClicked()
            }
        }
    }

    private fun handlePayClicked() {
        // TODO: Handle
    }

    private fun getCards(): List<VGSCheckoutCard> {
        val brands = VGSCheckoutCardBrand.BRANDS.toList()
        val result = mutableListOf<VGSCheckoutCard>()
        for (i in 0..10) {
            result.add(
                VGSCheckoutCard(
                    UUID.randomUUID().toString(),
                    "Test $i",
                    "$i$i$i$i",
                    "09/24",
                    brands.random().name
                )
            )
        }
        return result
    }

    companion object {

        private const val KEY_BUNDLE_BUTTON_TITLE = "com.verygoodsecurity.vgscheckout.button_title"

        fun create(buttonTitle: String): Fragment =
            SelectPaymentMethodFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_BUNDLE_BUTTON_TITLE, buttonTitle)
                }
            }
    }
}