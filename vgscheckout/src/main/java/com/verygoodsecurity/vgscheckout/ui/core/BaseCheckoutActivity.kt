package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.analytic.event.CancelEvent
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.model.VGSCheckoutResultBundle
import com.verygoodsecurity.vgscheckout.ui.fragment.core.BaseFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.method.SelectPaymentMethodFragment
import com.verygoodsecurity.vgscheckout.ui.fragment.save.SaveCardFragment
import com.verygoodsecurity.vgscheckout.util.extension.setScreenshotsAllowed

internal abstract class BaseCheckoutActivity<C : CheckoutConfig> : AppCompatActivity(),
    NavigationHandler, ToolbarHandler, ResultHolder {

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
        // TODO: move to fragment
        config.analyticTracker.log(CancelEvent)
        setResult(Activity.RESULT_CANCELED)
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

    @CallSuper
    protected open fun initView(savedInstanceState: Bundle?) {
        initToolbar()
        if (savedInstanceState == null) {
            initFragment()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.mtToolbar))
    }

    protected open fun initFragment() {
        navigateToSaveCard()
    }

    companion object {

        const val FRAGMENT_TAG = "com.verygoodsecurity.vgscheckout.fragment_tag"

        private const val KEY_RESULT_BUNDLE = "com.verygoodsecurity.vgscheckout.result_bundle"
    }
}