package com.verygoodsecurity.vgscheckout.ui.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.verygoodsecurity.vgscheckout.R
import com.verygoodsecurity.vgscheckout.collect.core.VGSCollect
import com.verygoodsecurity.vgscheckout.collect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscheckout.collect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldState
import com.verygoodsecurity.vgscheckout.collect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscheckout.collect.widget.PersonNameEditText
import com.verygoodsecurity.vgscheckout.collect.widget.VGSTextInputLayout
import com.verygoodsecurity.vgscheckout.config.core.CheckoutConfiguration
import com.verygoodsecurity.vgscheckout.model.CheckoutResultContract
import com.verygoodsecurity.vgscheckout.util.extension.disableScreenshots
import com.verygoodsecurity.vgscheckout.util.extension.toCheckoutResult

internal abstract class BaseCheckoutActivity<C : CheckoutConfiguration> :
    AppCompatActivity(), VgsCollectResponseListener {

    protected val config: C by lazy { resolveConfig(intent) }

    protected val collect by lazy {
        resolveCollect().apply {
            addOnResponseListeners(this@BaseCheckoutActivity)
        }
    }

    private lateinit var payButton: MaterialButton

    abstract fun resolveConfig(intent: Intent): C

    abstract fun resolveCollect(): VGSCollect

    abstract fun onPayClicked()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableScreenshots()
        setContentView(R.layout.vgs_checkout_activity)
        initView(savedInstanceState)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
    }

    override fun onResponse(response: VGSResponse?) {
        val resultBundle = CheckoutResultContract.Result(response?.toCheckoutResult()).toBundle()
        setResult(Activity.RESULT_OK, Intent().putExtras(resultBundle))
        finish()
    }

    @CallSuper
    protected open fun initView(savedInstanceState: Bundle?) {
        val vgsTilPersonName = findViewById<VGSTextInputLayout>(R.id.vgsTilPersonName)
        val vgsTiedPersonName = findViewById<PersonNameEditText>(R.id.vgsTiedPersonName)

        vgsTiedPersonName?.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {

            override fun onStateChange(state: FieldState) {
                if (state.contentLength > 5) {
                    vgsTilPersonName.setError("Error")
                } else {
                    vgsTilPersonName.setError(null)
                }
            }
        })

        val tilPersonName = findViewById<TextInputLayout>(R.id.til)
        val tiedPersonName = findViewById<TextInputEditText>(R.id.tied)

        tiedPersonName.doOnTextChanged { text, _, _, _ ->
            if ((text?.length ?: 0) > 5) {
                tilPersonName.error = "Error"
            } else {
                tilPersonName.error = null
            }
        }
    }
}