package com.verygoodsecurity.vgscheckout.demo.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.demo.CheckoutType
import com.verygoodsecurity.vgscheckout.demo.R

class CheckoutSettingsActivity : AppCompatActivity(R.layout.activity_settings) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showSettingsFragment(intent.extras?.getSerializable(KEY_BUNDLE_TYPE) as CheckoutType)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSettingsFragment(type: CheckoutType) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fcvRoot, CheckoutSettingsFragment.create(type))
            .commit()
    }

    companion object {

        private const val KEY_BUNDLE_TYPE =
            "com.verygoodsecurity.vgscheckout.demo.settings.key_bundle_type"

        fun start(context: Context, type: CheckoutType) {
            val intent = Intent(context, CheckoutSettingsActivity::class.java)
            intent.putExtra(KEY_BUNDLE_TYPE, type)
            context.startActivity(intent)
        }
    }
}