package com.verygoodsecurity.vgscheckout.demo.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscheckout.demo.R

class CheckoutSettingsActivity : AppCompatActivity(R.layout.activity_settings) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showSettingsFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSettingsFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fcvRoot, CheckoutSettingsFragment())
            .commit()
    }
}