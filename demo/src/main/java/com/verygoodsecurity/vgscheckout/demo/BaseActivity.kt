package com.verygoodsecurity.vgscheckout.demo

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.config.ui.core.VGSCheckoutFormValidationBehaviour
import com.verygoodsecurity.vgscheckout.config.ui.view.core.VGSCheckoutFieldVisibility
import com.verygoodsecurity.vgscheckout.demo.settings.CheckoutSettingsActivity

abstract class BaseActivity constructor(@LayoutRes layoutId: Int) : AppCompatActivity(layoutId) {

    protected abstract val type: CheckoutType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.base, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.itemConfig -> {
                CheckoutSettingsActivity.start(this, type)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected fun SharedPreferences.getBoolean(@StringRes key: Int, default: Boolean = true) =
        getBoolean(getString(key), default)

    protected fun SharedPreferences.getFieldVisibility(
        @StringRes key: Int,
        visibleByDefault: Boolean = true
    ): VGSCheckoutFieldVisibility {
        val value = getBoolean(getString(key), visibleByDefault)
        return if (value) {
            VGSCheckoutFieldVisibility.VISIBLE
        } else {
            VGSCheckoutFieldVisibility.HIDDEN
        }
    }

    protected fun SharedPreferences.getValidationBehaviour(
        @StringRes key: Int,
        default: String = "on_submit"
    ): VGSCheckoutFormValidationBehaviour {
        val value = getString(getString(key), default)
        return if (value == default) {
            VGSCheckoutFormValidationBehaviour.ON_SUBMIT
        } else {
            VGSCheckoutFormValidationBehaviour.ON_FOCUS
        }
    }
}