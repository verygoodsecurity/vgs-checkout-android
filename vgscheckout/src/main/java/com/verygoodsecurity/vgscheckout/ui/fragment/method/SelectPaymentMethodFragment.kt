package com.verygoodsecurity.vgscheckout.ui.fragment.method

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.ui.fragment.core.LoadingHandler
import com.verygoodsecurity.vgscheckout.ui.fragment.method.adapter.CardsAdapter
import com.verygoodsecurity.vgscheckout.util.extension.requireString

internal class SelectPaymentMethodFragment : Fragment(R.layout.vgs_checkout_select_method_fragment),
    LoadingHandler {

    private val buttonTitle: String by lazy { requireString(KEY_BUNDLE_BUTTON_TITLE) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }


    override fun setIsLoading(isLoading: Boolean) {

    }

    private fun initView(view: View) {
        initSavedCardsView(view)
        initPayButton(view)
    }

    private fun initSavedCardsView(view: View) {
        view.findViewById<RecyclerView>(R.id.rvCards)?.let {
            it.adapter = CardsAdapter(emptyList())
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

    }

    companion object {

        private const val KEY_BUNDLE_BUTTON_TITLE = "key_bundle_button_title"

        fun create(buttonTitle: String): Fragment =
            SelectPaymentMethodFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_BUNDLE_BUTTON_TITLE, buttonTitle)
                }
            }
    }
}