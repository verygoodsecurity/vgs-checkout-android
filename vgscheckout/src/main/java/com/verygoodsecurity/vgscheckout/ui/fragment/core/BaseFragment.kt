package com.verygoodsecurity.vgscheckout.ui.fragment.core

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutPaymentConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.networking.command.core.VGSCheckoutCancellable
import com.verygoodsecurity.vgscheckout.ui.core.NavigationHandler
import com.verygoodsecurity.vgscheckout.ui.core.ResultHandler
import com.verygoodsecurity.vgscheckout.ui.core.ToolbarHandler
import com.verygoodsecurity.vgscheckout.ui.fragment.save.SaveAndPayFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.save.SaveCardFragment
import com.verygoodsecurity.vgscheckout.util.extension.requireParcelable

internal abstract class BaseFragment<C : CheckoutConfig> : Fragment {

    constructor() : super()

    constructor(@LayoutRes id: Int) : super(id)

    protected val config: C by lazy { requireParcelable(KEY_BUNDLE_CONFIG) }
    protected val title: String by lazy { generateButtonTitle() }

    protected lateinit var navigationHandler: NavigationHandler
    protected lateinit var toolbarHandler: ToolbarHandler
    protected lateinit var resultHandler: ResultHandler

    private var transactionRequest: VGSCheckoutCancellable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationHandler = requireActivity() as NavigationHandler
        toolbarHandler = requireActivity() as ToolbarHandler
        resultHandler = requireActivity() as ResultHandler
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateToolbarTitle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        transactionRequest?.cancel()
    }

    protected fun showSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    protected fun showRetrySnackBar(message: String, onRetry: (() -> Unit)? = null) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.vgs_checkout_no_network_retry)) { onRetry?.invoke() }
            .show()
    }

    private fun generateButtonTitle(): String = when (config) {
        is VGSCheckoutAddCardConfig -> getString(R.string.vgs_checkout_button_save_card_title)
        else -> getString(R.string.vgs_checkout_button_pay_title)
    }

    private fun updateToolbarTitle() {
        toolbarHandler.setTitle(getToolbarTitle())
    }

    private fun getToolbarTitle(): String = getString(
        when {
            config is VGSCheckoutCustomConfig -> R.string.vgs_checkout_add_card_title
            config is VGSCheckoutAddCardConfig || this is SaveCardFragment -> R.string.vgs_checkout_new_card_title
            config is VGSCheckoutPaymentConfig || this is SaveAndPayFragment -> R.string.vgs_checkout_pay_with_new_card_title
            else -> throw IllegalArgumentException("Unknown type of config.")
        }
    )

    companion object {

        private const val KEY_BUNDLE_CONFIG = "com.verygoodsecurity.vgscheckout.config"

        inline fun <reified T : BaseFragment<*>> create(config: CheckoutConfig): BaseFragment<*> =
            T::class.java.newInstance().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_BUNDLE_CONFIG, config)
                }
            }
    }
}