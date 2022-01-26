package com.verygoodsecurity.vgscheckout.ui.fragment.method

import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.ui.core.OnPaymentMethodSelectedListener
import com.verygoodsecurity.vgscheckout.ui.core.ToolbarHandler
import com.verygoodsecurity.vgscheckout.ui.fragment.core.LoadingHandler
import com.verygoodsecurity.vgscheckout.ui.fragment.method.adapter.PaymentMethodsAdapter
import com.verygoodsecurity.vgscheckout.ui.fragment.method.decorator.MarginItemDecoration
import com.verygoodsecurity.vgscheckout.util.extension.getDrawableCompat
import com.verygoodsecurity.vgscheckout.util.extension.requireParcelable
import com.verygoodsecurity.vgscheckout.util.extension.requireString
import com.verygoodsecurity.vgscheckout.util.extension.setEnabled

internal class SelectPaymentMethodFragment : Fragment(R.layout.vgs_checkout_select_method_fragment),
    LoadingHandler, PaymentMethodsAdapter.OnPaymentMethodClickListener {

    private val config: VGSCheckoutPaymentConfig by lazy { requireParcelable(KEY_BUNDLE_CONFIG) }
    private val buttonTitle: String by lazy { requireString(KEY_BUNDLE_BUTTON_TITLE) }

    private lateinit var toolbarHandler: ToolbarHandler
    private lateinit var listener: OnPaymentMethodSelectedListener
    private lateinit var adapter: PaymentMethodsAdapter

    private var payButton: MaterialButton? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = requireActivity() as OnPaymentMethodSelectedListener
        toolbarHandler = requireActivity() as ToolbarHandler
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        toolbarHandler.setTitle(getString(R.string.vgs_checkout_title))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_ITEM_POSITION, adapter.getSelectedPosition())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        adapter.setSelectedPosition(savedInstanceState?.getInt(KEY_SELECTED_ITEM_POSITION) ?: 0)
    }

    override fun setIsLoading(isLoading: Boolean) {
        setViewsEnabled(!isLoading)
        setSaveButtonIsLoading(isLoading)
    }

    override fun onNewCardClick() {
        listener.onNewCardSelected()
    }

    private fun initView(view: View) {
        initSavedCardsView(view)
        initPayButton(view)
    }

    private fun initSavedCardsView(view: View) {
        view.findViewById<RecyclerView>(R.id.rvPaymentMethods)?.let {
            it.itemAnimator = null
            adapter = PaymentMethodsAdapter(config.savedCards, this)
            it.adapter = adapter
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
            payButton = it
            it.text = buttonTitle
            it.setOnClickListener {
                listener.onCardSelected(adapter.getSelectedCard())
            }
        }
    }

    private fun setViewsEnabled(isEnabled: Boolean) {
        (view as? ViewGroup)?.setEnabled(isEnabled, true, payButton)
    }

    private fun setSaveButtonIsLoading(isLoading: Boolean) {
        view?.findViewById<MaterialButton>(R.id.mbPay)?.let {
            it.isClickable = !isLoading
            if (isLoading) {
                it.text = getString(R.string.vgs_checkout_button_processing_title)
                it.icon = getDrawableCompat(R.drawable.vgs_checkout_ic_loading_animated_white_16)
                (it.icon as? Animatable)?.start()
            } else {
                it.text = buttonTitle
                it.icon = null
            }
        }
    }

    companion object {

        private const val KEY_SELECTED_ITEM_POSITION = "selected_item_position"

        private const val KEY_BUNDLE_CONFIG = "com.verygoodsecurity.vgscheckout.config"
        private const val KEY_BUNDLE_BUTTON_TITLE = "com.verygoodsecurity.vgscheckout.button_title"

        fun create(config: CheckoutConfig, buttonTitle: String): Fragment =
            SelectPaymentMethodFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_BUNDLE_CONFIG, config)
                    putString(KEY_BUNDLE_BUTTON_TITLE, buttonTitle)
                }
            }
    }
}