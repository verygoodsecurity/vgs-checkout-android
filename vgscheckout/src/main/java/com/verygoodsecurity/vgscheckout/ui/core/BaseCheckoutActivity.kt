package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.CancelEvent
import com.verygoodsecurity.vgscheckout.collect.core.api.analityc.event.RequestEvent
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSError
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.collect.view.InputFieldView
import com.verygoodsecurity.vgscheckout.collect.widget.*
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfig
import com.verygoodsecurity.vgscheckout.config.networking.core.VGSCheckoutHostnamePolicy
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.ui.fragment.core.BaseManualInputFragment
import com.verygoodsecurity.vgscheckout.util.CollectProvider
import com.verygoodsecurity.vgscheckout.util.extension.*

internal abstract class BaseCheckoutActivity<C : CheckoutConfig> : AppCompatActivity(),
    CheckoutResolver, VgsCollectResponseListener {

    protected val config: C by lazy { resolveConfig(intent) }

    protected val collect: VGSCollect by lazy {
        CollectProvider().get(this, config).apply {
            addOnResponseListeners(this@BaseCheckoutActivity)
        }
    }

    abstract fun resolveConfig(intent: Intent): C

    abstract fun hasCustomHeaders(): Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenshotsAllowed(config.isScreenshotsAllowed)
        setContentView(R.layout.vgs_checkout_activity)
        initView(savedInstanceState)
    }

    override fun onBackPressed() {
        config.analyticTracker.log(CancelEvent)
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun bind(vararg view: InputFieldView) {
        collect.bindView(*view)
    }

    override fun unbind(vararg view: InputFieldView) {
        collect.unbindView(*view)
    }

    override fun checkout(invalidFields: List<String>) {
        if (invalidFields.isEmpty()) {
            with(config.routeConfig) {
                collect.asyncSubmit(
                    VGSRequest.VGSRequestBuilder()
                        .setPath(path)
                        .setMethod(requestOptions.httpMethod.toCollectHTTPMethod())
                        .setCustomData(requestOptions.extraData)
                        .setCustomHeader(requestOptions.extraHeaders)
                        .setFieldNameMappingPolicy(requestOptions.mergePolicy.toCollectMergePolicy())
                        .build()
                )
            }
        }
    }

    override fun onResponse(response: VGSResponse?) {
        (response as? VGSResponse.ErrorResponse)?.let {
            if (it.code == VGSError.NO_NETWORK_CONNECTIONS.code) {
                showNetworkConnectionErrorSnackBar()
                return
            }
        }
        val resultBundle = CheckoutResultContract.Result(response?.toCheckoutResult()).toBundle()
        setResult(Activity.RESULT_OK, Intent().putExtras(resultBundle))
        finish()
    }

    @CallSuper
    protected open fun initView(savedInstanceState: Bundle?) {
        initToolbar()
        showManualInputFragment()
    }

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.mtToolbar))
    }

    private fun showManualInputFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fcvContainer, BaseManualInputFragment.create(config.formConfig))
            .commit()
    }

    private fun showNetworkConnectionErrorSnackBar() {
        val message = getString(R.string.vgs_checkout_no_network_error)
        Snackbar.make(window.decorView.rootView, message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.vgs_checkout_no_network_retry)) { checkout(emptyList()) }
            .show()
    }

    private fun sendRequestEvent(isSuccessful: Boolean, invalidFields: List<String>) {
        with(config) {
            analyticTracker.log(
                RequestEvent(
                    isSuccessful,
                    routeConfig.hostnamePolicy is VGSCheckoutHostnamePolicy.CustomHostname,
                    routeConfig.requestOptions.extraData.isNotEmpty(),
                    hasCustomHeaders(),
                    formConfig.addressOptions.countryOptions.validCountries.isNotEmpty(),
                    routeConfig.requestOptions.mergePolicy,
                    invalidFields
                )
            )
        }
    }
}