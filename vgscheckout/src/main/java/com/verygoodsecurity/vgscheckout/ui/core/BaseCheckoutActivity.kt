package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.analytic.event.PaymentMethodSelectedEvent
import com.verygoodsecurity.vgscheckout.analytic.event.CancelEvent
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutAddCardConfig
import com.verygoodsecurity.vgscheckout.config.VGSCheckoutCustomConfig
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResult
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.ui.fragment.core.BaseFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.method.SelectPaymentMethodFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.save.SaveCardFragment
import com.verygoodsecurity.vgscheckout.util.extension.setScreenshotsAllowed
import com.verygoodsecurity.vgscheckout.util.extension.toCheckoutResult

internal abstract class BaseCheckoutActivity<C : CheckoutConfig> : AppCompatActivity(),
    NavigationHandler, ToolbarHandler, ResultHandler {

    protected val config: C by lazy { CheckoutResultContract.Args.fromIntent<C>(intent).config }

    private var resultBundle = VGSCheckoutResultBundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenshotsAllowed(config.isScreenshotsAllowed)
        setContentView(R.layout.vgs_checkout_activity)
        initView(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_RESULT_BUNDLE, resultBundle)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getParcelable<VGSCheckoutResultBundle>(KEY_RESULT_BUNDLE)?.let {
            resultBundle = it
        }
    }

    override fun onBackPressed() {
        config.analyticTracker.log(CancelEvent())
        setResult(Activity.RESULT_CANCELED, VGSCheckoutResult.Canceled(resultBundle))
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun navigateToSaveCard() {
        val fragment = BaseFragment.create<SaveCardFragment>(config)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcvContainer, fragment, FRAGMENT_TAG)
            .commit()
    }

    override fun navigateToPaymentMethods() {
        val fragment = BaseFragment.create<SelectPaymentMethodFragment>(config)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcvContainer, fragment, FRAGMENT_TAG)
            .commit()
    }

    override fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun getResultBundle(): VGSCheckoutResultBundle = resultBundle

    override fun setResult(isSuccessful: Boolean) {
        logPaymentMethodSelected()
        setResult(Activity.RESULT_OK, resultBundle.toCheckoutResult(isSuccessful))
        finish()
    }

    @CallSuper
    protected open fun initView(savedInstanceState: Bundle?) {
        initToolbar()
        if (savedInstanceState == null) {
            initFragment()
        }
    }

    protected open fun initFragment() {
        navigateToSaveCard()
    }

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.mtToolbar))
    }

    private fun logPaymentMethodSelected() {
        if (config is VGSCheckoutAddCardConfig) {
            config.analyticTracker.log(
                PaymentMethodSelectedEvent(
                    resultBundle.getBoolean(VGSCheckoutResultBundle.IS_PRE_SAVED_CARD) ?: false,
                    config is VGSCheckoutCustomConfig
                )
            )
        }
    }

    private fun setResult(resultCode: Int, result: VGSCheckoutResult) {
        val checkoutResultBundle = CheckoutResultContract.Result(result).toBundle()
        setResult(resultCode, Intent().putExtras(checkoutResultBundle))
    }

    companion object {

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        internal const val FRAGMENT_TAG = "com.verygoodsecurity.vgscheckout.fragment_tag"

        private const val KEY_RESULT_BUNDLE = "com.verygoodsecurity.vgscheckout.result_bundle"
    }
}