package com.verygoodsecurity.vgscheckout.ui.fragment.method

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.networking.command.DeleteCreditCardCommand
import com.verygoodsecurity.vgscheckout.ui.fragment.core.BaseFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.method.adapter.PaymentMethodsAdapter
import com.verygoodsecurity.vgscheckout.ui.fragment.method.decorator.MarginItemDecoration
import com.verygoodsecurity.vgscheckout.util.extension.getBaseUrl
import com.verygoodsecurity.vgscheckout.util.extension.getDrawableCompat
import com.verygoodsecurity.vgscheckout.util.extension.setVisible

// TODO: Handle deleted items after screen rotation
internal class SelectPaymentMethodFragment :
    BaseFragment<VGSCheckoutAddCardConfig>(R.layout.vgs_checkout_select_method_fragment),
    PaymentMethodsAdapter.OnItemClickListener {

    private lateinit var paymentMethodsRv: RecyclerView
    private lateinit var adapter: PaymentMethodsAdapter
    private lateinit var payButton: MaterialButton

    private var isLoading: Boolean = false

    private var deleteCardCommand: DeleteCreditCardCommand? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initView(view)
        initToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.selec_payment_method_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (!isLoading && item.itemId == R.id.delete) {
            handleDeleteCardClicked()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_ITEM_POSITION, adapter.getSelectedPosition())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        adapter.setSelectedPosition(savedInstanceState?.getInt(KEY_SELECTED_ITEM_POSITION) ?: 0)
    }

    override fun onNewCardClick() {
        navigationHandler.navigateToSaveCard()
    }

    private fun initView(view: View) {
        initPaymentMethodsList(view)
        initPayButton(view)
    }

    private fun initToolbar() {
        toolbarHandler.setTitle(getString(R.string.vgs_checkout_title))
    }

    private fun initPaymentMethodsList(view: View) {
        paymentMethodsRv = view.findViewById(R.id.rvPaymentMethods)
        adapter = PaymentMethodsAdapter(config.savedCards.toMutableList(), this)
        paymentMethodsRv.itemAnimator = null
        paymentMethodsRv.adapter = adapter
        val paddingSmall =
            resources.getDimensionPixelSize(R.dimen.vgs_checkout_margin_padding_size_small)
        val paddingMedium =
            resources.getDimensionPixelSize(R.dimen.vgs_checkout_margin_padding_size_medium)
        paymentMethodsRv.addItemDecoration(
            MarginItemDecoration(
                paddingSmall,
                paddingMedium,
                paddingMedium,
                paddingSmall
            )
        )
    }

    private fun initPayButton(view: View) {
        payButton = view.findViewById(R.id.mbPay)
        payButton.text = title
        payButton.setOnClickListener {
            setLoading(true)
            // TODO: Handle pay button click
        }
    }

    private fun handleDeleteCardClicked() {
        MaterialAlertDialogBuilder(requireContext(), R.style.VGSCheckout_RemoveCardDialog)
            .setTitle(getString(R.string.vgs_checkout_delete_dialog_title))
            .setMessage(getString(R.string.vgs_checkout_delete_dialog_message))
            .setPositiveButton(getString(R.string.vgs_checkout_delete_dialog_positive_button_title)) { _, _ ->
                deleteSelectedCard()
            }
            .setNegativeButton(getString(R.string.vgs_checkout_delete_dialog_negative_button_title)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteSelectedCard() {
        setLoading(true)
        deleteCardCommand = DeleteCreditCardCommand(requireContext())
        deleteCardCommand?.execute(
            DeleteCreditCardCommand.Params(
                config.getBaseUrl(requireContext()),
                config.routeConfig.path,
                config.accessToken,
                adapter.getSelectedCard().finId
            ), ::handleDeleteCreditCardResponse
        )
    }

    private fun handleDeleteCreditCardResponse(result: DeleteCreditCardCommand.Result) {
        setLoading(false)
        // TODO: Remove item only on successful response
        adapter.getItems().find { it.finId == result.id }?.let {
            adapter.removeItem(it)
        }
        // TODO: Handle response
    }

    private fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        setViewsEnabled(!isLoading)
        setSaveButtonIsLoading(isLoading)
    }

    private fun setViewsEnabled(isEnabled: Boolean) {
        view?.findViewById<View>(R.id.viewOverlay)?.setVisible(!isEnabled)
    }

    private fun setSaveButtonIsLoading(isLoading: Boolean) {
        payButton.isClickable = !isLoading
        if (isLoading) {
            payButton.text = getString(R.string.vgs_checkout_button_processing_title)
            payButton.icon = getDrawableCompat(R.drawable.vgs_checkout_ic_loading_animated_white_16)
            (payButton.icon as? Animatable)?.start()
        } else {
            payButton.text = title
            payButton.icon = null
        }
    }

    companion object {

        private const val KEY_SELECTED_ITEM_POSITION = "selected_item_position"
    }
}